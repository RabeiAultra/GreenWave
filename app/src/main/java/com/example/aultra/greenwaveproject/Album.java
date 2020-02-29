package com.example.aultra.greenwaveproject;

public class Album {
    private String id;
    private String title;
    private String image;
    private String num_of_songs;


    public Album(String id,String title, String image, String num_of_songs){
        this.id=id;
        this.title=title;
        this.num_of_songs=num_of_songs;
        this.image=image;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getNum_of_songs() {
        return num_of_songs;
    }

    public void setNum_of_songs(String num_of_songs) {
        this.num_of_songs = num_of_songs;
    }
}
