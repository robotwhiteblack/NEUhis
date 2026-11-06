package com.antrain.his;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.Banner;

@MapperScan("com.antrain.his.mapper")
@SpringBootApplication
public class WebApp{

	public static void main(String[] args) {
		SpringApplication application = new SpringApplication(WebApp.class); 
        application.setBannerMode(Banner.Mode.OFF);//关闭
        application.run(args);  
	}

}
