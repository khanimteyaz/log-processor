package org.my.infra.log.config;

import org.apache.commons.dbcp.BasicDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.io.IOException;

@Configuration
public class HiveDataSourceConfig {

	@Value("${hive.url}")
	private String hiveConnectionURL;

	@Value("${hive.username}")
	private String userName;

	@Value("${hive.password}")
	private String password;

	public DataSource getHiveDataSource() throws IOException {
		

		/*org.apache.hadoop.conf.Configuration conf = new org.apache.hadoop.conf.Configuration();
		conf.set("hadoop.security.authentication", "kerberos");
		UserGroupInformation.setConfiguration(conf);
		UserGroupInformation.loginUserFromKeytab(this.priniciple, this.keyTabLocation);*/
		//DataSourceBuilder.create().driverClassName()

		BasicDataSource dataSource = new BasicDataSource();
		dataSource.setUrl(this.hiveConnectionURL);
		dataSource.setDriverClassName("org.apache.hive.jdbc.HiveDriver");
		dataSource.setUsername(this.userName);
		dataSource.setPassword(this.password);
		
		return dataSource;
	}
	
	@Bean(name = "jdbcTemplate")
	public JdbcTemplate getJDBCTemplate() throws IOException {
		return new JdbcTemplate(getHiveDataSource());
	}
}

