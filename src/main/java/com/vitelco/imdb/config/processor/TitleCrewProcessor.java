package com.vitelco.imdb.config.processor;

import com.vitelco.imdb.persistence.entity.TitleCrew;
import org.springframework.batch.item.ItemProcessor;

public class TitleCrewProcessor implements ItemProcessor<TitleCrew, TitleCrew> {

    @Override
    public TitleCrew process(final TitleCrew titleCrew) {
        return titleCrew;
    }
}
