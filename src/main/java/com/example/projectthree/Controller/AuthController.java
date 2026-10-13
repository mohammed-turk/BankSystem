package com.example.projectthree.Controller;

import com.example.projectthree.Model.User;
import com.example.projectthree.Service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    @GetMapping("/get-all")
    public ResponseEntity<?> getAllUsers(@AuthenticationPrincipal User admin){
        return ResponseEntity.status(200).body(authService.getAllUsers(admin.getId()));
    }

    @GetMapping("/get/{userId}")
    public ResponseEntity<?> getUserById(@AuthenticationPrincipal User admin, @PathVariable Integer userId){
        return ResponseEntity.status(200).body(authService.getUserById(admin.getId(), userId));
    }
}
