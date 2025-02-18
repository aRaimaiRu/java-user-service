package com.eshop.service.userservice;

import com.eshop.common_lib.constant.RoleEnum;
import com.eshop.service.userservice.dto.RegisterRequest;
import com.eshop.service.userservice.model.Role;
import com.eshop.service.userservice.repository.RoleRepository;
import com.eshop.service.userservice.service.AuthenticationService;
import com.eshop.service.userservice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
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
	CommandLineRunner runner(RoleRepository roleRepository, Environment environment, AuthenticationService authenticationService)
	{
		return args -> {
			RoleEnum[] roles = RoleEnum.values();
			for (RoleEnum role : roles) {
				if (roleRepository.findByName(role).isEmpty()) {
					roleRepository.save(Role.builder().name(role).build());
				}
			}

			// seed admin if dev profile
			if (environment.getActiveProfiles().length > 0 && environment.getActiveProfiles()[0].equals("dev")) {
				RegisterRequest req = RegisterRequest.builder()
						.email("admin@email.com")
						.password("password")
						.firstname("admin")
						.firstname("admin")
						.build();

				authenticationService.registerAdmin(req);
			}
		};
	}

}
