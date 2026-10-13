package com.example.projectthree.Controller;

import com.example.projectthree.API.ApiResponse;
import com.example.projectthree.DTO.CustomerDTO;
import com.example.projectthree.Model.User;
import com.example.projectthree.Service.CustomerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/customer")
@RequiredArgsConstructor
public class CustomerController {
    private final CustomerService customerService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody @Valid CustomerDTO customerDTO){
        customerService.register(customerDTO);
        return ResponseEntity.status(200).body(new ApiResponse("customer registered"));
    }

    @GetMapping("/get-all")
    public ResponseEntity<?> getAll(){
        return ResponseEntity.status(200).body(customerService.getAll());
    }

    @GetMapping("/get/{customerId}")
    public ResponseEntity<?> getById(@PathVariable Integer customerId){
        return ResponseEntity.status(200).body(customerService.getById(customerId));
    }

    @PutMapping("/update/{customerId}")
    public ResponseEntity<?> updateCustomer(@AuthenticationPrincipal User user, @PathVariable Integer customerId, @RequestBody @Valid CustomerDTO customerDTO){
        customerService.updateCustomer(user.getId(), customerId, customerDTO);
        return ResponseEntity.status(200).body(new ApiResponse("customer updated"));
    }

    @DeleteMapping("/delete/{customerId}")
    public ResponseEntity<?> deleteCustomer(@AuthenticationPrincipal User user, @PathVariable Integer customerId){
        customerService.deleteCustomer(user.getId(), customerId);
        return ResponseEntity.status(200).body(new ApiResponse("customer deleted"));
    }

}
