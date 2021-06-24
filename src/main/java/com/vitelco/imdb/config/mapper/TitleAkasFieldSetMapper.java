package com.vitelco.imdb.config.mapper;

import com.vitelco.imdb.persistence.entity.TitleAkas;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;

public class TitleAkasFieldSetMapper implements FieldSetMapper<TitleAkas> {

    @Override
    public TitleAkas mapFieldSet(FieldSet fieldSet) {
        final TitleAkas titleAkas = new TitleAkas();
        titleAkas.setTitleId(fieldSet.readString("titleId"));
        titleAkas.setOrdering(fieldSet.readInt("ordering"));
        titleAkas.setTitle(fieldSet.readString("title"));
        titleAkas.setRegion(fieldSet.readString("region"));
        titleAkas.setLanguage(fieldSet.readString("language"));
        titleAkas.setTypes(fieldSet.readString("types").replace("\\",""));
        titleAkas.setAttributes(fieldSet.readString("attributes"));
        titleAkas.setOriginalTitle(fieldSet.readBoolean("originalTitle"));

        return titleAkas;

    }
}
