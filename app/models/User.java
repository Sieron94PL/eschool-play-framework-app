package models;

import be.objectify.deadbolt.java.models.Permission;
import be.objectify.deadbolt.java.models.Role;
import be.objectify.deadbolt.java.models.Subject;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.ebean.Finder;
import io.ebean.Model;

import io.ebean.annotation.Length;
import org.mindrot.jbcrypt.BCrypt;
import play.data.validation.Constraints;
import play.data.validation.ValidationError;

import javax.persistence.*;
import java.util.List;

@Entity
public class User extends Model implements Subject {

    @Id
    public Long id;

    public String email;

    @JsonIgnore
    public String password;

    @Column(columnDefinition = "tinyint default false")
    public boolean enabled;

    public String firstname;

    public String lastname;

    @JsonIgnore
    @OneToOne(cascade = CascadeType.ALL)
    public Student student;

    @JsonIgnore
    @OneToOne(cascade = CascadeType.ALL)
    public Teacher teacher;

    @ManyToMany(cascade = CascadeType.ALL)
    public List<SecurityRole> roles;

    @ManyToMany(cascade = CascadeType.ALL)
    public List<UserPermission> permissions;

    public static final Finder<Long, User> find = new Finder<Long, User>(User.class);

    public static User getUser(Long id) {
        return find.byId(id);
    }

    public static void addUser(User user) {
        find.db().save(user);
    }

    public static void editUser(User user) {
        find.db().update(user);
    }

    public static void removeUser(Long id) {
        User user = find.byId(id);
        if (user != null) {
            find.db().delete(user);
        }
    }

    public static List<User> getUsers() {
        return find.all();
    }

    public static User findByEmail(String email) {
        return find.query().where().eq("email", email).findUnique();
    }

    public boolean checkPasswords(String password) {
        return BCrypt.checkpw(password, find.query().where().eq("email", email).findUnique().password);
    }

    @Override
    public List<? extends Role> getRoles() {
        return roles;
    }

    @Override
    public List<? extends Permission> getPermissions() {
        return permissions;
    }

    @Override
    public String getIdentifier() {
        return email;
    }
}
