package com.vitelco.imdb.config.batch;

import com.vitelco.imdb.config.NotificationListener;
import com.vitelco.imdb.config.mapper.TitleRatingsFieldSetMapper;
import com.vitelco.imdb.config.processor.TitleRatingsProcessor;
import com.vitelco.imdb.persistence.entity.TitleRatings;
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
public class TitleRatingsBatch {
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    @Bean
    public FlatFileItemReader<TitleRatings> titleRatingsReader() {
        return new FlatFileItemReaderBuilder<TitleRatings>()
                .name("titleRatingsReader")
                .resource(new ClassPathResource("title.ratings.tsv"))
                .delimited()
                .names(new String[]{"tconst", "averageRating","numVotes"})
                .lineMapper(titleRatingsLineMapper())
                .maxItemCount(1000)
                .fieldSetMapper(new BeanWrapperFieldSetMapper<TitleRatings>() {{
                    setTargetType(TitleRatings.class);
                }})
                .build();
    }

    @Bean
    public LineMapper<TitleRatings> titleRatingsLineMapper() {

        final DefaultLineMapper<TitleRatings> defaultLineMapper = new DefaultLineMapper<>();
        final DelimitedLineTokenizer lineTokenizer = new DelimitedLineTokenizer();
        lineTokenizer.setDelimiter("\t");
        lineTokenizer.setStrict(false);
        lineTokenizer.setNames(new String[] {"tconst", "averageRating","numVotes"});
        final TitleRatingsFieldSetMapper fieldSetMapper = new TitleRatingsFieldSetMapper();
        defaultLineMapper.setFieldSetMapper(fieldSetMapper);
        defaultLineMapper.setLineTokenizer(lineTokenizer);
        return defaultLineMapper;
    }

    @Bean
    public TitleRatingsProcessor titleRatingsProcessor() {
        return new TitleRatingsProcessor();
    }

    @Bean
    public JdbcBatchItemWriter<TitleRatings> titleRatingsWriter(final DataSource dataSource) {
        return new JdbcBatchItemWriterBuilder<TitleRatings>()
                .itemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>())
                .sql("INSERT INTO TITLE_RATINGS (TCONST, AVERAGE_RATING, NUM_VOTES) VALUES (:tconst, :averageRating, :numVotes)")
                .dataSource(dataSource)
                .build();
    }

    @Bean
    public Job importTitleRatingsJob(NotificationListener listener, Step step7) {
        return jobBuilderFactory.get("importTitleRatingsJob")
                .incrementer(new RunIdIncrementer())
                .listener(listener)
                .flow(step7)
                .end()
                .build();
    }

    @Bean
    public Step step7(JdbcBatchItemWriter<TitleRatings> writer) {
        return stepBuilderFactory.get("step7")
                .<TitleRatings, TitleRatings> chunk(10)
                .reader(titleRatingsReader())
                .processor(titleRatingsProcessor())
                .writer(writer)
                .build();
    }
}
