package com.example.kilimoeasy;

public class CMoney {
    private String cpaymentId;
    private String cpaymentamount;
    private String cpaymentdate;
    private String cpaymentstatus;

    public CMoney(){

    }

    public CMoney(String cpaymentId, String cpaymentamount, String cpaymentdate, String cpaymentstatus) {
        this.cpaymentId = cpaymentId;
        this.cpaymentamount = cpaymentamount;
        this.cpaymentdate = cpaymentdate;
        this.cpaymentstatus = cpaymentstatus;
    }

    public String getCpaymentId() {
        return cpaymentId;
    }

    public String getCpaymentamount() {
        return cpaymentamount;
    }

    public String getCpaymentdate() {
        return cpaymentdate;
    }

    public String getCpaymentstatus() {
        return cpaymentstatus;
    }
}
