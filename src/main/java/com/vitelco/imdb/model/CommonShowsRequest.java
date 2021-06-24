package com.vitelco.imdb.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CommonShowsRequest {
    private String firstPersonName;
    private String secondPersonName;
}
