package com.example.kilimoeasy;

public class FOrders {
    private String farmerorderId;
    private String farmerquantity;
    private String farmerdate;
    private String farmertotalprice;
    private String farmerspinner;
    private String farmerorderlocation;
    private String farmerproduce;

    public FOrders(){

    }

    public FOrders(String farmerorderId, String farmerquantity, String farmerdate,
                   String farmertotalprice, String farmerspinner, String farmerorderlocation, String farmerproduce) {
        this.farmerorderId = farmerorderId;
        this.farmerquantity = farmerquantity;
        this.farmerdate = farmerdate;
        this.farmertotalprice = farmertotalprice;
        this.farmerspinner = farmerspinner;
        this.farmerorderlocation = farmerorderlocation;
        this.farmerproduce = farmerproduce;
    }

    public String getFarmerorderId() {
        return farmerorderId;
    }

    public String getFarmerquantity() {
        return farmerquantity;
    }

    public String getFarmerdate() {
        return farmerdate;
    }

    public String getFarmertotalprice() {
        return farmertotalprice;
    }

    public String getFarmerspinner() {
        return farmerspinner;
    }

    public String getFarmerorderlocation() {
        return farmerorderlocation;
    }

    public String getFarmerproduce() {
        return farmerproduce;
    }
}

