package com.vitelco.imdb.config.batch;

import com.vitelco.imdb.config.NotificationListener;
import com.vitelco.imdb.config.mapper.TitlePrincipalsFieldSetMapper;
import com.vitelco.imdb.config.processor.TitlePrincipalsProcessor;
import com.vitelco.imdb.persistence.entity.TitlePrincipals;
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
public class TitlePrincipalsBatch {
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;


    @Bean
    public FlatFileItemReader<TitlePrincipals> titlePrincipalsReader() {
        return new FlatFileItemReaderBuilder<TitlePrincipals>()
                .name("titlePrincipalsReader")
                .resource(new ClassPathResource("title.principals.tsv"))
                .delimited()
                .names(new String[]{"tconst", "ordering","nconst","category","job","characters"})
                .lineMapper(titlePrincipalsLineMapper())
                .maxItemCount(1000)
                .fieldSetMapper(new BeanWrapperFieldSetMapper<TitlePrincipals>() {{
                    setTargetType(TitlePrincipals.class);
                }})
                .build();
    }

    @Bean
    public LineMapper<TitlePrincipals> titlePrincipalsLineMapper() {

        final DefaultLineMapper<TitlePrincipals> defaultLineMapper = new DefaultLineMapper<>();
        final DelimitedLineTokenizer lineTokenizer = new DelimitedLineTokenizer();
        lineTokenizer.setDelimiter("\t");
        lineTokenizer.setStrict(false);
        lineTokenizer.setNames(new String[] {"tconst", "ordering","nconst","category","job","characters"});
        final TitlePrincipalsFieldSetMapper fieldSetMapper = new TitlePrincipalsFieldSetMapper();
        defaultLineMapper.setFieldSetMapper(fieldSetMapper);
        defaultLineMapper.setLineTokenizer(lineTokenizer);
        return defaultLineMapper;
    }

    @Bean
    public TitlePrincipalsProcessor titlePrincipalsProcessor() {
        return new TitlePrincipalsProcessor();
    }

    @Bean
    public JdbcBatchItemWriter<TitlePrincipals> titlePrincipalsWriter(final DataSource dataSource) {
        return new JdbcBatchItemWriterBuilder<TitlePrincipals>()
                .itemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>())
                .sql("INSERT INTO TITLE_PRINCIPALS (TCONST, ORDERING, NCONST, CATEGORY, JOB, CHARACTERS) VALUES (:tconst, :ordering, :nconst, :category, :job, :characters)")
                .dataSource(dataSource)
                .build();
    }

    @Bean
    public Job importTitlePrincipalsJob(NotificationListener listener, Step step6) {
        return jobBuilderFactory.get("importTitlePrincipalsJob")
                .incrementer(new RunIdIncrementer())
                .listener(listener)
                .flow(step6)
                .end()
                .build();
    }

    @Bean
    public Step step6(JdbcBatchItemWriter<TitlePrincipals> writer) {
        return stepBuilderFactory.get("step6")
                .<TitlePrincipals, TitlePrincipals> chunk(10)
                .reader(titlePrincipalsReader())
                .processor(titlePrincipalsProcessor())
                .writer(writer)
                .build();
    }



}
