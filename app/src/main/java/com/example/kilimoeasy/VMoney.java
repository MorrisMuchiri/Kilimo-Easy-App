package com.example.kilimoeasy;

public class VMoney {
    private String vpaymentId;
    private String vpaymentamount;
    private String vpaymentdate;
    private String vpaymentstatus;

    public VMoney(){

    }

    public VMoney(String vpaymentId, String vpaymentamount, String vpaymentdate, String vpaymentstatus) {
        this.vpaymentId = vpaymentId;
        this.vpaymentamount = vpaymentamount;
        this.vpaymentdate = vpaymentdate;
        this.vpaymentstatus = vpaymentstatus;
    }

    public String getVpaymentId() {
        return vpaymentId;
    }

    public String getVpaymentamount() {
        return vpaymentamount;
    }

    public String getVpaymentdate() {
        return vpaymentdate;
    }

    public String getVpaymentstatus() {
        return vpaymentstatus;
    }
}
