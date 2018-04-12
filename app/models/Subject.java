package models;


import com.fasterxml.jackson.annotation.JsonIgnore;
import io.ebean.Finder;
import io.ebean.Model;
import io.ebean.SqlRow;

import javax.persistence.*;
import java.util.List;

import static io.ebean.Ebean.find;

@Entity
public class Subject extends Model {

    @Id
    public Long id;

    @JsonIgnore
    public String name;

    @JsonIgnore
    @OneToMany(mappedBy = "subject")
    public List<Teacher> teachers;

    @ManyToMany
    public List<Clazz> clazzes;

    @Override
    public boolean equals(Object o) {
        Subject s = (Subject) o;
        if (s.id == id || s.name.equals(name)) {
            return true;
        }
        return false;
    }

    public static final Finder<Long, Subject> find = new Finder<>(Subject.class);

    public static Subject getSubject(Long id) {
        return find.byId(id);
    }

    public static void addSubject(Subject subject) {
        subject.save();
    }

    public static void editSubject(Subject subject) {
        find.db().update(subject);
    }

    public static void removeSubject(Long id) {
        find.deleteById(id);
    }

    public static List<Subject> getSubjects() {
        return find.all();
    }

}
