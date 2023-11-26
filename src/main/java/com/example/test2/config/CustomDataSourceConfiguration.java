package com.example.test2.config;

import com.example.test2.service.yaml.DataBaseProperties;
import com.example.test2.service.yaml.YamlProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.core.JdbcTemplate;
import javax.sql.DataSource;
import com.example.test2.service.DBContextHolder;

@RequiredArgsConstructor
@Configuration
public class CustomDataSourceConfiguration {

    public static final String DRIVER_CLASS_NAME = "org.postgresql.Driver";
    private final YamlProperties yamlProperties;

    @Lazy
    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public DataSource customDataSource() {
        DataBaseProperties dbSettings = yamlProperties.getDatasources().get(DBContextHolder.getCurrentDb());
        DataSourceBuilder dsBuilder = DataSourceBuilder.create();
        dsBuilder.driverClassName(DRIVER_CLASS_NAME);
        dsBuilder.url(dbSettings.getUrl());
        dsBuilder.username(dbSettings.getUser());
        dsBuilder.password(dbSettings.getPassword());
        return dsBuilder.build();
    }

    @Lazy
    @Qualifier("customJdbcTemplate")
    @Bean
    public JdbcTemplate customJdbcTemplate() {
        return new JdbcTemplate(customDataSource());
    }
}
