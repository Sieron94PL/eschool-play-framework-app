package models;

import be.objectify.deadbolt.java.models.Role;
import io.ebean.Finder;
import io.ebean.Model;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class SecurityRole extends Model implements Role {

    @Id
    public Long id;

    public String name;

    public static final Finder<Long, SecurityRole> find = new Finder<Long, SecurityRole>(SecurityRole.class);

    public static void save(SecurityRole securityRole){
        find.db().save(securityRole);
    }

    public String getName() {
        return name;
    }

    public static SecurityRole findByName(String name) {
        return find.query().where().eq("name", name).findUnique();
    }

}
