package com.example.projectthree.Controller;

import com.example.projectthree.API.ApiResponse;
import com.example.projectthree.Model.Account;
import com.example.projectthree.Model.User;
import com.example.projectthree.Service.AccountService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/account")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

    @GetMapping("/get-all")
    public ResponseEntity<?> getAll(){
        return ResponseEntity.status(200).body(accountService.getAll());
    }

    @GetMapping("/get/{accountId}")
    public ResponseEntity<?> getById(@AuthenticationPrincipal User user, @PathVariable Integer accountId){
        return ResponseEntity.status(200).body(accountService.getById(user.getId(), accountId));
    }

    @PostMapping("/add")
    public ResponseEntity<?> addAccount(@AuthenticationPrincipal User user, @RequestBody @Valid Account account){
        accountService.addAccount(user.getId(), account);
        return ResponseEntity.status(200).body(new ApiResponse("account created"));
    }

    @PutMapping("/update/{accountId}")
    public ResponseEntity<?> updateAccount(@AuthenticationPrincipal User user, @PathVariable Integer accountId, @RequestBody @Valid Account account){
        accountService.updateAccount(user.getId(), accountId, account);
        return ResponseEntity.status(200).body(new ApiResponse("account updated"));
    }

    @PutMapping("/activate/{accountId}")
    public ResponseEntity<?> activateAccount(@AuthenticationPrincipal User user, @PathVariable Integer accountId){
        accountService.activateAccount(user.getId(), accountId);
        return ResponseEntity.status(200).body(new ApiResponse("account activated"));
    }

    @PutMapping("/block/{accountId}")
    public ResponseEntity<?> blockAccount(@AuthenticationPrincipal User user, @PathVariable Integer accountId){
        accountService.blockAccount(user.getId(), accountId);
        return ResponseEntity.status(200).body(new ApiResponse("account blocked"));
    }

    @PutMapping("/deposit/{accountId}/{amount}")
    public ResponseEntity<?> deposit(@AuthenticationPrincipal User user, @PathVariable Integer accountId, @PathVariable Double amount){
        accountService.deposit(user.getId(), accountId, amount);
        return ResponseEntity.status(200).body(new ApiResponse("deposit successful"));
    }

    @PutMapping("/withdraw/{accountId}/{amount}")
    public ResponseEntity<?> withdraw(@AuthenticationPrincipal User user, @PathVariable Integer accountId, @PathVariable Double amount){
        accountService.withdraw(user.getId(), accountId, amount);
        return ResponseEntity.status(200).body(new ApiResponse("withdraw successful"));
    }

    @PutMapping("/transfer/{account1Id}/{account2Id}/{amount}")
    public ResponseEntity<?> transfer(@AuthenticationPrincipal User user, @PathVariable Integer account1Id, @PathVariable Integer account2Id, @PathVariable Double amount){
        accountService.transfer(user.getId(), account1Id, account2Id, amount);
        return ResponseEntity.status(200).body(new ApiResponse("transfer successful"));
    }

    @GetMapping("/customer/{customerId}")
    public ResponseEntity<?> getAccountsByUser(@AuthenticationPrincipal User user, @PathVariable Integer customerId){
        return ResponseEntity.status(200).body(accountService.getAccountsByUser(user.getId(), customerId));
    }

    @DeleteMapping("/delete/{accountId}")
    public ResponseEntity<?> deleteAccount(@AuthenticationPrincipal User user, @PathVariable Integer accountId){
        accountService.deleteAccount(user.getId(), accountId);
        return ResponseEntity.status(200).body(new ApiResponse("account deleted"));
    }
}
