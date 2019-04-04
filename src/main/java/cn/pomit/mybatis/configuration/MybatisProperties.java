package cn.pomit.mybatis.configuration;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.sql.DataSource;

import org.apache.ibatis.datasource.DataSourceFactory;
import org.apache.ibatis.datasource.jndi.JndiDataSourceFactory;
import org.apache.ibatis.datasource.pooled.PooledDataSourceFactory;
import org.apache.ibatis.datasource.unpooled.UnpooledDataSourceFactory;

import cn.pomit.mybatis.exception.ParamsNeedException;
import cn.pomit.mybatis.util.StringUtil;

public class MybatisProperties {
	public static final String PACKAGE_NAME_KEY = "mybatis.mapper.scan";
	public static final String DATASOURCE_PREFIX = "mybatis.datasource.";

	private final Map<String, Class<?>> TYPE_ALIASES = new HashMap<String, Class<?>>();

	private DataSource dataSource = null;
	private String packageName = null;
	private Properties properties = null;

	public MybatisProperties(String packageName, Properties properties) {
		this(packageName, null, properties);
	}

	public MybatisProperties(Properties properties) {
		this(null, null, properties);
	}

	public MybatisProperties(String packageName, DataSource dataSource) {
		this(packageName, dataSource, null);
	}

	public MybatisProperties(String packageName, DataSource dataSource, Properties properties) {
		TYPE_ALIASES.put("UNPOOLED", UnpooledDataSourceFactory.class);
		TYPE_ALIASES.put("POOLED", PooledDataSourceFactory.class);
		TYPE_ALIASES.put("JNDI", JndiDataSourceFactory.class);

		this.dataSource = dataSource;
		this.packageName = packageName;
		this.properties = properties;

		if (packageName == null && properties == null) {
			throw new ParamsNeedException("配置文件为空，packageName无法获取！");
		}

		if (dataSource == null && properties == null) {
			throw new ParamsNeedException("配置文件为空，dataSource无法获取！");
		}

		if (packageName == null) {
			String name = properties.getProperty(PACKAGE_NAME_KEY);
			if (StringUtil.isEmpty(name)) {
				throw new ParamsNeedException("配置文件未获取到mybatis.mapper.scan参数！");
			}
			this.packageName = packageName;
		}

		if (dataSource == null) {
			String datasourceType = DATASOURCE_PREFIX + "type";
			String type = properties.getProperty(datasourceType);
			if (StringUtil.isEmpty(type)) {
				throw new ParamsNeedException("配置文件未获取到mybatis.datasource.type参数！");
			}
			Properties props = new Properties();
			for (Object key : properties.keySet()) {
				String tmpKey = key.toString();
				if(tmpKey.startsWith(DATASOURCE_PREFIX) && !datasourceType.equals(tmpKey)){
					String datasourceKey = tmpKey.replace(DATASOURCE_PREFIX, "");
					props.put(datasourceKey, properties.get(key));
				}
			}

			DataSourceFactory factory;
			try {
				factory = (DataSourceFactory) TYPE_ALIASES.get(type.toUpperCase()).newInstance();
				factory.setProperties(props);

				this.dataSource = factory.getDataSource();
			} catch (InstantiationException | IllegalAccessException e) {
				throw new ParamsNeedException("datasource生成失败！", e);
			}
		}
	}

	public DataSource getDataSource() {
		return dataSource;
	}

	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	public String getPackageName() {
		return packageName;
	}

	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}

	public Properties getProperties() {
		return properties;
	}

	public void setProperties(Properties properties) {
		this.properties = properties;
	}

}
