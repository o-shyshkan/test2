package com.example.test2.service;

import com.example.test2.model.User;
import com.example.test2.repository.CustomRepository;
import com.example.test2.service.impl.UserServiceImpl;
import com.example.test2.service.yaml.DataBaseProperties;
import com.example.test2.service.yaml.YamlProperties;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.context.ApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;
import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;

public class UserServiceTest {
    private static final String USER_ID = "1";
    private static final String USER_NAME = "admin";
    private static final String USER_FIRST_NAME = "Bob";
    private static final String USER_SURE_NAME = "Petrov";
    private UserService userService;
    private YamlProperties yamlProperties;
    private List<DataBaseProperties> datasources;
    private DataBaseProperties dataBasePropertiesDB1;
    private DataBaseProperties dataBasePropertiesDB2;
    private CustomRepository customRepository;
    private ApplicationContext applicationContext;
    private JdbcTemplate customJdbcTemplate;
    private DataSource ds;
    private User user;
    private List<User> users;

    @BeforeEach
    void setUp() {
        yamlProperties = Mockito.mock(YamlProperties.class);
        dataBasePropertiesDB1 = Mockito.mock(DataBaseProperties.class);
        dataBasePropertiesDB2 = Mockito.mock(DataBaseProperties.class);
        customRepository = Mockito.mock(CustomRepository.class);
        applicationContext = Mockito.mock(ApplicationContext.class);
        ds = Mockito.mock(DataSource.class);
        customJdbcTemplate = Mockito.mock(JdbcTemplate.class);


        datasources = new ArrayList<>();
        datasources.add(dataBasePropertiesDB1);
        datasources.add(dataBasePropertiesDB2);
        user = new User();
        user.setId(USER_ID);
        user.setUsername(USER_NAME);
        user.setName(USER_FIRST_NAME);
        user.setSurname(USER_SURE_NAME);
        users = new ArrayList<>();
        users.add(user);
        userService = new UserServiceImpl(customRepository, applicationContext, yamlProperties);
    }

    @Test
    void getByUserName_Ok() {
        Mockito.when(yamlProperties.getDatasources()).thenReturn(datasources);
        Mockito.when(customRepository.getUsersByName(USER_NAME)).thenReturn(users);
        Mockito.when(applicationContext.getBean("customDataSource")).thenReturn(ds);
        Mockito.when(applicationContext.getBean("customJdbcTemplate")).thenReturn(customJdbcTemplate);
        List<User> actualUsers = userService.getByUserName(USER_NAME);
        Assertions.assertFalse(actualUsers.isEmpty());
        User actualUser = actualUsers.get(0);
        Assertions.assertNotNull(actualUser);
        Assertions.assertEquals(USER_ID, actualUser.getId());
        Assertions.assertEquals(USER_NAME, actualUser.getUsername());
        Assertions.assertEquals(USER_FIRST_NAME, actualUser.getName());
        Assertions.assertEquals(USER_SURE_NAME, actualUser.getSurname());
    }

    @Test
    void findAllUser_Ok() {
        Mockito.when(yamlProperties.getDatasources()).thenReturn(datasources);
        Mockito.when(customRepository.getAllUsers()).thenReturn(users);
        Mockito.when(applicationContext.getBean("customDataSource")).thenReturn(ds);
        Mockito.when(applicationContext.getBean("customJdbcTemplate")).thenReturn(customJdbcTemplate);
        List<User> actualUsers = userService.findAllUsers();
        Assertions.assertFalse(actualUsers.isEmpty());
        User actualUser = actualUsers.get(0);
        Assertions.assertNotNull(actualUser);
    }
}
