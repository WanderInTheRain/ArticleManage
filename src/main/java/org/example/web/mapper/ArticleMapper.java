package org.example.web.mapper;

import org.example.web.entity.Article;
import org.example.web.entity.User;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import javax.sql.DataSource;
import org.springframework.ui.Model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Component
public class ArticleMapper {

    private final JdbcTemplate jdbcTemplate;

    public ArticleMapper(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public List<Article> findById(Long id, Long authorid) {
        String sql = "SELECT * FROM article WHERE id = ? AND authorid = ?";
        try {
            return jdbcTemplate.query(sql, new Object[]{id, authorid}, (resultSet, i) -> {
                Article article = new Article();
                article.setId(resultSet.getLong("id"));
                article.setAuthorid(resultSet.getLong("authorid"));
                article.setTitle(resultSet.getString("title"));
                article.setContent(resultSet.getString("content"));
                article.setDate(resultSet.getTimestamp("date"));
                // 设置其他字段...
                return article;
            });
        } catch (EmptyResultDataAccessException e) {
            // 处理未找到文章的情况
            return null;
        }
    }

    public List<Article> findByShare() {
        String sql = "SELECT * FROM article WHERE share = 1";
        try {
            return jdbcTemplate.query(sql, new Object[]{}, (resultSet, i) -> {
                Article article = new Article();
                article.setId(resultSet.getLong("id"));
                article.setAuthorid(resultSet.getLong("authorid"));
                article.setTitle(resultSet.getString("title"));
                article.setContent(resultSet.getString("content"));
                article.setDate(resultSet.getTimestamp("date"));
                return article;
            });
        } catch (EmptyResultDataAccessException e) {
            // 处理未找到文章的情况
            return Collections.emptyList();
        }
    }

    public List<Article> findByTitle(String title, Long authorid) {
        String sql = "SELECT * FROM article WHERE title = ? AND authorid = ?";
        try {
            return jdbcTemplate.query(sql, new Object[]{title, authorid}, (resultSet, i) -> {
                Article article = new Article();
                article.setId(resultSet.getLong("id"));
                article.setAuthorid(resultSet.getLong("authorid"));
                article.setTitle(resultSet.getString("title"));
                article.setContent(resultSet.getString("content"));
                article.setDate(resultSet.getTimestamp("date"));
                return article;
            });
        } catch (EmptyResultDataAccessException e) {
            // 处理未找到文章的情况
            return Collections.emptyList();
        }
    }

    public List<Article> findBy(String method, String content, Model model) {
        Long userid = (Long) model.getAttribute("userid");
        String sql;
        String useridstr = String.valueOf(userid);
        if (method.equals("id")) {
            sql = "SELECT * FROM article.article WHERE id = " + content + " and authorid = " + useridstr;
        }
        else if (method.equals("share")) {
            sql = "SELECT * FROM article.article WHERE share = " + content;
        }
        else{
            sql = "SELECT * FROM article.article WHERE " + method + " = '" + content + "' and authorid = " + useridstr;
        }
        try {
            return jdbcTemplate.query(sql, new Object[]{}, (resultSet, i) -> {
                Article article = new Article();
                article.setId(resultSet.getLong("id"));
                article.setAuthorid(resultSet.getLong("authorid"));
                article.setTitle(resultSet.getString("title"));
                article.setContent(resultSet.getString("content"));
                article.setDate(resultSet.getTimestamp("date"));
                return article;
            });
        } catch (EmptyResultDataAccessException e) {
            // 处理未找到文章的情况
            return Collections.emptyList();
        }
    }


    public void insertArticle(Article article) {
        String sql = "INSERT INTO article (id, authorid, title, content, date, `key`, categoryid) " +
                "VALUES (?, ?, ?, ?, ?, ?, 1) ON DUPLICATE KEY UPDATE " +
                "title = VALUES(title), content = VALUES(content), date = VALUES(date), " +
                "`key` = VALUES(`key`), categoryid = VALUES(categoryid);";
        jdbcTemplate.update(sql, article.getId(), article.getAuthorid(), article.getTitle(), article.getContent(), article.getDate(), "w");
    }

    public long getFirstNonExistingId() {
        String sql = "SELECT MIN(t1.id) + 1 AS first_non_existing_id " +
                "FROM article.article t1 " +
                "LEFT JOIN article.article t2 ON t1.id + 1 = t2.id " +
                "WHERE t2.id IS NULL";
        Long firstNonExistingId = jdbcTemplate.queryForObject(sql, Long.class);
        return firstNonExistingId != null ? firstNonExistingId : 0L;
    }
    public List<Article> getArticlesByCategory(Long categoryId, Long userid) {
        String sql = "WITH RECURSIVE CategoryTree AS (\n" +
                "    SELECT categoryid\n" +
                "    FROM category\n" +
                "    WHERE categoryid = ?\n" +
                "    UNION ALL\n" +
                "    SELECT c.categoryid\n" +
                "    FROM category c\n" +
                "    INNER JOIN CategoryTree ct ON c.parentid = ct.categoryid\n" +
                ")\n" +
                "SELECT a.*\n" +
                "FROM article a\n" +
                "INNER JOIN CategoryTree ct ON a.categoryid = ct.categoryid\n" +
                "WHERE a.authorid = ?;\n";

        try {
            return jdbcTemplate.query(sql, new Object[]{categoryId, userid}, (resultSet, i) -> {
                Article article = new Article();
                article.setId(resultSet.getLong("id"));
                article.setAuthorid(resultSet.getLong("authorid"));
                article.setTitle(resultSet.getString("title"));
                article.setContent(resultSet.getString("content"));
                article.setDate(resultSet.getTimestamp("date"));
                article.setCategoryid(resultSet.getLong("categoryid"));
                return article;
            });
        } catch (EmptyResultDataAccessException e) {
            // 处理未找到文章的情况
            return Collections.emptyList();
        }
    }

}
