package com.vitelco.imdb.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;

@Data
@NoArgsConstructor
public class PersonNode {
    private String primaryName;
    private ArrayList<String> shows = new ArrayList<>();
    private int distance;
    private PersonNode prev;
    public PersonNode(String primaryName) {
        this.primaryName = primaryName;
    }
}
