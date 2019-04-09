package cn.pomit.mybatis.configuration;

import java.util.Properties;

import javax.sql.DataSource;

import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.ibatis.transaction.TransactionFactory;
import org.apache.ibatis.transaction.jdbc.JdbcTransactionFactory;

public class MybatisConfiguration {

	private static SqlSessionFactory sqlSessionFactory = null;

	public static void initConfiguration(Properties properties) {
		MybatisProperties mybatisProperties = new MybatisProperties(properties);
		initConfiguration(mybatisProperties);
	}

	public static void initConfiguration(String packageName, Properties properties) {
		MybatisProperties mybatisProperties = new MybatisProperties(packageName, properties);
		initConfiguration(mybatisProperties);
	}

	public static void initConfiguration(String packageName, DataSource dataSource) {
		MybatisProperties mybatisProperties = new MybatisProperties(packageName, dataSource);
		initConfiguration(mybatisProperties);
	}

	public static void initConfiguration(String packageName, DataSource dataSource, Properties properties) {
		MybatisProperties mybatisProperties = new MybatisProperties(packageName, dataSource, properties);
		initConfiguration(mybatisProperties);
	}

	public static void initConfiguration(MybatisProperties mybatisProperties) {
		TransactionFactory transactionFactory = new JdbcTransactionFactory();
		Environment environment = new Environment("development", transactionFactory, mybatisProperties.getDataSource());
		Configuration configuration = new Configuration(environment);
		configuration.addMappers(mybatisProperties.getPackageName());
		sqlSessionFactory = new SqlSessionFactoryBuilder().build(configuration);
	}

	public static SqlSessionFactory getSqlSessionFactory() {
		return sqlSessionFactory;
	}
}
