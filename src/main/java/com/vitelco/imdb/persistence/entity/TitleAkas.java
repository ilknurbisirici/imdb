package com.vitelco.imdb.persistence.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@Data
public class TitleAkas {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String titleId;
    private int ordering;
    private String title;
    private String region;
    private String language;
    private String types;
    private String attributes;
    private boolean originalTitle;
}
