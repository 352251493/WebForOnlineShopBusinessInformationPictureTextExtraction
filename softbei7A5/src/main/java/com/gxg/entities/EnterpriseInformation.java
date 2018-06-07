package com.gxg.entities;

import org.json.JSONObject;

/**
 * Created by 郭欣光 on 2018/6/7.
 */

public class EnterpriseInformation {

    private String name;
    private String registrationNumber;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRegistrationNumber() {
        return registrationNumber;
    }

    public void setRegistrationNumber(String registrationNumber) {
        this.registrationNumber = registrationNumber;
    }

    public JSONObject toJson() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.accumulate("name", name);
        jsonObject.accumulate("registration_number", registrationNumber);
        return jsonObject;
    }
}
