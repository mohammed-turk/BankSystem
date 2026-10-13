package com.example.projectthree.Service;

import com.example.projectthree.API.ApiException;
import com.example.projectthree.DTO.EmployeeDTO;
import com.example.projectthree.Model.Employee;
import com.example.projectthree.Model.User;
import com.example.projectthree.Repository.AuthRepository;
import com.example.projectthree.Repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final AuthRepository authRepository;
    private final PasswordEncoder passwordEncoder;

    public List<Employee> getAll(){
        return employeeRepository.findAll();
    }

    public Employee getById(Integer id){
        Employee employee = employeeRepository.findEmployeeById(id);
        if (employee == null)
            throw new ApiException("employee not found");
        return employee;
    }

    @Transactional
    public void register(EmployeeDTO employeeDTO){
        if (authRepository.findUserByUsername(employeeDTO.getUsername()) != null)
            throw new ApiException("username already taken");

        User user = new User(null, employeeDTO.getUsername(),
                passwordEncoder.encode(employeeDTO.getPassword()),
                employeeDTO.getName(), employeeDTO.getEmail(), "EMPLOYEE", null, null);

        Employee employee = new Employee(null, employeeDTO.getPosition(), employeeDTO.getSalary(), user);
        user.setEmployee(employee);
        authRepository.save(user);
    }

    @Transactional
    public void updateEmployee(Integer adminId, Integer employeeId, EmployeeDTO employeeDTO){
        User admin = authRepository.findUserById(adminId);
        if (admin == null)
            throw new ApiException("admin not found");

        Employee employee = employeeRepository.findEmployeeById(employeeId);
        if (employee == null)
            throw new ApiException("employee not found");

        User user = employee.getUser();
        user.setName(employeeDTO.getName());
        user.setEmail(employeeDTO.getEmail());
        user.setPassword(passwordEncoder.encode(employeeDTO.getPassword()));
        employee.setPosition(employeeDTO.getPosition());
        employee.setSalary(employeeDTO.getSalary());

        authRepository.save(user);
    }

    @Transactional
    public void deleteEmployee(Integer adminId, Integer employeeId){
        User admin = authRepository.findUserById(adminId);
        if (admin == null)
            throw new ApiException("admin not found");

        Employee employee = employeeRepository.findEmployeeById(employeeId);
        if (employee == null)
            throw new ApiException("employee not found");

        authRepository.delete(employee.getUser());
    }
}