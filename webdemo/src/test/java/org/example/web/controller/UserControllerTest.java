package org.example.web.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.example.web.entity.User;
import org.example.web.service.UserService;
import org.example.web.tools.SessionManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.MockitoAnnotations;
import org.springframework.ui.Model;

class UserControllerTest {

  @Mock
  private UserService userService;

  @InjectMocks
  private UserController userController;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  void saveuser() {
    User mockUser = new User(1L, "testUser", "oldPass", 1);
    when(userService.findUserById(1L)).thenReturn(mockUser);

    String result = userController.saveuser("testUser", "1", "oldPass", "newPass", "readonly");
    assertEquals("success", result);
  }

  @Test
  void logins() {
    String result = userController.logins("yang", "123456");
    assertEquals("-1", result);
  }

  @Test
  void loginout() {
    String sessionId = "sessionId";
    userController.loginout(sessionId);
  }

  @Test
  void test1() {
    String sessionId = "sessionId";

    String result = userController.test(sessionId);
    assertEquals(null, result);
  }

  @Test
  void registers() {
    when(userService.findByUsername("newUser")).thenReturn(null);
    when(userService.getFirstNonExistingId()).thenReturn(1L);

    String result = userController.registers("newUser", "newPass");
    assertEquals("success", result);
  }

  @Test
  void getusername() {
    User mockUser = new User(1L, "testUser", "testPass", 1);
    when(userService.findUserById(1L)).thenReturn(mockUser);

    String result = userController.getusername("1");
    assertEquals("testUser", result);
  }

  @Test
  void getuserauth() {
    User mockUser = new User(1L, "testUser", "testPass", 1);
    when(userService.findUserById(1L)).thenReturn(mockUser);

    Integer result = userController.getuserauth("1");
    assertEquals(1, result);
  }
}
