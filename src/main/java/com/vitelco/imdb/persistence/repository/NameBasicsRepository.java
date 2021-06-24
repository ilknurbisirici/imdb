package com.vitelco.imdb.persistence.repository;

import com.vitelco.imdb.persistence.entity.NameBasics;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface NameBasicsRepository extends JpaRepository<NameBasics, String> {
    List<NameBasics> findAllByPrimaryName(String primaryName);
    @Query(nativeQuery = true,value = "SELECT NCONST FROM TITLE_PRINCIPALS WHERE TCONST LIKE %:tconst%")
    List<String> getRelatedPersonsWithShows(String tconst);
}
