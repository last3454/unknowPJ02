package com.aplab.apsite.config;

import java.util.Base64;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;

import com.zaxxer.hikari.HikariDataSource;

import net.javacrumbs.shedlock.core.LockProvider;
import net.javacrumbs.shedlock.provider.jdbctemplate.JdbcTemplateLockProvider;

@Configuration
@MapperScan(value="com.aplab.apsite.dbmst.mapper", annotationClass=MstConnMapper.class, sqlSessionFactoryRef="mstSqlSessionFactory")
@EnableJpaRepositories(
    basePackages = "com.aplab.apsite.dbmst.repo",
    entityManagerFactoryRef = "primaryEntityManagerFactory",
    transactionManagerRef = "primaryTransactionManager"
)
public class DatabaseMstConfig {

    @Primary
    @Bean
    @ConfigurationProperties("spring.datasource.mst")
    public DataSourceProperties primaryDataSourceProperties() {
        return new DataSourceProperties();
    }

    @Primary
    @Bean
    @ConfigurationProperties("spring.datasource.mst.configuration")
    public DataSource primaryDataSource(
            @Qualifier("primaryDataSourceProperties") DataSourceProperties dataSourceProperties) {
        String password = new String(Base64.getDecoder().decode(dataSourceProperties.getPassword()));
        dataSourceProperties.setPassword(password);
        return dataSourceProperties.initializeDataSourceBuilder().type(HikariDataSource.class).build();
    }

    @Primary
    @Bean
    public LocalContainerEntityManagerFactoryBean primaryEntityManagerFactory(
            EntityManagerFactoryBuilder builder,
            @Qualifier("primaryDataSource") DataSource dataSource) {
            // LocalContainerEntityManagerFactoryBean bean = builder.dataSource(dataSource)
            //         .packages("com.aplab.apsite.dbmst.entity")
            //         .persistenceUnit("primaryEntityManager")
            //         .build();
        return builder
                        .dataSource(dataSource)
                        .packages("com.aplab.apsite.dbmst.entity")
                        .persistenceUnit("primaryEntityManager")
                        .build();
    }

    @Primary
    @Bean
    public PlatformTransactionManager primaryTransactionManager(
            @Qualifier("primaryEntityManagerFactory") EntityManagerFactory entityManagerFactory) {
        return new JpaTransactionManager(entityManagerFactory);
    }


    @Bean(name = "mstSqlSessionFactory")
    @Primary
    public SqlSessionFactory mstSqlSessionFactory(
            @Qualifier("primaryDataSource") DataSource primaryDataSource,
            ApplicationContext applicationContext) throws Exception {
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();

        org.apache.ibatis.session.Configuration configuration = new org.apache.ibatis.session.Configuration();
        configuration.setMapUnderscoreToCamelCase(true);

        sqlSessionFactoryBean.setConfiguration(configuration);
        sqlSessionFactoryBean.setDataSource(primaryDataSource);
        sqlSessionFactoryBean.setMapperLocations(applicationContext.getResources("classpath*:com/aplab/apsite/dbmst/mapper/*.xml"));
        return sqlSessionFactoryBean.getObject();
    }

    @Bean(name = "mstSqlSessionTemplate")
    @Primary
    public SqlSessionTemplate mstSqlSessionTemplate(SqlSessionFactory mstSqlSessionFactory) throws Exception {
        return new SqlSessionTemplate(mstSqlSessionFactory);
    }

    @Bean
    public LockProvider lockProvider(
        @Qualifier("primaryDataSource") DataSource dataSource
    ) {
        return new JdbcTemplateLockProvider(dataSource);
    }

}
