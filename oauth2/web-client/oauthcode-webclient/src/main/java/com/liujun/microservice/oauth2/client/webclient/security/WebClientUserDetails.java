package com.liujun.microservice.oauth2.client.webclient.security;

import com.liujun.microservice.oauth2.client.webclient.user.AuthWebclientUser;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.HashSet;

/** 用作用户安全认证 */
public class WebClientUserDetails implements UserDetails {

  private static final long serialVersionUID = 1L;

  private AuthWebclientUser authWebClientUser;

  public WebClientUserDetails(AuthWebclientUser authWebClientUser) {
    this.authWebClientUser = authWebClientUser;
  }

  public AuthWebclientUser getAuthWebClientUser() {
    return authWebClientUser;
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return new HashSet<>();
  }

  @Override
  public String getPassword() {
    return authWebClientUser.getPassword();
  }

  @Override
  public String getUsername() {
    return authWebClientUser.getUserName();
  }

  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    return true;
  }
}
