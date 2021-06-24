package com.vitelco.imdb.config.processor;


import com.vitelco.imdb.persistence.entity.TitleEpisode;
import org.springframework.batch.item.ItemProcessor;

public class TitleEpisodeProcessor implements ItemProcessor<TitleEpisode, TitleEpisode> {

    @Override
    public TitleEpisode process(final TitleEpisode titleEpisode) {
        return titleEpisode;
    }
}
