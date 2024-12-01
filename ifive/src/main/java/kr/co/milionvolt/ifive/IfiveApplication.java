package kr.co.milionvolt.ifive;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan(value={"mybatis.dao"})
public class IfiveApplication {
	public static void main(String[] args) {
		SpringApplication.run(IfiveApplication.class, args);
	}
}
