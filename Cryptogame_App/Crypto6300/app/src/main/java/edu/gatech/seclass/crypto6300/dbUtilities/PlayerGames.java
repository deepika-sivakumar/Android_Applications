package edu.gatech.seclass.crypto6300.dbUtilities;

import com.orm.SugarRecord;

public class PlayerGames extends SugarRecord {
    //Using capitalization in column names leads to problems in query
    //Always update version in AndroidManifest.xml if any changes made in DB classes
    private String username;
    private String cryptogramname;
    private String status;
    private String encryptedphrase;
    private String previousstate;
    private int remainingattempts;
    private int incorrectattempts;

    //Do not remove this
    public PlayerGames() {}

    // When a Player chooses a cryptogram, add the entry to this table with this constructor
    public PlayerGames(String username, String cryptogramName, String gameStatus, String encryptedPhrase, int remainingAttempts) {
        this.username = username;
        this.cryptogramname = cryptogramName;
        this.status = gameStatus;
        this.encryptedphrase = encryptedPhrase;
        this.remainingattempts = remainingAttempts;
        // When the player is created there is no previous State & incorrect attempts
        this.incorrectattempts = 0;
        this.previousstate = null;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getCryptogramName() {
        return cryptogramname;
    }

    public void setCryptogramName(String cryptogramName) {
        this.cryptogramname = cryptogramName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getEncryptedPhrase() {
        return encryptedphrase;
    }

    public void setEncryptedPhrase(String encryptedPhrase) {
        this.encryptedphrase = encryptedPhrase;
    }

    public String getPreviousState() {
        return previousstate;
    }

    public void setPreviousState(String previousState) {
        this.previousstate = previousState;
    }

    public int getRemainingAttempts() {
        return remainingattempts;
    }

    public void setRemainingAttempts(int remainingAttempts) {
        this.remainingattempts = remainingAttempts;
    }
    public int getIncorrectAttempts() {
        return incorrectattempts;
    }

    public void setIncorrectAttempts(int incorrectAttempts) {
        this.incorrectattempts = incorrectAttempts;
    }

}
