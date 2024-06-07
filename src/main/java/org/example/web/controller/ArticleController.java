package org.example.web.controller;

import org.example.web.entity.Article;
import org.example.web.entity.Comment;
import org.example.web.entity.User;
import org.example.web.mapper.ArticleMapper;
import org.example.web.mapper.UserMapper;
import org.example.web.service.ArticleService;
import org.example.web.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jackson.JsonMixinModule;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/article")
public class ArticleController {
    @Autowired
    private ArticleService articleService;
    @Autowired
    private JsonMixinModule jsonMixinModule;
    @Autowired
    private UserService userService;

    @PostMapping("/home")
    public String home(@RequestParam("username") String username,
                        @RequestParam("userid") Long userid,Model model) {
            model.addAttribute("userid",userid);
            model.addAttribute("username",username);
            return "success";
    }

    @PostMapping("/inedit")
    public String inEdit(Long userid, String username,Model model) {

        model.addAttribute("article", new Article());
        model.addAttribute("userid", userid);
        model.addAttribute("username", username);

        model.addAttribute("editmsg", "");
        return "edit";
    }

    @PostMapping("/save")
    public String saveArticle(Long authorid, Long id, String title,
                              String content,String username,
                              String share, String category,
                              String key, Model model) {
        category = "all-" + category;
        // 调用ArticleService更新文章信息
        boolean newArticle = false;
        if (id == null) {
            id = articleService.getFirstNonExistingId();
            newArticle = true;
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
        if (category == null) {
            article.setCategoryid(0L);
        }
        else{
            Long Categoryid = articleService.findCategoryIdByName(category);
            if (Categoryid != null) {
                article.setCategoryid(Categoryid);
            }
            else{
                List<String> names = Arrays.asList(category.split("-"));
                String tmp = new String();
                int i;
                for (i = 0; i < names.size(); i++) {
                    if (i != 0){
                        tmp = tmp + "-" + names.get(i);
                    }
                    else{
                        tmp = tmp + names.get(i);
                    }
                    if (articleService.findCategoryIdByName(tmp) != null) {
                        break;
                    }
                }
                for (int j = i+1; j < names.size(); j++) {
                    articleService.createCategory(names.get(j),articleService.findCategoryIdByName(tmp));
                    tmp = tmp + "-"  + names.get(j);
                }
                Long categoryid = articleService.findCategoryIdByName(tmp);
                article.setCategoryid(categoryid);
            }
        }
        article.setKey(key);
        if (share.equals("public")){
            article.setShare(1L);
        }
        else{
            article.setShare(0L);
        }

        User user = userService.findByUsername(username);

        if (newArticle) {
            if (user.getAuthority() >= 1) {
                articleService.insertArticle(article);
                model.addAttribute("editmsg", "修改成功");
            } else {
                model.addAttribute("editmsg", "权限不足");
            }
        }
        else{
            List<Article> oldarticles = articleService.findBy("idshare",id.toString(),model);
            Article oldarticle = oldarticles.get(0);
            if (oldarticle.getAuthorid() == authorid){
                articleService.insertArticle(article);
                model.addAttribute("editmsg", "修改成功");
            }
            else{
                model.addAttribute("editmsg", "不是作者");
            }
        }

        model.addAttribute("article", article);
        model.addAttribute("userid", authorid);
        model.addAttribute("username", username);
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
        List<Article> articles = articleService.findBy("idshare",articleId.toString(),model);

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
        List<Article> articles = articleService.findBy("idshare", articleId.toString(),model);
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

        List<Article> articles = articleService.findBy("idshare", articleId.toString(),model);
        Article article = articles.get(0);
        List<Comment> comments = articleService.findCommentByArticleId(articleId);

        // 将文章和评论信息传递到视图中
        model.addAttribute("articleid", articleId);
        model.addAttribute("article", article);
        model.addAttribute("comments", comments);

        return "comment"; // 返回评论页面的模板名称
    }

}
