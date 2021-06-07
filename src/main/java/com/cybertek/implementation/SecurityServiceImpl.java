package com.cybertek.implementation;

import com.cybertek.entity.User;
import com.cybertek.entity.common.UserPrincipal;
import com.cybertek.repository.UserRepository;
import com.cybertek.service.SecurityService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class SecurityServiceImpl implements SecurityService {
    //We are trying to describe the user to spring
    private UserRepository userRepository;

    public SecurityServiceImpl(UserRepository userRepository) {     //we injected
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {

        User user = userRepository.findByUserName(s);

        //If we dont have this app will crash, now app will keep running but throw exception
        if(user==null){
            throw new UsernameNotFoundException("This User does NOT exist");
        }


        return new UserPrincipal(user);
    }
}
