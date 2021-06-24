package com.vitelco.imdb.config.processor;

import com.vitelco.imdb.persistence.entity.TitlePrincipals;
import org.springframework.batch.item.ItemProcessor;

public class TitlePrincipalsProcessor implements ItemProcessor<TitlePrincipals, TitlePrincipals> {

    @Override
    public TitlePrincipals process(final TitlePrincipals titlePrincipals) {
        return titlePrincipals;
    }
}
