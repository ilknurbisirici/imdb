package com.vitelco.imdb.service;

import com.vitelco.imdb.model.*;

import java.util.List;

public interface ImdbService {
    public List<GenresFromNameResponse> genresFromName(String name) throws Exception;
    public List<CommonShowsResponse> commonShows(CommonShowsRequest commonShowsRequest);
    public DistanceResponse distance(DistanceRequest distanceRequest) throws Exception;
}
