package org.example.web.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.fasterxml.jackson.core.JsonProcessingException;
import java.util.ArrayList;
import java.util.Map;
import org.example.web.entity.Article;
import org.example.web.entity.Category;
import org.example.web.entity.Comment;
import org.example.web.service.ArticleService;
import org.example.web.tools.CategoryTree;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

class ArticleControllerTest {

  @Mock
  private ArticleService articleService;

  @InjectMocks
  private ArticleController articleController;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.initMocks(this);
  }

  @Test
  void handleFileUpload() throws IOException {
    MultipartFile file = mock(MultipartFile.class);
    when(file.getBytes()).thenReturn("file content".getBytes());
    when(file.getOriginalFilename()).thenReturn("image.jpg");

    ResponseEntity<Map<String, Object>> response = articleController.handleFileUpload(file);

    assertEquals(200, response.getStatusCodeValue());
    assertTrue(response.getBody().containsKey("url"));
  }

  @Test
  void getImage() throws IOException {
    ResponseEntity<Resource> response = articleController.getImage("image.jpg");

    assertEquals(200, response.getStatusCodeValue());
    assertEquals(MediaType.IMAGE_JPEG, response.getHeaders().getContentType());
  }

  @Test
  void searchmyArticle() {
    String userId = "1";
    List<Article> articles = List.of(new Article());
    when(articleService.findMyBlog(userId)).thenReturn(articles);

    List<Article> result = articleController.searchmyArticle(userId);

    assertNotNull(result);
    assertEquals(articles, result);
  }

  @Test
  void searchmyArticleByTitle() {
    String userId = "1";
    String title = "test title";
    List<Article> articles = List.of(new Article());
    when(articleService.findMyBlogByTitle(userId, title)).thenReturn(articles);

    List<Article> result = articleController.searchmyArticleByTitle(userId, title);

    assertNotNull(result);
    assertEquals(articles, result);
  }

  @Test
  void getMyblog() {
    String articleId = "1";
    Article article = new Article();
    when(articleService.findMyBlogById(articleId)).thenReturn(article);

    Article result = articleController.getMyblog(articleId);

    assertNotNull(result);
    assertEquals(article, result);
  }

  @Test
  void collectArticles() {
    Long userId = 1L;
    List<Article> articles = List.of(new Article());
    when(articleService.findCollect(userId)).thenReturn(articles);

    List<Article> result = articleController.collectArticles(userId);

    assertNotNull(result);
    assertEquals(articles, result);
  }

  @Test
  void setcollectArticles() {
    Long userId = 1L;
    String articleId = "1";

    String result = articleController.setcollectArticles(userId, articleId);

    verify(articleService).setCollect(userId, Long.parseLong(articleId));
    assertEquals("success", result);
  }

  @Test
  void deletecollectArticles() {
    Long userId = 1L;
    String articleId = "1";

    String result = articleController.deletecollectArticles(userId, articleId);

    verify(articleService).deleteCollect(userId, Long.parseLong(articleId));
    assertEquals("success", result);
  }

  @Test
  void getcollectArticles() {
    String userId = "1";
    Long articleId = 1L;
    String expected = "result";
    when(articleService.getCollect(Long.parseLong(userId), articleId)).thenReturn(expected);

    String result = articleController.getcollectArticles(userId, articleId);

    assertEquals(expected, result);
  }

  @Test
  void shareArticles() {
    List<Article> articles = List.of(new Article());
    when(articleService.findShare()).thenReturn(articles);

    List<Article> result = articleController.shareArticles();

    assertNotNull(result);
    assertEquals(articles, result);
  }

  @Test
  void shareArticlestitle() {
    String title = "test title";
    List<Article> articles = List.of(new Article());
    when(articleService.findShareBytitle(title)).thenReturn(articles);

    List<Article> result = articleController.shareArticlestitle(title);

    assertNotNull(result);
    assertEquals(articles, result);
  }

  @Test
  void getComments() {
    Long articleId = 1L;
    List<Comment> comments = List.of(new Comment());
    when(articleService.findCommentByArticleId(articleId)).thenReturn(comments);

    List<Comment> result = articleController.getComments(articleId);

    assertNotNull(result);
    assertEquals(comments, result);
  }

  @Test
  void getCategoryName() {
    Long categoryId = 1L;
    String expected = "Category Name";
    when(articleService.findCategoryNameById(categoryId)).thenReturn(expected);

    String result = articleController.getCategoryName(categoryId);

    assertEquals(expected, result);
  }

  @Test
  void getcategorytree() throws IOException {
    List<Category> categories = new ArrayList<>();
    when(articleService.getAllCategories()).thenReturn(categories);

    String result = articleController.getcategorytree();

    assertNotNull(result);
  }


  @Test
  void createArticle() {
    Long authorId = 1L;
    String title = "title";
    String content = "content";
    Long share = 1L;
    String categoryId = "1";
    String key = "key";
    Long expectedId = 1L;
    when(articleService.getFirstNonExistingId()).thenReturn(expectedId);

    String result = articleController.createArticle(authorId, title, content, share, categoryId, key);

    assertEquals("success", result);
  }

  @Test
  void saveArticle() {
    Long articleId = 1L;
    Long authorId = 1L;
    String title = "title";
    String content = "content";
    Long share = 1L;
    String categoryId = "1";
    String key = "key";

    String result = articleController.saveArticle(articleId, authorId, title, content, share, categoryId, key);

    assertEquals("success", result);
  }

  @Test
  void deleteArticle() {
    Long articleId = 1L;

    String result = articleController.deleteArticle(articleId);

    verify(articleService).deleteArticleById(articleId);
    assertEquals("success", result);
  }

  @Test
  void advancedSearch() {
    String author = "author";
    String title = "title";
    String startDate = "2023-01-01";
    String endDate = "2023-12-31";
    Long categoryId = 1L;
    String searchType = "type";
    String userId = "1";
    String content = "content";
    List<Article> articles = List.of(new Article());
    when(articleService.advancedSearch(userId, author, title, content, startDate, endDate, categoryId, searchType)).thenReturn(articles);

    List<Article> result = articleController.advancedSearch(author, title, startDate, endDate, categoryId, searchType, userId, content);

    assertNotNull(result);
    assertEquals(articles, result);
  }

  @Test
  void addCategory() {
    String name = "Category";
    Long parentId = 1L;
    Long expectedId = 1L;
    when(articleService.createCategory(name, parentId)).thenReturn(expectedId);

    Long result = articleController.addCategory(name, parentId);

    assertEquals(expectedId, result);
  }

  @Test
  void savecomment() {
    Long articleId = 1L;
    String content = "content";

    String result = articleController.savecomment(articleId, content);

    assertEquals("save success", result);
  }

  @Test
  void getblogbyid() {
    Long articleId = 1L;
    Article expected = new Article();
    when(articleService.findMyBlogById(articleId.toString())).thenReturn(expected);

    Article result = articleController.getblogbyid(articleId);

    assertNotNull(result);
    assertEquals(expected, result);
  }
}
