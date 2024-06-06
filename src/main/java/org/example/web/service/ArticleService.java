package org.example.web.service;

import org.example.web.entity.Article;
import org.example.web.entity.Comment;
import org.example.web.mapper.ArticleMapper;
import org.example.web.mapper.CategoryMapper;
import org.example.web.mapper.CommentMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;

import java.util.Arrays;
import java.util.List;

@Component
public class ArticleService{
    @Autowired
    private ArticleMapper articleMapper;
    @Autowired
    private CategoryMapper categoryMapper;
    @Autowired
    private CommentMapper contentMapper;

    public List<Article> findBy(String method, String content, Model model) {
        if (method.equals("category")) {
            Long userid = (Long) model.getAttribute("userid");
            List<String> names = Arrays.asList(content.split("-"));
            Long categoryid =  categoryMapper.getCategoryIdByNames(names);
            List articles = articleMapper.getArticlesByCategory(categoryid,userid);
            return articles;
        }
        else if (method.equals("id")) {
            Long userid = (Long) model.getAttribute("userid");
            Long articleId = Long.parseLong(content);
            return articleMapper.findById(articleId,userid);
        }
        else if (method.equals("share")) {
            return articleMapper.findByShare();
        }
        else if (method.equals("title")) {
            Long userid = (Long) model.getAttribute("userid");
            return articleMapper.findByTitle(content,userid);
        }
        return articleMapper.findBy(method, content, model);
    }

    public Long getFirstNonExistingId() {
        return articleMapper.getFirstNonExistingId();
    }

    public void insertArticle(Article article) {
        articleMapper.insertArticle(article);
    }

    public List<Comment> findCommentByArticleId(Long articleId) {
        return contentMapper.findByArticleId(articleId);
    }

    public void insertComment(String content, Long articleId) {
        Long commentId = contentMapper.getFirstNonExistingId();
        Comment comment = new Comment();
        comment.setContent(content);
        comment.setArticleid(articleId);
        comment.setComentid(commentId);
        contentMapper.saveOrUpdate(comment);
    }
}
