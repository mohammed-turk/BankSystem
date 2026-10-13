package com.example.projectthree.Controller;

import com.example.projectthree.API.ApiResponse;
import com.example.projectthree.DTO.EmployeeDTO;
import com.example.projectthree.Model.User;
import com.example.projectthree.Service.EmployeeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/employee")
@RequiredArgsConstructor
public class EmployeeController {
    private final EmployeeService employeeService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody @Valid EmployeeDTO employeeDTO){
        employeeService.register(employeeDTO);
        return ResponseEntity.status(200).body(new ApiResponse("employee registered"));
    }

    @GetMapping("/get-all")
    public ResponseEntity<?> getAll(){
        return ResponseEntity.status(200).body(employeeService.getAll());
    }

    @GetMapping("/get/{employeeId}")
    public ResponseEntity<?> getById(@PathVariable Integer employeeId){
        return ResponseEntity.status(200).body(employeeService.getById(employeeId));
    }

    @PutMapping("/update/{employeeId}")
    public ResponseEntity<?> updateEmployee(@AuthenticationPrincipal User admin, @PathVariable Integer employeeId, @RequestBody @Valid EmployeeDTO employeeDTO){
        employeeService.updateEmployee(admin.getId(), employeeId, employeeDTO);
        return ResponseEntity.status(200).body(new ApiResponse("employee updated"));
    }

    @DeleteMapping("/delete/{employeeId}")
    public ResponseEntity<?> deleteEmployee(@AuthenticationPrincipal User admin, @PathVariable Integer employeeId){
        employeeService.deleteEmployee(admin.getId(), employeeId);
        return ResponseEntity.status(200).body(new ApiResponse("employee deleted"));
    }
}
