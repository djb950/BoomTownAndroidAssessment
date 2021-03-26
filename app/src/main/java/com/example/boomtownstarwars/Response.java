package com.example.boomtownstarwars;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class Response {

    private String count;
    private String next;
    private String previous;

    @SerializedName("results")
    private List<Person> people = new ArrayList<>();

    public String getNext() {
        return next;
    }

    public List<Person> getPeople() {
        return people;
    }
}
