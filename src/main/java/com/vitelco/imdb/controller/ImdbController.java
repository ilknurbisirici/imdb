package com.vitelco.imdb.controller;

import com.vitelco.imdb.model.*;
import com.vitelco.imdb.service.ImdbService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class ImdbController{
    private final ImdbService imdbService;

    @PostMapping(value = "/genres", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<GenresFromNameResponse>> genres(@RequestBody GenresFromNameRequest request) throws Exception {
        List<GenresFromNameResponse> result = imdbService.genresFromName(request.getPersonName());
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping(value = "/common-shows", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<CommonShowsResponse>> commonShows(@RequestBody CommonShowsRequest request) throws Exception {
        List<CommonShowsResponse> result = imdbService.commonShows(request);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping(value = "/distance", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<DistanceResponse> distance(@RequestBody DistanceRequest request) throws Exception {
        DistanceResponse result = imdbService.distance(request);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
