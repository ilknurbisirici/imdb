package com.vitelco.imdb.config.mapper;

import com.vitelco.imdb.persistence.entity.TitleBasics;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;

public class TitleBasicsFieldSetMapper implements FieldSetMapper<TitleBasics> {

    @Override
    public TitleBasics mapFieldSet(FieldSet fieldSet) {
        final TitleBasics titleBasics = new TitleBasics();
        titleBasics.setTconst(fieldSet.readString("tconst"));
        titleBasics.setTitleType(fieldSet.readString("titleType"));
        titleBasics.setPrimaryTitle(fieldSet.readString("primaryTitle"));
        titleBasics.setOriginalTitle(fieldSet.readString("originalTitle"));
        titleBasics.setAdult(fieldSet.readBoolean("adult"));
        titleBasics.setStartYear(Integer.parseInt(fieldSet.readString("startYear").replace("\\N","0")));
        titleBasics.setEndYear(Integer.parseInt(fieldSet.readString("endYear").replace("\\N","0")));
        titleBasics.setRuntimeMinutes(Integer.parseInt(fieldSet.readString("runtimeMinutes").replace("\\N","0")));
        titleBasics.setGenres(fieldSet.readString("genres"));

        return titleBasics;

    }
}
