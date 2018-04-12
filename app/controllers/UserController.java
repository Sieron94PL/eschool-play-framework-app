package controllers;

import be.objectify.deadbolt.java.actions.SubjectNotPresent;
import be.objectify.deadbolt.java.actions.SubjectPresent;
import com.fasterxml.jackson.databind.JsonNode;
import com.sun.org.apache.regexp.internal.RE;
import models.Recaptcha;
import models.User;
import org.mindrot.jbcrypt.BCrypt;
import play.data.DynamicForm;
import play.data.Form;
import play.data.FormFactory;
import play.data.validation.ValidationError;
import play.i18n.Lang;
import play.i18n.Messages;
import play.i18n.MessagesApi;
import play.libs.Json;
import play.libs.ws.WS;
import play.libs.ws.WSBodyReadables;
import play.libs.ws.WSClient;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import scala.concurrent.Promise;
import views.html.login;
import views.html.registration;

import javax.inject.Inject;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletionStage;

public class UserController extends Controller implements WSBodyReadables {

    @Inject
    FormFactory formFactory;

    @Inject
    WSClient ws;

    Messages messages;

    public Result registration() {
        messages = Http.Context.current().messages();
        if (Controller.request().getQueryString("lang") != null) {
            String lang = Controller.request().getQueryString("lang");
            ctx().setTransientLang(lang);
            messages = Http.Context.current().messages();
            return ok(registration.render(messages, formFactory.form(User.class), formFactory.form(Recaptcha.class), null, null));
        }
        return ok(registration.render(messages, formFactory.form(User.class), formFactory.form(Recaptcha.class), null, null));
    }

    @SubjectNotPresent
    public Result submitUser() {
        messages = Http.Context.current().messages();
        Form<Recaptcha> formRecaptcha = formFactory.form(Recaptcha.class).bindFromRequest();
        String response = formRecaptcha.get().recaptcha;
        boolean isReCaptchaValid = ws.url(Recaptcha.getURL(response)).get().toCompletableFuture().join().asJson().get("success").booleanValue();
        Form<User> formUser = formFactory.form(User.class).bindFromRequest();
        if (User.findByEmail(formUser.get().email) != null) {
            String error = "Such a user already exists.";
            return badRequest(registration.render(messages, formUser, null, null, error));
        } else {
            if (isReCaptchaValid) {
                User user = formUser.get();
                String hashPassword = BCrypt.hashpw(user.password, BCrypt.gensalt());
                user.password = hashPassword;
                User.addUser(user);
                return redirect("/login");
            } else {
                String captchaError = "Invalid ReCaptcha.";
                return badRequest(registration.render(messages, formUser, null, captchaError, null));
            }
        }
    }


    public Result login() {
        return ok(login.render(formFactory.form(User.class), null, null));
    }

    @SubjectPresent
    public Result logout() {
        session().remove("email");
        return ok(login.render(null, null, "Logged out successfully."));
    }

    @SubjectNotPresent
    public Result signIn() {
        session().remove("email");
        Form<User> formUser = formFactory.form(User.class).bindFromRequest();
        User user = User.findByEmail(formUser.get().email);
        if (user != null) {
            if (user.checkPasswords(formUser.get().password) && user.enabled == true) {
                session().put("email", user.email);
                return redirect("/");
            } else {
                return unauthorized(login.render(null, "Invalid address email or password.", null));
            }
        } else {
            return unauthorized(login.render(null, "Such a user doesn't exist.", null));
        }
    }

    @SubjectPresent
    public Result getProfile() {
        String email = session().get("email");
        User user = User.findByEmail(email);
        return ok(views.html.profile.render(user));
    }
}






