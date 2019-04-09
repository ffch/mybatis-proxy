package cn.pomit.mybatis;

import java.lang.reflect.Proxy;

import cn.pomit.mybatis.configuration.MybatisConfiguration;
import cn.pomit.mybatis.sqlsession.SqlSessionTemplate;
import cn.pomit.mybatis.transaction.TransactionProxy;

public class ProxyHandlerFactory {
	private static SqlSessionTemplate sqlSessionTemplate = new SqlSessionTemplate(MybatisConfiguration.getSqlSessionFactory());;

	/**
	 * 获取mapper对象
	 * @param cls mapper接口
	 * @return mapper对象
	 */
	public static <T> T getMapper(Class<T> cls) {
		return sqlSessionTemplate.getMapper(cls);
	}
	
	/**
	 * 获取事务处理代理（使用jdk动态代理）
	 * @param cls 被代理类
	 * @return 代理对象
	 */
	public static <T> T getTransaction(Class<? extends T> cls){
		TransactionProxy transactionProxy = new TransactionProxy(cls, sqlSessionTemplate);
		return (T) Proxy.newProxyInstance(cls.getClassLoader(), cls.getInterfaces(), transactionProxy);
	}
}
