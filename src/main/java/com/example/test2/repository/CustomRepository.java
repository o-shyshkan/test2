package com.example.test2.repository;

import com.example.test2.mapper.impl.UserMapper;
import com.example.test2.model.User;
import com.example.test2.service.DBContextHolder;
import com.example.test2.service.yaml.DataBaseProperties;
import com.example.test2.service.yaml.YamlProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Lazy;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Map;

@Lazy
@Repository
@ComponentScan(basePackages = "com.example.test2")

public class CustomRepository {
    public static final String SQL_QUERY_TEMPLATE = "SELECT %s, %s, %s, %s FROM %s.%s";
    public static final String ID = "id";
    public static final String USERNAME = "username";
    public static final String NAME = "name";
    public static final String SURNAME = "surname";
    @Qualifier("customJdbcTemplate")
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private YamlProperties yamlProperties;

    public List<User> getAllUsers() {
        List<User> users= jdbcTemplate.query(buildSqlQuery(), new UserMapper());
        return users;
    }

    public List<User> getUsersByName(String username) {
        Map<String, String> mapColumn = yamlProperties.getDatasources()
                .get(DBContextHolder.getCurrentDb()).getMapping();
        return jdbcTemplate.query(buildSqlQuery()
                .concat(String.format(" WHERE %s = '%s' ", mapColumn.get(USERNAME), username)),
                new UserMapper());
    }

    private String buildSqlQuery() {
        DataBaseProperties dataBaseProperties = yamlProperties.getDatasources().get(DBContextHolder.getCurrentDb());
        Map<String, String> mapColumn = dataBaseProperties.getMapping();
        String sql = String.format(SQL_QUERY_TEMPLATE,
                mapColumn.get(ID),
                mapColumn.get(USERNAME),
                mapColumn.get(NAME),
                mapColumn.get(SURNAME),
                dataBaseProperties.getName(),
                dataBaseProperties.getTable());
        return sql;
    }
}
