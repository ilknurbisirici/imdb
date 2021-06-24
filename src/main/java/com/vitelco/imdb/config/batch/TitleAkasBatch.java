package com.vitelco.imdb.config.batch;

import com.vitelco.imdb.config.NotificationListener;
import com.vitelco.imdb.config.mapper.TitleAkasFieldSetMapper;
import com.vitelco.imdb.config.processor.TitleAkasProcessor;
import com.vitelco.imdb.persistence.entity.TitleAkas;
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
public class TitleAkasBatch {
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    @Bean
    public FlatFileItemReader<TitleAkas> titleAkasReader() {
        return new FlatFileItemReaderBuilder<TitleAkas>()
                .name("titleAkasReader")
                .resource(new ClassPathResource("title.akas.tsv"))
                .delimited()
                .names(new String[]{"titleId", "ordering","title","region","language","types","attributes","originalTitle"})
                .lineMapper(titleAkasLineMapper())
                .maxItemCount(1000)
                .fieldSetMapper(new BeanWrapperFieldSetMapper<TitleAkas>() {{
                    setTargetType(TitleAkas.class);
                }})
                .build();
    }

    @Bean
    public LineMapper<TitleAkas> titleAkasLineMapper() {

        final DefaultLineMapper<TitleAkas> defaultLineMapper = new DefaultLineMapper<>();
        final DelimitedLineTokenizer lineTokenizer = new DelimitedLineTokenizer();
        lineTokenizer.setDelimiter("\t");
        lineTokenizer.setStrict(false);
        lineTokenizer.setNames(new String[] {"titleId", "ordering","title","region","language","types","attributes","originalTitle"});
        final TitleAkasFieldSetMapper fieldSetMapper = new TitleAkasFieldSetMapper();
        defaultLineMapper.setFieldSetMapper(fieldSetMapper);
        defaultLineMapper.setLineTokenizer(lineTokenizer);
        return defaultLineMapper;
    }

    @Bean
    public TitleAkasProcessor titleAkasProcessor() {
        return new TitleAkasProcessor();
    }

    @Bean
    public JdbcBatchItemWriter<TitleAkas> titleAkasWriter(final DataSource dataSource) {
        return new JdbcBatchItemWriterBuilder<TitleAkas>()
                .itemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>())
                .sql("INSERT INTO TITLE_AKAS (TITLE_ID, ORDERING, TITLE, REGION, LANGUAGE, TYPES, ATTRIBUTES, ORIGINAL_TITLE) VALUES (:titleId, :ordering, :title, :region, :language, :types, :attributes, :originalTitle)")
                .dataSource(dataSource)
                .build();
    }

    @Bean
    public Job importTitleAkasJob(NotificationListener listener, Step step2) {
        return jobBuilderFactory.get("importTitleAkasJob")
                .incrementer(new RunIdIncrementer())
                .listener(listener)
                .flow(step2)
                .end()
                .build();
    }

    @Bean
    public Step step2(JdbcBatchItemWriter<TitleAkas> writer) {
        return stepBuilderFactory.get("step2")
                .<TitleAkas, TitleAkas> chunk(10)
                .reader(titleAkasReader())
                .processor(titleAkasProcessor())
                .writer(writer)
                .build();
    }
}
