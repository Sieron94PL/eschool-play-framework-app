package controllers;

import be.objectify.deadbolt.java.actions.Group;
import be.objectify.deadbolt.java.actions.Restrict;
import com.sun.org.apache.xpath.internal.operations.Mult;
import models.*;
import org.h2.value.Value;
import play.data.DynamicForm;
import play.data.Form;
import play.data.FormFactory;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.clazzes;
import views.html.users;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class AdminController extends Controller {


    @Inject
    FormFactory formFactory;

    @Restrict(@Group("ROLE_ADMIN"))
    public Result subjects() {
        List<Subject> subjects = Subject.getSubjects();
        return ok(views.html.subjects.render(subjects, formFactory.form(Subject.class)));
    }

    @Restrict(@Group("ROLE_ADMIN"))
    public Result addSubject() {
        Form<Subject> subjectForm = formFactory.form(Subject.class).bindFromRequest();
        Subject subject = subjectForm.get();
        if (!Subject.getSubjects().contains(subject) && subject.name != "") {
            Subject.addSubject(subject);
        }
        return redirect("/subjects");
    }

    @Restrict(@Group("ROLE_ADMIN"))
    public Result clazzes() {
        List<Clazz> clazzes = Clazz.getClazzes();
        if (Controller.request().getQueryString("id") != null) {
            Long clazzId = Long.valueOf(Controller.request().getQueryString("id"));
            Clazz clazz = Clazz.getClazz(clazzId);
            List<Subject> availableSubjects = Clazz.getAvailableSubjects(clazzId);
            List<Subject> clazzSubjects = Clazz.getSubjects(clazzId);
            return ok(views.html.clazzes.render(clazzes, null, clazz, availableSubjects, clazzSubjects));
        }
        return ok(views.html.clazzes.render(clazzes, formFactory.form(Clazz.class), null, null, null));
    }

    @Restrict(@Group("ROLE_ADMIN"))
    public Result removeSubjectFromClazz(Long subjectId, Long clazzId) {
        Subject subject = Subject.getSubject(subjectId);
        Clazz clazz = Clazz.getClazz(clazzId);
        subject.clazzes.remove(clazz);
        Subject.editSubject(subject);
        return redirect("/clazzes?id=" + clazzId);
    }

    @Restrict(@Group("ROLE_ADMIN"))
    public Result addSubjectToClazz(Long subjectId, Long clazzId) {
        Subject subject = Subject.getSubject(subjectId);
        Clazz clazz = Clazz.getClazz(clazzId);
        subject.clazzes.add(clazz);
        Subject.editSubject(subject);
        return redirect("/clazzes?id=" + clazzId);
    }

    @Restrict(@Group("ROLE_ADMIN"))
    public Result addClazz() {
        Form<Clazz> clazzForm = formFactory.form(Clazz.class).bindFromRequest();
        Clazz clazz = clazzForm.get();
        if (!Clazz.getClazzes().contains(clazz) && clazz.name != "") {
            Clazz.addClazz(clazz);
        }
        return redirect("/clazzes");
    }

    @Restrict(@Group("ROLE_ADMIN"))
    public Result securityRoles() {
        List<SecurityRole> securityRoles = SecurityRole.find.all();
        return ok(views.html.roles.render(securityRoles, formFactory.form(SecurityRole.class)));
    }

    @Restrict(@Group("ROLE_ADMIN"))
    public Result addSecurityRole() {
        Form<SecurityRole> securityRoleForm = formFactory.form(SecurityRole.class).bindFromRequest();
        SecurityRole securityRole = securityRoleForm.get();
        SecurityRole.save(securityRole);
        return redirect("/securityRoles");
    }

    @Restrict(@Group("ROLE_ADMIN"))
    public Result deleteUser(Long userId) {
        User.removeUser(userId);
        return redirect("/users");
    }

    @Restrict(@Group("ROLE_ADMIN"))
    public Result users() {
        Long id = 0L;
        List<User> users = User.getUsers();
        List<Clazz> clazzes = Clazz.getClazzes();
        List<Subject> subjects = Subject.getSubjects();
        if (Controller.request().getQueryString("id") != null) {
            id = Long.valueOf(Controller.request().getQueryString("id"));
            User user = User.getUser(id);
            if (user.teacher != null) {
                List<Clazz> currentClazzesTeacher = user.teacher.clazzes;
                if (user.teacher.subject != null) {
                    List<Clazz> clazzesWithoutTeacher = Clazz.getClazzesWithoutTeacher(user.teacher.subject.id);
                    return ok(views.html.users.render(user, formFactory.form(User.class).fill(user), users, clazzes, subjects, formFactory.form(MultipleSelectForm.class), clazzesWithoutTeacher, currentClazzesTeacher));
                } else {
                    return ok(views.html.users.render(user, formFactory.form(User.class).fill(user), users, clazzes, subjects, formFactory.form(MultipleSelectForm.class), null, currentClazzesTeacher));
                }
            }

            return ok(views.html.users.render(user, formFactory.form(User.class).fill(user), users, clazzes, subjects, formFactory.form(MultipleSelectForm.class), null, null));
        }
        return ok(views.html.users.render(null, null, users, clazzes, subjects, formFactory.form(MultipleSelectForm.class), null, null));
    }

    @Restrict(@Group("ROLE_ADMIN"))
    public Result editUser() {
        MultipleSelectForm multipleSelectForm = formFactory.form(MultipleSelectForm.class).bindFromRequest().get();
        DynamicForm dynamicForm = formFactory.form().bindFromRequest();
        User user = formFactory.form(User.class).bindFromRequest().get();
        String securityRole = dynamicForm.get("securityRoles");
        String studentClazz = dynamicForm.get("studentClazz");
        String teacherSubject = dynamicForm.get("teacherSubject");
        if (securityRole != null) {
            if (securityRole.equals("student_id")) {
                Student student = new Student();
                student.user = user;
                user.roles = new ArrayList<>();
                user.roles.add(SecurityRole.findByName("ROLE_STUDENT"));
                user.student = student;
                Student.addStudent(student);
                User.editUser(user);
            } else if (securityRole.equals("teacher_id")) {
                Teacher teacher = new Teacher();
                teacher.user = user;
                user.roles = new ArrayList<>();
                user.roles.add(SecurityRole.findByName("ROLE_TEACHER"));
                user.teacher = teacher;
                Teacher.addTeacher(teacher);
                User.editUser(user);
            } else {
                user.roles = new ArrayList<>();
                user.roles.add(SecurityRole.findByName("ROLE_ADMIN"));
                User.editUser(user);
            }
        }
        if (studentClazz != null) {
            Long clazzId = Long.valueOf(studentClazz);
            if (clazzId > 0) {
                Student student = User.findByEmail(user.email).student;
                student.clazz = Clazz.getClazz(clazzId);
                Student.editStudent(student);
            }
        }
        if (teacherSubject != null) {
            Long subjectId = Long.valueOf(teacherSubject);
            if (subjectId > 0) {
                Teacher teacher = User.findByEmail(user.email).teacher;
                teacher.subject = Subject.getSubject(subjectId);
                Teacher.editTeacher(teacher);
            }
        }
        if (multipleSelectForm.teacherClazzes != null) {
            Teacher teacher = User.findByEmail(user.email).teacher;
            List<Clazz> teacherClazzes = new ArrayList<>();
            for (int i = 0; i < multipleSelectForm.teacherClazzes.size(); i++) {
                String index = multipleSelectForm.teacherClazzes.get(i);
                teacherClazzes.add(Clazz.getClazz(Long.valueOf(index)));
            }
            teacher.clazzes = teacherClazzes;
            Teacher.editTeacher(teacher);
        }
        User.editUser(user);
        return redirect("/users");
    }
}
