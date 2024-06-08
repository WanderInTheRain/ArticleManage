package org.example.web.service;

import org.example.web.entity.Article;
import org.example.web.entity.Comment;
import org.example.web.entity.Category;
import org.example.web.mapper.ArticleMapper;
import org.example.web.mapper.CategoryMapper;
import org.example.web.mapper.CommentMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;

import java.util.ArrayList;
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

    public Long createCategory(String categoryName,Long parentId) {
        Category category = new Category();
        Long categoryid =  categoryMapper.getFirstNonExistingId();
        category.setCategoryid(categoryid);
        category.setName(categoryName);
        category.setParentid(parentId); // Set default parent ID, change if needed
        categoryMapper.insertCategory(category);
        return category.getCategoryid();
    }

    public Long findCategoryIdByName(String categoryName) {
        List<String> names = Arrays.asList(categoryName.split("-"));
        return categoryMapper.getCategoryIdByNames(names);
    }

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
            return articleMapper.findByIdAndAuthorId(articleId,userid);
        }
        else if (method.equals("idshare")) {
            Long articleId = Long.parseLong(content);
            return articleMapper.findById(articleId);
        }
        else if (method.equals("share")) {
            return articleMapper.findByShare();
        }
        else if (method.equals("title")) {
            Long userid = (Long) model.getAttribute("userid");
            return articleMapper.findByTitleAndAuthorId(content,userid);
        }
        else if (method.equals("all")) {
            Long userid = (Long) model.getAttribute("userid");
            return articleMapper.findByAuthorId(userid);
        }
        else if (method.equals("content")) {
            Long userid = (Long) model.getAttribute("userid");
            List<Article> articles = articleMapper.findByAuthorId(userid);
            List<Article> newarticles = new ArrayList<>();
            for (Article article : articles) {
                if (article.getContent().contains(content)) {
                    newarticles.add(article);
                }
            }
            return newarticles;
        }
        else if (method.equals("key")) {
            Long userid = (Long) model.getAttribute("userid");
            List<Article> articles = articleMapper.findByAuthorId(userid);
            List<Article> newarticles = new ArrayList<>();
            for (Article article : articles) {
                if (article.getKey().contains(content)) {
                    newarticles.add(article);
                }
            }
            return newarticles;
        }
        return articleMapper.findByIdAndAuthorId(0L,0L);
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

    public void deleteArticleByUseridAndId(Long userid, Long articleId) {
        articleMapper.deleteByIdAndAuthorId(articleId,userid);
    }
}
