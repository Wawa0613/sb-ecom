package com.ecommerce.project.controller;

import com.ecommerce.project.model.AppRole;
import com.ecommerce.project.model.Role;
import com.ecommerce.project.model.User;
import com.ecommerce.project.repositories.RoleRepository;
import com.ecommerce.project.repositories.UserRepository;
import com.ecommerce.project.security.Request.LoginRequest;
import com.ecommerce.project.security.Request.SignupRequest;
import com.ecommerce.project.security.Response.MessageResponse;
import com.ecommerce.project.security.Response.UserInfoResponse;
import com.ecommerce.project.security.jwt.JwtUtils;
import com.ecommerce.project.security.service.UserDetailsImpl;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;
import java.util.stream.Collectors;
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private JwtUtils jwtUtils;//用于生成和验证 JWT 令牌。

    @Autowired
    private AuthenticationManager authenticationManager;//Spring Security 提供的认证管理器，用于验证用户凭据。
    @Autowired
    UserRepository userRepository;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    RoleRepository roleRepository;
/*
authenticateUser 方法：
通过 @PostMapping("/signin") 注解映射到 /signin 路径，处理用户登录请求。
接收 LoginRequest 对象作为请求体，包含用户名和密码。
使用 AuthenticationManager 验证用户凭据。如果验证失败，返回状态为 404 的响应，提示 "Bad credentials"。
如果验证成功：
将认证信息存储到 SecurityContextHolder 中。
获取用户的详细信息（UserDetailsImpl）。
使用 JwtUtils 生成 JWT 令牌。
获取用户的角色列表。
构造 UserInfoResponse 对象，包含用户 ID、用户名、角色和 JWT 令牌。
返回状态为 200 的响应，携带用户信息。
 */
    @PostMapping("/signin")//该类负责处理用户登录请求，并返回认证结果（如 JWT 令牌和用户信息）。
    public ResponseEntity<?> authenticateUser(@RequestBody LoginRequest loginRequest) {//loginRequest封装用户登录请求的类，包含用户名和密码。
        Authentication authentication;
        try {
            authentication = authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
        } catch (AuthenticationException exception) {
            Map<String, Object> map = new HashMap<>();
            map.put("message", "Bad credentials");
            map.put("status", false);
            return new ResponseEntity<Object>(map, HttpStatus.NOT_FOUND);
        }


        //SecurityContextHolder：
        //是 Spring Security 提供的一个工具类，用于存储和获取当前线程的安全上下文（SecurityContext）。
        //SecurityContext 包含了与当前用户相关的安全信息，例如认证信息和权限。
        SecurityContextHolder.getContext().setAuthentication(authentication);//通过将认证信息存储到 SecurityContext 中，Spring Security
        // 可以在后续的请求中通过上下文获取当前用户的身份和权限信息，从而实现权限控制和安全检查。用户登录成功后，认证信息会被存储到 SecurityContext 中，
        // 后续的 API 请求可以通过上下文获取用户信息，而无需重新登录。

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();//这句代码的作用是从 Authentication
        // 对象中获取当前认证用户的详细信息，并将其转换为自定义的 UserDetailsImpl 类型。

        ResponseCookie jwtCookie = jwtUtils.generateJwtCookie(userDetails);

        List<String> roles = userDetails.getAuthorities().stream()
                .map(item -> item.getAuthority())
                .collect(Collectors.toList());

        UserInfoResponse response = new UserInfoResponse(userDetails.getId(),//封装认证成功后返回的用户信息。
                userDetails.getUsername(), roles);

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, jwtCookie.toString())
                .body(response);
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
        if (userRepository.existsByUserName(signUpRequest.getUsername())) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Username is already taken!"));
        }

        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Email is already in use!"));
        }

        // Create new user's account
        User user = new User(signUpRequest.getUsername(),
                signUpRequest.getEmail(),
                passwordEncoder.encode(signUpRequest.getPassword()));

        Set<String> strRoles = signUpRequest.getRole();
        Set<Role> roles = new HashSet<>();

        if (strRoles == null) {
            Role userRole = roleRepository.findByRoleName(AppRole.ROLE_USER)
                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
            roles.add(userRole);
        } else {
            strRoles.forEach(role -> {
                switch (role) {
                    case "admin":
                        Role adminRole = roleRepository.findByRoleName(AppRole.ROLE_ADMIN)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(adminRole);

                        break;
                    case "seller":
                        Role modRole = roleRepository.findByRoleName(AppRole.ROLE_SELLER)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(modRole);

                        break;
                    default:
                        Role userRole = roleRepository.findByRoleName(AppRole.ROLE_USER)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(userRole);
                }
            });
        }

        user.setRoles(roles);
        userRepository.save(user);

        return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
    }
}
