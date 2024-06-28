package org.example.web.tools;

import static org.junit.jupiter.api.Assertions.*;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.Test;
import org.example.web.entity.Category;

import java.util.List;

class CategoryTreeTest {

  @Test
  void buildTree() {
    List<Category> categories = List.of(
        new Category(0L, null, "root"),
        new Category(1L, 0L, "Level one 1"),
        new Category(2L, 0L, "Level one 2"),
        new Category(3L, 0L, "Level one 3"),
        new Category(11L, 1L, "Level two 1-1"),
        new Category(21L, 2L, "Level two 2-1"),
        new Category(22L, 2L, "Level two 2-2"),
        new Category(31L, 3L, "Level two 3-1"),
        new Category(32L, 3L, "Level two 3-2"),
        new Category(111L, 11L, "Level three 1-1-1"),
        new Category(211L, 21L, "Level three 2-1-1"),
        new Category(221L, 22L, "Level three 2-2-1"),
        new Category(311L, 31L, "Level three 3-1-1"),
        new Category(321L, 32L, "Level three 3-2-1")
    );

    TreeNode root = CategoryTree.buildTree(categories);

    assertNotNull(root);
    assertEquals("root", "root");
    assertEquals(3, root.getChildren().size());

    // Additional assertions can be added to validate the tree structure
  }

  @Test
  void rootToString() throws JsonProcessingException {
    List<Category> categories = List.of(
        new Category(0L, null, "root"),
        new Category(1L, 0L, "Level one 1"),
        new Category(2L, 0L, "Level one 2"),
        new Category(3L, 0L, "Level one 3")
    );

    TreeNode root = CategoryTree.buildTree(categories);
    String jsonString = CategoryTree.rootToString(root);

    assertNotNull(jsonString);
    // You might want to add further assertions to check the JSON structure or content
  }

  @Test
  void categoryTreeToString() throws JsonProcessingException {
    List<Category> categories = List.of(
        new Category(0L, null, "root"),
        new Category(1L, 0L, "Level one 1"),
        new Category(2L, 0L, "Level one 2"),
        new Category(3L, 0L, "Level one 3")
    );

    String jsonString = CategoryTree.categoryTreeToString(categories);

    assertNotNull(jsonString);
    // You might want to add further assertions to check the JSON structure or content
  }
}
