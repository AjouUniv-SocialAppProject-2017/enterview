package com.example.ajou.myapplication;

/**
 * Created by ajou on 2017-12-13.
 */

public class Mycommentlist_item {
    String comment;
    String date;

    String getComment(){
        return comment;
    }
    String getDate(){
        return date;
    }

    public Mycommentlist_item(){}

    public Mycommentlist_item(String comment, String date){
        this.date = date;
        this.comment = comment;
    }
}
