package controllers;

import be.objectify.deadbolt.java.actions.Group;
import be.objectify.deadbolt.java.actions.Restrict;
import models.*;
import play.data.DynamicForm;
import play.data.FormFactory;
import play.libs.Json;
import play.libs.mailer.Email;
import play.libs.mailer.MailerClient;
import play.mvc.Controller;
import play.mvc.Result;

import javax.inject.Inject;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

public class TeacherController extends Controller {

    @Inject
    FormFactory formFactory;

    @Inject
    MailerClient mailerClient;

    @Restrict(@Group("ROLE_TEACHER"))
    public Result marks() {
        String email = session().get("email");
        User user = User.findByEmail(email);
        Map<Student, List<Mark>> map = new HashMap<>();
        List<Mark> marks = new ArrayList<>();
        if (user.teacher.clazzes.size() != 0) {
            for (Student student : user.teacher.clazzes.get(0).students) {
                marks = Mark.getMarksByStudentIdAndSubjectId(student.id, user.teacher.subject.id);
                map.put(student, marks);
            }
        }
        Teacher teacher = user.teacher;
        List<Clazz> teacherClazzes = teacher.clazzes;
        List<Student> students = teacher.clazzes.get(0).students;
        Subject subject = teacher.subject;
        return ok(views.html.teacher.render(formFactory.form(Mark.class), students, teacher, teacherClazzes, subject, map));
    }

    @Restrict(@Group("ROLE_TEACHER"))
    public Result addMark() {
        String email = session().get("email");
        User user = User.findByEmail(email);
        Teacher teacher = user.teacher;
        DynamicForm dynamicForm = formFactory.form().bindFromRequest();
        Student student = Student.getStudent(Long.valueOf(dynamicForm.get("students")));
        Mark mark = formFactory.form(Mark.class).bindFromRequest().get();
        mark.student = student;
        Mark.addMark(mark);

        String subject = "Eschool - dodano nową ocenę";
        DateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date date = new Date();

        String from = "eschool.pl@gmail.com";
        String url = "http://localhost:9000";
        String format = "Witaj <strong>%s</strong>,<br/><br/>Dodano ocenę <strong>%.1f</strong> z przedmiotu <strong>%s</strong>.<br/>Aby zobaczyć listę Twoich ocen zaloguj się na portal <a href=%s>Eschool</a>."
                + " <br/><br/>Wiadomość ta została wysłana automatycznie | dokładna data i czas: %s";

        String body = String.format(format, student.user.firstname, mark.value, teacher.subject.name, url, df.format(date.getTime()));

        Email email_ = new Email().setSubject(subject).setFrom(from).addTo(student.user.email).setBodyHtml(body);
        mailerClient.send(email_);

        return redirect("/marks");
    }
}
