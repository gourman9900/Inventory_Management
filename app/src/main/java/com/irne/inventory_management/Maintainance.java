package com.irne.inventory_management;

public class Maintainance {
    private String sn,parts,remarks,maintainance_date,maintainance_id;

    public Maintainance(){

    }

    public Maintainance(String sn, String parts, String remarks, String maintainance_date, String maintainance_id) {
        this.sn = sn;
        this.parts = parts;
        this.remarks = remarks;
        this.maintainance_date = maintainance_date;
        this.maintainance_id = maintainance_id;
    }

    public String getsn() {
        return sn;
    }

    public void setsn(String sn) {
        this.sn = sn;
    }

    public String getParts() {
        return parts;
    }

    public void setParts(String parts) {
        this.parts = parts;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getMaintainance_date() {
        return maintainance_date;
    }

    public void setMaintainance_date(String maintainance_date) {
        this.maintainance_date = maintainance_date;
    }

    public String getMaintainance_id() {
        return maintainance_id;
    }

    public void setMaintainance_id(String maintainance_id) {
        this.maintainance_id = maintainance_id;
    }
}
