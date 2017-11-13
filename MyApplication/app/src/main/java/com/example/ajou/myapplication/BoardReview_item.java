package com.example.ajou.myapplication;

/**
 * Created by thfad_000 on 2017-05-28.
 */
public class BoardReview_item {

    String name ;
    String desc;

    public String getName(){ return this.name; }
    public String getDesc(){
        return this.desc;
    }

    public BoardReview_item(String name, String desc){

        this.name=name;
        this.desc = desc;

    }
}



