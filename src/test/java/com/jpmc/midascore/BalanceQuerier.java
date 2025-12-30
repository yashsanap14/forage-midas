package com.jpmc.midascore;

import com.jpmc.midascore.entity.UserRecord;
import com.jpmc.midascore.foundation.Balance;
import com.jpmc.midascore.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class BalanceQuerier {
    @Autowired
    private UserRepository userRepository;

    public UserRecord getUserByName(String name) {
        return userRepository.findByName(name);
    }

    public float getBalanceByName(String name) {
        UserRecord user = userRepository.findByName(name);
        return user != null ? user.getBalance() : 0;
    }

    public Balance query(Long userId) {
        UserRecord user = userRepository.findById(userId.longValue());
        if (user != null) {
            return new Balance(user.getBalance());
        }
        return new Balance(0);
    }
}
