package com.wdqsoft.common.config;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;

@Configuration
//mapper接口文件  interface文件路径

@MapperScan(basePackages = "com.wdqsoft.dao.cmsdao.mapper.cmsdbh2"
        ,sqlSessionTemplateRef = "h2dbSqlSessionTemplate")
public class H2DBConfig {
    // mapper xml 文件路径   classpath*:mybatis/mapper/*/*Mapper.xml
//    static final String MAPPER_LOCATION = "classpath: ./dao/src/main/resources/mybatis/mapper/cmsdbh2/*.xml";
    static final String MAPPER_LOCATION = "classpath*:mybatis/mapper/*/*Mapper.xml";
    // 实体类存放位置
    static final String TypeAliasesPackage="com.wdqsoft.common.entity.cmsdbh2";

    /**
     * 初始化数据库 创建数据源
     * @return
     */
    @Bean(name = "h2dbDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.h2db")
    public DataSource dataSource(){

        return DataSourceBuilder.create().build();
    }

    /**
     * 创建sqlsessionfactory工厂
     * @param dataSource
     * @return
     * @throws Exception
     */
    @Bean(name = "h2dbSqlSessionFactory")
    public SqlSessionFactory sqlSessionFactory(@Qualifier("h2dbDataSource") DataSource dataSource) throws Exception{
        SqlSessionFactoryBean sqlSessionFactoryBean=new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(dataSource);
        //配置xml文件
        sqlSessionFactoryBean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources(MAPPER_LOCATION));
        //配置数据对应的实体类bean路径
        sqlSessionFactoryBean.setTypeAliasesPackage(TypeAliasesPackage);
        return sqlSessionFactoryBean.getObject();
    }

    /**
     * 创建事务
     * @param dataSource
     * @return
     * @throws Exception
     */
    @Bean(name = "h2dbDataSourceTransactionManager")
    public DataSourceTransactionManager dataSourceTransactionManager(
            @Qualifier("h2dbDataSource") DataSource dataSource) throws Exception {
        return new DataSourceTransactionManager(dataSource);
    }

    /**
     * 创建模板
     *@param sqlSessionFactory
     *@return SqlSessionTemplate
     */
    @Bean(name = "h2dbSqlSessionTemplate")
    public SqlSessionTemplate primarySqlSessionTemplate(@Qualifier("h2dbSqlSessionFactory") SqlSessionFactory sqlSessionFactory) {
        return new SqlSessionTemplate(sqlSessionFactory);
    }

}
