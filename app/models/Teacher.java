package models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.ebean.Ebean;
import io.ebean.Finder;
import io.ebean.Model;

import javax.persistence.*;
import java.util.List;

@Entity
public class Teacher extends Model {

    @Id
    public Long id;

    @JsonIgnore
    @OneToOne
    public User user;

    @ManyToOne
    public Subject subject;

    @ManyToMany
    public List<Clazz> clazzes;

    public static final Finder<Long, Teacher> find = new Finder<>(Teacher.class);

    public static Teacher getTeacher(Long id) {
        return find.byId(id);
    }

    public static void addTeacher(Teacher teacher) {
        find.db().save(teacher);
    }

    public static void editTeacher(Teacher teacher) {
        find.db().update(teacher);
    }

    public static void removeTeacher(Long id) {
        Teacher teacher = find.byId(id);
        if (teacher != null) {
            find.db().delete(teacher);
        }
    }

    public static List<Teacher> getClazzTeachers(Long clazzId) {
        List<Teacher> teachers = Ebean.find(Teacher.class).fetch("clazzes", "*").where().eq("clazz_id", clazzId).findList();
        return teachers;
    }

    public static Teacher findTeacherByClazzAndSubject(Long clazzId, Long subjectId) {
        Teacher teacher = Ebean.find(Teacher.class).fetch("clazzes", "*").where().eq("clazz_id", clazzId).where().eq("subject_id", subjectId).findOne();
        if (teacher == null) {
            return null;
        } else {
            return teacher;
        }
    }


}
