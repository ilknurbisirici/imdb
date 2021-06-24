package com.vitelco.imdb.config.batch;

import com.vitelco.imdb.config.NotificationListener;
import com.vitelco.imdb.config.mapper.TitleCrewFieldSetMapper;
import com.vitelco.imdb.config.processor.TitleCrewProcessor;
import com.vitelco.imdb.persistence.entity.TitleCrew;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import javax.sql.DataSource;

@Configuration
@EnableBatchProcessing
@RequiredArgsConstructor
public class TitleCrewBatch {
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    @Bean
    public FlatFileItemReader<TitleCrew> titleCrewReader() {
        return new FlatFileItemReaderBuilder<TitleCrew>()
                .name("titleCrewReader")
                .resource(new ClassPathResource("title.crew.tsv"))
                .delimited()
                .names(new String[]{"tconst", "directors","writers"})
                .lineMapper(titleCrewLineMapper())
                .maxItemCount(1000)
                .fieldSetMapper(new BeanWrapperFieldSetMapper<TitleCrew>() {{
                    setTargetType(TitleCrew.class);
                }})
                .build();
    }

    @Bean
    public LineMapper<TitleCrew> titleCrewLineMapper() {

        final DefaultLineMapper<TitleCrew> defaultLineMapper = new DefaultLineMapper<>();
        final DelimitedLineTokenizer lineTokenizer = new DelimitedLineTokenizer();
        lineTokenizer.setDelimiter("\t");
        lineTokenizer.setStrict(false);
        lineTokenizer.setNames(new String[] {"tconst", "directors","writers"});
        final TitleCrewFieldSetMapper fieldSetMapper = new TitleCrewFieldSetMapper();
        defaultLineMapper.setFieldSetMapper(fieldSetMapper);
        defaultLineMapper.setLineTokenizer(lineTokenizer);
        return defaultLineMapper;
    }

    @Bean
    public TitleCrewProcessor titleCrewProcessor() {
        return new TitleCrewProcessor();
    }

    @Bean
    public JdbcBatchItemWriter<TitleCrew> titleCrewWriter(final DataSource dataSource) {
        return new JdbcBatchItemWriterBuilder<TitleCrew>()
                .itemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>())
                .sql("INSERT INTO TITLE_CREW (TCONST, DIRECTORS, WRITERS) VALUES (:tconst, :directors, :writers)")
                .dataSource(dataSource)
                .build();
    }

    @Bean
    public Job importTitleCrewJob(NotificationListener listener, Step step4) {
        return jobBuilderFactory.get("importTitleCrewJob")
                .incrementer(new RunIdIncrementer())
                .listener(listener)
                .flow(step4)
                .end()
                .build();
    }

    @Bean
    public Step step4(JdbcBatchItemWriter<TitleCrew> writer) {
        return stepBuilderFactory.get("step4")
                .<TitleCrew, TitleCrew> chunk(10)
                .reader(titleCrewReader())
                .processor(titleCrewProcessor())
                .writer(writer)
                .build();
    }
}
