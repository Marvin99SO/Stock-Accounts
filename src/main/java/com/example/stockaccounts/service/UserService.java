package com.example.stockaccounts.service;

import com.example.stockaccounts.dto.AccountResponseDTO;
import com.example.stockaccounts.dto.CreateAccountDTO;
import com.example.stockaccounts.dto.CreateUserDTO;
import com.example.stockaccounts.dto.UpdateUserDTO;
import com.example.stockaccounts.entity.Account;
import com.example.stockaccounts.entity.BillingAddress;
import com.example.stockaccounts.entity.User;
import com.example.stockaccounts.repository.AccountRepository;
import com.example.stockaccounts.repository.BillingAddressRepository;
import com.example.stockaccounts.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;
import java.util.List;

@Service
public class UserService {
    private UserRepository userRepository;
    private AccountRepository accountRepository;
    private BillingAddressRepository billingAddressRepository;

    public UserService(
            UserRepository userRepository,
            AccountRepository accountRepository,
            BillingAddressRepository billingAddressRepository) {
        this.userRepository = userRepository;
        this.accountRepository = accountRepository;
        this.billingAddressRepository = billingAddressRepository;
    }

    public UUID createUser(CreateUserDTO createUserDTO) {
        var entity = new User(
                createUserDTO.username(),
                createUserDTO.email(),
                createUserDTO.password(),
                Instant.now(),
                null);
        var userSaved = userRepository.save(entity);
        return userSaved.getUserId();
    }

    public Optional<User> getUserById(String userId) {
        return userRepository.findById(UUID.fromString(userId));
    }

    public List<User> listUsers() {
        return userRepository.findAll();
    }

    public void updateUser(String userId, UpdateUserDTO updateUserDTO) {
        var id = UUID.fromString(userId);
        var userEntity = userRepository.findById(id);
        if (userEntity.isPresent()) {
            var user = userEntity.get();
            if(updateUserDTO.username() != null){
                user.setUsername(updateUserDTO.username());
            }
            if(updateUserDTO.password() != null){
                user.setPassword(updateUserDTO.password());
            }
            userRepository.save(user);
        }
    }

    public void deleteUserById(String userId) {
        var id = UUID.fromString(userId);
        var userExists = userRepository.existsById(id);
        if (userExists) {
            userRepository.deleteById(id);
        }
    }

    public void createAccount(String userId, CreateAccountDTO accountDTO) {
        var user = userRepository.findById(UUID.fromString(userId)).
                orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário não existe!"));

        var account = new Account(
                user,
                null,
                accountDTO.description(),
                new ArrayList<>()
        );

        var accountCreated = accountRepository.save(account);

        var billingAddress = new BillingAddress(
                //accountCreated.getAccountId(),
                accountCreated,
                accountDTO.street(),
                accountDTO.number()
        );

        billingAddressRepository.save(billingAddress);
    }

    public List<AccountResponseDTO> listAccounts(String userId) {
        var user = userRepository.findById(UUID.fromString(userId)).
                orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário não existe!"));

        return user.getAccountts()
                .stream()
                .map(account ->
                        new AccountResponseDTO(account.getAccountId().toString(),account.getDescription()))
                .toList();
    }
}
