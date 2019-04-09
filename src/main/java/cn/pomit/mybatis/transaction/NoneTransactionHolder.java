package cn.pomit.mybatis.transaction;

import org.apache.ibatis.session.SqlSession;

public class NoneTransactionHolder extends AbstractTransactionHolder {

	public NoneTransactionHolder(SqlSession session) {
		super(session);
	}

	@Override
	public void closeSqlSession() {
		LOGGER.debug("关闭sqlSession。");
		sqlSession.close();
		sqlSession = null;
	}

	@Override
	public void commitSqlSession() {
		LOGGER.debug("提交sqlSession。");
		sqlSession.commit(true);
	}

}
