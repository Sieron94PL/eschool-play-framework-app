package controllers;

import be.objectify.deadbolt.java.actions.Group;
import be.objectify.deadbolt.java.actions.Restrict;
import it.innove.play.pdf.PdfGenerator;
import models.*;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.pdf;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StudentController extends Controller {

    @Inject
    PdfGenerator pdfGenerator;


    public static Map<Teacher, List<Mark>> getStudentMarks(Student student) {
        Map<Teacher, List<Mark>> studentMarks = new HashMap<>();
        if (student.clazz != null) {
            Long clazzId = student.clazz.id;
            Long studentId = student.id;

            List<Mark> marks;
            for (Subject subject : Clazz.getSubjects(clazzId)) {
                marks = new ArrayList<>();
                for (Mark mark : Mark.getMarksByStudentIdAndSubjectId(studentId, subject.id)) {
                    marks.add(mark);
                }
                if (Teacher.findTeacherByClazzAndSubject(clazzId, subject.id) != null) {
                    Teacher teacher = Teacher.findTeacherByClazzAndSubject(clazzId, subject.id);
                    studentMarks.put(teacher, marks);
                } else {
                    Teacher teacher = new Teacher();
                    teacher.subject = subject;
                    studentMarks.put(teacher, marks);
                }

            }
            return studentMarks;
        } else {
            return null;
        }

    }

    @Restrict(@Group("ROLE_STUDENT"))
    public Result getStudentMarks() {
        String email = session().get("email");
        User user = User.findByEmail(email);
        Student student = user.student;
        Map<Teacher, List<Mark>> studentMarks = getStudentMarks(student);
        return ok(views.html.student.render(studentMarks, student));
    }

    @Restrict(@Group("ROLE_STUDENT"))
    public Result generatePdf() {
        String email = session().get("email");
        User user = User.findByEmail(email);
        Student student = user.student;
        Map<Teacher, List<Mark>> studentMarks = getStudentMarks(student);
        return pdfGenerator.ok(pdf.render(studentMarks, student), "http://localhost:9000");
    }
}



