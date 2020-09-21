package com.wdqsoft.common.config;


import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;

@Configuration
public class MysqlDBConfig {

    static final String MAPPER_LOCATION = "classpath*:mapper/mysqldb/*.xml";
    static final String TypeAliasesPackage="com.cms.bean";
    /**
     * 初始化数据库 创建数据源
     * @return
     */
     @Bean(name = "mysqldbDataSource")
     @ConfigurationProperties(prefix = "spring.datasource.mysqldb")
     @Primary
    public DataSource dataSource(){

        return DataSourceBuilder.create().build();
    }

    /**
     * 创建sqlsessionfactory工厂
     * @param dataSource
     * @return
     * @throws Exception
     */
    @Bean(name = "mysqldbSqlSessionFactory")
    @Primary
    public SqlSessionFactory sqlSessionFactory(@Qualifier("mysqldbDataSource") DataSource dataSource) throws Exception{
        SqlSessionFactoryBean sqlSessionFactoryBean=new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(dataSource);
        sqlSessionFactoryBean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources(MAPPER_LOCATION));
        sqlSessionFactoryBean.setTypeAliasesPackage(TypeAliasesPackage);
        return sqlSessionFactoryBean.getObject();
    }

    /**
     * 创建事务
     * @param dataSource
     * @return
     * @throws Exception
     */
    @Bean(name = "mysqldbDataSourceTransactionManager")
    @Primary
    public DataSourceTransactionManager dataSourceTransactionManager(
            @Qualifier("mysqldbDataSource") DataSource dataSource) throws Exception {
        return new DataSourceTransactionManager(dataSource);
    }

    /**
     * 创建模板
     *@param sqlSessionFactory
     *@return SqlSessionTemplate
     */
    @Bean(name = "mysqldbSqlSessionTemplate")
    @Primary
    public SqlSessionTemplate primarySqlSessionTemplate(@Qualifier("mysqldbSqlSessionFactory") SqlSessionFactory sqlSessionFactory) {
        return new SqlSessionTemplate(sqlSessionFactory);
    }
}
