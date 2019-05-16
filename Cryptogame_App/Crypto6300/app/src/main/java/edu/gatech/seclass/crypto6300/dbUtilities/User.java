package edu.gatech.seclass.crypto6300.dbUtilities;

import com.orm.SugarRecord;
import com.orm.dsl.Unique;

public class User extends SugarRecord {
    //Using capitalization in column names leads to problems in query
    //Always update version in AndroidManifest.xml if any changes made in DB classes
    @Unique
    private String username;
    private Boolean isadmin;

    public User() {}

    public User(String username, Boolean isAdmin ){
        this.username = username;
        this.isadmin = isAdmin;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Boolean getAdmin() {
        return isadmin;
    }

    public void setAdmin(Boolean admin) {
        isadmin = admin;
    }

}
