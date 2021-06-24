package com.vitelco.imdb.config.processor;

import com.vitelco.imdb.persistence.entity.TitleBasics;
import org.springframework.batch.item.ItemProcessor;

public class TitleBasicsProcessor implements ItemProcessor<TitleBasics, TitleBasics> {

    @Override
    public TitleBasics process(final TitleBasics titleBasics) {
        return titleBasics;
    }
}
