package org.example.web.service;

import static org.junit.jupiter.api.Assertions.*;

import org.example.web.entity.User;
import org.example.web.mapper.UserMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.*;

class UserServiceTest {

  @Mock
  private UserMapper userMapper;

  private UserService userService;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.initMocks(this);
    userService = new UserService(userMapper);
  }

  @Test
  void insertUser() {
    User user = new User(); // create a User object as needed
    userService.insertUser(user);

    // Verify that userMapper.insertUser(user) was called once
    verify(userMapper, times(1)).insertUser(user);
  }

  @Test
  void findByUsername() {
    String username = "testuser";
    User user = new User(); // create a User object as needed
    when(userMapper.findByUsername(username)).thenReturn(user);

    User foundUser = userService.findByUsername(username);

    // Verify that userMapper.findByUsername(username) was called once
    verify(userMapper, times(1)).findByUsername(username);

    // Assert that the returned user is the same as the one from the mock
    assertEquals(user, foundUser);
  }

  @Test
  void findByUsernameAndPassword() {
    String username = "testuser";
    String password = "testpassword";
    User user = new User(); // create a User object as needed
    when(userMapper.findByUsernameAndPassword(username, password)).thenReturn(user);

    User foundUser = userService.findByUsernameAndPassword(username, password);

    // Verify that userMapper.findByUsernameAndPassword(username, password) was called once
    verify(userMapper, times(1)).findByUsernameAndPassword(username, password);

    // Assert that the returned user is the same as the one from the mock
    assertEquals(user, foundUser);
  }

  @Test
  void getFirstNonExistingId() {
    long expectedId = 1L; // Adjust as per your expected behavior
    when(userMapper.getFirstNonExistingId()).thenReturn(expectedId);

    long actualId = userService.getFirstNonExistingId();

    // Verify that userMapper.getFirstNonExistingId() was called once
    verify(userMapper, times(1)).getFirstNonExistingId();

    // Assert that the returned ID matches the expected ID
    assertEquals(expectedId, actualId);
  }

  @Test
  void findUserById() {
    long userId = 123L; // Adjust with a valid user ID
    User user = new User(); // create a User object as needed
    when(userMapper.findByUserid(userId)).thenReturn(user);

    User foundUser = userService.findUserById(userId);

    // Verify that userMapper.findByUserid(userId) was called once
    verify(userMapper, times(1)).findByUserid(userId);

    // Assert that the returned user is the same as the one from the mock
    assertEquals(user, foundUser);
  }
}
