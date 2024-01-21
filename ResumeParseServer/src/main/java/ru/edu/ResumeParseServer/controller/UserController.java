package ru.edu.ResumeParseServer.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import ru.edu.ResumeParseServer.model.UserDTO;
import ru.edu.ResumeParseServer.model.Users;
import ru.edu.ResumeParseServer.repository.UserRepository;
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

        String hashedPassword = BCrypt.hashpw(userDTO.getPassword(), BCrypt.gensalt());
        Users newUser = new Users(userDTO.getLogin(), hashedPassword, userDTO.isAdmin());
        userRepository.save(newUser);
        return ResponseEntity.ok("COMPLETE");
    }

    @PostMapping("/login")
    public ResponseEntity<String> loginUser(@RequestBody UserDTO userDTO) {
        Users user = userRepository.findByLogin(userDTO.getLogin());
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not found");
        } else if (BCrypt.checkpw(userDTO.getPassword(), user.getPassword())) {
            if (user.isAdmin()) {
                return ResponseEntity.ok("AUTHORIZED_ADMIN");
            }
            return ResponseEntity.ok("AUTHORIZED");
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("UNAUTHORIZED");
    }
}
