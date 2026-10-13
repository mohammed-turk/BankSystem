package com.example.projectthree.Repository;

import com.example.projectthree.Model.Account;
import com.example.projectthree.Model.Customer;
import com.example.projectthree.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AccountRepository extends JpaRepository<Account,Integer> {
    Account findAccountById(Integer id);
    List<Account> findAllByCustomer(Customer customer);
    Account findAccountByAccountNumber(String accountNumber);
}
