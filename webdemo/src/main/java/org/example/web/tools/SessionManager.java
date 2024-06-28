package org.example.web.tools;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class SessionManager {
  private static final String SESSION_FILE = "D:\\desktop\\study\\3s\\software\\webdemo\\src\\main\\resources\\session.md";

  // 新建 session，并返回生成的 session ID
  public static String createSession(String sessionData) {
    String sessionId = UUID.randomUUID().toString().substring(0, 20);

    try (BufferedReader br = new BufferedReader(new FileReader(SESSION_FILE))) {
      String line;
      while ((line = br.readLine()) != null) {
        String[] parts = line.split(",");
        if (parts.length == 2 && parts[1].equals(sessionData)) {
          // If session ID already exists, return existing session data
          return parts[0];
        }
      }
    } catch (IOException e) {
      e.printStackTrace();
    }

    // If session ID does not exist, append new session to the file
    try (FileWriter fw = new FileWriter(SESSION_FILE, true);
        BufferedWriter bw = new BufferedWriter(fw);
        PrintWriter out = new PrintWriter(bw)) {
      out.println(sessionId + "," + sessionData);
    } catch (IOException e) {
      e.printStackTrace();
    }

    return sessionId;
  }

  // 根据 sessionId 查找 session
  public static String getSession(String sessionId) {
    try (BufferedReader br = new BufferedReader(new FileReader(SESSION_FILE))) {
      String line;
      while ((line = br.readLine()) != null) {
        String[] parts = line.split(",");
        if (parts.length == 2 && parts[0].equals(sessionId)) {
          return parts[1];
        }
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
    return null;
  }

  // 删除 session
  public static void deleteSession(String sessionId) {
    // Define the file path
    String filePath = SESSION_FILE;

    // Create a list to hold lines of the file temporarily
    List<String> lines = new ArrayList<>();

    // Read the file and remove the line with the specified session ID
    try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
      String line;
      while ((line = reader.readLine()) != null) {
        // Check if the line contains the session ID
        if (!line.startsWith(sessionId + ",")) {
          lines.add(line);
        }
      }
    } catch (IOException e) {
      e.printStackTrace(); // Handle or log the exception as needed
    }

    // Write the updated lines back to the file
    try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
      for (String line : lines) {
        writer.write(line);
        writer.newLine();
      }
    } catch (IOException e) {
      e.printStackTrace(); // Handle or log the exception as needed
    }
  }

  // 示例用法
  public static void main(String[] args) {
    // 新建 session
    String session1Id = createSession("Session data 1");
    String session2Id = createSession("Session data 2");

    // 查找 session
    String session1Data = getSession(session1Id);
    System.out.println("Session 1 data: " + session1Data);

    // 删除 session
    deleteSession(session2Id);
    System.out.println("Session 2 deleted.");

    // 再次查找已删除的 session
    String session2Data = getSession(session2Id);
    if (session2Data == null) {
      System.out.println("Session 2 not found.");
    }
  }
}
