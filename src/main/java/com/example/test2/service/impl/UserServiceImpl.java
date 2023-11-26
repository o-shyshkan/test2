package com.example.test2.service.impl;

import com.example.test2.model.User;
import com.example.test2.repository.CustomRepository;
import com.example.test2.service.UserService;
import com.example.test2.service.yaml.DataBaseProperties;
import com.example.test2.service.yaml.YamlProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import com.example.test2.service.DBContextHolder;
import javax.sql.DataSource;
import java.util.Collection;
import java.util.List;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    public static final String BEAN_CUSTOM_DATA_SOURCE = "customDataSource";
    public static final String BEAN_CUSTOM_JDBC_TEMPLATE = "customJdbcTemplate";
    private final CustomRepository customRepository;
    private final ApplicationContext applicationContext;
    private final YamlProperties yamlProperties;

    @Override
    public List<User> findAllUsers() {
        List<DataBaseProperties> datasources = yamlProperties.getDatasources();
        List<User> users = IntStream.range(0, datasources.size())
                .mapToObj(this::getUsers)
                .flatMap(Collection::stream)
                .toList();
        return users;
    }

    @Override
    public List<User> getByUserName(String username) {
        List<DataBaseProperties> datasources = yamlProperties.getDatasources();
        List<User> user = IntStream.range(0, datasources.size())
                .mapToObj(e -> getUsersByName(e, username))
                .flatMap(Collection::stream)
                .toList();
        return user;
    }

    private List<User> getUsers(Integer indexBase) {
        refreshCustomJdbc(indexBase);
        return customRepository.getAllUsers();
    }

    private List<User> getUsersByName(Integer indexBase, String username) {
        refreshCustomJdbc(indexBase);
        return customRepository.getUsersByName(username);
    }

    private void refreshCustomJdbc(int indexBase) {
        DBContextHolder.setCurrentDb(indexBase);
        DataSource ds = (DataSource) applicationContext.getBean(BEAN_CUSTOM_DATA_SOURCE);
        JdbcTemplate customJdbcTemplate = (JdbcTemplate) applicationContext.getBean(BEAN_CUSTOM_JDBC_TEMPLATE);
        customJdbcTemplate.setDataSource(ds);
    }
}
