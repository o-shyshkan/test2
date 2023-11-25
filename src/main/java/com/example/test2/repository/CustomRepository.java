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
    @Qualifier("customJdbcTemplate")
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private YamlProperties yamlProperties;

    public List<User> getAllUser() {
        List<User> users= jdbcTemplate.query(buildSqlQuery(), new UserMapper());
        return users;
    }

    public List<User> getUsersByName(String username) {
        Map<String, String> mapColumn = yamlProperties.getDatasources()
                .get(DBContextHolder.getCurrentDb()).getMapping();
        return jdbcTemplate.query(buildSqlQuery()
                .concat(String.format(" WHERE %s = '%s' ", mapColumn.get("username"), username)),
                new UserMapper());
    }

    private String buildSqlQuery() {
        DataBaseProperties dataBaseProperties = yamlProperties.getDatasources().get(DBContextHolder.getCurrentDb());
        Map<String, String> mapColumn = dataBaseProperties.getMapping();
        String sql = String.format(SQL_QUERY_TEMPLATE,
                mapColumn.get("id"),
                mapColumn.get("username"),
                mapColumn.get("name"),
                mapColumn.get("surname"),
                dataBaseProperties.getName(),
                dataBaseProperties.getTable());
        return sql;
    }
}
