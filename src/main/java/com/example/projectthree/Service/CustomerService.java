package com.example.projectthree.Service;

import com.example.projectthree.API.ApiException;
import com.example.projectthree.DTO.CustomerDTO;
import com.example.projectthree.Model.Customer;
import com.example.projectthree.Model.User;
import com.example.projectthree.Repository.AuthRepository;
import com.example.projectthree.Repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomerService {

    private final CustomerRepository customerRepository;

    private final AuthRepository authRepository;
    private final PasswordEncoder passwordEncoder;

    public List<Customer> getAll(){
        return customerRepository.findAll();
    }

    public Customer getById(Integer id){
        Customer customer = customerRepository.findCustomerById(id);
        if (customer == null)
            throw new ApiException("customer not found");
        return customer;
    }

    @Transactional
    public void register(CustomerDTO customerDTO){
        if (authRepository.findUserByUsername(customerDTO.getUsername()) != null)
            throw new ApiException("username already taken");

        User user = new User(null, customerDTO.getUsername(),
                passwordEncoder.encode(customerDTO.getPassword()),
                customerDTO.getName(), customerDTO.getEmail(), "CUSTOMER", null, null);

        Customer customer = new Customer(null, customerDTO.getPhoneNumber(), user, null);
        user.setCustomer(customer);
        authRepository.save(user);

    }

    @Transactional
    public void updateCustomer(Integer userId, Integer customerId, CustomerDTO customerDTO){
        User loggedUser = authRepository.findUserById(userId);
        Customer customer = customerRepository.findCustomerById(customerId);
        if (loggedUser == null)
            throw new ApiException("user not found");
        if (customer == null)
            throw new ApiException("customer not found");
        if (!loggedUser.getRole().equals("ADMIN") && !customer.getUser().getId().equals(userId))
            throw new ApiException("cannot update");

        User user = customer.getUser();
        user.setName(customerDTO.getName());
        user.setEmail(customerDTO.getEmail());
        user.setPassword(passwordEncoder.encode(customerDTO.getPassword()));
        customer.setPhoneNumber(customerDTO.getPhoneNumber());

        authRepository.save(user);
    }

    @Transactional
    public void deleteCustomer(Integer userId, Integer customerId){
        User loggedUser = authRepository.findUserById(userId);
        Customer customer = customerRepository.findCustomerById(customerId);
        if (loggedUser == null)
            throw new ApiException("user not found");
        if (customer == null)
            throw new ApiException("customer not found");
        if (!loggedUser.getRole().equals("ADMIN") && !customer.getUser().getId().equals(userId))
            throw new ApiException("cannot delete");

        authRepository.delete(customer.getUser());
    }

}
