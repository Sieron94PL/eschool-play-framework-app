package models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.ebean.Ebean;
import io.ebean.Finder;
import io.ebean.Model;

import javax.persistence.*;
import java.util.List;

@Entity
public class Mark extends Model {

    @JsonIgnore
    @Id
    public int id;

    @JsonIgnore
    @ManyToOne
    public Subject subject;

    @JsonIgnore
    @ManyToOne(cascade = CascadeType.ALL)
    public Student student;

    public double value;

    public static final Finder<Long, Mark> find = new Finder<>(Mark.class);

    public static void addMark(Mark mark){
        mark.save();
    }

    public static List<Mark> getMarksByStudentIdAndSubjectId(Long studentId, Long subjectId){
        return Ebean.find(Mark.class).where().eq("student_id", studentId).eq("subject_id", subjectId).findList();
    }

    public static List<Mark> getSubjects(Long studentId){
        return Ebean.find(Mark.class).where().eq("student_id", studentId).findList();
    }



}
