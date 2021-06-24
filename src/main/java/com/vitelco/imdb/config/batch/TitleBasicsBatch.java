package com.vitelco.imdb.config.batch;

import com.vitelco.imdb.config.NotificationListener;
import com.vitelco.imdb.config.mapper.TitleBasicsFieldSetMapper;
import com.vitelco.imdb.config.processor.TitleBasicsProcessor;
import com.vitelco.imdb.persistence.entity.TitleBasics;
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
public class TitleBasicsBatch {
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    @Bean
    public FlatFileItemReader<TitleBasics> titleBasicsReader() {
        return new FlatFileItemReaderBuilder<TitleBasics>()
                .name("titleBasicsReader")
                .resource(new ClassPathResource("title.basics.tsv"))
                .delimited()
                .names(new String[]{"tconst", "titleType","primaryTitle","originalTitle","adult","startYear","endYear","runtimeMinutes","genres"})
                .lineMapper(titleBasicsLineMapper())
                .maxItemCount(1000)
                .fieldSetMapper(new BeanWrapperFieldSetMapper<TitleBasics>() {{
                    setTargetType(TitleBasics.class);
                }})
                .build();
    }

    @Bean
    public LineMapper<TitleBasics> titleBasicsLineMapper() {

        final DefaultLineMapper<TitleBasics> defaultLineMapper = new DefaultLineMapper<>();
        final DelimitedLineTokenizer lineTokenizer = new DelimitedLineTokenizer();
        lineTokenizer.setDelimiter("\t");
        lineTokenizer.setStrict(false);
        lineTokenizer.setNames(new String[] {"tconst", "titleType","primaryTitle","originalTitle","adult","startYear","endYear","runtimeMinutes","genres"});
        final TitleBasicsFieldSetMapper fieldSetMapper = new TitleBasicsFieldSetMapper();
        defaultLineMapper.setFieldSetMapper(fieldSetMapper);
        defaultLineMapper.setLineTokenizer(lineTokenizer);
        return defaultLineMapper;
    }

    @Bean
    public TitleBasicsProcessor titleBasicsProcessor() {
        return new TitleBasicsProcessor();
    }

    @Bean
    public JdbcBatchItemWriter<TitleBasics> titleBasicsWriter(final DataSource dataSource) {
        return new JdbcBatchItemWriterBuilder<TitleBasics>()
                .itemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>())
                .sql("INSERT INTO TITLE_BASICS (TCONST, TITLE_TYPE, PRIMARY_TITLE, ORIGINAL_TITLE, ADULT, START_YEAR, END_YEAR, RUNTIME_MINUTES, GENRES) VALUES (:tconst, :titleType, :primaryTitle, :originalTitle, :adult, :startYear, :endYear, :runtimeMinutes, :genres)")
                .dataSource(dataSource)
                .build();
    }

    @Bean
    public Job importTitleBasicsJob(NotificationListener listener, Step step3) {
        return jobBuilderFactory.get("importTitleBasicsJob")
                .incrementer(new RunIdIncrementer())
                .listener(listener)
                .flow(step3)
                .end()
                .build();
    }

    @Bean
    public Step step3(JdbcBatchItemWriter<TitleBasics> writer) {
        return stepBuilderFactory.get("step3")
                .<TitleBasics, TitleBasics> chunk(10)
                .reader(titleBasicsReader())
                .processor(titleBasicsProcessor())
                .writer(writer)
                .build();
    }
}

