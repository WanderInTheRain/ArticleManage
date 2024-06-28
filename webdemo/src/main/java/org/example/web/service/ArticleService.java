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

    public ArticleService(ArticleMapper articleMapper, CategoryMapper categoryMapper, CommentMapper commentMapper) {
        this.articleMapper = articleMapper;
        this.categoryMapper = categoryMapper;
        this.contentMapper = commentMapper;
    }

    public Long createCategory(String categoryName,Long parentId) {
        Category category = new Category();
        Long categoryid =  categoryMapper.getFirstNonExistingId();
        category.setCategoryid(categoryid);
        category.setName(categoryName);
        category.setParentid(parentId); // Set default parent ID, change if needed
        categoryMapper.insertCategory(category);
        return category.getCategoryid();
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

    public List<Article> findMyBlog(String userid) {
        return articleMapper.getArticlesByUserid(userid);
    }

    public Article findMyBlogById(String articleid) {
        Long id = Long.parseLong(articleid);
        List<Article> articles = articleMapper.findById(id);
        if (articles.size() > 0) {
            return articles.get(0);
        }
        else{
            return null;
        }
    }

    public List<Article> findMyBlogByTitle(String userid, String title) {
        return articleMapper.getArticlesByUseridAndTitle(userid, title);
    }

    public List<Category> getAllCategories(){
        return categoryMapper.getAllCategories();
    }

    public String findCategoryNameById(Long categoryId) {
        return categoryMapper.findCategoryNameById(categoryId);
    }

    public List<Article> advancedSearch(String userid, String author, String title,String content, String startdate,
        String enddate, Long categoryid, String searchType) {
        if (searchType.equals("sharedArticles")) {
            List<Long> subCategory = categoryMapper.getAllSubcategoryIds(categoryid);
            subCategory.add(categoryid);
            return articleMapper.advancedSearchShare(author,title,content,startdate,enddate,subCategory);
        }
        else if (searchType.equals("myArticles")) {
            List<Long> subCategory = categoryMapper.getAllSubcategoryIds(categoryid);
            subCategory.add(categoryid);
            return articleMapper.advancedSearchMy(userid,title,content,startdate,enddate,subCategory);
        }
        else{
            return null;
        }
    }

    public void deleteArticleById(Long articleid) {
        articleMapper.deleteById(articleid);
    }

    public List<Article> findShare() {
        return articleMapper.findByShare();
    }

    public List<Article> findShareBytitle(String title) {
        return articleMapper.getArticlesByShareAndTitle(title);
    }

    public List<Article> findCollect(Long userid) {
        return articleMapper.getArticlesByUseridAndCollect(userid);
    }

    public void setCollect(Long userid, Long articleid) {
        articleMapper.insertCollect(userid,articleid);
        articleMapper.incrementStarsByArticleId(articleid);
    }

    public void deleteCollect(Long userid, Long articleid) {
        articleMapper.deleteCollect(userid,articleid);
        articleMapper.decrementStarsByArticleId(articleid);
    }

    public String getCollect(Long userid, Long articleid) {
        List<Article> articles = articleMapper.getArticlesByUseridAndCollectAndArticleid(userid,articleid);
        return !articles.isEmpty() ?"success":"fail";
    }
}
