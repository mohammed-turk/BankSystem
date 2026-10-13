package com.example.projectthree.Service;

import com.example.projectthree.API.ApiException;
import com.example.projectthree.Model.Account;
import com.example.projectthree.Model.Customer;
import com.example.projectthree.Model.User;
import com.example.projectthree.Repository.AccountRepository;
import com.example.projectthree.Repository.AuthRepository;
import com.example.projectthree.Repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;
    private final CustomerRepository customerRepository;
    private final AuthRepository authRepository;

    public List<Account> getAll(){
        return accountRepository.findAll();
    }

    public Account getById(Integer userId, Integer accountId){
        User user = authRepository.findUserById(userId);
        Account account = accountRepository.findAccountById(accountId);
        if (user == null)
            throw new ApiException("user not found");
        if (account == null)
            throw new ApiException("account not found");
        if (user.getRole().equals("CUSTOMER") && !account.getCustomer().getId().equals(userId))
            throw new ApiException("cannot view this account");

        return account;
    }

    public void addAccount(Integer customerId, Account account){
        Customer customer = customerRepository.findCustomerById(customerId);
        if (customer == null)
            throw new ApiException("customer not found");
        if (accountRepository.findAccountByAccountNumber(account.getAccountNumber()) != null)
            throw new ApiException("account number already exists");

        account.setId(null);
        account.setIsActive(false);
        account.setCustomer(customer);
        accountRepository.save(account);
    }

    public void updateAccount(Integer userId, Integer accountId, Account account){
        User user = authRepository.findUserById(userId);
        Account oldAccount = accountRepository.findAccountById(accountId);
        if (user == null)
            throw new ApiException("user not found");
        if (oldAccount == null)
            throw new ApiException("account not found");
        if (!user.getRole().equals("ADMIN") && !oldAccount.getCustomer().getId().equals(userId))
            throw new ApiException("cannot update");

        Account existing = accountRepository.findAccountByAccountNumber(account.getAccountNumber());
        if (existing != null && !existing.getId().equals(accountId))
            throw new ApiException("account number already exists");

        oldAccount.setAccountNumber(account.getAccountNumber());
        accountRepository.save(oldAccount);
    }

    public void activateAccount(Integer userId, Integer accountId){
        User user = authRepository.findUserById(userId);
        if (user == null)
            throw new ApiException("user not found");
        Account account = accountRepository.findAccountById(accountId);
        if (account == null)
            throw new ApiException("account not found");

        if (account.getIsActive())
            throw new ApiException("account is already active");

        account.setIsActive(true);
        accountRepository.save(account);
    }

    public void blockAccount(Integer userId, Integer accountId){
        User user = authRepository.findUserById(userId);
        if (user == null)
            throw new ApiException("user not found");
        Account account = accountRepository.findAccountById(accountId);
        if (account == null)
            throw new ApiException("account not found");

        if (!account.getIsActive())
            throw new ApiException("account is already blocked");

        account.setIsActive(false);
        accountRepository.save(account);
    }

    @Transactional
    public void deposit(Integer customerId, Integer accountId, Double amount){
        Customer customer = customerRepository.findCustomerById(customerId);
        if (customer == null)
            throw new ApiException("customer not found");
        Account account = accountRepository.findAccountById(accountId);
        if (account == null)
            throw new ApiException("account not found");
        if (!account.getCustomer().getId().equals(customerId))
            throw new ApiException("cannot deposit into this account");
        if (!account.getIsActive())
            throw new ApiException("account is not active");
        if (amount == null || amount <= 0)
            throw new ApiException("amount must be greater than 0");

        account.setBalance(account.getBalance() + amount);
        accountRepository.save(account);
    }

    @Transactional
    public void withdraw(Integer customerId, Integer accountId, Double amount){
        Customer customer = customerRepository.findCustomerById(customerId);
        if (customer == null)
            throw new ApiException("customer not found");
        Account account = accountRepository.findAccountById(accountId);
        if (account == null)
            throw new ApiException("account not found");
        if (!account.getCustomer().getId().equals(customerId))
            throw new ApiException("cannot withdraw from this account");
        if (!account.getIsActive())
            throw new ApiException("account is not active");
        if (amount == null || amount <= 0)
            throw new ApiException("amount must be greater than 0");
        if (account.getBalance() < amount)
            throw new ApiException("cannot withdraw an amount greater than the balance");

        account.setBalance(account.getBalance() - amount);
        accountRepository.save(account);
    }

    @Transactional
    public void transfer(Integer userId, Integer account1Id, Integer account2Id, Double amount){
        User user = authRepository.findUserById(userId);
        if (user == null)
            throw new ApiException("user not found");
        Account account1 = accountRepository.findAccountById(account1Id);
        if (account1 == null)
            throw new ApiException("account 1 not found");
        Account account2 = accountRepository.findAccountById(account2Id);
        if (account2 == null)
            throw new ApiException("account 2 not found");

        if (!account1.getCustomer().getId().equals(userId))
            throw new ApiException("cannot transfer from this account");
        if (account1.getId().equals(account2.getId()))
            throw new ApiException("cannot transfer to the same account");
        if (!account1.getIsActive() || !account2.getIsActive())
            throw new ApiException("both accounts must be active");
        if (amount == null || amount <= 0)
            throw new ApiException("amount must be greater than 0");
        if (account1.getBalance() < amount)
            throw new ApiException("cannot transfer an amount greater than the balance");

        account1.setBalance(account1.getBalance() - amount);
        account2.setBalance(account2.getBalance() + amount);
        accountRepository.save(account1);
        accountRepository.save(account2);
    }

    public List<Account> getAccountsByUser(Integer userId, Integer customerId){
        User user = authRepository.findUserById(userId);
        if (user == null)
            throw new ApiException("user not found");
        Customer customer = customerRepository.findCustomerById(customerId);
        if (customer == null)
            throw new ApiException("customer not found");
        if (user.getRole().equals("CUSTOMER") && !userId.equals(customerId))
            throw new ApiException("cannot view another customer's accounts");

        return accountRepository.findAllByCustomer(customer);
    }



    public void deleteAccount(Integer userId, Integer accountId){
        User user = authRepository.findUserById(userId);
        Account account = accountRepository.findAccountById(accountId);
        if (user == null)
            throw new ApiException("user not found");
        if (account == null)
            throw new ApiException("account not found");
        if (!user.getRole().equals("ADMIN") && !account.getCustomer().getId().equals(userId))
            throw new ApiException("cannot delete");


        accountRepository.delete(account);
    }



}
