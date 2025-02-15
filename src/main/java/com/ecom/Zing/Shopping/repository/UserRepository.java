package com.ecom.Zing.Shopping.repository;

import com.ecom.Zing.Shopping.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Integer> {
    public User findByEmail(String email);

    public List<User> findByRole(String role);
    public User findByResetToken(String resetToken);

}
