package com.eshop.service.userservice;

import com.eshop.common_lib.constant.RoleEnum;
import com.eshop.service.userservice.model.Role;
import com.eshop.service.userservice.repository.RoleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableAsync;


@EnableJpaAuditing
@SpringBootApplication
@EnableAsync
@EnableJpaRepositories
public class UserServiceApplication {
	public static void main(String[] args)
	{
		SpringApplication.run(UserServiceApplication.class, args);
	}

	@Bean
	CommandLineRunner runner(RoleRepository roleRepository)
	{
		return args -> {
			RoleEnum[] roles = RoleEnum.values();
			for (RoleEnum role : roles) {
				if (roleRepository.findByName(role).isEmpty()) {
					roleRepository.save(Role.builder().name(role).build());
				}
			}
		};
	}

}
