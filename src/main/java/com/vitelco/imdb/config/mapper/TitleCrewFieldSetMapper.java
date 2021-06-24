package com.vitelco.imdb.config.mapper;
import com.vitelco.imdb.persistence.entity.TitleCrew;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;

public class TitleCrewFieldSetMapper implements FieldSetMapper<TitleCrew> {

    @Override
    public TitleCrew mapFieldSet(FieldSet fieldSet) {
        final TitleCrew titleCrew = new TitleCrew();
        titleCrew.setTconst(fieldSet.readString("tconst"));
        titleCrew.setDirectors(fieldSet.readString("directors"));
        titleCrew.setWriters(fieldSet.readString("directors"));
        return titleCrew;

    }
}
