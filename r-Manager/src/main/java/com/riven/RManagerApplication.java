package com.riven;

import com.riven.core.dao.impl.BaseDaoImpl;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories(repositoryBaseClass = BaseDaoImpl.class)
public class RManagerApplication {

    public static void main(String[] args) {
        SpringApplication.run(RManagerApplication.class, args);
    }

}
