package com.springboot.com;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

import java.util.Arrays;

@SpringBootApplication
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	@Bean
	public CommandLineRunner commandLineRunner(ApplicationContext ctx){
		return args -> {
			System.out.println("The following beans are created by the Spring Boot:");
			String[] beans = ctx.getBeanDefinitionNames();
			Arrays.sort(beans);
			for(String bean:beans) System.out.println(bean);
		};
	}
}

/*
Spring Boot adds the following:
@Configuration: Tags the class as a source of bean definitions for the application context.
@EnableAutoConfiguration: Tells Spring Boot to start adding beans based on classpath settings, other beans, and various property settings.
	For example, if spring-webmvc is on the classpath, this annotation flags the application as a web application and activates key behaviors,
	such as setting up a DispatcherServlet.
@ComponentScan: Tells Spring to look for other components, configurations, and services in the com/example package, letting it find the controllers.
CommandLineRunner method marked as a @Bean runs on start up.
	It retrieves all the beans that were created by your application
	or automatically added by Spring Boot. It sorts them and prints them out.
*/

/**
 * By default all beans are singletons therfore Spring is able to manage them so easily
 *
 * The application implements use of
 * 		Spring CommandLineRunner
 * 		RestController(GET/PUT/POST/DEL)
 * 		Service and Component Beans
 * 		Spring Boot Application Tests
 * 		Interceptor(Before/After Sending Req)
 * 		WebMvcConfigurerAdapter to register the Interceptor
 */