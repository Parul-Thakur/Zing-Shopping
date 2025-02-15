package com.ecom.Zing.Shopping.service;

import com.ecom.Zing.Shopping.model.User;

import java.util.List;

public interface UserService {
   public  User saveUser(User user);
    public User getUserByEmail(String email);
    public List<User> getUsers(String role);
    Boolean updateAccountStatus(Integer id, Boolean status);
    public void increaseFailedAttempt(User user);
    public void userAccountLock(User user);
    public Boolean unlockAccountTimeExpired(User user);
    public void resetAttempt(int id);
    public void updateUserResetToken(String email, String resetToken);
    public User getUserByToken(String resetToken);
    public User updateUser(User user);
}
