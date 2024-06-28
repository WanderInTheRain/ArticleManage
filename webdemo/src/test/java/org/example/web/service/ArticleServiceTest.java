package org.example.web.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.example.web.entity.Article;
import org.example.web.entity.Category;
import org.example.web.entity.Comment;
import org.example.web.mapper.ArticleMapper;
import org.example.web.mapper.CategoryMapper;
import org.example.web.mapper.CommentMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

class ArticleServiceTest {

  private ArticleService articleService;
  private ArticleMapper articleMapper;
  private CategoryMapper categoryMapper;
  private CommentMapper commentMapper;

  @BeforeEach
  void setUp() {
    articleMapper = mock(ArticleMapper.class);
    categoryMapper = mock(CategoryMapper.class);
    commentMapper = mock(CommentMapper.class);
    articleService = new ArticleService(articleMapper, categoryMapper, commentMapper);
  }

  @Test
  void testCreateCategory() {
    // Setup
    String categoryName = "Test Category";
    Long parentId = 1L;
    Category category = new Category();
    category.setCategoryid(1L); // Assuming category id setup

    when(categoryMapper.getFirstNonExistingId()).thenReturn(1L);
    doNothing().when(categoryMapper).insertCategory(any(Category.class));

    // Execution
    Long categoryId = articleService.createCategory(categoryName, parentId);

    // Verification
    assertNotNull(categoryId);
    assertEquals(1L, categoryId);
  }

  @Test
  void testGetFirstNonExistingId() {
    // Setup
    when(articleMapper.getFirstNonExistingId()).thenReturn(1L);

    // Execution
    Long id = articleService.getFirstNonExistingId();

    // Verification
    assertEquals(1L, id);
  }

  @Test
  void testInsertArticle() {
    // Setup
    Article article = new Article();

    // Execution
    assertDoesNotThrow(() -> articleService.insertArticle(article));

    // Verification
    verify(articleMapper, times(1)).insertArticle(article);
  }

  @Test
  void testFindCommentByArticleId() {
    // Setup
    Long articleId = 1L;
    List<Comment> comments = new ArrayList<>();
    when(commentMapper.findByArticleId(articleId)).thenReturn(comments);

    // Execution
    List<Comment> result = articleService.findCommentByArticleId(articleId);

    // Verification
    assertNotNull(result);
    assertEquals(comments, result);
  }

  @Test
  void testInsertComment() {
    // Setup
    String content = "Test Comment";
    Long articleId = 1L;
    when(commentMapper.getFirstNonExistingId()).thenReturn(1L);
    doNothing().when(commentMapper).saveOrUpdate(any(Comment.class));

    // Execution
    assertDoesNotThrow(() -> articleService.insertComment(content, articleId));

    // Verification
    verify(commentMapper, times(1)).saveOrUpdate(any(Comment.class));
  }

  @Test
  void testFindMyBlog() {
    // Setup
    String userId = "testUser";
    List<Article> articles = new ArrayList<>();
    when(articleMapper.getArticlesByUserid(userId)).thenReturn(articles);

    // Execution
    List<Article> result = articleService.findMyBlog(userId);

    // Verification
    assertNotNull(result);
    assertEquals(articles, result);
  }

  @Test
  void testFindMyBlogById() {
    // Setup
    String articleId = "3";
    Article article = new Article();

    // Execution
    Article result = articleService.findMyBlogById(articleId);

    // Verification
    assertNull(result);
    assertNotEquals(article, result);
  }

  @Test
  void testFindMyBlogByTitle() {
    // Setup
    String userId = "testUser";
    String title = "Test Title";
    List<Article> articles = new ArrayList<>();
    when(articleMapper.getArticlesByUseridAndTitle(userId, title)).thenReturn(articles);

    // Execution
    List<Article> result = articleService.findMyBlogByTitle(userId, title);

    // Verification
    assertNotNull(result);
    assertEquals(articles, result);
  }

  @Test
  void testGetAllCategories() {
    // Setup
    List<Category> categories = new ArrayList<>();
    when(categoryMapper.getAllCategories()).thenReturn(categories);

    // Execution
    List<Category> result = articleService.getAllCategories();

    // Verification
    assertNotNull(result);
    assertEquals(categories, result);
  }

  @Test
  void testFindCategoryNameById() {
    // Setup
    Long categoryId = 1L;
    String categoryName = "Test Category";
    when(categoryMapper.findCategoryNameById(categoryId)).thenReturn(categoryName);

    // Execution
    String result = articleService.findCategoryNameById(categoryId);

    // Verification
    assertNotNull(result);
    assertEquals(categoryName, result);
  }

  @Test
  void testAdvancedSearch_SharedArticles() {
    // Setup
    String userId = "testUser";
    String author = "Test Author";
    String title = "Test Title";
    String content = "Test Content";
    String startDate = "2024-01-01";
    String endDate = "2024-06-01";
    Long categoryId = 1L;
    String searchType = "sharedArticles";
    List<Long> subCategories = new ArrayList<>();
    subCategories.add(categoryId);
    when(categoryMapper.getAllSubcategoryIds(categoryId)).thenReturn(subCategories);
    List<Article> articles = new ArrayList<>();
    when(articleMapper.advancedSearchShare(author, title, content, startDate, endDate, subCategories))
        .thenReturn(articles);

    // Execution
    List<Article> result = articleService.advancedSearch(userId, author, title, content, startDate, endDate,
        categoryId, searchType);

    // Verification
    assertNotNull(result);
    assertEquals(articles, result);
  }

  @Test
  void testAdvancedSearch_MyArticles() {
    // Setup
    String userId = "testUser";
    String author = "Test Author";
    String title = "Test Title";
    String content = "Test Content";
    String startDate = "2024-01-01";
    String endDate = "2024-06-01";
    Long categoryId = 1L;
    String searchType = "myArticles";
    List<Long> subCategories = new ArrayList<>();
    subCategories.add(categoryId);
    when(categoryMapper.getAllSubcategoryIds(categoryId)).thenReturn(subCategories);
    List<Article> articles = new ArrayList<>();
    when(articleMapper.advancedSearchMy(userId, title, content, startDate, endDate, subCategories))
        .thenReturn(articles);

    // Execution
    List<Article> result = articleService.advancedSearch(userId, author, title, content, startDate, endDate,
        categoryId, searchType);

    // Verification
    assertNotNull(result);
    assertEquals(articles, result);
  }

  @Test
  void testAdvancedSearch_Null() {
    // Setup
    String userId = "testUser";
    String author = "Test Author";
    String title = "Test Title";
    String content = "Test Content";
    String startDate = "2024-01-01";
    String endDate = "2024-06-01";
    Long categoryId = 1L;
    String searchType = "invalidType";

    // Execution
    List<Article> result = articleService.advancedSearch(userId, author, title, content, startDate, endDate,
        categoryId, searchType);

    // Verification
    assertNull(result);
  }

  @Test
  void testDeleteArticleById() {
    // Setup
    Long articleId = 1L;
    doNothing().when(articleMapper).deleteById(articleId);

    // Execution
    assertDoesNotThrow(() -> articleService.deleteArticleById(articleId));

    // Verification
    verify(articleMapper, times(1)).deleteById(articleId);
  }

  @Test
  void testFindShare() {
    // Setup
    List<Article> articles = new ArrayList<>();
    when(articleMapper.findByShare()).thenReturn(articles);

    // Execution
    List<Article> result = articleService.findShare();

    // Verification
    assertNotNull(result);
    assertEquals(articles, result);
  }

  @Test
  void testFindShareBytitle() {
    // Setup
    String title = "Test Title";
    List<Article> articles = new ArrayList<>();
    when(articleMapper.getArticlesByShareAndTitle(title)).thenReturn(articles);

    // Execution
    List<Article> result = articleService.findShareBytitle(title);

    // Verification
    assertNotNull(result);
    assertEquals(articles, result);
  }

  @Test
  void testFindCollect() {
    // Setup
    Long userId = 1L;
    List<Article> articles = new ArrayList<>();
    when(articleMapper.getArticlesByUseridAndCollect(userId)).thenReturn(articles);

    // Execution
    List<Article> result = articleService.findCollect(userId);

    // Verification
    assertNotNull(result);
    assertEquals(articles, result);
  }

  @Test
  void testSetCollect() {
    // Setup
    Long userId = 1L;
    Long articleId = 1L;
    doNothing().when(articleMapper).insertCollect(userId, articleId);
    doNothing().when(articleMapper).incrementStarsByArticleId(articleId);

    // Execution
    assertDoesNotThrow(() -> articleService.setCollect(userId, articleId));

    // Verification
    verify(articleMapper, times(1)).insertCollect(userId, articleId);
    verify(articleMapper, times(1)).incrementStarsByArticleId(articleId);
  }

  @Test
  void testDeleteCollect() {
    // Setup
    Long userId = 1L;
    Long articleId = 1L;
    doNothing().when(articleMapper).deleteCollect(userId, articleId);
    doNothing().when(articleMapper).decrementStarsByArticleId(articleId);

    // Execution
    assertDoesNotThrow(() -> articleService.deleteCollect(userId, articleId));

    // Verification
    verify(articleMapper, times(1)).deleteCollect(userId, articleId);
    verify(articleMapper, times(1)).decrementStarsByArticleId(articleId);
  }

  @Test
  void testGetCollect_Success() {
    // Setup
    Long userId = 1L;
    Long articleId = 1L;
    List<Article> articles = new ArrayList<>();
    articles.add(new Article());
    when(articleMapper.getArticlesByUseridAndCollectAndArticleid(userId, articleId)).thenReturn(articles);

    // Execution
    String result = articleService.getCollect(userId, articleId);

    // Verification
    assertEquals("success", result);
  }

  @Test
  void testGetCollect_Fail() {
    // Setup
    Long userId = 1L;
    Long articleId = 1L;
    List<Article> articles = new ArrayList<>();

    // Execution
    String result = articleService.getCollect(userId, articleId);

    // Verification
    assertEquals("fail", result);
  }
}
