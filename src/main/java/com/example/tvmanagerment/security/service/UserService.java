package com.example.tvmanagerment.security.service;
import com.example.tvmanagerment.Model.Role;
import com.example.tvmanagerment.Model.Users;
import com.example.tvmanagerment.Repository.IRoleRepository;
import com.example.tvmanagerment.Repository.IUserRepository;
import com.example.tvmanagerment.security.UserPrinciple;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
public class UserService implements UserDetailsService {
    @Autowired
    private IUserRepository userRepository;
    @Autowired
    private IRoleRepository roleRepository;

    public Users findByUsername(String name) {
        return userRepository.findByUsername(name);
    }
    public void save(Users user) {
        userRepository.save(user);
    }
    @Override
    public UserDetails loadUserByUsername(String username) {
        return UserPrinciple.build(userRepository.findByUsername(username));
    }
    @Transactional
    public void addUserWithDefaultRole(Users user) {
        Users savedUser = userRepository.save(user);
        Role defaultRole = roleRepository.findById(2L).orElseThrow(() -> new RuntimeException("Default role not found"));
        savedUser.getRoles().add(defaultRole);
        userRepository.save(savedUser);
    }

}
