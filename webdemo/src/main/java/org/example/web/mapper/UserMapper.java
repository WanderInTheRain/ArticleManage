package org.example.web.mapper;

import org.example.web.entity.User;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;

@Component
public class UserMapper {

    private final JdbcTemplate jdbcTemplate;

    public UserMapper(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public User findByUsernameAndPassword(String username, String password) {
        String sql = "SELECT * FROM user WHERE name = ? AND password = ?";
        try {
            return jdbcTemplate.queryForObject(sql, new Object[]{username,password}, (resultSet, i) -> {
                User user = new User();
                user.setId(resultSet.getLong("id"));
                user.setUsername(resultSet.getString("name"));
                user.setPassword(resultSet.getString("password"));
                user.setAuthority(resultSet.getInt("authority"));
                // You can add more fields here if needed
                return user;
            });
        } catch (EmptyResultDataAccessException e) {
            // 处理未找到用户的情况
            return null;
        }
    }

    public User findByUsername(String username) {
        String sql = "SELECT * FROM user WHERE name = ?";
        try {
            return jdbcTemplate.queryForObject(sql, new Object[]{username}, (resultSet, i) -> {
                User user = new User();
                user.setId(resultSet.getLong("id"));
                user.setUsername(resultSet.getString("name"));
                user.setPassword(resultSet.getString("password"));
                user.setAuthority(resultSet.getInt("authority"));
                // You can add more fields here if needed
                return user;
            });
        } catch (EmptyResultDataAccessException e) {
            // 处理未找到用户的情况
            return null;
        }
    }

    public long getFirstNonExistingId() {
        String sql = "SELECT MIN(t1.id) + 1 AS first_non_existing_id " +
                "FROM user t1 " +
                "LEFT JOIN user t2 ON t1.id + 1 = t2.id " +
                "WHERE t2.id IS NULL";
        Long firstNonExistingId = jdbcTemplate.queryForObject(sql, Long.class);
        return firstNonExistingId != null ? firstNonExistingId : 0L;
    }

    public void insertUser(User user) {
        String sql = "INSERT INTO user (id, name, password, authority) " +
                "VALUES (?, ?, ?, ?) " +
                "ON DUPLICATE KEY UPDATE " +
                "name = VALUES(name), password = VALUES(password), authority = VALUES(authority)";
        jdbcTemplate.update(sql, user.getId(), user.getUsername(), user.getPassword(), user.getAuthority());
    }

  public User findByUserid(long userid) {
      String sql = "SELECT * FROM user WHERE id = ?";
      try {
          return jdbcTemplate.queryForObject(sql, new Object[]{userid}, (resultSet, i) -> {
              User user = new User();
              user.setId(resultSet.getLong("id"));
              user.setUsername(resultSet.getString("name"));
              user.setPassword(resultSet.getString("password"));
              user.setAuthority(resultSet.getInt("authority"));
              // You can add more fields here if needed
              return user;
          });
      } catch (EmptyResultDataAccessException e) {
          // 处理未找到用户的情况
          return null;
      }
  }
}
