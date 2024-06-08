package org.example.web.controller;
import org.example.web.entity.Article;
import org.example.web.entity.User;
import org.example.web.mapper.UserMapper;
import org.example.web.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;

@Controller
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;
    @GetMapping("/loginweb")
    public String loginweb(Model model) {
        // 这里可以加入验证逻辑，比如检查用户名
        model.addAttribute("logerr","");
        return "login";
    }
    @GetMapping("/registerweb")
    public String registerweb(Model model) {
        // 这里可以加入验证逻辑，比如检查用户名
        model.addAttribute("regerr","");
        return "register";
    }

    @PostMapping("/edit")
    public String edit(String username,Long userid,Model model) {
        // 这里可以加入验证逻辑，比如检查用户名
        model.addAttribute("userid",userid);
        model.addAttribute("username",username);
        model.addAttribute("usereditmsg","");
        return "useredit";
    }

    @PostMapping("/save")
    public String save(String username,Long userid, String password,String permissions,  Model model) {
        // 这里可以加入验证逻辑，比如检查用户名
        Integer permit;
        if (permissions.equals("readonly")) {
            permit = 0;
        }
        else{
            permit = 1;
        }
        userService.insertUser(new User(userid,username,password,permit));
        model.addAttribute("userid",userid);
        model.addAttribute("username",username);
        model.addAttribute("usereditmsg","修改成功");
        return "useredit";
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public String login(@RequestParam("username") String username,
                        @RequestParam("password") String password,Model model) {
        // 这里可以加入验证逻辑，比如检查用户名密码是否正确
        User user = userService.findByUsernameAndPassword(username,password);
        if (user != null) {
            model.addAttribute("userid",user.getId());
            model.addAttribute("username",user.getUsername());
            return "success";
        }
        else{
            model.addAttribute("logerr","no user found");
            return "login";
        }
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public String register(@RequestParam("username") String username,
                           @RequestParam("password") String password,Model model) {
        User user = userService.findByUsername(username);
        if (password == null || password.length() < 6) {
            model.addAttribute("regerr","no password");
            return "register";
        }
        else if (user != null) {
            model.addAttribute("regerr","user already exists");
            return "register";
        }
        else{
            long id = userService.getFirstNonExistingId();
            userService.insertUser(new User(id,username,password,1));
            model.addAttribute("logerr","register success");
            return "login";
        }
    }
}


