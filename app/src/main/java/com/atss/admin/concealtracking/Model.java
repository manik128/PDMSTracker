package com.atss.admin.concealtracking;

/**
 * Created by Admin on 27-06-2017.
 */

public class Model {
    private String sNo;
    private String items;
    private String qty;
    private String vqty;
    private String tqty;
    private Boolean select;
    private String delqty;
    private String ditems;


    public void setsNo(String sNo) {
        this.sNo = sNo;
    }

    public void setItems(String items) {
        this.items = items;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }

    public void setSelect(Boolean select) {
        this.select = select;
    }

//    public Model(String sNo, String items, String qty, String select) {
//        this.sNo = sNo;
//        this.items = items;
//        this.qty = qty;
//        this.select = select;
//    }

    public String getsNo() {
        return sNo;
    }

    public String getItems() {
        return items;
    }

    public String getQty() {
        return qty;
    }

    public Boolean getSelect() {
        return select;
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

    public String getDelqty() {
        return delqty;
    }

    public void setDelqty(String delqty) {
        this.delqty = delqty;
    }

    public String getDitems() {
        return ditems;
    }

    public void setDitems(String ditems) {
        this.ditems = ditems;
    }
}
