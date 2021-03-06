package models;

import be.objectify.deadbolt.java.models.Permission;
import io.ebean.Finder;
import io.ebean.Model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class UserPermission extends Model implements Permission {

    @Id
    public Long id;

    @Column(name = "permission_value")
    public String value;

    public static final Finder<Long, UserPermission> find = new Finder<Long, UserPermission>(UserPermission.class);

    @Override
    public String getValue() {
        return null;
    }

    public static UserPermission findByValue(String value) {
        return find.query().where().eq("value", value).findUnique();
    }
}
