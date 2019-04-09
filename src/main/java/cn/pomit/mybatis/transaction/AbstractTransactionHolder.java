package cn.pomit.mybatis.transaction;

import org.apache.ibatis.logging.Log;
import org.apache.ibatis.logging.LogFactory;
import org.apache.ibatis.session.SqlSession;

public abstract class AbstractTransactionHolder {
	protected final Log LOGGER = LogFactory.getLog(getClass());

	protected SqlSession sqlSession;

	public AbstractTransactionHolder(SqlSession session) {
		this.sqlSession = session;
	}

	public abstract void closeSqlSession();

	public abstract void commitSqlSession();

	public void commitTransaction() {
		LOGGER.debug("开启事务提交。");
		sqlSession.commit(true);
	}

	public SqlSession getSqlSession() {
		return sqlSession;
	}

	public void setSqlSession(SqlSession sqlSession) {
		this.sqlSession = sqlSession;
	}

	public void rollbackTransaction() {
		LOGGER.debug("开启事务回滚。");
		sqlSession.rollback(true);
	}
}
