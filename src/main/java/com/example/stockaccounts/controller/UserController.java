package com.example.stockaccounts.controller;

import com.example.stockaccounts.dto.AccountResponseDTO;
import com.example.stockaccounts.dto.CreateAccountDTO;
import com.example.stockaccounts.dto.CreateUserDTO;
import com.example.stockaccounts.dto.UpdateUserDTO;
import com.example.stockaccounts.entity.User;
import com.example.stockaccounts.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/v1/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody CreateUserDTO createUserDTO) {
        var userId = userService.createUser(createUserDTO);
        return ResponseEntity.created(URI.create("/v1/users/" + userId.toString())).build();
    }

    @GetMapping("/{userId}")
    public ResponseEntity<User> getUserById(@PathVariable("userId") String userId) {
        return userService.getUserById(userId)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new NoSuchElementException("Usuário com Id: " + userId + " não encontrado"));
    }

    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        var users = userService.listUsers();
        return ResponseEntity.ok(users);
    }

    @PutMapping("/{userId}")
    public ResponseEntity<Void> updateUser(@PathVariable("userId") String userId, @RequestBody UpdateUserDTO updateUserDTO) {
        userService.updateUser(userId, updateUserDTO);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> deleteUser(@PathVariable("userId") String userId) {
        userService.deleteUserById(userId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{userId}/accounts")
    public ResponseEntity<Void> createAccountById(@PathVariable("userId") String userId,
                                              @RequestBody CreateAccountDTO accountDTO) {
        userService.createAccount(userId, accountDTO);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{userId}/accounts")
    public ResponseEntity<List<AccountResponseDTO>> getAccountById(@PathVariable("userId") String userId) {
        var accounts =  userService.listAccounts(userId);
        return ResponseEntity.ok(accounts);
    }
}
