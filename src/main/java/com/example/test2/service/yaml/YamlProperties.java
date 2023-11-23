package com.example.test2.service.yaml;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.util.List;

@Component
@ConfigurationProperties
@Data
public class YamlProperties {
    private List<DataBaseProperties> datasources = new ArrayList<>();
}
