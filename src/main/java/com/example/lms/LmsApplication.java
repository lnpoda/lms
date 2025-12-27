package com.example.lms;

import com.example.lms.entity.LMSUser;
import com.example.lms.entity.Role;
import com.example.lms.repository.LMSUserRepository;
import com.example.lms.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.Set;

@EnableScheduling
@EnableJpaAuditing
@SpringBootApplication
public class LmsApplication {

	@Autowired
	public LMSUserRepository LMSUserRepository;

	@Autowired
	public RoleRepository roleRepository;

	public static void main(String[] args) {
		SpringApplication.run(LmsApplication.class, args);
	}

	@Bean
	public CommandLineRunner commandLineRunner(ApplicationContext ctx) {
		return args -> {
			if (LMSUserRepository.count() == 0) {
				LMSUser lmsUser = new LMSUser();
				lmsUser.setEmail("loanuser@email.com");
				lmsUser.setPassword("{noop}12345");


				Role userRole = new Role();
				userRole.setRole("ROLE_APPLICANT");
				roleRepository.save(userRole);

				lmsUser.setRole(userRole);
				LMSUserRepository.save(lmsUser);



				LMSUser admin = new LMSUser();
				admin.setEmail("loanadmin@email.com");
				admin.setPassword("{noop}54321");


				Role adminRole = new Role();
				adminRole.setRole("ROLE_ADMIN");
				roleRepository.save(adminRole);

				admin.setRole(adminRole);
				LMSUserRepository.save(admin);

			}
		};

	}
}
