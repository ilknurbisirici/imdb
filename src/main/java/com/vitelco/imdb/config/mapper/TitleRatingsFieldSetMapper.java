package com.vitelco.imdb.config.mapper;

import com.vitelco.imdb.persistence.entity.TitleRatings;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;

public class TitleRatingsFieldSetMapper implements FieldSetMapper<TitleRatings> {

    @Override
    public TitleRatings mapFieldSet(FieldSet fieldSet) {
        final TitleRatings titleRatings = new TitleRatings();
        titleRatings.setTconst(fieldSet.readString("tconst"));
        titleRatings.setNumVotes(Integer.parseInt(fieldSet.readString("numVotes").replace("\\N", "0")));
        titleRatings.setAverageRating(Double.parseDouble(fieldSet.readString("averageRating").replace("\\N", "0")));
        return titleRatings;

    }
}
