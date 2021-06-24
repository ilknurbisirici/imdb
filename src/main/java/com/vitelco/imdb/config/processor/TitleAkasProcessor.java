package com.vitelco.imdb.config.processor;
import com.vitelco.imdb.persistence.entity.TitleAkas;
import org.springframework.batch.item.ItemProcessor;

public class TitleAkasProcessor implements ItemProcessor<TitleAkas, TitleAkas> {

    @Override
    public TitleAkas process(final TitleAkas titleAkas) {
        return titleAkas;
    }
}
