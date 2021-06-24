package com.vitelco.imdb.persistence.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Data
public class TitleCrew {
    @Id
    private String tconst;
    private String directors;
    private String writers;
}
