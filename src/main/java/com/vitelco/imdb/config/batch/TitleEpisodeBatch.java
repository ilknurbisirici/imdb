package com.vitelco.imdb.config.batch;

import com.vitelco.imdb.config.NotificationListener;
import com.vitelco.imdb.config.mapper.TitleEpisodeFieldSetMapper;
import com.vitelco.imdb.config.processor.TitleEpisodeProcessor;
import com.vitelco.imdb.persistence.entity.TitleEpisode;
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
public class TitleEpisodeBatch {
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    @Bean
    public FlatFileItemReader<TitleEpisode> titleEpisodeReader() {
        return new FlatFileItemReaderBuilder<TitleEpisode>()
                .name("titleEpisodeReader")
                .resource(new ClassPathResource("title.episode.tsv"))
                .delimited()
                .names(new String[]{"tconst", "parentTconst","seasonNumber","episodeNumber"})
                .lineMapper(titleEpisodeLineMapper())
                .maxItemCount(1000)
                .fieldSetMapper(new BeanWrapperFieldSetMapper<TitleEpisode>() {{
                    setTargetType(TitleEpisode.class);
                }})
                .build();
    }

    @Bean
    public LineMapper<TitleEpisode> titleEpisodeLineMapper() {

        final DefaultLineMapper<TitleEpisode> defaultLineMapper = new DefaultLineMapper<>();
        final DelimitedLineTokenizer lineTokenizer = new DelimitedLineTokenizer();
        lineTokenizer.setDelimiter("\t");
        lineTokenizer.setStrict(false);
        lineTokenizer.setNames(new String[] {"tconst", "parentTconst","seasonNumber","episodeNumber"});
        final TitleEpisodeFieldSetMapper fieldSetMapper = new TitleEpisodeFieldSetMapper();
        defaultLineMapper.setFieldSetMapper(fieldSetMapper);
        defaultLineMapper.setLineTokenizer(lineTokenizer);
        return defaultLineMapper;
    }

    @Bean
    public TitleEpisodeProcessor titleEpisodeProcessor() {
        return new TitleEpisodeProcessor();
    }

    @Bean
    public JdbcBatchItemWriter<TitleEpisode> titleEpisodeWriter(final DataSource dataSource) {
        return new JdbcBatchItemWriterBuilder<TitleEpisode>()
                .itemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>())
                .sql("INSERT INTO TITLE_EPISODE (TCONST, PARENT_TCONST, SEASON_NUMBER, EPISODE_NUMBER) VALUES (:tconst, :parentTconst, :seasonNumber, :episodeNumber)")
                .dataSource(dataSource)
                .build();
    }

    @Bean
    public Job importTitleEpisodeJob(NotificationListener listener, Step step5) {
        return jobBuilderFactory.get("importTitleEpisodeJob")
                .incrementer(new RunIdIncrementer())
                .listener(listener)
                .flow(step5)
                .end()
                .build();
    }

    @Bean
    public Step step5(JdbcBatchItemWriter<TitleEpisode> writer) {
        return stepBuilderFactory.get("step5")
                .<TitleEpisode, TitleEpisode> chunk(10)
                .reader(titleEpisodeReader())
                .processor(titleEpisodeProcessor())
                .writer(writer)
                .build();
    }
}
