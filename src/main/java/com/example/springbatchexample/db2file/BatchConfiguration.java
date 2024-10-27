package com.example.springbatchexample.db2file;

import com.example.springbatchexample.Customer;
import javax.sql.DataSource;
import lombok.RequiredArgsConstructor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.transaction.PlatformTransactionManager;

@RequiredArgsConstructor
@Configuration
public class BatchConfiguration {

    private final JobRepository jobRepository;
    private final PlatformTransactionManager platformTransactionManager;
    private final DataSource dataSource;

    @Bean
    public SqlSessionFactory sqlSessionFactory() throws Exception {
        SqlSessionFactoryBean factoryBean = new SqlSessionFactoryBean();
        factoryBean.setDataSource(dataSource);
        factoryBean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources("classpath:mapper/*.xml"));
        return factoryBean.getObject();
    }
    @Bean
    public Job exportJob() throws Exception {
        return new JobBuilder("exportJob", jobRepository)
            .incrementer(new RunIdIncrementer())
            .start(exportStep())
            .build();
    }

    @Bean
    public Step exportStep() throws Exception {
        return new StepBuilder("exportStep", jobRepository)
            .<Customer, Customer>chunk(10, platformTransactionManager)
            .reader(customerItemReader(sqlSessionFactory()))
            .writer(textFileItemWriter())
            .transactionManager(platformTransactionManager)
            .build();
    }

    @Bean
    public CustomerItemReader customerItemReader(SqlSessionFactory sqlSessionFactory) {
        return new CustomerItemReader(sqlSessionFactory);
    }

    @Bean
    public TextFileCustomerItemWriter textFileItemWriter() {
        return new TextFileCustomerItemWriter("output.txt");
    }
}
