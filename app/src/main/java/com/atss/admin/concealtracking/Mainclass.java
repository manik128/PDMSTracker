package com.atss.admin.concealtracking;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

import java.util.ArrayList;

/**
 * Created by Admin on 08-02-2017.
 */

public class Mainclass extends Application {
    String address;
    String locadd;
    String name;
    String mobile;
    String email;
    String empid;
    String taskdate;
    String frtime;
    String totime;
    String taskdescription;
    String contactperson;
       String  fromaddress;
    ArrayList<Model> ar;
    ArrayList<String> ar1;
    String status;
  int otp;
Double lat;

    Double lang;
String Bde;
    private String sNo;
    private String items;
    private String delqty;
    private String devqty;
    private String vqty;
    private String tqty;
    private Boolean select;
    private String ditems;
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }
    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getLocadd() {
        return locadd;
    }

    public void setLocadd(String locadd) {
        this.locadd = locadd;
    }


    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLang() {
        return lang;
    }

    public void setLang(Double lang) {
        this.lang = lang;
    }

    public String getBde() {
        return Bde;
    }

    public void setBde(String bde) {
        Bde = bde;
    }

    public int getOtp() {
        return otp;
    }

    public void setOtp(int otp) {
        this.otp = otp;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getEmpid() {
        return empid;
    }

    public void setEmpid(String empid) {
        this.empid = empid;
    }

    public String getTaskdate() {
        return taskdate;
    }

    public void setTaskdate(String taskdate) {
        this.taskdate = taskdate;
    }

    public String getTotime() {
        return totime;
    }

    public void setTotime(String totime) {
        this.totime = totime;
    }

    public String getFrtime() {
        return frtime;
    }

    public void setFrtime(String frtime) {
        this.frtime = frtime;
    }

    public String getTaskdescription() {
        return taskdescription;
    }

    public void setTaskdescription(String taskdescription) {
        this.taskdescription = taskdescription;
    }

    public String getContactperson() {
        return contactperson;
    }

    public void setContactperson(String contactperson) {
        this.contactperson = contactperson;
    }

    public String getFromaddress() {
        return fromaddress;
    }

    public void setFromaddress(String fromaddress) {
        this.fromaddress = fromaddress;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getsNo() {
        return sNo;
    }

    public void setsNo(String sNo) {
        this.sNo = sNo;
    }

    public String getItems() {
        return items;
    }

    public void setItems(String items) {
        this.items = items;
    }

    public String getDelqty() {
        return delqty;
    }

    public void setDelqty(String delqty) {
        this.delqty = delqty;
    }

    public String getDevqty() {
        return devqty;
    }

    public void setDevqty(String devqty) {
        this.devqty = devqty;
    }

    public String getVqty() {
        return vqty;
    }

    public void setVqty(String vqty) {
        this.vqty = vqty;
    }

    public String getTqty() {
        return tqty;
    }

    public void setTqty(String tqty) {
        this.tqty = tqty;
    }

    public String getDitems() {
        return ditems;
    }

    public void setDitems(String ditems) {
        this.ditems = ditems;
    }

    public ArrayList<Model> getAr() {
        return ar;
    }

    public void setAr(ArrayList<Model> ar) {
        this.ar = ar;
    }

    public ArrayList<String> getAr1() {
        return ar1;
    }

    public void setAr1(ArrayList<String> ar1) {
        this.ar1 = ar1;
    }
}
