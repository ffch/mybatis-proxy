package cn.pomit.mybatis.sqlsession;

import static cn.pomit.mybatis.sqlsession.SqlSessionUtils.getTransactionHolder;
import static java.lang.reflect.Proxy.newProxyInstance;
import static org.apache.ibatis.reflection.ExceptionUtil.unwrapThrowable;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.cursor.Cursor;
import org.apache.ibatis.executor.BatchResult;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import cn.pomit.mybatis.transaction.AbstractTransactionHolder;

public class SqlSessionTemplate implements SqlSession {
	private final SqlSessionFactory sqlSessionFactory;

	private final ExecutorType executorType;

	private final SqlSession sqlSessionProxy;

	/**
	 * Constructs a Spring managed SqlSession with the {@code SqlSessionFactory}
	 * provided as an argument.
	 *
	 * @param sqlSessionFactory
	 */
	public SqlSessionTemplate(SqlSessionFactory sqlSessionFactory) {
		this(sqlSessionFactory, sqlSessionFactory.getConfiguration().getDefaultExecutorType());
	}

	/**
	 * Constructs a Spring managed SqlSession with the {@code SqlSessionFactory}
	 * provided as an argument and the given {@code ExecutorType}
	 * {@code ExecutorType} cannot be changed once the
	 * {@code SqlSessionTemplate} is constructed.
	 *
	 * @param sqlSessionFactory
	 * @param executorType
	 */
	public SqlSessionTemplate(SqlSessionFactory sqlSessionFactory, ExecutorType executorType) {
		this.sqlSessionFactory = sqlSessionFactory;
		this.executorType = executorType;
		this.sqlSessionProxy = (SqlSession) newProxyInstance(SqlSessionFactory.class.getClassLoader(),
				new Class[] { SqlSession.class }, new SqlSessionInterceptor());
	}

	public SqlSessionFactory getSqlSessionFactory() {
		return this.sqlSessionFactory;
	}

	public ExecutorType getExecutorType() {
		return this.executorType;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public <T> T selectOne(String statement) {
		return this.sqlSessionProxy.<T> selectOne(statement);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public <T> T selectOne(String statement, Object parameter) {
		return this.sqlSessionProxy.<T> selectOne(statement, parameter);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public <K, V> Map<K, V> selectMap(String statement, String mapKey) {
		return this.sqlSessionProxy.<K, V> selectMap(statement, mapKey);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public <K, V> Map<K, V> selectMap(String statement, Object parameter, String mapKey) {
		return this.sqlSessionProxy.<K, V> selectMap(statement, parameter, mapKey);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public <K, V> Map<K, V> selectMap(String statement, Object parameter, String mapKey, RowBounds rowBounds) {
		return this.sqlSessionProxy.<K, V> selectMap(statement, parameter, mapKey, rowBounds);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public <T> Cursor<T> selectCursor(String statement) {
		return this.sqlSessionProxy.selectCursor(statement);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public <T> Cursor<T> selectCursor(String statement, Object parameter) {
		return this.sqlSessionProxy.selectCursor(statement, parameter);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public <T> Cursor<T> selectCursor(String statement, Object parameter, RowBounds rowBounds) {
		return this.sqlSessionProxy.selectCursor(statement, parameter, rowBounds);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public <E> List<E> selectList(String statement) {
		return this.sqlSessionProxy.<E> selectList(statement);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public <E> List<E> selectList(String statement, Object parameter) {
		return this.sqlSessionProxy.<E> selectList(statement, parameter);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public <E> List<E> selectList(String statement, Object parameter, RowBounds rowBounds) {
		return this.sqlSessionProxy.<E> selectList(statement, parameter, rowBounds);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void select(String statement, ResultHandler handler) {
		this.sqlSessionProxy.select(statement, handler);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void select(String statement, Object parameter, ResultHandler handler) {
		this.sqlSessionProxy.select(statement, parameter, handler);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void select(String statement, Object parameter, RowBounds rowBounds, ResultHandler handler) {
		this.sqlSessionProxy.select(statement, parameter, rowBounds, handler);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int insert(String statement) {
		return this.sqlSessionProxy.insert(statement);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int insert(String statement, Object parameter) {
		return this.sqlSessionProxy.insert(statement, parameter);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int update(String statement) {
		return this.sqlSessionProxy.update(statement);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int update(String statement, Object parameter) {
		return this.sqlSessionProxy.update(statement, parameter);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int delete(String statement) {
		return this.sqlSessionProxy.delete(statement);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int delete(String statement, Object parameter) {
		return this.sqlSessionProxy.delete(statement, parameter);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public <T> T getMapper(Class<T> type) {
		return getConfiguration().getMapper(type, this);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void commit() {
		throw new UnsupportedOperationException("Manual commit is not allowed over a Spring managed SqlSession");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void commit(boolean force) {
		throw new UnsupportedOperationException("Manual commit is not allowed over a Spring managed SqlSession");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void rollback() {
		throw new UnsupportedOperationException("Manual rollback is not allowed over a Spring managed SqlSession");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void rollback(boolean force) {
		throw new UnsupportedOperationException("Manual rollback is not allowed over a Spring managed SqlSession");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void close() {
		throw new UnsupportedOperationException("Manual close is not allowed over a Spring managed SqlSession");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void clearCache() {
		this.sqlSessionProxy.clearCache();
	}

	/**
	 * {@inheritDoc}
	 *
	 */
	@Override
	public Configuration getConfiguration() {
		return this.sqlSessionFactory.getConfiguration();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Connection getConnection() {
		return this.sqlSessionProxy.getConnection();
	}

	/**
	 * {@inheritDoc}
	 *
	 * @since 1.0.2
	 *
	 */
	@Override
	public List<BatchResult> flushStatements() {
		return this.sqlSessionProxy.flushStatements();
	}

	/**
	 * Proxy needed to route MyBatis method calls to the proper SqlSession got
	 * from Spring's Transaction Manager It also unwraps exceptions thrown by
	 * {@code Method#invoke(Object, Object...)} to pass a
	 * {@code PersistenceException} to the
	 * {@code PersistenceExceptionTranslator}.
	 */
	private class SqlSessionInterceptor implements InvocationHandler {
		@Override
		public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
			AbstractTransactionHolder transactionHolder = getTransactionHolder(
					SqlSessionTemplate.this.sqlSessionFactory, SqlSessionTemplate.this.executorType);
			SqlSession sqlSession = transactionHolder.getSqlSession();
			try {
				Object result = method.invoke(sqlSession, args);
				transactionHolder.commitSqlSession();
				return result;
			} catch (Throwable t) {
				Throwable unwrapped = unwrapThrowable(t);
				throw unwrapped;
			} finally {
				transactionHolder.closeSqlSession();
			}
		}
	}

}
