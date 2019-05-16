package edu.gatech.seclass.crypto6300.dbUtilities;

import com.orm.SugarRecord;
import com.orm.dsl.Unique;

public class Player extends SugarRecord {
    //Using capitalization in column names leads to problems in query
    //Always update version in AndroidManifest.xml if any changes made in DB classes
    @Unique
    private String username;
    private String firstname;
    private String lastname;
    private String category;
    private int wins;
    private int loss;

    //Do not remove this
    public Player() {}

    public Player(String username, String firstName, String lastName, String category){
        this.username = username;
        //this.isAdmin = isAdmin;
        //this.loggedIn = loggedIn;
        this.firstname = firstName;
        this.lastname = lastName;
        this.category = category;
        this.wins = 0;
        this.loss = 0;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFirstName() {
        return firstname;
    }

    public void setFirstName(String firstName) {
        this.firstname = firstName;
    }

    public String getLastName() {
        return lastname;
    }

    public void setLastName(String lastName) {
        this.lastname = lastName;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getNoOfWins() {
        return wins;
    }

    public void setNoOfWins(int noOfWins) {
        this.wins = noOfWins;
    }

    public int getNoOfLoss() {
        return loss;
    }

    public void setNoOfLoss(int noOfLoss) {
        this.loss = noOfLoss;
    }
}