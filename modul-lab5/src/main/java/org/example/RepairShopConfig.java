package org.example;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.example.repository.ComputerRepairRequestRepository;
import org.example.repository.ComputerRepairedFormRepository;
import org.example.repository.jdbc.ComputerRepairRequestJdbcRepository;
import org.example.repository.jdbc.ComputerRepairedFormJdbcRepository;
import org.example.services.ComputerRepairServices;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

@Configuration
public class RepairShopConfig {
    @Bean
    Properties getProps() {
        Properties props = new Properties();
        try {
            System.out.println("Searchin bd.config in directory " + ((new File(".")).getAbsolutePath()));
            props.load(new FileReader("bd.config"));
        }catch (IOException e) {
            System.err.println("Configuration file bf.config not found" + e);
        }

        return props;
    }

    @Bean
    ComputerRepairRequestRepository requestsRepo(){
        return new ComputerRepairRequestJdbcRepository(getProps());
    }

    @Bean
    ComputerRepairedFormRepository formsRepo(){
        return new ComputerRepairedFormJdbcRepository(getProps());
    }

    @Bean
    ComputerRepairServices services(){
        return new ComputerRepairServices(requestsRepo(), formsRepo());
    }

}
