package com.example.test2.config;

import com.example.test2.service.DBContextHolder;
import com.example.test2.service.yaml.DataBaseProperties;
import com.example.test2.service.yaml.YamlProperties;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


@RunWith(SpringRunner.class)
@ContextConfiguration(classes=CustomDataSourceConfiguration.class, loader= AnnotationConfigContextLoader.class)
public class CustomDataSourceConfigurationTest {
    @Autowired
    private DataSource customDataSource;
    @Autowired
    private YamlProperties yamlProperties;

    @Test
    void customDataSource_Ok() {
        yamlProperties = Mockito.mock(YamlProperties.class);
        DataBaseProperties dataBaseProperties = new DataBaseProperties();
        dataBaseProperties.setName("uamed_db");
        dataBaseProperties.setStrategy("postgre");
        dataBaseProperties.setUrl("jdbc:postgresql://localhost:5432/postgres");
        dataBaseProperties.setTable("user");
        dataBaseProperties.setUser("sa");
        dataBaseProperties.setPassword("sa");
        HashMap<String, String> mapColumn = new HashMap<>();
        mapColumn.put("id","user_id");
        mapColumn.put("username","username");
        mapColumn.put("name","firstname");
        mapColumn.put("surname","lastname");
        dataBaseProperties.setMapping(mapColumn);
        List<DataBaseProperties> dataBasePropertiesList = new ArrayList<>();
        dataBasePropertiesList.add(dataBaseProperties);
        Mockito.when(yamlProperties.getDatasources()).thenReturn(dataBasePropertiesList);
        //Mockito.when(yamlProperties.getDatasources().get(DBContextHolder.getCurrentDb())).thenReturn(dataBaseProperties);
    /*    AnnotationConfigApplicationContext ctx
                = new AnnotationConfigApplicationContext();
        ctx.register(CustomDataSourceConfiguration.class);
        ctx.register(YamlProperties.class);
        ctx.refresh();*/
        DataSource actualDataSource =  ctx.getBean(DataSource.class);
        Assertions.assertNotNull(actualDataSource);

    }
}
