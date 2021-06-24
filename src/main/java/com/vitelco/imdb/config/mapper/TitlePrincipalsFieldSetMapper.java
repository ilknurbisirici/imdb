package com.vitelco.imdb.config.mapper;

import com.vitelco.imdb.persistence.entity.TitlePrincipals;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;

public class TitlePrincipalsFieldSetMapper implements FieldSetMapper<TitlePrincipals> {

    @Override
    public TitlePrincipals mapFieldSet(FieldSet fieldSet) {
        final TitlePrincipals titlePrincipals = new TitlePrincipals();
        titlePrincipals.setTconst(fieldSet.readString("tconst"));
        titlePrincipals.setNconst(fieldSet.readString("nconst"));
        titlePrincipals.setCategory(fieldSet.readString("category"));
        titlePrincipals.setJob(fieldSet.readString("job"));
        titlePrincipals.setCharacters(fieldSet.readString("characters"));
        titlePrincipals.setOrdering(Integer.parseInt(fieldSet.readString("ordering").replace("\\N","0")));
        return titlePrincipals;

    }
}
