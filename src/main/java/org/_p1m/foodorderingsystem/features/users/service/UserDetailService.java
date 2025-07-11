package org._p1m.foodorderingsystem.features.users.service;

import org._p1m.foodorderingsystem.features.users.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;

public interface UserDetailService {

//public UserDetails loadByEmail(String email);
    public UserDetails loadByUserName(String username);
}
