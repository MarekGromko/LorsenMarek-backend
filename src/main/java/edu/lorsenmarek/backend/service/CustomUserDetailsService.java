package edu.lorsenmarek.backend.service;

import edu.lorsenmarek.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Collections;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    @Autowired private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException{
        edu.lorsenmarek.backend.model.User user = userRepository.findByEmail(email);
        if (user == null){
            throw  new UsernameNotFoundException("User Not Found with email");
        }
        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPwdDigest(),
                Collections.emptyList()

        );
    }

}
