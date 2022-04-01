package com.jetbrains.testcontainersdemo;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.BindMode;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.containers.RabbitMQContainer;
import org.testcontainers.containers.output.OutputFrame;
import org.testcontainers.containers.output.Slf4jLogConsumer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.io.IOException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@Testcontainers
public class CustomerIntegrationTests {

    @Autowired
    private CustomerDao customerDao;

    // Version Docker Hub

    @Container
    private static MySQLContainer mySQLContainer = new MySQLContainer("mysql:5.7.37");

    @Container
    private static RabbitMQContainer rabbitMQContainer = new RabbitMQContainer("rabbitmq:latest");

    @Container
    private static GenericContainer genericContainer = new GenericContainer("rabbitmq:latest");

   // One Shot Database
    //testcontainers.reuse.enable=true

    private static MySQLContainer container = (MySQLContainer) new MySQLContainer("mysql:5.7.37")
       .withReuse(true);

    @BeforeAll
    public static void setup() {
        container.start();
    }
        //.withExposedPorts(2000);
 //   private static GenericContainer genericContainer = new GenericContainer("myImage:myTag");

   @DynamicPropertySource
   public static void overrideProps(DynamicPropertyRegistry registry) {
       registry.add("spring.datasource.url",mySQLContainer::getJdbcUrl);
       registry.add("spring.datasource.username",mySQLContainer::getUsername);
       registry.add("spring.datasource.password",mySQLContainer::getPassword);
   }

    @Test
    void when_using_a_clean_db_this_should_be_empty() {
        List<Customer> customers = customerDao.findAll();
        assertThat(customers).hasSize(2);
    }

    @Test
    void some_functions_container() throws IOException, InterruptedException {
     genericContainer.withClasspathResourceMapping("application.properties","/tmp/application.properties", BindMode.READ_ONLY);
     genericContainer.withFileSystemBind("C:\\dev\\application.properties","/tmp/bla.txt",BindMode.READ_ONLY);
     genericContainer.execInContainer("ll","pwd");
     String logs = genericContainer.getLogs(OutputFrame.OutputType.STDOUT);
     // genericContainer.withLogConsumer(new Slf4jLogConsumer("s"));Integer portOnMyMachine = genericContainer.getMappedPort(3306);
    }
}