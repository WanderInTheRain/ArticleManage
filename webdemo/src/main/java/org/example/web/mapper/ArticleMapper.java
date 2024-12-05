package org.example.web.mapper;

import java.util.ArrayList;
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
                article.setStars(resultSet.getLong("stars"));
                return article;
            });
        } catch (EmptyResultDataAccessException e) {
            // 处理未找到文章的情况
            return Collections.emptyList();
        }
    }

    public void insertArticle(Article article) {
        String sql =
            "INSERT INTO article (id, authorid, title, content, date, `key`, categoryid, share) " +
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
                article.setCategoryid(resultSet.getLong("categoryid"));
                article.setStars(resultSet.getLong("stars"));
                // 设置其他字段...
                return article;
            });
        } catch (EmptyResultDataAccessException e) {
            // 处理未找到文章的情况
            return null;
        }
    }

    public List<Article> getArticlesByUserid(String userid) {
        String sql = "SELECT id, title, date, stars FROM article WHERE authorid = ?";
        try {
            return jdbcTemplate.query(sql, new Object[]{userid}, (resultSet, i) -> {
                Article article = new Article();
                article.setId(resultSet.getLong("id"));
                article.setTitle(resultSet.getString("title"));
                article.setDate(resultSet.getTimestamp("date"));
                article.setStars(resultSet.getLong("stars"));
                return article;
            });
        } catch (EmptyResultDataAccessException e) {
            // 处理未找到文章的情况
            return null;
        }
    }

    public List<Article> getArticlesByUseridAndCollect(Long userid) {
        String sql = "SELECT a.*\n"
            + "FROM collect c\n"
            + "JOIN article a ON c.articleid = a.id\n"
            + "WHERE c.userid = ?;";
        try {
            return jdbcTemplate.query(sql, new Object[]{userid}, (resultSet, i) -> {
                Article article = new Article();
                article.setId(resultSet.getLong("id"));
                article.setTitle(resultSet.getString("title"));
                article.setDate(resultSet.getTimestamp("date"));
                article.setStars(resultSet.getLong("stars"));
                return article;
            });
        } catch (EmptyResultDataAccessException e) {
            // 处理未找到文章的情况
            return null;
        }
    }

    public List<Article> getArticlesByUseridAndCollectAndArticleid(Long userid,Long articleid) {
        String sql = "SELECT a.*\n"
            + "FROM collect c\n"
            + "JOIN article a ON c.articleid = a.id\n"
            + "WHERE c.userid = ? AND c.articleid = ?;";
        try {
            return jdbcTemplate.query(sql, new Object[]{userid,articleid}, (resultSet, i) -> {
                Article article = new Article();
                article.setId(resultSet.getLong("id"));
                article.setTitle(resultSet.getString("title"));
                article.setDate(resultSet.getTimestamp("date"));
                article.setStars(resultSet.getLong("stars"));
                return article;
            });
        } catch (EmptyResultDataAccessException e) {
            // 处理未找到文章的情况
            return null;
        }
    }

    public List<Article> getArticlesByUseridAndTitle(String userid, String title) {
        String sql = "SELECT id, title, date, stars  FROM article WHERE authorid = ? and title LIKE ?";
        try {
            return jdbcTemplate.query(sql, new Object[]{userid, '%' + title + '%'}, (resultSet, i) -> {
                Article article = new Article();
                article.setId(resultSet.getLong("id"));
                article.setTitle(resultSet.getString("title"));
                article.setDate(resultSet.getTimestamp("date"));
                article.setStars(resultSet.getLong("stars"));
                return article;
            });
        } catch (EmptyResultDataAccessException e) {
            // 处理未找到文章的情况
            return null;
        }
    }

    public List<Article> advancedSearchShare(String author, String title, String content, String startDate, String endDate, List<Long> subCategory) {
        StringBuilder sql = new StringBuilder("SELECT * FROM article WHERE share=1 ");
        List<Object> params = new ArrayList<>();

        if (author != null && !author.isEmpty()) {
            String authorSql = "SELECT id FROM user WHERE name = ?";
            try {
                Long authorId = jdbcTemplate.queryForObject(authorSql, new Object[]{author}, Long.class);
                if (authorId != null) {
                    sql.append("AND authorid = ? ");
                    params.add(authorId);
                } else {
                    return new ArrayList<>(); // 如果没有找到对应的作者，返回空列表
                }
            } catch (EmptyResultDataAccessException e) {
                return new ArrayList<>(); // 如果查询作者时没有找到结果，返回空列表
            }
        }
        if (title != null && !title.isEmpty()) {
            sql.append("AND title LIKE ? ");
            params.add("%" + title + "%");
        }
        if (content != null && !content.isEmpty()) {
            sql.append("AND content LIKE ? ");
            params.add("%" + content + "%");
        }
        if (startDate != null) {
            sql.append("AND date >= ? ");
            params.add(startDate);
        }
        if (endDate != null) {
            sql.append("AND date <= ? ");
            params.add(endDate);
        }
        if (subCategory != null && !subCategory.isEmpty()) {
            sql.append("AND categoryid IN (");
            for (int i = 0; i < subCategory.size(); i++) {
                if (i > 0) {
                    sql.append(", ");
                }
                sql.append("?");
            }
            sql.append(") ");
            params.addAll(subCategory);
        }

        try {
            return jdbcTemplate.query(sql.toString(), params.toArray(), (resultSet, i) -> {
                Article article = new Article();
                article.setId(resultSet.getLong("id"));
                article.setAuthorid(resultSet.getLong("authorid"));
                article.setTitle(resultSet.getString("title"));
                article.setDate(resultSet.getTimestamp("date"));
                article.setKey(resultSet.getString("key"));
                article.setShare(resultSet.getLong("share"));
                article.setCategoryid(resultSet.getLong("categoryid"));
                article.setStars(resultSet.getLong("stars"));
                return article;
            });
        } catch (EmptyResultDataAccessException e) {
            return new ArrayList<>();
        }
    }

    public void deleteById(Long articleId) {
        String sql = "DELETE FROM article WHERE id = ?";
        jdbcTemplate.update(sql, articleId);
    }

    public List<Article> getArticlesByShareAndTitle(String title) {
        String sql = "SELECT id, title, date, stars  FROM article WHERE share = 1 and title LIKE ?";
        try {
            return jdbcTemplate.query(sql, new Object[]{'%' + title + '%'}, (resultSet, i) -> {
                Article article = new Article();
                article.setId(resultSet.getLong("id"));
                article.setTitle(resultSet.getString("title"));
                article.setDate(resultSet.getTimestamp("date"));
                article.setStars(resultSet.getLong("stars"));
                return article;
            });
        } catch (EmptyResultDataAccessException e) {
            // 处理未找到文章的情况
            return null;
        }
    }

    public void insertCollect(Long userid, Long articleid) {
        String sql = "INSERT INTO collect (userid, articleid) VALUES (?, ?)";
        jdbcTemplate.update(sql,userid,articleid);
    }

    public void incrementStarsByArticleId(Long articleId) {
        String sql = "UPDATE article SET stars = stars + 1 WHERE id = ?";
        jdbcTemplate.update(sql, articleId);
    }

    public void decrementStarsByArticleId(Long articleId) {
        String sql = "UPDATE article SET stars = stars - 1 WHERE id = ?";
        jdbcTemplate.update(sql, articleId);
    }

    public void deleteCollect(Long userid, Long articleid) {
        String sql = "DELETE FROM collect WHERE userid = ? AND articleid = ?";
        jdbcTemplate.update(sql, userid, articleid);
    }

  public List<Article> advancedSearchMy(String userid, String title, String content, String startdate, String enddate, List<Long> subCategory) {
      StringBuilder sql = new StringBuilder("SELECT * FROM article WHERE 1=1 ");
      List<Object> params = new ArrayList<>();

      if (userid != null && !userid.isEmpty()) {
          sql.append("AND authorid = ? ");
          params.add(userid);
      }
      if (title != null && !title.isEmpty()) {
          sql.append("AND title LIKE ? ");
          params.add("%" + title + "%");
      }
      if (content != null && !content.isEmpty()) {
          sql.append("AND content LIKE ? ");
          params.add("%" + content + "%");
      }
      if (startdate != null) {
          sql.append("AND date >= ? ");
          params.add(startdate);
      }
      if (enddate != null) {
          sql.append("AND date <= ? ");
          params.add(enddate);
      }
      if (subCategory != null && !subCategory.isEmpty()) {
          sql.append("AND categoryid IN (");
          for (int i = 0; i < subCategory.size(); i++) {
              if (i > 0) {
                  sql.append(", ");
              }
              sql.append("?");
          }
          sql.append(") ");
          params.addAll(subCategory);
      }

      try {
          return jdbcTemplate.query(sql.toString(), params.toArray(), (resultSet, i) -> {
              Article article = new Article();
              article.setId(resultSet.getLong("id"));
              article.setAuthorid(resultSet.getLong("authorid"));
              article.setTitle(resultSet.getString("title"));
              article.setDate(resultSet.getTimestamp("date"));
              article.setKey(resultSet.getString("key"));
              article.setShare(resultSet.getLong("share"));
              article.setCategoryid(resultSet.getLong("categoryid"));
              article.setStars(resultSet.getLong("stars"));
              return article;
          });
      } catch (EmptyResultDataAccessException e) {
          return new ArrayList<>();
      }
    }
}
