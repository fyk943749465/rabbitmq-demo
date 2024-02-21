package com.example.sqlitedemo;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.jdbc.JdbcTemplateAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;
import java.util.Map;

@SpringBootTest
@Slf4j
class SqliteDemoApplicationTests {


    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    void contextLoads() {
    }


    @Test
    void testSqlit() {
        List<Map<String, Object>> maps = jdbcTemplate.queryForList("select * from area");

        maps.stream().forEach(e -> {

            e.forEach((k, v) -> {
                log.info("{} {}", k, v);
            });
        });
        log.info("数量{}", maps.size());
    }
}
