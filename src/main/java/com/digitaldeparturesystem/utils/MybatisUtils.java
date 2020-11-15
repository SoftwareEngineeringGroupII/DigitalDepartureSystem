package com.digitaldeparturesystem.utils;


import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.IOException;
import java.io.InputStream;

/**
 * sqlSessionFactory  --> sqlSession
 *
 */
public class MybatisUtils {

    static {
        try {
            //使用mybatis第一步：获取sqlSessionFactory对象
            String resource = "mybatis-config.xml";
            //读取配置文件
            InputStream inputStream = Resources.getResourceAsStream(resource);
            //创建sqlSessionFactory工厂
            sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static SqlSessionFactory sqlSessionFactory;

    //有了sqlSessionFactory，我们就可以获取SqlSession的实例了
    //SqlSession完全包括了面向数据库执行SQL命令所需的所有方法
    public static SqlSession getSqlSession(){
        //使用工厂生成sqlSession对象
        //mybatis默认是不自动提交事务的，所以在insert、update、delete后要手动提交事务
        //如果这里传入一个sqlSessionFactory.openSession(true);那么就不用手动提交了
        return sqlSessionFactory.openSession();
    }

}
