package com.example.lms;

import com.example.lms.entity.Customer;
import com.example.lms.repository.CustomerRepository;
import com.example.lms.service.ReportService;
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
	CustomerRepository customerRepository;

	@Autowired
	ReportService reportService;

	public static void main(String[] args) {
		SpringApplication.run(LmsApplication.class, args);
	}

	@Bean
	public CommandLineRunner commandLineRunner(ApplicationContext ctx) {
		return args -> {
			Customer customer1 = new Customer();
			customer1.setName("testcustomer1");
			customer1.setEmail("testmail1@email.com");
			customer1.setMobileNumber("999999999");

			Customer customer2 = new Customer();
			customer2.setName("testcustomer2");
			customer2.setEmail("testmail2@email.com");
			customer2.setMobileNumber("999999998");

			customerRepository.save(customer1);
			customerRepository.save(customer2);

			reportService.generateAndSaveLoanReportPDF();
			reportService.generateAndSaveLoanReportCSV("./reports/loan_report.csv");
		};

	}
}
