package com.vitelco.imdb.config.mapper;

import com.vitelco.imdb.persistence.entity.TitleEpisode;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;

public class TitleEpisodeFieldSetMapper implements FieldSetMapper<TitleEpisode> {

    @Override
    public TitleEpisode mapFieldSet(FieldSet fieldSet) {
        final TitleEpisode titleEpisode = new TitleEpisode();
        titleEpisode.setTconst(fieldSet.readString("tconst"));
        titleEpisode.setParentTconst(fieldSet.readString("parentTconst"));
        titleEpisode.setSeasonNumber(Integer.parseInt(fieldSet.readString("seasonNumber").replace("\\N","0")));
        titleEpisode.setEpisodeNumber(Integer.parseInt(fieldSet.readString("episodeNumber").replace("\\N","0")));
        return titleEpisode;

    }
}
