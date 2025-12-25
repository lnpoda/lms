package com.example.lms;

import com.example.lms.entity.LMSUser;
import com.example.lms.repository.LMSUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@EnableJpaAuditing
@SpringBootApplication
public class LmsApplication {

	@Autowired
	public LMSUserRepository LMSUserRepository;

	public static void main(String[] args) {
		SpringApplication.run(LmsApplication.class, args);
	}

	@Bean
	public CommandLineRunner commandLineRunner(ApplicationContext ctx) {
		return args -> {
			if (LMSUserRepository.count() == 0) {
				LMSUser LMSUser = new LMSUser();
				LMSUser.setEmail("loanuser@email.com");
				LMSUser.setPassword("{noop}12345");
				LMSUser.setRole("ROLE_APPLICANT");

				LMSUser admin = new LMSUser();
				admin.setEmail("loanadmin@email.com");
				admin.setPassword("{noop}54321");
				admin.setRole("ROLE_ADMIN");

				LMSUserRepository.save(LMSUser);
				LMSUserRepository.save(admin);
			}
		};

	}
}
