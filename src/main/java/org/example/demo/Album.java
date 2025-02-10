package org.example.demo;

import java.util.ArrayList;

public class Album {
    private String title;
    private String artist;
    private String genre;
    private ArrayList<Song> songs;

    public Album (String title, String artist, String genre, ArrayList<Song> songs) {
        this.title = title;
        this.artist = artist;
        this.genre = genre;
        this.songs = songs;
    }

    public Album () {}

    public ArrayList<Song> getSongs() {
        return songs;
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
