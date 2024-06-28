package org.example.web.mapper;

import static org.junit.jupiter.api.Assertions.*;

import org.example.web.entity.Category;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;

import javax.sql.DataSource;
import java.util.List;

@JdbcTest
@ContextConfiguration(classes = {CategoryMapper.class})
@ComponentScan("org.example.web.mapper")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE) // 不替换自定义的 DataSource
@TestPropertySource(locations = "classpath:application.yml") // 加载测试属性文件
class CategoryMapperTest {

  @Autowired
  private CategoryMapper categoryMapper;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  void getFirstNonExistingId() {
    long result = categoryMapper.getFirstNonExistingId();
    assertNotEquals(4L, result);
  }

  @Test
  void insertCategory() {
    Category category = new Category();
    category.setCategoryid(4L);
    category.setName("Test Category");
    category.setParentid(1L);

    categoryMapper.insertCategory(category);

    List<Category> categories = categoryMapper.getAllCategories();
    assertNotEquals(4, categories.size());
  }

  @Test
  void getAllCategories() {
    List<Category> categories = categoryMapper.getAllCategories();
    assertNotEquals(3, categories.size());
  }

  @Test
  void findCategoryNameById() {
    String name = categoryMapper.findCategoryNameById(1L);
    assertNotEquals("Root", name);
  }

  @Test
  void getAllSubcategoryIds() {
    List<Long> subcategoryIds = categoryMapper.getAllSubcategoryIds(1L);
    assertEquals(2, subcategoryIds.size());
    assertFalse(subcategoryIds.contains(2L));
    assertTrue(subcategoryIds.contains(3L));
  }
}
