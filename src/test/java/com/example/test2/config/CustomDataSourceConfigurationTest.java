package com.example.test2.config;

import com.example.test2.service.yaml.DataBaseProperties;
import com.example.test2.service.yaml.YamlProperties;
import com.zaxxer.hikari.HikariDataSource;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
@RunWith(SpringRunner.class)
public class CustomDataSourceConfigurationTest {
    public static final String DRIVER_CLASS_NAME = "org.postgresql.Driver";
    public static final String JDBC_POSTGRESQL_LOCALHOST_5432_POSTGRES = "jdbc:postgresql://localhost:5432/postgres";
    public static final String USER_DB = "sa";
    public static final String PASSWORD_DB = "sa";
    @Autowired
    private ApplicationContext applicationContext;
    private YamlProperties yamlProperties;

    @Test
    public void customDataSource_Ok() {
        yamlProperties = Mockito.mock(YamlProperties.class);
        DataBaseProperties dataBaseProperties = new DataBaseProperties();
        dataBaseProperties.setUrl(JDBC_POSTGRESQL_LOCALHOST_5432_POSTGRES);
        dataBaseProperties.setUser(USER_DB);
        dataBaseProperties.setPassword(PASSWORD_DB);
        List<DataBaseProperties> dataBasePropertiesList = new ArrayList<>();
        dataBasePropertiesList.add(dataBaseProperties);
        Mockito.when(yamlProperties.getDatasources()).thenReturn(dataBasePropertiesList);
        HikariDataSource actualDataSource = (HikariDataSource) applicationContext.getBean("customDataSource");
        Assertions.assertNotNull(actualDataSource);
        Assertions.assertEquals(DRIVER_CLASS_NAME, actualDataSource.getDriverClassName());
        Assertions.assertEquals(JDBC_POSTGRESQL_LOCALHOST_5432_POSTGRES, actualDataSource.getJdbcUrl());
        Assertions.assertEquals(USER_DB, actualDataSource.getUsername());
        Assertions.assertEquals(PASSWORD_DB, actualDataSource.getPassword());
    }
}
