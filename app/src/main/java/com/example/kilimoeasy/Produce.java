package com.example.kilimoeasy;

public class Produce {
    String type_id;
    String pname;
    String sale_price;
    String buy_price;


    public Produce(){

    }

    public Produce(String type_id, String pname, String sale_price, String buy_price) {
        this.type_id = type_id;
        this.pname = pname;
        this.sale_price = sale_price;
        this.buy_price = buy_price;
    }

    public String getType_id() {
        return type_id;
    }

    public String getPname() {
        return pname;
    }

    public String getSale_price() {
        return sale_price;
    }

    public String getBuy_price() {
        return buy_price;
    }
}
