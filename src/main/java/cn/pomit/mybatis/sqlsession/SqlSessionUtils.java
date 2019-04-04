package cn.pomit.mybatis.sqlsession;

import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

public class SqlSessionUtils {

	public static SqlSession getSqlSession(SqlSessionFactory sessionFactory) {
		ExecutorType executorType = sessionFactory.getConfiguration().getDefaultExecutorType();
		return getSqlSession(sessionFactory, executorType);
	}

	public static SqlSession getSqlSession(SqlSessionFactory sessionFactory, ExecutorType executorType) {
		// SqlSessionHolder holder = (SqlSessionHolder)
		// TransactionSynchronizationManager.getResource(sessionFactory);
		//
		// SqlSession session = sessionHolder(executorType, holder);
		// if (session != null) {
		// return session;
		// }
		//
		// if (LOGGER.isDebugEnabled()) {
		// LOGGER.debug("Creating a new SqlSession");
		// }

		SqlSession session = sessionFactory.openSession(executorType);

		// registerSessionHolder(sessionFactory, executorType,
		// exceptionTranslator, session);

		return session;
	}

	public static void closeSqlSession(SqlSession session, SqlSessionFactory sessionFactory) {
		session.close();
	}
}
