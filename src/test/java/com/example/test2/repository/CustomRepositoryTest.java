package com.example.test2.repository;

import com.example.test2.model.User;
import com.example.test2.service.DBContextHolder;
import com.example.test2.service.yaml.DataBaseProperties;
import com.example.test2.service.yaml.YamlProperties;
import org.junit.ClassRule;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.utility.MountableFile;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@RunWith(SpringRunner.class)
@ActiveProfiles("tc")
@ContextConfiguration(initializers = {CustomRepositoryTest.Initializer.class})
class CustomRepositoryTest {
	public static final String USED_ADMIN = "admin";
	@Autowired
	public CustomRepository customRepository;
	@Autowired
	private YamlProperties yamlProperties;
	@Autowired
	private ApplicationContext applicationContext;
	private User admin;
	@ClassRule
	public static PostgreSQLContainer postgreSQLContainer = (PostgreSQLContainer) new PostgreSQLContainer("postgres:11.1")
			.withDatabaseName("integration-tests-db")
			.withUsername("sa")
			.withPassword("sa")
			.withCopyFileToContainer(
					MountableFile.forClasspathResource("init.sql"),
					"/docker-entrypoint-initdb.d/schema.sql");
	static {
		postgreSQLContainer.start();
	}

	@BeforeEach
	void setUp() {
		List<DataBaseProperties> datasources = new ArrayList<>();
		DataBaseProperties dataBaseProperties = new DataBaseProperties();
		dataBaseProperties.setName("uamed_db");
		dataBaseProperties.setStrategy("postgre");
		dataBaseProperties.setUrl(postgreSQLContainer.getJdbcUrl());
		dataBaseProperties.setTable("user");
		dataBaseProperties.setUser(postgreSQLContainer.getUsername());
		dataBaseProperties.setPassword(postgreSQLContainer.getPassword());
		HashMap<String, String> mapColumn = new HashMap<>();
		mapColumn.put("id","user_id");
		mapColumn.put("username","username");
		mapColumn.put("name","firstname");
		mapColumn.put("surname","lastname");
		dataBaseProperties.setMapping(mapColumn);
		datasources.add(dataBaseProperties);
		yamlProperties.setDatasources(datasources);
		DBContextHolder.setCurrentDb(0);
		DataSource ds = (DataSource) applicationContext.getBean("customDataSource");
		JdbcTemplate customJdbcTemplate = (JdbcTemplate) applicationContext.getBean("customJdbcTemplate");
		customJdbcTemplate.setDataSource(ds);
		admin = new User();
		admin.setId("1");
		admin.setUsername("admin");
		admin.setName("Admin");
		admin.setSurname("Adminov");
	}

	@Test
	@Transactional
	public void getAllUser_Ok() {
		List<User> actualUsers = customRepository.getAllUser();
		Assertions.assertFalse(actualUsers.isEmpty());
		Assertions.assertEquals(3, actualUsers.size());
	}

	@Test
	@Transactional
	public void getUsersByName_Ok() {
		List<User> actualUsers = customRepository.getUsersByName(USED_ADMIN);
		Assertions.assertFalse(actualUsers.isEmpty());
		Assertions.assertEquals(1, actualUsers.size());
		User actualUser = actualUsers.get(0);
		Assertions.assertNotNull(actualUser);
		Assertions.assertEquals(actualUser, admin);
		Assertions.assertEquals("1", actualUser.getId());
		Assertions.assertEquals("admin", actualUser.getUsername());
		Assertions.assertEquals("Admin", actualUser.getName());
		Assertions.assertEquals("Adminov", actualUser.getSurname());
	}

	@Test
	@Transactional
	public void getUsersByName_IsIncorrectEmail_NotOk() {
		List<User> actualUsers = customRepository.getUsersByName(null);
		Assertions.assertTrue(actualUsers.isEmpty());
	}

	static class Initializer
			implements ApplicationContextInitializer<ConfigurableApplicationContext> {
		public void initialize(ConfigurableApplicationContext configurableApplicationContext) {
			TestPropertyValues.of(
					"spring.datasource.url=" + postgreSQLContainer.getJdbcUrl(),
					"spring.datasource.username=" + postgreSQLContainer.getUsername(),
					"spring.datasource.password=" + postgreSQLContainer.getPassword()
			).applyTo(configurableApplicationContext.getEnvironment());
		}
	}
}
