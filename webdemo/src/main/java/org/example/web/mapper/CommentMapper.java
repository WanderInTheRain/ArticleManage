package org.example.web.mapper;

import org.springframework.ui.Model;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;
import org.example.web.entity.Comment;

@Component
public class CommentMapper {

    private final JdbcTemplate jdbcTemplate;

    public CommentMapper(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    // Method to insert or update a comment
    public void saveOrUpdate(Comment comment) {
        String sql = "INSERT INTO comment (commentid, content, articleid) " +
                "VALUES (?, ?, ?)  ON DUPLICATE KEY UPDATE " +
                "content = VALUES(content), articleid = VALUES(articleid), commentid = VALUES(commentid);"; // 移除了对 timestamp 列的更新
        jdbcTemplate.update(sql, comment.getComentid(), comment.getContent(), comment.getArticleid());
    }


    // Method to find comments by articleId
    public List<Comment> findByArticleId(Long articleid) {
        String sql = "SELECT * FROM comment WHERE articleid = ?";
        try {
            return jdbcTemplate.query(sql, new Object[]{articleid}, (ResultSet rs, int rowNum) -> {
                Comment comment = new Comment();
                comment.setComentid(rs.getLong("commentid")); // Ensure this matches your entity field name
                comment.setContent(rs.getString("content"));
                comment.setArticleid(rs.getLong("articleid"));
                return comment;
            });
        } catch (EmptyResultDataAccessException e) {
            // Handle case when no comments are found
            return Collections.emptyList();
        }
    }

    public long getFirstNonExistingId() {
        String sql = "SELECT MIN(t1.commentid) + 1 AS first_non_existing_id " +
                "FROM comment t1 " +
                "LEFT JOIN comment t2 ON t1.commentid + 1 = t2.commentid " +
                "WHERE t2.commentid IS NULL";
        Long firstNonExistingId = jdbcTemplate.queryForObject(sql, Long.class);
        return firstNonExistingId != null ? firstNonExistingId : 0L;
    }
}
