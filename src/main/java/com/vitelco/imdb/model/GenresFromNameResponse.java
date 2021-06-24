package com.vitelco.imdb.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class GenresFromNameResponse {
    private String originalTitle;
    private String genres;

    public GenresFromNameResponse(String originalTitle, String genres) {
        this.originalTitle = originalTitle;
        this.genres = genres;
    }
}
