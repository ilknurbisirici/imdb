package com.vitelco.imdb.persistence.entity;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data

public class NameBasics {
    @Id
    private String nconst;
    private String primaryName;
    private int birthYear;
    private int deathYear;
    private String primaryProfession;
    private String knownForTitles;
}
