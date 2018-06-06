package com.gxg.entities;

import org.json.JSONObject;

import java.sql.Timestamp;

/**
 * Created by 郭欣光 on 2018/5/28.
 */

public class Record {

    private String id;
    private String imgSrc;
    private String saveSrc;
    private int imgAll;
    private int imgAlready;
    private int imgError;
    private int processNumber;
    private int processTime;
    private Timestamp time;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImgSrc() {
        return imgSrc;
    }

    public void setImgSrc(String imgSrc) {
        this.imgSrc = imgSrc;
    }

    public String getSaveSrc() {
        return saveSrc;
    }

    public void setSaveSrc(String saveSrc) {
        this.saveSrc = saveSrc;
    }

    public int getImgAll() {
        return imgAll;
    }

    public void setImgAll(int imgAll) {
        this.imgAll = imgAll;
    }

    public int getImgAlready() {
        return imgAlready;
    }

    public void setImgAlready(int imgAlready) {
        this.imgAlready = imgAlready;
    }

    public int getImgError() {
        return imgError;
    }

    public void setImgError(int imgError) {
        this.imgError = imgError;
    }

    public int getProcessNumber() {
        return processNumber;
    }

    public void setProcessNumber(int processNumber) {
        this.processNumber = processNumber;
    }

    public int getProcessTime() {
        return processTime;
    }

    public void setProcessTime(int processTime) {
        this.processTime = processTime;
    }

    public Timestamp getTime() {
        return time;
    }

    public void setTime(Timestamp time) {
        this.time = time;
    }

    public JSONObject toJSON() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.accumulate("id", id);
        jsonObject.accumulate("img_src", imgSrc);
        jsonObject.accumulate("save_src", saveSrc);
        jsonObject.accumulate("img_all", imgAll);
        jsonObject.accumulate("img_already", imgAlready);
        jsonObject.accumulate("img_error", imgError);
        jsonObject.accumulate("process_number", processNumber);
        jsonObject.accumulate("process_time", processTime);
        jsonObject.accumulate("time", time);
        return jsonObject;
    }
}
