package com.example.kilimoeasy;

public class FMoney {
    private String fpaymentId;
    private String fpaymentamount;
    private String fpaymentdate;
    private String fpaymentstatus;

    public FMoney(){

    }

    public FMoney(String fpaymentId, String fpaymentamount, String fpaymentdate, String fpaymentstatus) {
        this.fpaymentId = fpaymentId;
        this.fpaymentamount = fpaymentamount;
        this.fpaymentdate = fpaymentdate;
        this.fpaymentstatus = fpaymentstatus;
    }

    public String getFpaymentId() {
        return fpaymentId;
    }

    public String getFpaymentamount() {
        return fpaymentamount;
    }

    public String getFpaymentdate() {
        return fpaymentdate;
    }

    public String getFpaymentstatus() {
        return fpaymentstatus;
    }
}
