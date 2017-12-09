package com.example.ajou.myapplication;

/**
 * 프레그먼트 2번째 게시판
 * 날짜, 닉네임, 사진, 글, 별점, 댓글, 제목(질문)
 */
public class Board_item {

    int image;
    String name ;
    String title;
    String date;
    String desc;
    String comment;
    String url;

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
    public String getUrl(){ return this.url; }

    public Board_item(){}

    public Board_item(int image, String name, String title, String date, String desc, String comment,String url){

        this.image=image;
        this.name=name;
        this.title=title;
        this.date = date;
        this.desc = desc;
        this.comment = comment;
        this.url = url;

    }

}