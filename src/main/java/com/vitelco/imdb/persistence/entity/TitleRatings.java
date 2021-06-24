package com.vitelco.imdb.persistence.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToOne;

@Entity
@Data
public class TitleRatings {
    @Id
    private String tconst;
    private Double averageRating;
    private int numVotes;
}
