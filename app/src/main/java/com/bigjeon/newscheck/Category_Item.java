package com.bigjeon.newscheck;

public class Category_Item {

    private String URL;

    private String writeDate;

    private String title;
    private int id;
    public String getWriteDate() {
        return writeDate;
    }

    //URL
    public void setWriteDate(String writeDate) {
        this.writeDate = writeDate;
    }
    public String getTitle() {
        return title;
    }

    //제목
    public void setTitle(String title) {
        this.title = title;
    }
    public int getId() {
        return id;
    }

    //각 뉴스당 id부여
    public void setId(int id) {
        this.id = id;
    }

    //URL지정
    public String getURL() {
        return URL;
    }
    public void setURL(String URL) {
        this.URL = URL;
    }
}
