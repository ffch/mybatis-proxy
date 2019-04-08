package cn.pomit.mybatis.sqlsession;

import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import cn.pomit.mybatis.transaction.TransactionHolder;
import cn.pomit.mybatis.transaction.TransactionManager;

public class SqlSessionUtils {

	public static TransactionHolder getSqlSession(SqlSessionFactory sessionFactory) {
		ExecutorType executorType = sessionFactory.getConfiguration().getDefaultExecutorType();
		return getSqlSession(sessionFactory, executorType);
	}

	public static TransactionHolder getSqlSession(SqlSessionFactory sessionFactory, ExecutorType executorType) {
		TransactionHolder transactionHolder = (TransactionHolder) TransactionManager.getResource(sessionFactory);

		if (transactionHolder == null) {
			transactionHolder = new TransactionHolder(sessionFactory.openSession(executorType), false);
			return transactionHolder;
		}
		if (transactionHolder.getSession() == null) {
			transactionHolder.setSession(sessionFactory.openSession(executorType));
			transactionHolder.setHasTransaction(false);
		}
		return transactionHolder;
	}

	public static TransactionHolder registerSqlSession(SqlSessionFactory sessionFactory, ExecutorType executorType) {
		TransactionHolder transactionHolder = new TransactionHolder(sessionFactory.openSession(executorType), true);
		TransactionManager.bindResource(sessionFactory, transactionHolder);
		return transactionHolder;
	}

	public static void closeSqlSession(TransactionHolder transactionHolder, SqlSessionFactory sessionFactory) {
		SqlSession session = transactionHolder.getSession();
		if(session != null){
			session.close();
			session = null;
		}

		if (transactionHolder.isHasTransaction()) {
			TransactionManager.unbindResource(sessionFactory);
		}

	}
}
