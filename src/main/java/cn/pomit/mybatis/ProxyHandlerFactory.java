package cn.pomit.mybatis;

import java.lang.reflect.Proxy;

import cn.pomit.mybatis.configuration.MybatisConfiguration;
import cn.pomit.mybatis.sqlsession.SqlSessionTemplate;
import cn.pomit.mybatis.transaction.TransactionProxy;

public class ProxyHandlerFactory {
	private static SqlSessionTemplate sqlSessionTemplate = new SqlSessionTemplate(MybatisConfiguration.getSqlSessionFactory());;

	public static <T> T getMapper(Class<T> cls) {
		return sqlSessionTemplate.getMapper(cls);
	}
	
	public static <T> T getTransaction(Class<? extends T> cls){
		TransactionProxy transactionProxy = new TransactionProxy(cls, sqlSessionTemplate);
		return (T) Proxy.newProxyInstance(cls.getClassLoader(), cls.getInterfaces(), transactionProxy);
	}
}
