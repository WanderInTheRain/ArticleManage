package org.example.web.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import org.example.web.entity.Category;
import org.example.web.tools.CategoryTree;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import java.io.IOException;
import java.nio.file.Files;
import org.example.web.entity.Article;
import org.example.web.entity.Comment;
import org.example.web.entity.User;
import org.example.web.service.ArticleService;
import org.example.web.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jackson.JsonMixinModule;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
@CrossOrigin(origins = "https://frp-fun.top:56561") // 允许来自8080端口的请求
@RequestMapping("/article")
public class ArticleController {
    @Autowired
    private ArticleService articleService;
    @Autowired
    private JsonMixinModule jsonMixinModule;
    @Autowired
    private UserService userService;

    @PostMapping("/imgsave")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> handleFileUpload(@RequestParam("image") MultipartFile file) {
        String message;
        String UPLOADED_FOLDER = "./src/main/resources/image/";
        Map<String, Object> response = new HashMap<>();
        try {
            // 获取上传文件的字节流
            byte[] bytes = file.getBytes();
            // 构建文件路径
            Path path = Paths.get(UPLOADED_FOLDER + file.getOriginalFilename());
            // 将文件写入指定路径
            // 创建目录（如果不存在）
            Files.createDirectories(path.getParent());

            // 将文件写入指定路径
            Files.write(path, bytes);

            String imageUrl = "https://frp-fun.top:52702/article/image/" + file.getOriginalFilename(); // Assuming you have a mapping to serve static images
            response.put("url", imageUrl);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception e) {
            response.put("error", "File upload failed: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @GetMapping("/image/{imageName}")
    public ResponseEntity<Resource> getImage(@PathVariable String imageName) throws IOException {
        String IMAGE_FOLDER = "D:\\desktop\\study\\3s\\software\\webdemo\\src\\main\\resources\\image\\";

        Resource resource = new FileSystemResource(IMAGE_FOLDER + imageName);

        // 检查文件是否存在并可读
        if (!resource.exists() || !resource.isReadable()) {
            // 返回适当的响应，比如404
            return ResponseEntity.notFound().build();
        }

        // 设置响应类型为图片类型
        MediaType mediaType = MediaType.IMAGE_JPEG; // 默认为JPEG
        if (imageName.toLowerCase().endsWith(".png")) {
            mediaType = MediaType.IMAGE_PNG;
        } else if (imageName.toLowerCase().endsWith(".gif")) {
            mediaType = MediaType.IMAGE_GIF;
        }

        return ResponseEntity.ok()
            .contentType(mediaType)
            .body(resource);
    }

    @PostMapping("/myblogsearch")// 允许来自8080端口的请求
    @ResponseBody
    public List<Article> searchmyArticle(String userid) {
        List<Article> articlesTitles = articleService.findMyBlog(userid);
        return articlesTitles;
    }

    @PostMapping("/myblogsearchbytitle")// 允许来自8080端口的请求
    @ResponseBody
    public List<Article> searchmyArticleByTitle(String userid,String title) {
        List<Article> articles = articleService.findMyBlogByTitle(userid, title);
        return articles;
    }

    @PostMapping("/getmyblog")// 允许来自8080端口的请求
    @ResponseBody
    public Article getMyblog(String articleid) {
        Article article = articleService.findMyBlogById(articleid);
        return article;
    }

    @PostMapping("/collectblog")
    @ResponseBody
    public List<Article> collectArticles(Long userid) {
        List<Article> articles = articleService.findCollect(userid);
        return articles;
    }

    @PostMapping("/setcollectblog")
    @ResponseBody
    public String setcollectArticles(Long userid,String articleid) {
        articleService.setCollect(userid, Long.parseLong(articleid));
        return "success";
    }

    @PostMapping("/deletecollectblog")
    @ResponseBody
    public String deletecollectArticles(Long userid,String articleid) {
        articleService.deleteCollect(userid, Long.parseLong(articleid));
        return "success";
    }

    @PostMapping("/getcollectblogs")
    @ResponseBody
    public String getcollectArticles(String userid,Long articleid) {
        String ans = articleService.getCollect(Long.parseLong(userid), articleid);
        return ans;
    }

    @PostMapping("/shareblog")
    @ResponseBody
    public List<Article> shareArticles() {
        List<Article> articles = articleService.findShare();
        return articles;
    }

    @PostMapping("/shareblogtitle")
    @ResponseBody
    public List<Article> shareArticlestitle(String title) {
        List<Article> articles = articleService.findShareBytitle(title);
        return articles;
    }

    @PostMapping("/getcomment")
    @ResponseBody
    public List<Comment> getComments(Long articleId) {
        // 重新加载文章和评论信息
        List<Comment> comments = articleService.findCommentByArticleId(articleId);

        return comments; // 返回评论页面的模板名称
    }

    @PostMapping("/getcategoryname")
    @ResponseBody
    public String getCategoryName(Long categoryId) {
        // 重新加载文章和评论信息
        String categoryName = articleService.findCategoryNameById(categoryId);

        return categoryName; // 返回评论页面的模板名称
    }

    @PostMapping("/getcategorytree")
    @ResponseBody
    public String getcategorytree() throws JsonProcessingException {
        List<Category> categories = articleService.getAllCategories();
        String jsontree = CategoryTree.categoryTreeToString(categories);
        return jsontree;
    }

    @PostMapping("/createarticle")
    @ResponseBody // This tells Spring to return the response as JSON
    public String createArticle(Long authorid, String title,
        String content, Long share, String categoryid,
        String key) {
        Long id = articleService.getFirstNonExistingId();

        LocalDateTime now = LocalDateTime.now();

        // 定义MySQL DATETIME格式
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        // 格式化当前日期时间为MySQL DATETIME格式
        String formattedDateTime = now.format(formatter);
        System.out.println("Formatted DateTime: " + formattedDateTime);

        // 将LocalDateTime转换为Date
        Date date = Date.from(now.atZone(ZoneId.systemDefault()).toInstant());

        Article article = new Article();
        article.setId(id);
        article.setTitle(title);
        article.setContent(content);
        article.setAuthorid(authorid);
        article.setDate(date);
        article.setKey(key);
        article.setCategoryid(Long.parseLong(categoryid));
        article.setStars(0L);
        article.setShare(share);

        articleService.insertArticle(article);
        return "success";
    }

    @PostMapping("/savearticle")
    @ResponseBody // This tells Spring to return the response as JSON
    public String saveArticle(Long articleid, Long authorid, String title,
        String content, Long share, String categoryid,
        String key) {
        Long id = articleid;

        LocalDateTime now = LocalDateTime.now();

        // 定义MySQL DATETIME格式
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        // 格式化当前日期时间为MySQL DATETIME格式
        String formattedDateTime = now.format(formatter);
        System.out.println("Formatted DateTime: " + formattedDateTime);

        // 将LocalDateTime转换为Date
        Date date = Date.from(now.atZone(ZoneId.systemDefault()).toInstant());

        Article article = new Article();
        article.setId(id);
        article.setTitle(title);
        article.setContent(content);
        article.setAuthorid(authorid);
        article.setDate(date);
        article.setKey(key);
        article.setCategoryid(Long.parseLong(categoryid));
        article.setStars(0L);
        article.setShare(share);

        articleService.insertArticle(article);
        return "success";
    }

    @PostMapping("/deletearticle")
    @ResponseBody // This tells Spring to return the response as JSON
    public String deleteArticle(Long articleid){
        articleService.deleteArticleById(articleid);
        return "success";
    }

    @PostMapping("/advancedsearchshare")
    @CrossOrigin(origins = "frp-fun.top:56561") // 允许来自8080端口的请求
    @ResponseBody
    public List<Article> advancedSearch(@RequestParam(required = false) String author,
    @RequestParam(required = false) String title,
    @RequestParam(required = false) String startdate,
    @RequestParam(required = false) String enddate,
    @RequestParam(required = false) Long categoryid,String searchType,String userid,String content) {
        List<Article> articles = articleService.advancedSearch(userid,author,title,content,startdate,enddate,categoryid,searchType);
        return articles;
    }

    @PostMapping("/addcategory")
    @ResponseBody
    public Long addCategory(String name,Long parentid){
        Long categoryid = articleService.createCategory(name,parentid);
        return categoryid;
    }

    @PostMapping("/savecomment")
    @ResponseBody
    public String savecomment(
        @RequestParam("articleId") Long articleId,
        @RequestParam("content") String content) {

        articleService.insertComment(content,articleId);

        return "save success"; // 返回评论页面的模板名称
    }

    @PostMapping("/getblogbyid")
    @ResponseBody
    public Article getblogbyid(
        @RequestParam("articleId") Long articleId) {

        Article article = articleService.findMyBlogById(articleId.toString());

        return article; // 返回评论页面的模板名称
    }
}
