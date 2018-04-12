package models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.ebean.Finder;
import io.ebean.Model;

import javax.persistence.*;
import java.util.List;

@Entity
public class Student extends Model {
    @Id
    public Long id;

    @OneToOne
    public User user;

    @JsonIgnore
    @ManyToOne
    public Clazz clazz;

    public static final Finder<Long, Student> find = new Finder<>(Student.class);

    public static Student getStudent(Long id) {
        return find.byId(id);
    }

    public static void addStudent(Student student) {
        student.save();
    }

    public static void removeStudent(Long id) {
        find.deleteById(id);
    }

    public static void editStudent(Student student) {
        find.db().update(student);
    }

    public void deleteStudent(Long id) {
        find.deleteById(id);
    }

}
