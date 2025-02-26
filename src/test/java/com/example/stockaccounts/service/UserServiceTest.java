package com.example.stockaccounts.service;

import com.example.stockaccounts.dto.CreateUserDTO;
import com.example.stockaccounts.dto.UpdateUserDTO;
import com.example.stockaccounts.entity.User;
import com.example.stockaccounts.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @Captor
    private ArgumentCaptor<User> userArgumentCaptor;

    @Captor
    private ArgumentCaptor<UUID> uuidArgumentCaptor;

    @Nested
    class CreateUser{
        @Test
        @DisplayName("create a user successfully")
        void shouldCreateUser(){
            //Arrange
            UUID mockUserId = UUID.randomUUID();
            var user = new User(
                    "username",
                    "email@mail.com",
                    "abcxyz",
                    Instant.now(),
                    null);
            user.setUserId(mockUserId);

            doReturn(user).when(userRepository).save(userArgumentCaptor.capture());

            var input = new CreateUserDTO(
                    "username",
                    "email@mail.com",
                    "abcxyz");
            //Act
            var output = userService.createUser(input);
            //Assert
            assertNotNull(output);
            var capturedUser = userArgumentCaptor.getValue();
            assertEquals(input.username(), capturedUser.getUsername());
            assertEquals(input.email(), capturedUser.getEmail());
            assertEquals(input.password(), capturedUser.getPassword());
        }

        @Test
        void shouldThrowExceptionWhenUserAlreadyExists(){}

        @Test
        @DisplayName("throw an exception when an error occurs")
        void shouldThowExceptionWhenAnErrorOccurred(){
            doThrow(new RuntimeException()).when(userRepository).save(any());

            var input = new CreateUserDTO(
                    "username",
                    "email@mail.com",
                    "abcxyz");
            //Act + Assert
            assertThrows(RuntimeException.class, () -> userService.createUser(input));
        }
    }
    
    @Nested
    class getUserById{
        @Test
        @DisplayName("Get user by the Id successfully When Optional is present")
        void shouldGetUserByIdSuccessfullyWhenOptionalIsPresent() {
            //Arrange
            UUID mockUserId = UUID.randomUUID();
            var user = new User(
                    "username",
                    "email@mail.com",
                    "abcxyz",
                    Instant.now(),
                    null);
            user.setUserId(mockUserId);

            doReturn(Optional.of(user)).when(userRepository).findById(uuidArgumentCaptor.capture());
            //Act
            var output = userService.getUserById(user.getUserId().toString());
            //Assert
            assertTrue(output.isPresent());
            assertEquals(user.getUserId(), uuidArgumentCaptor.getValue());
        }

        @Test
        @DisplayName("Shouldn't get user by the Id successfully When Optional is empty")
        void shouldntGetUserByIdSuccessfullyWhenOptionalIsEmpty() {
            //Arrange
            UUID userId = UUID.randomUUID();

            doReturn(Optional.empty()).when(userRepository).findById(uuidArgumentCaptor.capture());
            //Act
            var output = userService.getUserById(userId.toString());
            //Assert
            assertTrue(output.isEmpty());
            assertEquals(userId, uuidArgumentCaptor.getValue());
        }
    }

    @Nested
    class listUsers{
        @Test
        @DisplayName("should return all user successfulluy")
        void shouldReturnAllUserSuccessfulluy() {
            //Arrange
            UUID mockUserId = UUID.randomUUID();
            var user = new User(
                    "username",
                    "email@mail.com",
                    "abcxyz",
                    Instant.now(),
                    null);
            user.setUserId(mockUserId);

            var userList = List.of(user);
            doReturn(userList).when(userRepository).findAll();
            //Act
            var output = userService.listUsers();
            //Assert
            assertNotNull(output);
            assertEquals(userList.size(), output.size());
        }
    }

    @Nested
    class deleteUserById{
        @Test
        @DisplayName("should Delete User By Id successfully when user exists")
        void shouldDeleteUserByIdSuccessfully() {
            //Arrange
            doReturn(true).when(userRepository).existsById(uuidArgumentCaptor.capture());
            doNothing().when(userRepository).deleteById(uuidArgumentCaptor.capture());
            var userId = UUID.randomUUID();
            //Act
            userService.deleteUserById(userId.toString());
            //Assert
            var idList = uuidArgumentCaptor.getAllValues();
            assertEquals(userId, idList.get(0));
            assertEquals(userId, idList.get(1));

            verify(userRepository, times(1)).existsById(idList.get(0));
            verify(userRepository, times(1)).deleteById(idList.get(1));
        }

        @Test
        @DisplayName("shouldn't Delete User By Id when user doesn't exists")
        void shouldNotDeleteUserByIdWhenUserDoesNotExist() {
            //Arrange
            doReturn(false).when(userRepository).existsById(uuidArgumentCaptor.capture());
            var userId = UUID.randomUUID();
            //Act
            userService.deleteUserById(userId.toString());
            //Assert
            assertEquals(userId, uuidArgumentCaptor.getValue());

            verify(userRepository, times(1)).existsById(uuidArgumentCaptor.getValue());
            verify(userRepository, times(0)).deleteById(any());
        }
    }

    @Nested
    class updateUserById{
        @Test
        @DisplayName("Should update user by the Id successfully When user exists with the fields username and password are filled")
        void shouldUpdateUserByIdSuccessfullyWhenUserExistsWithTheFieldsUsernameAndPassword() {
            //Arrange
            UUID mockUserId = UUID.randomUUID();
            var updatedUserDto = new UpdateUserDTO("newUsername", "newSecret");
            var user = new User(
                    "username",
                    "email@mail.com",
                    "abcxyz",
                    Instant.now(),
                    null);
            user.setUserId(mockUserId);

            doReturn(Optional.of(user)).when(userRepository).findById(uuidArgumentCaptor.capture());
            doReturn(user).when(userRepository).save(userArgumentCaptor.capture());
            //Act
            userService.updateUser(user.getUserId().toString(), updatedUserDto);
            //Assert
            assertEquals(user.getUserId(), uuidArgumentCaptor.getValue());

            assertEquals(updatedUserDto.username(), userArgumentCaptor.getValue().getUsername());
            assertEquals(updatedUserDto.password(), userArgumentCaptor.getValue().getPassword());

            verify(userRepository, times(1)).findById((uuidArgumentCaptor.getValue()));
            verify(userRepository, times(1)).save(user);
        }

        @Test
        @DisplayName("Should not update user by the Id successfully When user doesn't exists")
        void shouldNotUpdateUserByIdSuccessfullyWhenUserDoesNotExists() {
            //Arrange
            UUID userId = UUID.randomUUID();
            var updatedUserDto = new UpdateUserDTO("newUsername", "newSecret");

            doReturn(Optional.empty()).when(userRepository).findById(uuidArgumentCaptor.capture());
            //Act
            userService.updateUser(userId.toString(), updatedUserDto);
            //Assert
            assertEquals(userId, uuidArgumentCaptor.getValue());

            verify(userRepository, times(1)).findById((uuidArgumentCaptor.getValue()));
            verify(userRepository, times(0)).save(any());
        }
    }

}