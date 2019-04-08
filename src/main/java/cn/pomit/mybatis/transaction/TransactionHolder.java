package cn.pomit.mybatis.transaction;

import org.apache.ibatis.session.SqlSession;

public class TransactionHolder {
	private SqlSession session;
	private boolean hasTransaction = false;

	public SqlSession getSession() {
		return session;
	}

	public void setSession(SqlSession session) {
		this.session = session;
	}

	public boolean isHasTransaction() {
		return hasTransaction;
	}

	public void setHasTransaction(boolean hasTransaction) {
		this.hasTransaction = hasTransaction;
	}

	public TransactionHolder(SqlSession session, boolean hasTransaction) {
		this.session = session;
		this.hasTransaction = hasTransaction;
	}

}
