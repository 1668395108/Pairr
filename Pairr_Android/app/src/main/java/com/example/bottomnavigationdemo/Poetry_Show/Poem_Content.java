package com.example.bottomnavigationdemo.Poetry_Show;

public class Poem_Content {
    private String poemname;
    private String dynasty;
    private String name;
    private String content;
    private String translation;
    private String comment;
    private String appreciation;

    public Poem_Content(String poemname, String dynasty, String name, String content) {
        this.poemname = poemname;
        this.dynasty = dynasty;
        this.name = name;
        this.content = content;
    }

    public Poem_Content() {
        this.poemname = poemname;
        this.dynasty = dynasty;
        this.name = name;
        this.content = content;
        this.translation = translation;
        this.comment = comment;
        this.appreciation = appreciation;
    }

    public String getPoemname() {
        return poemname;
    }

    public void setPoemname(String poemname) {
        this.poemname = poemname;
    }

    public String getDynasty() {
        return dynasty;
    }

    public void setDynasty(String dynasty) {
        this.dynasty = dynasty;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTranslation() {
        return translation;
    }

    public void setTranslation(String translation) {
        this.translation = translation;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getAppreciation() {
        return appreciation;
    }

    public void setAppreciation(String appreciation) {
        this.appreciation = appreciation;
    }
}
