package com.abood.blog;

import org.json.JSONException;
import org.json.JSONObject;
import java.util.Date;

public class Answers {

    private String aId;
    private String aUser;
    private Date Date;
    private String aDate;
    private String aContent;
    private String pId;


    public Answers() {

        Date = new Date();

    }

    public String getaId() {
        return aId;
    }

    public void setaId(String aId) {
        this.aId = aId;
    }

    public String getaUser() {
        return aUser;
    }

    public void setaUser(String aUser) {
        this.aUser = aUser;
    }

    public java.util.Date getDate() {
        return Date;
    }

    public String getaDate() {
        return aDate;
    }

    public void setaDate(String aDate) {
        this.aDate = aDate;
    }

    public String getaContent() {
        return aContent;
    }

    public void setaContent(String aContent) {
        this.aContent = aContent;
    }

    public String getpId() {
        return pId;
    }

    public void setpId(String pId) {
        this.pId = pId;
    }


    static Answers createTask(JSONObject answer) throws JSONException {

        Answers aAnswers = new Answers();

        aAnswers.setaUser(answer.getString("user"));
        aAnswers.setaContent(answer.getString("answer"));

        return  aAnswers;

    }

}
