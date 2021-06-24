package com.vitelco.imdb.config.mapper;

import com.vitelco.imdb.persistence.entity.NameBasics;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.stereotype.Component;

@Component
public class NameBasicsFieldSetMapper implements FieldSetMapper<NameBasics> {

    @Override
    public NameBasics mapFieldSet(FieldSet fieldSet) {
        final NameBasics nameBasics = new NameBasics();
        nameBasics.setNconst(fieldSet.readString("nconst"));
        nameBasics.setKnownForTitles(fieldSet.readString("knownForTitles"));
        nameBasics.setPrimaryProfession(fieldSet.readString("primaryProfession"));
        nameBasics.setPrimaryName(fieldSet.readString("primaryName"));
        nameBasics.setDeathYear(fieldSet.readString("deathYear").equals("\\N") ? 0 : fieldSet.readInt("deathYear"));
        nameBasics.setBirthYear(fieldSet.readString("birthYear").equals("\\N") ? 0 : fieldSet.readInt("birthYear"));
        return nameBasics;

    }

}
