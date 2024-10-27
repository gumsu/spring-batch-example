package com.example.springbatchexample.file2db;

import com.example.springbatchexample.Customer;
import javax.sql.DataSource;
import lombok.RequiredArgsConstructor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
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
    public SqlSessionTemplate sqlSessionTemplate(SqlSessionFactory sqlSessionFactory) {
        return new SqlSessionTemplate(sqlSessionFactory);
    }

    @Bean
    public SqlSessionFactory sqlSessionFactory(DataSource dataSource) throws Exception {
        SqlSessionFactoryBean factoryBean = new SqlSessionFactoryBean();
        factoryBean.setDataSource(dataSource);
        factoryBean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources("classpath:mapper/*.xml"));
        return factoryBean.getObject();
    }

    @Bean
    public Job importJob() throws Exception {
        return new JobBuilder("importJob", jobRepository)
            .incrementer(new RunIdIncrementer())
            .start(importStep())
            .build();
    }

    @Bean
    public Step importStep() throws Exception {
        return new StepBuilder("importStep", jobRepository)
            .<Customer, Customer>chunk(10, platformTransactionManager)
            .reader(customerReader().reader())
            .writer(customerWriter(sqlSessionTemplate(sqlSessionFactory(dataSource))))
            .transactionManager(platformTransactionManager)
            .build();
    }

    @Bean
    public CustomItemReader customerReader() {
        return new CustomItemReader();
    }

    @Bean
    public DbCustomerItemWriter customerWriter(SqlSessionTemplate sqlSessionTemplate) {
        return new DbCustomerItemWriter(sqlSessionTemplate);
    }
}
