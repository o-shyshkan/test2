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


@SpringBootTest
@RunWith(SpringRunner.class)
@ActiveProfiles("tc")
@ContextConfiguration(initializers = {CustomRepositoryTest.Initializer.class})
class CustomRepositoryTest {
	public static final String USER_ADMIN = "admin";
	public static final String USER_ID_NUMBER = "1";
	public static final String USER_NAME_ADMIN = "admin";
	public static final String USER_FIRST_NAME = "Admin";
	public static final String USER_SURE_NAME = "Adminov";
	public static final String BEAN_CUSTOM_DATA_SOURCE = "customDataSource";
	public static final String BEAN_CUSTOM_JDBC_TEMPLATE = "customJdbcTemplate";
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
		DataSource ds = (DataSource) applicationContext.getBean(BEAN_CUSTOM_DATA_SOURCE);
		JdbcTemplate customJdbcTemplate = (JdbcTemplate) applicationContext.getBean(BEAN_CUSTOM_JDBC_TEMPLATE);
		customJdbcTemplate.setDataSource(ds);
		admin = new User();
		admin.setId(USER_ID_NUMBER);
		admin.setUsername(USER_NAME_ADMIN);
		admin.setName(USER_FIRST_NAME);
		admin.setSurname(USER_SURE_NAME);
	}

	@Test
	@Transactional
	public void getAllUser_Ok() {
		List<User> actualUsers = customRepository.getAllUsers();
		Assertions.assertFalse(actualUsers.isEmpty());
		Assertions.assertEquals(3, actualUsers.size());
	}

	@Test
	@Transactional
	public void getUsersByName_Ok() {
		List<User> actualUsers = customRepository.getUsersByName(USER_ADMIN);
		Assertions.assertFalse(actualUsers.isEmpty());
		Assertions.assertEquals(1, actualUsers.size());
		User actualUser = actualUsers.get(0);
		Assertions.assertNotNull(actualUser);
		Assertions.assertEquals(actualUser, admin);
		Assertions.assertEquals(USER_ID_NUMBER, actualUser.getId());
		Assertions.assertEquals(USER_NAME_ADMIN, actualUser.getUsername());
		Assertions.assertEquals(USER_FIRST_NAME, actualUser.getName());
		Assertions.assertEquals(USER_SURE_NAME, actualUser.getSurname());
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
