package com.example.kilimoeasy;

public class VOrders {
    private String vendororderId;
    private String vendorquantity;
    private String vendordate;
    private String vendortotalprice;
    private String vendorspinner;
    private String vendororderlocation;
    private String vendorproduce;

    public VOrders(){

    }

    public VOrders(String vendororderId, String vendorquantity, String vendordate,
                   String vendortotalprice, String vendorspinner, String vendororderlocation, String vendorproduce) {
        this.vendororderId = vendororderId;
        this.vendorquantity = vendorquantity;
        this.vendordate = vendordate;
        this.vendortotalprice = vendortotalprice;
        this.vendorspinner = vendorspinner;
        this.vendororderlocation = vendororderlocation;
        this.vendorproduce = vendorproduce;
    }

    public String getVendororderId() {
        return vendororderId;
    }

    public String getVendorquantity() {
        return vendorquantity;
    }

    public String getVendordate() {
        return vendordate;
    }

    public String getVendortotalprice() {
        return vendortotalprice;
    }

    public String getVendorspinner() {
        return vendorspinner;
    }

    public String getVendororderlocation() {
        return vendororderlocation;
    }

    public String getVendorproduce() {
        return vendorproduce;
    }
}
