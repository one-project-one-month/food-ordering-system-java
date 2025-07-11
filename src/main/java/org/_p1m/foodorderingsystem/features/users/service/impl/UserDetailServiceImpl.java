package org._p1m.foodorderingsystem.features.users.service.impl;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org._p1m.foodorderingsystem.features.users.repository.ProfileRepository;
import org._p1m.foodorderingsystem.features.users.repository.UserRepository;
import org._p1m.foodorderingsystem.features.users.service.UserDetailService;
import org._p1m.foodorderingsystem.model.Profile;
import org._p1m.foodorderingsystem.model.User;
import org._p1m.foodorderingsystem.model.UserDetail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailServiceImpl implements UserDetailsService {
    private  UserRepository userRepository;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return null;
    }
}
