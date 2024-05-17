package com.example.tvmanagerment.Controller;
import com.example.tvmanagerment.Model.Role;
import com.example.tvmanagerment.Model.Users;
import com.example.tvmanagerment.Repository.IRoleRepository;
import com.example.tvmanagerment.Repository.IUserRepository;
import com.example.tvmanagerment.security.service.JwtResponse;
import com.example.tvmanagerment.security.service.JwtService;
import com.example.tvmanagerment.security.service.UserService;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private UserService userService;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    IUserRepository userRepository;
    IRoleRepository roleRepository;

    private EntityManager entityManager;


    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Users user) {
        Authentication authentication
                = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtService.generateTokenLogin(authentication);
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        Users currentUser = userService.findByUsername(user.getUsername());
        return ResponseEntity.ok(new JwtResponse(currentUser.getId(), jwt, userDetails.getUsername(), userDetails.getUsername(), userDetails.getAuthorities()));
    }



    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody Users user) {

        if(userService.findByUsername(user.getUsername()) != null) {
            return ResponseEntity.badRequest().body("Username already exists");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userService.addUserWithDefaultRole(user);

        return ResponseEntity.ok("Đăng kí tài khoản thành công");
    }

}