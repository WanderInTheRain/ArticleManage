package org.example.web.mapper;

import org.example.web.entity.Article;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import javax.sql.DataSource;

import java.util.Collections;
import java.util.List;

@Component
public class ArticleMapper {

    private static JdbcTemplate jdbcTemplate;

    public ArticleMapper(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public List<Article> findByIdAndAuthorId(Long id, Long authorid) {
        String sql = "SELECT * FROM article WHERE id = ? AND authorid = ?";
        try {
            return jdbcTemplate.query(sql, new Object[]{id, authorid}, (resultSet, i) -> {
                Article article = new Article();
                article.setId(resultSet.getLong("id"));
                article.setAuthorid(resultSet.getLong("authorid"));
                article.setTitle(resultSet.getString("title"));
                article.setContent(resultSet.getString("content"));
                article.setDate(resultSet.getTimestamp("date"));
                article.setKey(resultSet.getString("key"));
                article.setShare(resultSet.getLong("share"));
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
                article.setKey(resultSet.getString("key"));
                article.setShare(resultSet.getLong("share"));
                return article;
            });
        } catch (EmptyResultDataAccessException e) {
            // 处理未找到文章的情况
            return Collections.emptyList();
        }
    }
    public List<Article> findByAuthorId(Long authorid) {
        String sql = "SELECT * FROM article WHERE authorid = ?";
        try {
            return jdbcTemplate.query(sql, new Object[]{authorid}, (resultSet, i) -> {
                Article article = new Article();
                article.setId(resultSet.getLong("id"));
                article.setAuthorid(resultSet.getLong("authorid"));
                article.setTitle(resultSet.getString("title"));
                article.setContent(resultSet.getString("content"));
                article.setDate(resultSet.getTimestamp("date"));
                article.setKey(resultSet.getString("key"));
                article.setShare(resultSet.getLong("share"));
                return article;
            });
        } catch (EmptyResultDataAccessException e) {
            // 处理未找到文章的情况
            return Collections.emptyList();
        }
    }

    public List<Article> findByTitleAndAuthorId(String title, Long authorid) {
        String sql = "SELECT * FROM article WHERE title = ? AND authorid = ?";
        try {
            return jdbcTemplate.query(sql, new Object[]{title, authorid}, (resultSet, i) -> {
                Article article = new Article();
                article.setId(resultSet.getLong("id"));
                article.setAuthorid(resultSet.getLong("authorid"));
                article.setTitle(resultSet.getString("title"));
                article.setContent(resultSet.getString("content"));
                article.setDate(resultSet.getTimestamp("date"));
                article.setKey(resultSet.getString("key"));
                article.setShare(resultSet.getLong("share"));
                return article;
            });
        } catch (EmptyResultDataAccessException e) {
            // 处理未找到文章的情况
            return Collections.emptyList();
        }
    }

    public void insertArticle(Article article) {
        String sql = "INSERT INTO article (id, authorid, title, content, date, `key`, categoryid, share) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?) ON DUPLICATE KEY UPDATE " +
                "title = VALUES(title), content = VALUES(content), date = VALUES(date), " +
                "`key` = VALUES(`key`), categoryid = VALUES(categoryid), share = VALUES(share);";

        jdbcTemplate.update(sql, article.getId(), article.getAuthorid(),
                article.getTitle(), article.getContent(),
                article.getDate(), article.getKey(),
                article.getCategoryid(), article.getShare());
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
                article.setKey(resultSet.getString("key"));
                article.setShare(resultSet.getLong("share"));
                return article;
            });
        } catch (EmptyResultDataAccessException e) {
            // 处理未找到文章的情况
            return Collections.emptyList();
        }
    }
    public List<Article> findById(Long id) {
        String sql = "SELECT * FROM article WHERE id = ?";
        try {
            return jdbcTemplate.query(sql, new Object[]{id}, (resultSet, i) -> {
                Article article = new Article();
                article.setId(resultSet.getLong("id"));
                article.setAuthorid(resultSet.getLong("authorid"));
                article.setTitle(resultSet.getString("title"));
                article.setContent(resultSet.getString("content"));
                article.setDate(resultSet.getTimestamp("date"));
                article.setKey(resultSet.getString("key"));
                article.setShare(resultSet.getLong("share"));
                // 设置其他字段...
                return article;
            });
        } catch (EmptyResultDataAccessException e) {
            // 处理未找到文章的情况
            return null;
        }
    }
    public void deleteByIdAndAuthorId(Long articleId, Long authorId) {
        String sql = "DELETE FROM article.article WHERE id = ? AND authorid = ?";
        jdbcTemplate.update(sql, articleId, authorId);
    }
}
