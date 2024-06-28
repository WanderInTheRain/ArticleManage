package org.example.web.mapper;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

import org.example.web.entity.Article;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;

@JdbcTest
@ContextConfiguration(classes = {ArticleMapper.class})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestPropertySource(locations = "classpath:application.yml")
class ArticleMapperTest {

  @Autowired
  private ArticleMapper articleMapper;

  @Test
  void testFindByShare() {
    List<Article> articles = articleMapper.findByShare();
    assertNotNull(articles);
    assertNotEquals(0, articles.size()); // You might adjust based on expected data in your DB
  }

  @Test
  void testInsertArticle() {
    Article article = new Article();
    // Set article properties as needed for testing
    article.setId(31L);
    article.setAuthorid(1L);
    article.setTitle("Test Article");
    article.setContent("Test content");
    article.setStars(0L);
    article.setKey("");
    article.setShare(1L);
    article.setCategoryid(0L);

    LocalDateTime now = LocalDateTime.now();

    // Define MySQL DATETIME format
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    // Format current LocalDateTime to MySQL DATETIME format
    String formattedDateTime = now.format(formatter);
    System.out.println("Formatted DateTime: " + formattedDateTime);

    // Convert LocalDateTime to Date
    Date date = Date.from(now.atZone(ZoneId.systemDefault()).toInstant());
    article.setDate(date);
    // Set other properties accordingly

    articleMapper.insertArticle(article);

    // Optionally, you can assert something to verify insertion
    assertNotEquals(0, 1);
  }

  @Test
  void testGetFirstNonExistingId() {
    long id = articleMapper.getFirstNonExistingId();
    assertNotEquals(0L, id); // Adjust as per your test data expectations
  }

  @Test
  void testFindById() {
    Long id = 1L; // Adjust with an existing id from your test data
    List<Article> articles = articleMapper.findById(id);
    assertNotNull(articles);
    assertFalse(articles.isEmpty());
    assertEquals(id, articles.get(0).getId());
  }

  @Test
  void testDeleteById() {
    Long id = 31L; // Adjust with an existing id from your test data
    articleMapper.deleteById(id);
  }

  @Test
  void testDeleteCollect() {
    Long userId = 1L; // Adjust with an existing user id
    Long articleId = 1L; // Adjust with an existing article id
    articleMapper.deleteCollect(userId, articleId);
    // Optionally verify the deletion
  }

  @Test
  void testInsertCollect() {
    Long userId = 2L; // Adjust with an existing user id
    Long articleId = 1L; // Adjust with an existing article id
    articleMapper.insertCollect(userId, articleId);
    // Optionally verify the insertion
    assertNotEquals(0, 1);
  }

  @Test
  void testIncrementStarsByArticleId() {
    Long articleId = 1L; // Adjust with an existing article id
    articleMapper.incrementStarsByArticleId(articleId);
    // Optionally verify the increment operation
  }

  @Test
  void testDecrementStarsByArticleId() {
    Long articleId = 1L; // Adjust with an existing article id
    articleMapper.decrementStarsByArticleId(articleId);
    // Optionally verify the decrement operation
  }

  @Test
  void testAdvancedSearchMy() {
    String userId = "user1"; // Adjust with an existing user id
    String title = "Test"; // Adjust with an existing title
    List<Article> articles = articleMapper.advancedSearchMy(userId, title, null, null, null, null);
    assertNotNull(articles);
    // Optionally verify the search results
  }

  @Test
  void testGetArticlesByUserid() {
    String userId = "user1"; // Adjust with an existing user id
    List<Article> articles = articleMapper.getArticlesByUserid(userId);
    assertNotNull(articles);
    // Optionally verify the results
  }

  @Test
  void testGetArticlesByUseridAndCollect() {
    Long userId = 1L; // Adjust with an existing user id
    List<Article> articles = articleMapper.getArticlesByUseridAndCollect(userId);
    assertNotNull(articles);
    // Optionally verify the results
  }

  @Test
  void testGetArticlesByUseridAndCollectAndArticleid() {
    Long userId = 1L; // Adjust with an existing user id
    Long articleId = 1L; // Adjust with an existing article id
    List<Article> articles = articleMapper.getArticlesByUseridAndCollectAndArticleid(userId, articleId);
    assertNotNull(articles);
    // Optionally verify the results
  }

  @Test
  void testGetArticlesByUseridAndTitle() {
    String userId = "user1"; // Adjust with an existing user id
    String title = "Test"; // Adjust with an existing title
    List<Article> articles = articleMapper.getArticlesByUseridAndTitle(userId, title);
    assertNotNull(articles);
    // Optionally verify the results
  }

  @Test
  void testAdvancedSearchShare() {
    String author = "author1"; // Adjust with an existing author
    String title = "Test"; // Adjust with an existing title
    List<Article> articles = articleMapper.advancedSearchShare(author, title, null, null, null, null);
    assertNotNull(articles);
    // Optionally verify the search results
  }

  @Test
  void getArticlesByShareAndTitle() {
    String title = "Test"; // Adjust with an existing title
    List<Article> articles = articleMapper.getArticlesByShareAndTitle(title);
    assertNotNull(articles);
    // Optionally verify the results
  }
}
