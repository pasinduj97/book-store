package com.koffemakerz.bookstore;

public class Model {
    private String id, title, author, description, price, genre, image;
    private String burrowed;

    public Model(String id, String title, String author, String description, String price, String genre, String image, String burrowed) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.description = description;
        this.price = price;
        this.genre = genre;
        this.image = image;
        this.burrowed = burrowed;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public String getDescription() {
        return description;
    }

    public String getPrice() {
        return price;
    }

    public String getGenre() {
        return genre;
    }

    public String getImage(){
        return image;
    }

    public String getBurrowed(){
        return burrowed;
    }
}
