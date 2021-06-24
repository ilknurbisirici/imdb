package com.vitelco.imdb.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommonShowsResponse {
    private String orginalTitle;
    private boolean adult;
    private String genres;
}
