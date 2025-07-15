package com.sahan.chatApp.repository;

import com.sahan.chatApp.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepo extends JpaRepository<User,String> {
    @Override
    <S extends User> S save(S user);
}
