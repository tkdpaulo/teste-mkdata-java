package com.mkdata.testemkdatajava;

import jakarta.annotation.PostConstruct;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.jdbc.core.JdbcTemplate;

@SpringBootApplication
public class TesteMkdataJavaApplication {
	private final JdbcTemplate jdbcTemplate;

	public TesteMkdataJavaApplication(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	public static void main(String[] args) {
		SpringApplication.run(TesteMkdataJavaApplication.class, args);
	}

	@PostConstruct
	public void testDatabaseConnection() {
		String query = "SELECT 1";
		Integer result = jdbcTemplate.queryForObject(query, Integer.class);
		System.out.println("Test query result: " + result);
	}
}
