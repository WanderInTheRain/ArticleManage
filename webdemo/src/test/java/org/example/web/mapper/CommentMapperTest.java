package org.example.web.mapper;

import org.example.web.entity.Comment;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest // Use SpringBootTest for full application context
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestPropertySource(locations = "classpath:application.yml")
class CommentMapperTest {

  @Autowired
  private CommentMapper commentMapper;

  @Test
  void saveOrUpdate() {
    // Create a new comment
    Comment comment = new Comment();
    comment.setComentid(1L); // Example ID
    comment.setContent("This is a test comment");
    comment.setArticleid(1L); // Example article ID

    // Save or update the comment
    commentMapper.saveOrUpdate(comment);

    // Optionally, assert something to verify the save/update was successful
    // For example, you might fetch the comment from DB and assert its content
    // Here, you could add assertions if needed
  }

  @Test
  void findByArticleId() {
    // Find comments by a specific article ID
    Long articleId = 123L; // Example article ID

    // Call the method to find comments
    var comments = commentMapper.findByArticleId(articleId);

    // Assert that the list of comments is not null and contains at least one comment
    assertNotNull(comments);
    assertTrue(comments.isEmpty());

    // Optionally, you can assert specific details of the retrieved comment(s)
  }

  @Test
  void getFirstNonExistingId() {
    // Call the method to get the first non-existing comment ID
    long firstNonExistingId = commentMapper.getFirstNonExistingId();

    // Assert that the firstNonExistingId is greater than 0, assuming IDs are positive
    assertTrue(firstNonExistingId > 0);
  }
}
