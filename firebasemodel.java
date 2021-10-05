package com.example.notesapp;

public class firebasemodel {
    private String title;
    private String content;
    public firebasemodel(){

    }
    public firebasemodel(String title,String content){
        this.content=content;
        this.title=title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getContent() {
        return content;
    }
}
