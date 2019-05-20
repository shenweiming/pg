package cn.ecust;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import tk.mybatis.spring.annotation.MapperScan;
@EnableAutoConfiguration
@SpringBootApplication
@MapperScan("cn.ecust.user.dao")
public class PgApplication  {
    public static void main(String[] args) {
        SpringApplication.run(PgApplication.class, args);}
    }

