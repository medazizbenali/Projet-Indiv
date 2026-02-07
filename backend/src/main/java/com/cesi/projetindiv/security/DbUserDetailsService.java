package com.cesi.projetindiv.security;

import com.cesi.projetindiv.domain.AppUser;
import com.cesi.projetindiv.repository.AppUserRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class DbUserDetailsService implements UserDetailsService {

  private final AppUserRepository repo;

  public DbUserDetailsService(AppUserRepository repo) {
    this.repo = repo;
  }

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    AppUser u = repo.findByEmail(username)
      .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    return User.withUsername(u.getEmail())
      .password(u.getPasswordHash())
      .roles(u.getRole())
      .build();
  }
}
