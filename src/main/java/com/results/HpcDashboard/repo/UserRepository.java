package com.results.HpcDashboard.repo;

import com.results.HpcDashboard.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long > {
    User findByEmail(String email);
    User findByUserName(String userName);
}