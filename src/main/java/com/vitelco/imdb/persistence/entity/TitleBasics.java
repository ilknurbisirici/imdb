package com.vitelco.imdb.persistence.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.lang.reflect.Array;

@Entity
@Data
public class TitleBasics {
    @Id
    private String tconst;
    private String titleType;
    private String primaryTitle;
    private String originalTitle;
    private boolean adult;
    private int startYear;
    private int endYear;
    private int runtimeMinutes;
    private String genres;
}
