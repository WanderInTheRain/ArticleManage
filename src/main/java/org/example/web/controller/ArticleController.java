package org.example.web.controller;

import org.example.web.entity.Article;
import org.example.web.entity.Comment;
import org.example.web.entity.User;
import org.example.web.mapper.ArticleMapper;
import org.example.web.mapper.UserMapper;
import org.example.web.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jackson.JsonMixinModule;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/article")
public class ArticleController {
    @Autowired
    private ArticleService articleService;
    @Autowired
    private JsonMixinModule jsonMixinModule;

    @PostMapping("/inedit")
    public String inEdit(Long userid, String username,Model model) {

        model.addAttribute("article", new Article());
        model.addAttribute("userid", userid);
        model.addAttribute("username", username);

        model.addAttribute("editmsg", "");
        return "edit";
    }

    @PostMapping("/save")
    public String saveArticle(Long authorid, Long id, String title, String content,String username,Model model) {
        // 调用ArticleService更新文章信息
        if (id == null) {
            id = articleService.getFirstNonExistingId();
        }

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

        articleService.insertArticle(article);

        model.addAttribute("article", article);
        model.addAttribute("userid", authorid);
        model.addAttribute("username", username);

        model.addAttribute("editmsg", "修改成功");
        return "edit";
    }


    @PostMapping("/searchpage")
    public String searchPage(Long userid,String username,Model model) {
        model.addAttribute("userid",userid);
        model.addAttribute("username",username);
        return "search";
    }

    @PostMapping("/search")
    public String searchArticle(Long userid,String username,String method,String methodcontent, Model model) {
        model.addAttribute("userid",userid);
        model.addAttribute("username",username);
        List<Article> articles = articleService.findBy(method,methodcontent,model);
        model.addAttribute("articles", articles);
        return "search-result";
    }

    @PostMapping("/share")
    public String shareArticle(Long userid,String username,Model model) {
        model.addAttribute("userid",userid);
        model.addAttribute("username",username);
        List<Article> articles = articleService.findBy("share","1",model);
        model.addAttribute("articles", articles);
        return "search-result";
    }

    @PostMapping("/edit")
    public String editArticle(@RequestParam("userid") Long userid,
                              @RequestParam("username") String username,
                              @RequestParam("articleId") Long articleId,
                              Model model) {
        // 在这里执行加载文章内容的操作，然后将文章内容传递到编辑页面
        // 例如，根据 articleId 从数据库加载文章内容
        model.addAttribute("userid", userid);
        model.addAttribute("username", username);
        List<Article> articles = articleService.findBy("id",articleId.toString(),model);

        // 将文章内容和用户信息传递到编辑页面
        model.addAttribute("article", articles.get(0));
        model.addAttribute("editmsg", "");
        return "edit"; // 返回编辑页面的模板名称
    }

    @PostMapping("/comment")
    public String showComments(@RequestParam("userid") Long userid,
                               @RequestParam("username") String username,
                               @RequestParam("articleId") Long articleId,
                               Model model) {
        model.addAttribute("userid", userid);
        model.addAttribute("username", username);
        // 重新加载文章和评论信息
        List<Article> articles = articleService.findBy("id", articleId.toString(),model);
        Article article = articles.get(0);
        List<Comment> comments = articleService.findCommentByArticleId(articleId);

        // 将文章和评论信息传递到视图中
        model.addAttribute("articleid", articleId);
        model.addAttribute("article", article);
        model.addAttribute("comments", comments);

        return "comment"; // 返回评论页面的模板名称
    }

    @PostMapping("/commentsave")
    public String commentsave(@RequestParam("userid") Long userid,
                               @RequestParam("username") String username,
                               @RequestParam("articleId") Long articleId,
                                @RequestParam("content") String content,
                               Model model) {
        model.addAttribute("userid", userid);
        model.addAttribute("username", username);

        articleService.insertComment(content,articleId);

        List<Article> articles = articleService.findBy("id", articleId.toString(),model);
        Article article = articles.get(0);
        List<Comment> comments = articleService.findCommentByArticleId(articleId);

        // 将文章和评论信息传递到视图中
        model.addAttribute("articleid", articleId);
        model.addAttribute("article", article);
        model.addAttribute("comments", comments);

        return "comment"; // 返回评论页面的模板名称
    }

}
