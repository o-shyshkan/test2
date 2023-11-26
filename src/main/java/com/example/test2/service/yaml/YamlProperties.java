package com.example.test2.service.yaml;

import lombok.Data;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.util.List;

@Component
@ConfigurationProperties
@Data
public class YamlProperties {
    private List<DataBaseProperties> datasources = new ArrayList<>();
}
