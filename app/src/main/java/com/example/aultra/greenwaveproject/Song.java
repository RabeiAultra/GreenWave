package com.example.aultra.greenwaveproject;



public class Song {
    private String id;
    private String title;
    private String image;
    private String description;
    private String url;
    private String duration;
    private String next;
    private String previous;
    private String isFavorite;

    public Song(String id, String title, String image, String description, String url, String duration, String next, String previous, String isFavorite) {
        this.id = id;
        this.title = title;
        this.image = image;
        this.description = description;
        this.url = url;
        this.duration = duration;
        this.next = next;
        this.previous = previous;
        this.isFavorite = isFavorite;
    }

    @Override
    public String toString() {
        return "Song{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", image='" + image + '\'' +
                ", description='" + description + '\'' +
                ", url='" + url + '\'' +
                ", duration='" + duration + '\'' +
                '}';
    }

    public Song(String id, String title, String image, String description, String url, String duration, String next, String previous) {
        this.id = id;
        this.title = title;
        this.image = image;
        this.description = description;
        this.url = url;
        this.duration = duration;
        this.next = next;
        this.previous = previous;
    }

    public Song(String id, String title, String image, String description, String url, String duration) {
        this.id = id;
        this.title = title;
        this.image = image;
        this.description = description;
        this.url = url;
        this.duration = duration;
    }

    public Song(String id, String title, String image, String duration) {
        this.id = id;
        this.title = title;
        this.image = image;
        this.duration = duration;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getNext() {
        return next;
    }

    public void setNext(String next) {
        this.next = next;
    }

    public String getPrevious() {
        return previous;
    }

    public void setPrevious(String previous) {
        this.previous = previous;
    }

    public boolean getIsFavorite() {
        if(isFavorite.equalsIgnoreCase("True")){return true;}
        return false;
    }

    public void setIsFavorite(String isFavorite) {
        this.isFavorite = isFavorite;
    }


}

