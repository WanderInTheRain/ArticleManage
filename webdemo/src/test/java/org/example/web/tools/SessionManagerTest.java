package org.example.web.tools;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.util.UUID;

class SessionManagerTest {
  private static final String SESSION_FILE = "D:\\desktop\\study\\3s\\software\\webdemo\\src\\main\\resources\\session.md";

  @BeforeEach
  void setUp() throws IOException {
    // Clear the session file before each test
    PrintWriter writer = new PrintWriter(SESSION_FILE);
    writer.close();
  }

  @AfterEach
  void tearDown() throws IOException {
    // Clear the session file after each test
    PrintWriter writer = new PrintWriter(SESSION_FILE);
    writer.close();
  }

  @Test
  void createSession() {
    String sessionData = "Test session data";
    String sessionId = SessionManager.createSession(sessionData);

    assertNotNull(sessionId);
    assertTrue(sessionId.length() <= 20); // Check session ID length condition

    // Check if session was written to the file
    try (BufferedReader br = new BufferedReader(new FileReader(SESSION_FILE))) {
      String line;
      boolean found = false;
      while ((line = br.readLine()) != null) {
        String[] parts = line.split(",");
        if (parts.length == 2 && parts[0].equals(sessionId) && parts[1].equals(sessionData)) {
          found = true;
          break;
        }
      }
      assertTrue(found, "Session not found in session file");
    } catch (IOException e) {
      fail("Exception thrown while reading session file");
    }
  }

  @Test
  void getSession() {
    String sessionData = "Test session data";
    String sessionId = SessionManager.createSession(sessionData);

    String retrievedSessionData = SessionManager.getSession(sessionId);
    assertEquals(sessionData, retrievedSessionData);
  }

  @Test
  void deleteSession() {
    String sessionData = "Test session data";
    String sessionId = SessionManager.createSession(sessionData);

    SessionManager.deleteSession(sessionId);

    // Check if session was deleted from the file
    try (BufferedReader br = new BufferedReader(new FileReader(SESSION_FILE))) {
      String line;
      boolean found = false;
      while ((line = br.readLine()) != null) {
        String[] parts = line.split(",");
        if (parts.length == 2 && parts[0].equals(sessionId) && parts[1].equals(sessionData)) {
          found = true;
          break;
        }
      }
      assertFalse(found, "Deleted session found in session file");
    } catch (IOException e) {
      fail("Exception thrown while reading session file");
    }
  }
}
