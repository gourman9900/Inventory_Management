package com.irne.inventory_management;


public class QrCodes {
    private String sn;
    private String name;
    private String mdate;
    private String MaintainanceDate;
    private String maintainance_part;
    private String maintainance_remark;
    private String maintainanceid;

    public QrCodes() {
    }

    public String getsn() {
        return sn;
    }

    public void setsn(String sn) {
        this.sn = sn;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMdate() {
        return mdate;
    }

    public void setMdate(String mdate) {
        this.mdate = mdate;
    }

    public String getMaintainanceDate() {
        return MaintainanceDate;
    }

    public void setMaintainanceDate(String maintainanceDate) {
        MaintainanceDate = maintainanceDate;
    }

    public String getMaintainance_part() {
        return maintainance_part;
    }

    public void setMaintainance_part(String maintainance_part) {
        this.maintainance_part = maintainance_part;
    }

    public String getMaintainance_remark() {
        return maintainance_remark;
    }

    public void setMaintainance_remark(String maintainance_remark) {
        this.maintainance_remark = maintainance_remark;
    }

    public String getMaintainanceid() {
        return maintainanceid;
    }

    public void setMaintainanceid(String maintainanceid) {
        this.maintainanceid = maintainanceid;
    }
}
