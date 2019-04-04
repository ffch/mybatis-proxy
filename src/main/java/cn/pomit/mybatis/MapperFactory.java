package cn.pomit.mybatis;

import cn.pomit.mybatis.configuration.MybatisConfiguration;
import cn.pomit.mybatis.sqlsession.SqlSessionTemplate;

public class MapperFactory {
	private static SqlSessionTemplate sqlSessionTemplate = new SqlSessionTemplate(MybatisConfiguration.getSqlSessionFactory());;

	public static <T> T getMapper(Class<T> cls) {
		return sqlSessionTemplate.getMapper(cls);
	}
}
