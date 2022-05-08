package com.fastbuild;

import com.alibaba.druid.pool.DruidDataSourceFactory;
import com.fastbuild.framework.web.domain.server.Sys;
import org.apache.ibatis.binding.MapperMethod;
import org.apache.ibatis.builder.xml.XMLMapperBuilder;
import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.defaults.DefaultSqlSessionFactory;
import org.apache.ibatis.transaction.jdbc.JdbcTransactionFactory;

import javax.sql.DataSource;
import java.io.ByteArrayInputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MybatisTest2 {

    public static void main (String[] args) throws Exception {
        // 获取数据源
        Map<String, String> dataSourceProperties = new HashMap<>();
        dataSourceProperties.put("driverClassName", "com.mysql.cj.jdbc.Driver");
        dataSourceProperties.put("url", "jdbc:mysql://182.92.230.75:3306/ry-vue?useUnicode=true&characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&useSSL=true&serverTimezone=GMT%2B8");
        dataSourceProperties.put("username", "root");
        dataSourceProperties.put("password", "FastBuild@2021");
        DataSource dataSource = DruidDataSourceFactory.createDataSource(dataSourceProperties);

        JdbcTransactionFactory jdbcTransactionFactory = new JdbcTransactionFactory();
        Environment environment = new Environment.Builder("test").transactionFactory(jdbcTransactionFactory).dataSource(dataSource).build();
        Configuration configuration = new Configuration(environment);

        String resource = "com/fastbuild/test/TestMapper.xml";
        String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\n" +
                "<!DOCTYPE mapper\n" +
                "PUBLIC \"-//mybatis.org//DTD Mapper 3.0//EN\"\n" +
                "\"http://mybatis.org/dtd/mybatis-3-mapper.dtd\">\n" +
                "<mapper namespace=\"com.fastbuild.test.TestMapper\">\n" +
                "<select id=\"checkConfigKeyUnique\" parameterType=\"java.util.Map\" resultType=\"java.util.Map\">\n" +
                "        select config_id, config_name, config_key, config_value, config_type, create_by, create_time, update_by, update_time, remark  from sys_config" +
                "        <where> <if test=\"configKey != null and configKey != '' \">config_key = #{configKey}</if></where>\n" +
                "    </select>\n" +
                "</mapper>";
        XMLMapperBuilder builder = new XMLMapperBuilder(new ByteArrayInputStream(xml.getBytes()), configuration, resource, configuration.getSqlFragments());
        builder.parse();

        Map<String, String> map = new HashMap<>();
        map.put("configKey", "sys.user.initPassword");
        RowBounds rowBounds = new RowBounds(0, 10);

        System.out.println(configuration.getDatabaseId());

        DefaultSqlSessionFactory defaultSqlSessionFactory = new DefaultSqlSessionFactory(configuration);
        SqlSession sqlSession = defaultSqlSessionFactory.openSession();
        List list = sqlSession.selectList("com.fastbuild.test.TestMapper.checkConfigKeyUnique", map, rowBounds);
        System.out.println(list);

        MappedStatement ms = configuration.getMappedStatement("com.fastbuild.test.TestMapper.checkConfigKeyUnique");
        System.out.println(ms.getResultMaps());
    }


}
