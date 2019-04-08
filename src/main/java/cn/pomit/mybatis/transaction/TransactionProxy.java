package cn.pomit.mybatis.transaction;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

import org.apache.ibatis.logging.Log;
import org.apache.ibatis.logging.LogFactory;
import org.apache.ibatis.reflection.ExceptionUtil;

import cn.pomit.mybatis.annotation.Transactional;
import cn.pomit.mybatis.exception.ProxyErrorException;
import cn.pomit.mybatis.sqlsession.SqlSessionTemplate;
import cn.pomit.mybatis.sqlsession.SqlSessionUtils;

public class TransactionProxy implements InvocationHandler {
	private static final Log LOGGER = LogFactory.getLog(TransactionProxy.class);
	private Object targert = null;

	private boolean hasTransaction = false;
	private SqlSessionTemplate sqlSessionTemplate;
	private Class<? extends Throwable>[] exceptions;

	public TransactionProxy(Class<?> cls, SqlSessionTemplate sqlSessionTemplate) throws ProxyErrorException {
		try {
			this.targert = cls.getConstructor().newInstance();
			this.sqlSessionTemplate = sqlSessionTemplate;
			Transactional transactional = cls.getAnnotation(Transactional.class);
			if (transactional != null) {
				hasTransaction = true;
				exceptions = transactional.rollbackFor();
			}
		} catch (Exception e) {
			LOGGER.error("代理对象生成失败，无构造函数？", e);
			throw new ProxyErrorException(e);
		}
	}

	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		if (!hasTransaction) {
			LOGGER.debug("no 事务");
			return method.invoke(targert, args);
		} else {
			TransactionHolder transactionHolder = SqlSessionUtils.registerSqlSession(
					sqlSessionTemplate.getSqlSessionFactory(), sqlSessionTemplate.getExecutorType());
			LOGGER.debug("事务注册成功");
			try {
				Object result = method.invoke(targert, args);
				if (transactionHolder.isHasTransaction()) {	
					transactionHolder.getSession().commit(true);
					LOGGER.debug("事务提交成功");
				}
				return result;
			} catch (Exception e) {
				Throwable unwrap = ExceptionUtil.unwrapThrowable(e);
				if (exceptions == null || exceptions.length < 1) {
					if (unwrap instanceof RuntimeException) {
						transactionHolder.getSession().rollback(true);
					}
				} else {
					boolean catchExcep = false;
					for (Class<? extends Throwable> exp : exceptions) {
						if (exp.isAssignableFrom(unwrap.getClass())) {
							catchExcep = true;
							break;
						}
					}
					if (catchExcep) {
						transactionHolder.getSession().rollback(true);
					}
				}
				throw unwrap;
			} finally {
				LOGGER.debug("关闭session");
				SqlSessionUtils.closeSqlSession(transactionHolder, sqlSessionTemplate.getSqlSessionFactory());
			}

		}

	}

}
