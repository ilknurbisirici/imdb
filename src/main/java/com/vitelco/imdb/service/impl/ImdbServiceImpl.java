package com.vitelco.imdb.service.impl;

import com.vitelco.imdb.exceptions.BadRequestException;
import com.vitelco.imdb.exceptions.InvalidArgumentException;
import com.vitelco.imdb.model.*;
import com.vitelco.imdb.persistence.entity.NameBasics;
import com.vitelco.imdb.persistence.entity.TitleBasics;
import com.vitelco.imdb.persistence.repository.NameBasicsRepository;
import com.vitelco.imdb.persistence.repository.TitleBasicsRepository;
import com.vitelco.imdb.service.ImdbService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class ImdbServiceImpl implements ImdbService {
    private final NameBasicsRepository nameBasicsRepository;
    private final TitleBasicsRepository titleBasicsRepository;
    private static Map<String, List<PersonNode>> graph = new HashMap<>();
    private static final String TITLE_SEPERATOR = ",";
    private static final String KEVIN_BACON_NAME = "Kevin Bacon";
    private static final String KEVIN_BACON_ID = "nm0000102";
    private static final String PERSON_NAME_REQUIRED = "Person name is required.";
    private static final String PERSON_IS_NOT_FOUND= "Person is not found.";

    @Override
    public List<GenresFromNameResponse> genresFromName(String name) throws Exception{
        List<String> titleList = getTitlesWithPersonName(name);
        if(titleList.isEmpty()){
            throw new InvalidArgumentException(PERSON_IS_NOT_FOUND);
        }
        List<TitleBasics> titleBasics = titleList.stream().map(titleBasicsRepository::findAllByTconst).collect(Collectors.toList());
        List<GenresFromNameResponse> genresFromNameResponses = titleBasics.stream()
                .map(x -> x != null ? new GenresFromNameResponse(x.getOriginalTitle(), x.getGenres()) : null)
                .collect(Collectors.toList());
        return genresFromNameResponses;
    }

    public List<CommonShowsResponse> commonShows(CommonShowsRequest commonShowsRequest) {
        List<String> firstPersonTitles = getTitlesWithPersonName(commonShowsRequest.getFirstPersonName());
        List<String> secondPersonTitles = getTitlesWithPersonName(commonShowsRequest.getSecondPersonName());
        List<String> commonTitles = firstPersonTitles.stream()
                .filter(secondPersonTitles::contains)
                .collect(Collectors.toList());
        List<TitleBasics> titleBasics = commonTitles.stream()
                .map(titleBasicsRepository::findAllByTconst)
                .collect(Collectors.toList());
        List<CommonShowsResponse> commonShowsResponses = titleBasics.stream()
                .map(x -> x != null ? new CommonShowsResponse(x.getOriginalTitle(), x.isAdult(), x.getGenres()) : null)
                .collect(Collectors.toList());
        return commonShowsResponses;

    }


    public List<String> getTitlesWithPersonName(String name) {
        List<String> titleList = new ArrayList<>();
        for (String s : nameBasicsRepository.findAllByPrimaryName(name).stream()
                .map(NameBasics::getKnownForTitles)
                .collect(Collectors.toList())) {
            String[] title = s.split(TITLE_SEPERATOR);
            titleList.addAll(Arrays.asList(title));
        }
        return titleList;
    }
    @Override
    public DistanceResponse distance(DistanceRequest distanceRequest) throws Exception{
        if( distanceRequest.getPersonName() == null) {
            throw new BadRequestException(PERSON_NAME_REQUIRED);
        }
        String person = nameBasicsRepository.findAllByPrimaryName(distanceRequest.getPersonName()).get(0).getNconst();
        if(distanceRequest.getPersonName().equals(KEVIN_BACON_NAME)) {
            return new DistanceResponse(-1);
        }
        if( person == null) {
            throw new InvalidArgumentException(PERSON_IS_NOT_FOUND);
        }
        Map<String, List<PersonNode>> graphResult = createPersonGraph();
        graph = new HashMap<>();

        Queue<PersonNode> queue = new LinkedList<PersonNode>();
        PersonNode mainActor = new PersonNode(person);
        mainActor.setDistance(0);
        queue.add(mainActor);
        Set<String> visited = new HashSet<String>();
        visited.add(person);
        while(!queue.isEmpty()) {
            PersonNode last = queue.remove();
            String lastNode = last.getPrimaryName();
            if(lastNode.equals(KEVIN_BACON_ID))  {
                return new DistanceResponse(last.getDistance());
            }
            for(PersonNode node: graphResult.get(lastNode)) {
                node.setDistance(last.getDistance() + 1);
                node.setPrev(last);
                if(!visited.contains(node.getPrimaryName())) {
                    queue.add(node);
                    visited.add(node.getPrimaryName());
                }
            }
        }
        return new DistanceResponse(-1);
    }



    private Map<String, List<PersonNode>> createPersonGraph() {

        List<String> showList = getAllShows();
        for(String show: showList){
            List<String> relatedPersonList = nameBasicsRepository.getRelatedPersonsWithShows(show);
            addPersonsToGraph(relatedPersonList,show);
        }
        return graph;
    }

    private Map<String, List<PersonNode>> addPersonsToGraph(List<String> personList, String show) {
        for(String person: personList){
            if(graph.get(person)!=null) {
                for(String left: personList) {
                    if(!left.equals(person)) {
                        Boolean contains = false;
                        List<PersonNode> list = graph.get(person);
                        if(!list.isEmpty()){
                            for(PersonNode relatedPerson: list) {
                                if(relatedPerson.getPrimaryName().equals(left)) {
                                    relatedPerson.getShows().add(person);
                                    contains = true;
                                    break;
                                }
                            }
                            if(!contains) {
                                PersonNode newNode = new PersonNode(left);
                                newNode.getShows().add(person);
                                graph.get(person).add(newNode);
                            }
                        }
                    }
                }
            }
            else {
                List<PersonNode> newList = new ArrayList<PersonNode>();
                for(String left: personList){
                    if(!left.equals(person)) {
                        PersonNode node = new PersonNode(left);
                        node.getShows().add(show);
                        newList.add(node);
                    }
                }
                graph.put(person, newList);
            }
        }
        return graph;
    }

    private List<String> getAllShows(){
        List<String> titleList = new ArrayList<>();
        for (String s : nameBasicsRepository.findAll().stream()
                .map(NameBasics::getKnownForTitles)
                .collect(Collectors.toList())) {
            String[] title = s.split(TITLE_SEPERATOR);
            titleList.addAll(Arrays.asList(title));
        }

        return titleList.stream().distinct().collect(Collectors.toList());
    }
}
