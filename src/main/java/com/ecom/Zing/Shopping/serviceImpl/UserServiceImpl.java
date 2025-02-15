package com.ecom.Zing.Shopping.serviceImpl;

import com.ecom.Zing.Shopping.model.User;
import com.ecom.Zing.Shopping.repository.UserRepository;
import com.ecom.Zing.Shopping.service.UserService;
import com.ecom.Zing.Shopping.util.AppConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public User saveUser(User user) {
        user.setRole("ROLE_USER");
        user.setIsEnable(true);
        user.setAccountNonLocked(true);
        user.setFailedAttempt(0);
        String encodePassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodePassword);
        User savedUser = userRepository.save(user);
        return savedUser;
    }

    @Override
    public User getUserByEmail(String email) {
        System.out.println("Fetching user by email: " + email);
        return userRepository.findByEmail(email);
    }

    @Override
    public List<User> getUsers(String role) {
        return userRepository.findByRole(role);

    }

    @Override
    public void increaseFailedAttempt(User user) {
        int attempt = user.getFailedAttempt() + 1;
        user.setFailedAttempt(attempt);
        userRepository.save(user);
    }

    @Override
    public void userAccountLock(User user) {
        user.setAccountNonLocked(false);
        user.setLockTime(new Date());
        userRepository.save(user);
    }

    @Override
    public Boolean unlockAccountTimeExpired(User user) {
        long lockTime = user.getLockTime().getTime();
        long unLockTime = lockTime+ AppConstant.UNLOCK_DURATION_TIME;
       long currentTime = System.currentTimeMillis();
       if(unLockTime<currentTime){
           user.setAccountNonLocked(true);
           user.setFailedAttempt(0);
           userRepository.save(user);
           return true;
       }
        return false;
    }

    @Override
    public void resetAttempt(int id) {

    }

    @Override
    public void updateUserResetToken(String email, String resetToken) {
        User findByEmail = userRepository.findByEmail(email);
        findByEmail.setResetToken(resetToken);
        userRepository.save(findByEmail);
    }

    @Override
    public User getUserByToken(String token) {
        return userRepository.findByResetToken(token);
    }


    @Override
    public User updateUser(User user) {
        return userRepository.save(user);
    }

    @Override
    public Boolean updateAccountStatus(Integer id, Boolean status) {
        Optional<User> findByUser = userRepository.findById(id);
        if (findByUser.isPresent()) {
            User user = findByUser.get();
            user.setIsEnable(status);
            userRepository.save(user);
            return true;
        }
        return false;
    }
}




