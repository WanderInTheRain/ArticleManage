package org.example.web.mapper;

import org.example.web.entity.Category;
import org.example.web.entity.User;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.util.Collections;
import java.util.List;

@Component
public class CategoryMapper {

    private final JdbcTemplate jdbcTemplate;

    public CategoryMapper(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public long getFirstNonExistingId() {
        String sql = "SELECT MIN(t1.categoryid) + 1 AS first_non_existing_id " +
                "FROM category t1 " +
                "LEFT JOIN category t2 ON t1.categoryid + 1 = t2.categoryid " +
                "WHERE t2.categoryid IS NULL";
        Long firstNonExistingId = jdbcTemplate.queryForObject(sql, Long.class);
        return firstNonExistingId != null ? firstNonExistingId : 0L;
    }

    public void insertCategory(Category category) {
        String sql = "INSERT INTO category (categoryid, name, parentid) VALUES (?, ?, ?)";
        try {
            jdbcTemplate.update(sql, category.getCategoryid(), category.getName(), category.getParentid());
        } catch (DataAccessException e) {
            // 捕获数据库访问异常并处理
            System.err.println("Error inserting category: " + e.getMessage());
            // 可以进一步记录日志或重新抛出异常
            // throw e; // 如果需要重新抛出异常
        }
    }


    public List<Category> getAllCategories() {
        String sql = "SELECT categoryid, name, parentid FROM category";
        try {
            // Use BeanPropertyRowMapper to automatically map rows to Category objects
            return jdbcTemplate.query(sql, BeanPropertyRowMapper.newInstance(Category.class));
        } catch (EmptyResultDataAccessException e) {
            return Collections.emptyList(); // Return an empty list if no categories found
        }
    }

    public String findCategoryNameById(Long categoryId) {
        if (categoryId == null) {
            return null;
        }

        String sql = "SELECT name FROM category WHERE categoryid = ?";

        try {
            return jdbcTemplate.queryForObject(sql, new Object[]{categoryId}, String.class);
        } catch (EmptyResultDataAccessException e) {
            return null; // Return null if no category is found with the given ID
        }
    }

    public List<Long> getAllSubcategoryIds(Long categoryId) {
        if (categoryId == null) {
            return Collections.emptyList();
        }

        String sql = "WITH RECURSIVE Subcategories AS ( " +
            "  SELECT categoryid FROM category WHERE parentid = ? " +
            "  UNION ALL " +
            "  SELECT c.categoryid FROM category c " +
            "  INNER JOIN Subcategories s ON c.parentid = s.categoryid " +
            ") " +
            "SELECT categoryid FROM Subcategories";

        try {
            return jdbcTemplate.queryForList(sql, new Object[]{categoryId}, Long.class);
        } catch (EmptyResultDataAccessException e) {
            return Collections.emptyList(); // Return an empty list if no subcategories found
        }
    }
}
