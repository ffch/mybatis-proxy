package cn.pomit.mybatis.sqlsession;

import org.apache.ibatis.logging.Log;
import org.apache.ibatis.logging.LogFactory;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import cn.pomit.mybatis.transaction.AbstractTransactionHolder;
import cn.pomit.mybatis.transaction.NoneTransactionHolder;
import cn.pomit.mybatis.transaction.TransactionHolder;
import cn.pomit.mybatis.transaction.TransactionManager;

public class SqlSessionUtils {
	private static final Log LOGGER = LogFactory.getLog(SqlSessionUtils.class);
	
	public static AbstractTransactionHolder getTransactionHolder(SqlSessionFactory sessionFactory) {
		ExecutorType executorType = sessionFactory.getConfiguration().getDefaultExecutorType();
		return getTransactionHolder(sessionFactory, executorType);
	}

	public static AbstractTransactionHolder getTransactionHolder(SqlSessionFactory sessionFactory,
			ExecutorType executorType) {
		LOGGER.debug("开启 SqlSession。");
		AbstractTransactionHolder transactionHolder = (AbstractTransactionHolder) TransactionManager
				.getResource(sessionFactory);

		if (transactionHolder == null) {
			transactionHolder = new NoneTransactionHolder(sessionFactory.openSession(executorType));
			return transactionHolder;
		}
		return transactionHolder;
	}

	public static AbstractTransactionHolder registerTransaction(SqlSessionFactory sessionFactory,
			ExecutorType executorType) {
		LOGGER.debug("开启事务绑定。");
		AbstractTransactionHolder transactionHolder = new TransactionHolder(sessionFactory.openSession(executorType));
		TransactionManager.bindResource(sessionFactory, transactionHolder);
		return transactionHolder;
	}

	public static void deRegisterTransaction(AbstractTransactionHolder transactionHolder,
			SqlSessionFactory sessionFactory) {
		SqlSession session = transactionHolder.getSqlSession();
		if (session != null) {
			session.close();
			session = null;
		}
		LOGGER.debug("事务解绑关闭。");
		TransactionManager.unbindResource(sessionFactory);
	}
}
