package com.vitelco.imdb.persistence.repository;

import com.vitelco.imdb.persistence.entity.TitleBasics;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TitleBasicsRepository extends JpaRepository<TitleBasics,String> {
    TitleBasics findAllByTconst(String tconst);
}
