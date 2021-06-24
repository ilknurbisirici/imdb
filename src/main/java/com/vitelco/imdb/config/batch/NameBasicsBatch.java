package com.vitelco.imdb.config.batch;
import com.vitelco.imdb.config.NotificationListener;
import com.vitelco.imdb.config.mapper.NameBasicsFieldSetMapper;
import com.vitelco.imdb.config.processor.NameBasicsProcessor;
import com.vitelco.imdb.persistence.entity.NameBasics;
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
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import javax.sql.DataSource;

@Configuration
@EnableBatchProcessing
@RequiredArgsConstructor
public class NameBasicsBatch {
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    @Bean
    public FlatFileItemReader<NameBasics> nameBasicsReader() {
        return new FlatFileItemReaderBuilder<NameBasics>()
                .name("nameBasicsReader")
                .resource(new ClassPathResource("name.basics.tsv"))
                .delimited()
                .names(new String[]{"nconst", "primaryName","birthYear","deathYear","primaryProfession","knownForTitles"})
                .lineMapper(nameBasicsLineMapper())
                .maxItemCount(1000)
                .fieldSetMapper(new BeanWrapperFieldSetMapper<NameBasics>() {{
                    setTargetType(NameBasics.class);
                }})
                .build();
    }

    @Bean
    public LineMapper<NameBasics> nameBasicsLineMapper() {

        final DefaultLineMapper<NameBasics> defaultLineMapper = new DefaultLineMapper<>();
        final DelimitedLineTokenizer lineTokenizer = new DelimitedLineTokenizer();
        lineTokenizer.setDelimiter("\t");
        lineTokenizer.setStrict(false);
        lineTokenizer.setNames(new String[] {"nconst", "primaryName","birthYear","deathYear","primaryProfession","knownForTitles"});
        final NameBasicsFieldSetMapper fieldSetMapper = new NameBasicsFieldSetMapper();
        defaultLineMapper.setFieldSetMapper(fieldSetMapper);
        defaultLineMapper.setLineTokenizer(lineTokenizer);
        return defaultLineMapper;
    }

    @Bean
    public NameBasicsProcessor nameBasicProcessor() {
        return new NameBasicsProcessor();
    }

    @Bean
    public JdbcBatchItemWriter<NameBasics> nameBasicsWriter(final DataSource dataSource) {
        return new JdbcBatchItemWriterBuilder<NameBasics>()
                .itemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>())
                .sql("INSERT INTO NAME_BASICS (NCONST, PRIMARY_NAME, BIRTH_YEAR, DEATH_YEAR, PRIMARY_PROFESSION, KNOWN_FOR_TITLES) VALUES (:nconst, :primaryName, :birthYear, :deathYear, :primaryProfession, :knownForTitles)")
                .dataSource(dataSource)
                .build();
    }

    @Bean
    public Job importNameBasicsJob(NotificationListener listener, Step step1) {
        return jobBuilderFactory.get("importNameBasicsJob")
                .incrementer(new RunIdIncrementer())
                .listener(listener)
                .flow(step1)
                .end()
                .build();
    }

    @Bean
    public Step step1(JdbcBatchItemWriter<NameBasics> writer) {
        return stepBuilderFactory.get("step1")
                .<NameBasics, NameBasics> chunk(10)
                .reader(nameBasicsReader())
                .processor(nameBasicProcessor())
                .writer(writer)
                .build();
    }
}
