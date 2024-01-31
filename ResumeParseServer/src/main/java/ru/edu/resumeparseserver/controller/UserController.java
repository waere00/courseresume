package ru.edu.resumeparseserver.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import ru.edu.resumeparseserver.model.UserDTO;
import ru.edu.resumeparseserver.model.Users;
import ru.edu.resumeparseserver.repository.UserRepository;
import org.springframework.http.HttpStatus;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserRepository userRepository;

    @Autowired
    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @PostMapping("/register")
    @Transactional
    public ResponseEntity<String> registerUser(@RequestBody UserDTO userDTO) {
        Users existingUser = userRepository.findByLogin(userDTO.getLogin());
        if (existingUser != null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User already exists");
        }
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String hashedPassword = encoder.encode(userDTO.getPassword());
        Users newUser = new Users(userDTO.getLogin(), hashedPassword, userDTO.isAdmin());
        userRepository.save(newUser);
        return ResponseEntity.ok("COMPLETE");
    }
    @PostMapping("/login")
    public ResponseEntity<String> loginUser(@RequestBody UserDTO userDTO) {
        Users user = userRepository.findByLogin(userDTO.getLogin());
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not found");
        }
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        if (encoder.matches(userDTO.getPassword(), user.getPassword())) {
            String role = user.isAdmin() ? "AUTHORIZED_ADMIN" : "AUTHORIZED";
            return ResponseEntity.ok(role);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("UNAUTHORIZED");
        }
    }
}