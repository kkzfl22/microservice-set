package com.liujun.microservice.oauth2.client.webclient.security;

import com.liujun.microservice.oauth2.client.webclient.repository.AuthUserRepository;
import com.liujun.microservice.oauth2.client.webclient.user.AuthWebclientUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * @author liujun
 * @version 0.0.1
 */
@Service
public class WebClientUserDetailsService implements UserDetailsService {

  @Autowired private AuthUserRepository userRepository;

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    Optional<AuthWebclientUser> userinfo = userRepository.findByUserName(username);

    if (!userinfo.isPresent()) {
      throw new UsernameNotFoundException("invalid username or password");
    }

    return new WebClientUserDetails(userinfo.get());
  }
}
