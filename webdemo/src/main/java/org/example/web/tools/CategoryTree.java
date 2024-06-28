package org.example.web.tools;

import com.fasterxml.jackson.core.JsonProcessingException;
import java.util.*;
import org.example.web.entity.Category;
import com.fasterxml.jackson.databind.ObjectMapper;

public class CategoryTree {

  public static TreeNode buildTree(List<Category> categories) {
    Map<Long, TreeNode> map = new HashMap<>();
    TreeNode root = null;

    // Initialize all nodes and map them
    for (Category category : categories) {
      TreeNode node = new TreeNode(category.getCategoryid().toString(), category.getName());
      map.put(category.getCategoryid(), node);
    }

    // Build the tree
    for (Category category : categories) {
      TreeNode node = map.get(category.getCategoryid());
      if (category.getParentid() == null) {
        root = node;  // Found root node
      } else {
        TreeNode parent = map.get(category.getParentid());
        if (parent != null) {
          parent.getChildren().add(node);
        }
      }
    }

    return root;
  }
  public static String rootToString(TreeNode root) throws JsonProcessingException {
    ObjectMapper mapper = new ObjectMapper();
    String jsonTree = mapper.writeValueAsString(root);
    return jsonTree;
  }
  public static String categoryTreeToString(List<Category> categories) throws JsonProcessingException {
    TreeNode root = buildTree(categories);
    String jsonTree = rootToString(root);
    return jsonTree;
  }

  public static void main(String[] args) {
    // Example usage
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

    TreeNode tree = buildTree(categories);
    System.out.println(tree.toString());
  }
}
