package com.example.ajou.myapplication;

/**
 * Created by ajou on 2017-12-13.
 */

public class Mycommentlist_item {
    String comment;
    String date;
    String brdIdx;

    String getComment(){
        return comment;
    }
    String getDate(){
        return date;
    }
    String getBrdIdx() {return brdIdx;}

    public Mycommentlist_item(){}

    public Mycommentlist_item(String comment, String date, String brdIdx){
        this.date = date;
        this.comment = comment;
        this.brdIdx=brdIdx;
    }
}
