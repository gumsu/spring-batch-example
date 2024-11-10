package com.example.springbatchexample.db2db;

import java.util.ArrayList;
import java.util.List;
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
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.support.CompositeItemProcessor;
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
    public Job personJob() throws Exception {
        return new JobBuilder("personJob", jobRepository)
            .incrementer(new RunIdIncrementer())
            .start(customStep())
            .build();
    }


    @Bean
    public Step customStep() throws Exception {
        return new StepBuilder("customStep", jobRepository)
            .tasklet(customTasklet(sqlSessionTemplate(sqlSessionFactory(dataSource))), platformTransactionManager)
            .build();
    }

    @Bean
    public CustomTasklet customTasklet(SqlSessionTemplate sqlSessionTemplate) {
        return new CustomTasklet(sqlSessionTemplate);
    }

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
    public CompositeItemProcessor compositeItemProcessor() {
        List<ItemProcessor> delegates = new ArrayList<>();
        delegates.add(new CustomerItemProcessor());
        delegates.add(new EmployeeItemProcessor());

        CompositeItemProcessor processor = new CompositeItemProcessor();

        processor.setDelegates(delegates);

        return processor;
    }
//    @Bean
//    public ItemProcessor<Customer, Person> customerPersonItemProcessor() {
//        return customer -> {
//            person.setCustomerNumber(customer.getCustomerNumber());
//            person.setCustomerName(customer.getCustomerName());
//            person.setCountry(customer.getCountry());
//            return person;
//        };
//    }
//
//    @Bean
//    public ItemProcessor<Employee, Person> employeePersonItemProcessor() {
//        return employee -> {
//            person.setEmployeeNumber(employee.getEmployeeNumber());
//            person.setEmployeeName(employee.getLastName());
//            person.setEmail(employee.getEmail() + "@gmail.com");
//            return person;
//        };
//    }

    @Bean
    public PersonItemWriter personItemWriter(SqlSessionTemplate sqlSessionTemplate) {
        return new PersonItemWriter(sqlSessionTemplate);
    }
}
