package models;


import com.fasterxml.jackson.annotation.JsonIgnore;
import io.ebean.Ebean;
import io.ebean.Finder;
import io.ebean.Model;
import io.ebean.SqlRow;
import play.data.validation.Constraints;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.validation.Constraint;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Clazz extends Model {
    @Id
    public Long id;

    @Constraints.Required
    public String name;

    @JsonIgnore
    @OneToMany(mappedBy = "clazz")
    public List<Student> students;

    @Override
    public boolean equals(Object o) {
        Clazz c = (Clazz) o;
        if (c.id == id || c.name.equals(name)) {
            return true;
        }
        return false;
    }

    public static final Finder<Long, Clazz> find = new Finder<>(Clazz.class);

    public static List<Clazz> getClazzes() {
        return find.all();
    }

    public static void addClazz(Clazz clazz) {
        clazz.save();
    }

    public static Clazz getClazz(Long id) {
        return find.byId(id);
    }

    public static void editClazz(Clazz clazz) {
        find.db().update(clazz);
    }

    public static void removeClazz(Long id) {
        find.deleteById(id);
    }

    public static List<Subject> getSubjects(Long clazzId) {
        List<Subject> subjects = Ebean.find(Subject.class).fetch("clazzes", "*").where().eq("clazz_id", clazzId).findList();
        return subjects;
    }

    public static List<Subject> getAvailableSubjects(Long clazzId) {
        List<Subject> subjects = Subject.getSubjects();
        List<Subject> result = Ebean.find(Subject.class).fetch("clazzes", "*").where().eq("clazz_id", clazzId).findList();
        List<Subject> clazzSubjects;
        clazzSubjects = result;
        if (clazzSubjects.size() != 0) {
            subjects.removeAll(clazzSubjects);
        }
        return subjects;
    }

    public static List<Clazz> getClazzesWithoutTeacher(Long subjectId) {
        List<Clazz> clazzes = Clazz.getClazzes();
        List<Teacher> teachers = Subject.getSubject(subjectId).teachers;
        List<Long> ids = new ArrayList<>();
        List<Clazz> clazzesWithoutTeacher = new ArrayList<>();

        for (int i = 0; i < teachers.size(); i++)
            for (Clazz clazz : Subject.getSubject(subjectId).teachers.get(i).clazzes) {
                if (!ids.contains(clazz.id)) {
                    ids.add(clazz.id);
                }
            }

        for (Clazz clazz : clazzes) {
            if (Subject.getSubject(subjectId).clazzes.contains(clazz) && !ids.contains(clazz.id)) {
                clazzesWithoutTeacher.add(clazz);
            }

        }
        return clazzesWithoutTeacher;
    }

}
