package com.example.ajou.myapplication;

/**
 * Created by ajou on 2017-12-13.
 */

public class Myboardlist_item {
    String date;
    String desc;
    String brdIdx;
    public String getDate(){ return this.date; }
    public String getDesc(){ return this.desc;  }
    public String getBrdIdx(){ return this.brdIdx; }

    public Myboardlist_item(){}

    public Myboardlist_item(String date, String desc, String brdIdx){

        this.date = date;
        this.desc = desc;
        this.brdIdx = brdIdx;

    }
}