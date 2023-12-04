package me.chang.gpms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

//@SpringBootApplication(
//        exclude = {
//                ElasticsearchDataAutoConfiguration.class,
//                ElasticsearchRepositoriesAutoConfiguration.class
//        })
@SpringBootApplication
public class GpmsApplication {

    public static void main(String[] args) {
        SpringApplication.run(GpmsApplication.class, args);
    }

}
