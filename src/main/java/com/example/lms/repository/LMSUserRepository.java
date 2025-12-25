package com.example.lms.repository;

import com.example.lms.entity.LMSUser;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface LMSUserRepository extends CrudRepository<LMSUser, Long> {

    Optional<LMSUser> findByEmail(String email);
}
