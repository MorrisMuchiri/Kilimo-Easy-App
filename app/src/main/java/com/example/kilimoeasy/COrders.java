package com.example.kilimoeasy;

public class COrders {
    private String customerorderId;
    private String customerquantity;
    private String customerdate;
    private String customertotalprice;
    private String customerspinner;
    private String customerorderlocation;
    private String customerproduce;

    public COrders(){

    }

    public COrders(String customerorderId, String customerquantity, String customerdate,
                   String customertotalprice, String customerspinner, String customerorderlocation, String customerproduce) {
        this.customerorderId = customerorderId;
        this.customerquantity = customerquantity;
        this.customerdate = customerdate;
        this.customertotalprice = customertotalprice;
        this.customerspinner = customerspinner;
        this.customerorderlocation = customerorderlocation;
        this.customerproduce = customerproduce;
    }

    public String getCustomerorderId() {
        return customerorderId;
    }

    public String getCustomerquantity() { return customerquantity; }

    public String getCustomerdate() {
        return customerdate;
    }

    public String getCustomertotalprice() {
        return customertotalprice;
    }

    public String getCustomerspinner() {
        return customerspinner;
    }

    public String getCustomerorderlocation() {
        return customerorderlocation;
    }

    public String getCustomerproduce() {
        return customerproduce;
    }
}
