package com.vitelco.imdb.persistence.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Data
public class TitleEpisode {
    @Id
    private String tconst;
    private String parentTconst;
    private int seasonNumber;
    private int episodeNumber;
}
