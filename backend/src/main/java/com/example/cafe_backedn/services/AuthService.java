package com.example.cafe_backedn.services;

import com.example.cafe_backedn.advices.ApiResponse;
import com.example.cafe_backedn.dto.LoginDto;
import com.example.cafe_backedn.dto.UserDto;
import com.example.cafe_backedn.entity.UserEntity;
import com.example.cafe_backedn.exception.ResourceNotFound;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public LoginDto loginUser(LoginDto userInfo) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(userInfo.getEmail(),userInfo.getPassword())
        );
        UserEntity user =(UserEntity) authentication.getPrincipal();
        String token = jwtService.generateToken(user);
        return new LoginDto(user.getEmail(),user.getPassword(),token);
    }
}
