package edu.gatech.seclass.crypto6300.dbUtilities;

import com.orm.SugarRecord;
import com.orm.dsl.Unique;

public class Cryptogram extends SugarRecord {
    //Using capitalization in column names leads to problems in query
    //Always update version in AndroidManifest.xml if any changes made in DB classes
    @Unique
    private String cryptogramname;
    private String solution;
    private int easyattempts;
    private int normalattempts;
    private int hardattempts;

    //Do not remove this
    public Cryptogram() {}

    public Cryptogram(String cryptogramName, String solution , int easyAttempts, int normalAttempts, int hardAttempts){
        this.cryptogramname = cryptogramName;
        this.solution = solution;
        this.easyattempts = easyAttempts;
        this.normalattempts = normalAttempts;
        this.hardattempts = hardAttempts;
    }

    public String getCryptogramName() {
        return cryptogramname;
    }

    public void setCryptogramName(String cryptogramName) {
        this.cryptogramname = cryptogramName;
    }

    public String getSolution() {
        return solution;
    }

    public void setSolution(String solution) {
        this.solution = solution;
    }

    public int getEasyAttempts() {
        return easyattempts;
    }

    public void setEasyAttempts(int easyAttempts) {
        this.easyattempts = easyAttempts;
    }

    public int getNormalAttempts() {
        return normalattempts;
    }

    public void setNormalAttempts(int normalAttempts) {
        this.normalattempts = normalAttempts;
    }

    public int getHardAttempts() {
        return hardattempts;
    }

    public void setHardAttempts(int hardAttempts) {
        this.hardattempts = hardAttempts;
    }
}
