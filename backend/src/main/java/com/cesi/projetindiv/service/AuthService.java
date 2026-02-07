package com.cesi.projetindiv.service;

import com.cesi.projetindiv.api.dto.*;
import com.cesi.projetindiv.domain.AppUser;
import com.cesi.projetindiv.repository.AppUserRepository;
import com.cesi.projetindiv.security.JwtService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {

  private final AppUserRepository userRepo;
  private final PasswordEncoder encoder;
  private final AuthenticationManager authManager;
  private final JwtService jwtService;

  public AuthService(AppUserRepository userRepo, PasswordEncoder encoder, AuthenticationManager authManager, JwtService jwtService) {
    this.userRepo = userRepo;
    this.encoder = encoder;
    this.authManager = authManager;
    this.jwtService = jwtService;
  }

  @Transactional
  public AuthResponse register(AuthRegisterRequest request) {
    String email = request.email().trim().toLowerCase();
    if (userRepo.existsByEmail(email)) {
      throw new IllegalArgumentException("email_already_used");
    }
    String hash = encoder.encode(request.password());
    // For demo: default role USER
    userRepo.save(new AppUser(email, hash, "USER"));
    String token = jwtService.generateToken(email);
    return new AuthResponse(token, "Bearer", jwtService.getExpirationSeconds());
  }

  public AuthResponse login(AuthLoginRequest request) {
    Authentication auth = authManager.authenticate(
      new UsernamePasswordAuthenticationToken(request.email().trim().toLowerCase(), request.password())
    );
    String token = jwtService.generateToken(auth.getName());
    return new AuthResponse(token, "Bearer", jwtService.getExpirationSeconds());
  }
}
