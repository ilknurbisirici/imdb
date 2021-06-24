package com.vitelco.imdb.config.processor;

import com.vitelco.imdb.persistence.entity.TitleRatings;
import org.springframework.batch.item.ItemProcessor;

public class TitleRatingsProcessor implements ItemProcessor<TitleRatings, TitleRatings> {

    @Override
    public TitleRatings process(final TitleRatings titleRatings) {
        return titleRatings;
    }
}
