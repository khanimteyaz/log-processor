package org.my.infra.log.controller;

import com.google.common.collect.Maps;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCallback;
import org.springframework.web.bind.annotation.*;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RestController
@RequestMapping("/hive")
public class SampleHiveController {

	@Autowired
	private JdbcTemplate jdbcTemplate;
	private Logger logger=LoggerFactory.getLogger(this.getClass());

	@RequestMapping(value = "/{schemaName}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<Map<String, Object>>> showTables(@PathVariable String schemaName) {
		List<Map<String, Object>> rows = null;
		jdbcTemplate.execute("use " + schemaName);
		rows = jdbcTemplate.queryForList("show tables");
		logger.info("Returning response of list of tables under {}",schemaName);
		return new ResponseEntity<List<Map<String, Object>>>(rows, HttpStatus.OK);
	}

	@RequestMapping(value = "/error", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<Map<String, Object>>> logError() {
		List<Map<String, Object>> rows = null;
		jdbcTemplate.execute("use retail");
		rows = jdbcTemplate.queryForList("show tables");
		try {
			throw new IllegalArgumentException("Wrong input given");
		} catch(Exception ex) {
			logger.error(ex.getMessage(),ex);
		}
		return new ResponseEntity<List<Map<String, Object>>>(rows, HttpStatus.OK);
	}

	@RequestMapping(value = "/databases", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<Map<String, Object>>> showDatabaeses() {
		List<Map<String, Object>> rows = null;
		rows = jdbcTemplate.queryForList("show databases");
		return new ResponseEntity<List<Map<String, Object>>>(rows, HttpStatus.OK);
	}

	@RequestMapping(value="/{schemaName}/{entity}",
			method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
		public ResponseEntity<List<Map<String, Object>>> get(@PathVariable("schemaName")String schemaName,
				@PathVariable("entity")String entity) {
			List<Map<String, Object>> rows = null;
			String query=String.format("select * from %s.%s limit 10",schemaName,entity);
			logger.info("Returning data for table {}.{}",schemaName,entity);
			System.out.println("query = " + query);
			rows = jdbcTemplate.queryForList(query);
			return new ResponseEntity<List<Map<String, Object>>>(rows, HttpStatus.OK);
		}



	@RequestMapping(value="/{schemaName}/{entity}",
			method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Map.Entry<String, String>> insert(@PathVariable("schemaName")String schemaName,
															@PathVariable("entity")String entity,
															@RequestBody Map<String,String> body) {
		String s="[app=]+(.*?)[,]\\s[host=]+(.*?)[}] (\\[.*?]) ([A-Z]+) (\\[.*?]) (.*)";
		/*String insertInto=String.format("insert into %s.%s (host,identity,username," +
				"event_time,request,event_status,payload_size) values(?,?,?,?,?,?,?)",schemaName,entity);*/
		String insertInto=String.format("insert into %s.%s (msg) values(?)",schemaName,entity);
		jdbcTemplate.execute(insertInto, new PreparedStatementCallback<Object>() {
			@Override
			public Object doInPreparedStatement(PreparedStatement preparedStatement) throws SQLException, DataAccessException {

				preparedStatement.setString(1,body.get("msg"));
				return preparedStatement.executeUpdate();
			}
		});

		return ResponseEntity.ok(Maps.immutableEntry("Status","Success"));
	}
	public static void main(String args[]) {
		String line = "{app=hdfs-datanode, host=192.168.0.4} [2018-11-12 01:10:31,451] INFO [ControllerEventThread controllerId=0] Starting (kafka.controller.ControllerEventManager$ControllerEventThread)\n";
		String pattern = "[{app=]+(.*?)[,]\\s[host=]+(.*?)[}] (\\[.*?]) ([A-Z]+) (\\[.*?]) (.*)";

		// Create a Pattern object
		Pattern r = Pattern.compile(pattern);

		// Now create matcher object.
		Matcher m = r.matcher(line);

		if (m.find()) {
			System.out.println("Found value: " + m.group(0));
			System.out.println("Found value: " + m.group(1));
			System.out.println("Found value: " + m.group(2));
			System.out.println("Found value: " + m.group(3));
			System.out.println("Found value: " + m.group(4));
			System.out.println("Found value: " + m.group(5));
			System.out.println("Found value: " + m.group(6));
		} else {
			System.out.println("NO MATCH");
		}
	}

}
