package com.example.test2.config;

import com.example.test2.service.yaml.DataBaseProperties;
import com.example.test2.service.yaml.YamlProperties;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.core.JdbcTemplate;
import javax.sql.DataSource;
import com.example.test2.service.DBContextHolder;

@Configuration
public class CustomDataSourceConfiguration {
    @Autowired
    private YamlProperties yamlProperties;

    @Lazy
    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public DataSource customDataSource() {
        DataSourceBuilder dsBuilder = DataSourceBuilder.create();
        dsBuilder.driverClassName("org.postgresql.Driver");
        DataBaseProperties dbSettings = yamlProperties.getDatasources().get(DBContextHolder.getCurrentDb());
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
