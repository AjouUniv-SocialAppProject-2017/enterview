package com.example.ajou.myapplication;

/**
 * Created by thfad_000 on 2017-05-05.
 * 프레그먼트 3번째 자랑글 리스트 아이템
 * 날짜, 닉네임, 사진, 글, 좋아요, 댓글
 */
public class Board_item {

    int image;
    String name ;
    String title;
    String date;
    String desc;
    String comment;

    public int getImage(){
        return this.image;
    }
    public String getName(){ return this.name; }
    public String getTitle(){ return this.title; }
    public String getDate(){
        return this.date;
    }
    public String getDesc(){
        return this.desc;
    }
    public String getComment(){ return this.comment; }

    public Board_item(){}

    public Board_item(int image, String name, String title, String date, String desc, String comment){

        this.image=image;
        this.name=name;
        this.title=title;
        this.date = date;
        this.desc = desc;
        this.comment = comment;

    }

}