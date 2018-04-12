package controllers;

import models.*;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EschoolRestController extends Controller {

    public Result getStudentsByClazzId(Long clazzId) {
        return ok(Json.toJson(Clazz.getClazz(clazzId).students));
    }

    public Result getStudentsByClazzIdAndSubjectId(Long clazzId, Long subjectId) {
        Map<String, List<Mark>> map = new HashMap<>();
        List<Student> students = Clazz.getClazz(clazzId).students;
        for (Student student : students) {
            map.put(student.user.firstname + ' ' + student.user.lastname, Mark.getMarksByStudentIdAndSubjectId(student.id, subjectId));
        }
        return ok(Json.toJson(map));
    }

    public Result getClazzesWithoutTeacher(Long subjectId) {
        return ok(Json.toJson(Clazz.getClazzesWithoutTeacher(subjectId)));
    }


    public Result getListOfMarks(Long clazzId, Long subjectId) {
        Map<String, List<Mark>> map = new HashMap<>();
        List<Student> students = Clazz.getClazz(clazzId).students;

        for (Student student : students) {
            map.put(student.user.firstname + ' ' + student.user.lastname,
                    Mark.getMarksByStudentIdAndSubjectId(student.id, subjectId));
        }
        return ok(Json.toJson(map));
    }

}
