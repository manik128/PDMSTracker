package com.atss.admin.concealtracking;

import java.util.ArrayList;

/**
 * Created by Admin on 04-07-2017.
 */

public class ReturnDetails {
    private String sNo;
    private String items;
    private String delqty;
    private String devqty;
    private String vqty;
    private String tqty;
    private Boolean select;
    private String ditems;
    private ArrayList<String> allitems;

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

    public Boolean getSelect() {
        return select;
    }

    public void setSelect(Boolean select) {
        this.select = select;
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

    public String getDitems() {
        return ditems;
    }

    public void setDitems(String ditems) {
        this.ditems = ditems;
    }

    public ArrayList<String> getAllitems() {
        return allitems;
    }

    public void setAllitems(ArrayList<String> allitems) {
        this.allitems = allitems;
    }
}
