package org.example.web.mapper;

import org.example.web.entity.Category;
import org.example.web.entity.User;
import org.springframework.dao.EmptyResultDataAccessException;
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

    public Long getCategoryIdByNames(List<String> names) {
        if (names == null || names.isEmpty()) {
            return null;
        }

        // 构建 SQL 查询
        String sql = "SELECT categoryid FROM category WHERE name IN (";
        for (int i=0; i<names.size(); i++) {
            if (i != names.size() - 1) {
                sql +=  "'" + names.get(i) + "'" + ",";
            }
            else{
                sql +=  "'" + names.get(i) + "'";
            }
        }
        sql += ") and name = '" + names.getLast() + "';";

        try {
            // 执行查询并返回结果
            return jdbcTemplate.queryForObject(sql, Long.class);
        } catch (EmptyResultDataAccessException e) {
            // 处理未找到分类的情况
            return null;
        }
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
        String sql = "INSERT INTO category (categoryid, name, parentid) " +
                "VALUES (?, ?, ?) ;";
        jdbcTemplate.update(sql, category.getCategoryid(),category.getName(), category.getParentid());
    }

}
