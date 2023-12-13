package com.example.bean;

public class Courier {
    private String courier_id = null;
    private String name = null;
    private String mobile = null;
    private String password = null;

    public String getCourier_id(){
        return courier_id;
    }
    public void setCourier_id(String courier_id){
        this.courier_id = courier_id;
    }
    public String getName(){
        return name;
    }
    public void setName(String name){
        this.name = name;
    }

    public String getMobile(){
        return mobile;
    }
    public void setMobile(String mobile){
        this.mobile = mobile;
    }

    public String getPassword(){
        return password;
    }
    public void  setPassword(String password){
        this.password = password;
    }




}
