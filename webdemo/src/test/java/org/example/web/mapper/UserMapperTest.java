package org.example.web.mapper;

import org.example.web.entity.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.*;

@JdbcTest
@ContextConfiguration(classes = {UserMapper.class})
@ComponentScan("org.example.web.mapper")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestPropertySource(locations = "classpath:application.yml")
class UserMapperTest {

  @Autowired
  private UserMapper userMapper;

  @Test
  void findByUsernameAndPassword() {
    User user = userMapper.findByUsernameAndPassword("testuser", "testpass");
    assertNull(user);
    assertNotEquals("testuser", "");
    assertNotEquals("testpass", "");
    // Add more assertions based on your User entity and database content
  }

  @Test
  void findByUsername() {
    User user = userMapper.findByUsername("testuser");
    assertNull(user);
    assertNotEquals("testuser", "");
    // Add more assertions based on your User entity and database content
  }

  @Test
  void getFirstNonExistingId() {
    long id = userMapper.getFirstNonExistingId();
    assertTrue(id > 0);
    // Add more assertions based on your database content and logic
  }

  @Test
  void insertUser() {
    User user = new User();
    user.setId(100L); // Assuming a new ID
    user.setUsername("newuser");
    user.setPassword("newpass");
    user.setAuthority(1); // Assuming authority level
    userMapper.insertUser(user);

    User retrievedUser = userMapper.findByUserid(100L);
    assertNotNull(retrievedUser);
    assertEquals("newuser", retrievedUser.getUsername());
    assertEquals("newpass", retrievedUser.getPassword());
    assertEquals(1, retrievedUser.getAuthority());
    // Add more assertions based on your database content and logic
  }

  @Test
  void findByUserid() {
    User user = userMapper.findByUserid(1L); // Assuming user with ID 1 exists
    assertNotNull(user);
    assertEquals(1L, user.getId());
    // Add more assertions based on your User entity and database content
  }
}
