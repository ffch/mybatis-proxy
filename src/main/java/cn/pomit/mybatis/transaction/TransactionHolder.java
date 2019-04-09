package cn.pomit.mybatis.transaction;

import org.apache.ibatis.session.SqlSession;

public class TransactionHolder extends AbstractTransactionHolder {

	public TransactionHolder(SqlSession session) {
		super(session);
	}

	@Override
	public void closeSqlSession() {

	}

	@Override
	public void commitSqlSession() {

	}

}
