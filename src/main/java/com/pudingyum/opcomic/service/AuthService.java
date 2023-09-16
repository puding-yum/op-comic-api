package com.pudingyum.opcomic.service;

import com.pudingyum.opcomic.config.JwtUtils;
import com.pudingyum.opcomic.constant.ERole;
import com.pudingyum.opcomic.domain.dao.Role;
import com.pudingyum.opcomic.domain.dao.User;
import com.pudingyum.opcomic.domain.dto.JwtToken;
import com.pudingyum.opcomic.domain.dto.UserDto;
import com.pudingyum.opcomic.domain.dto.UserLoginDto;
import com.pudingyum.opcomic.repository.RoleRepository;
import com.pudingyum.opcomic.repository.UserRepository;
import com.pudingyum.opcomic.utils.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
@Slf4j
public class AuthService {
    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    JwtUtils jwtUtils;

    @Autowired
    AuthenticationManager authenticationManager;

    public ResponseEntity<Object> register(UserLoginDto userLoginDto){
        if (Boolean.TRUE.equals(userRepository.existsByUsername(userLoginDto.getUsername()))) {
            return Response.build("username exist", null, null, HttpStatusCode.valueOf(400));
        }

        User user = User.builder().username(userLoginDto.getUsername()).password(encoder.encode(userLoginDto.getPassword())).build();

        Set<Role> roles = new HashSet<>();

        Optional<Role> userRole = roleRepository.findByName(ERole.USER);
        if(userRole.isEmpty()){
            log.info("Role user not found");
            return Response.build("Internal server error", null, null, HttpStatusCode.valueOf(500));
        }
        roles.add(userRole.get());

        user.setRoles(roles);
        userRepository.save(user);

        UserDto userDto = UserDto.builder()
                .id(user.getId())
                .username(user.getUsername())
                .build();

        return Response.build("Register success", userDto, null, HttpStatusCode.valueOf(201));
    }

    public ResponseEntity<Object> login(UserLoginDto userLoginDto){
        Optional<User> user = userRepository.findByUsername(userLoginDto.getUsername());
        if(user.isEmpty()){
            return Response.build("username or password incorrect", null, null, HttpStatusCode.valueOf(400));
        }

        Boolean isPasswordCorrect = encoder.matches(userLoginDto.getPassword(), user.get().getPassword());
        if (Boolean.FALSE.equals(isPasswordCorrect)) {
            return Response.build("username or password incorrect", null, null, HttpStatus.BAD_REQUEST);
        }

        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userLoginDto.getUsername(), userLoginDto.getPassword()));
        String jwt = "";
        if (authentication.isAuthenticated()) {
            jwt = jwtUtils.generateToken(userLoginDto.getUsername());
        }else{
            return Response.build("user not found", null, null, HttpStatusCode.valueOf(400));
        }

        JwtToken token = JwtToken.builder().token(jwt).build();

        return Response.build("Login success", token, null, HttpStatusCode.valueOf(200));
    }
}