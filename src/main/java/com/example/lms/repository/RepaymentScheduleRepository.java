package com.example.lms.repository;

import com.example.lms.entity.RepaymentSchedule;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface RepaymentScheduleRepository extends CrudRepository<RepaymentSchedule, Long> {

    Optional<RepaymentSchedule> findByLoanLoanApplicationApplicationReferenceCode(String applicationReferenceCode);
}
