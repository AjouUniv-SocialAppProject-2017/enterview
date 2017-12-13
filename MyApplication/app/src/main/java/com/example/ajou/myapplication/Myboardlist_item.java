package com.example.ajou.myapplication;

/**
 * Created by ajou on 2017-12-13.
 */

public class Myboardlist_item {
    String text;
    String brdIdx;
    public String getText(){ return this.text; }
    public String getBrdIdx(){ return this.brdIdx; }

    public Myboardlist_item(){}

    public Myboardlist_item(String text, String brdIdx){

         this.text = text;
        this.brdIdx = brdIdx;

    }
}