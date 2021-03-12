package com.example.bg_rem.act.activity;

public class PostItem {
    // django 서버의 모델과 동일하게 구성
    private String title;
    private String text;
//    private String img;
//    private Image img;

    public String getText() {
        return text;
    }

    public String getTitle(){
        return title;
    }

//    public String getImage(){
//        return img;
//    }

    public void setTitle(String s){
        title = s;
    }

    public void setText(String s){
        text = s;
    }

//    public void setImage(String image) { img = image;
//        this.img = image;
//    }
}