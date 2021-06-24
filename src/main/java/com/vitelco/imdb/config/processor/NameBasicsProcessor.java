package com.vitelco.imdb.config.processor;

import com.vitelco.imdb.persistence.entity.NameBasics;
import org.springframework.batch.item.ItemProcessor;

public class NameBasicsProcessor implements ItemProcessor<NameBasics, NameBasics>{

    @Override
    public NameBasics process(final NameBasics nameBasics) {
        return nameBasics;
    }
}
