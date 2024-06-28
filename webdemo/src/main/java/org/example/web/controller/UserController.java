package org.example.web.controller;
import jakarta.persistence.criteria.CriteriaBuilder.In;
import org.example.web.entity.Article;
import org.example.web.entity.User;
import org.example.web.mapper.UserMapper;
import org.example.web.tools.SessionManager;
import org.example.web.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;

@Controller
@CrossOrigin(origins = "https://frp-fun.top:56561") // 允许来自8080端口的请求
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping("/saveuser")
    @CrossOrigin(origins = "frp-fun.top:56561") // 允许来自8080端口的请求
    @ResponseBody
    public String saveuser(String username,String userid,String oldpassword, String password,String permissions) {
        // 这里可以加入验证逻辑，比如检查用户名
        User user = userService.findUserById(Long.parseLong(userid));
        if (!user.getPassword().equals(oldpassword)) {
            return "fail";
        }
        int permit;
        if (permissions.equals("readonly")) {
            permit = 0;
        }
        else{
            permit = 1;
        }
        if (password.isEmpty()) {
            userService.insertUser(new User(Long.parseLong(userid), username, oldpassword, permit));
        }
        else{
            userService.insertUser(new User(Long.parseLong(userid), username, password, permit));
        }
        return "success";
    }

    @RequestMapping(value = "/logins", method = RequestMethod.POST)
    @ResponseBody
    public String logins(@RequestParam("username") String username,
        @RequestParam("password") String password) {
        // 这里可以加入验证逻辑，比如检查用户名密码是否正确
        User user = userService.findByUsernameAndPassword(username,password);
        if (user != null) {
            String sessionId;
            sessionId = SessionManager.createSession(user.getId().toString());
            return sessionId;
        }
        else{
            return "-1";
        }
    }

    @RequestMapping(value = "/logout", method = RequestMethod.POST)
    @ResponseBody
    public void loginout(String sessionId){
        // 这里可以加入验证逻辑，比如检查用户名密码是否正确
        SessionManager.deleteSession(sessionId);
    }

    @RequestMapping(value = "/test", method = RequestMethod.POST)
    @CrossOrigin(origins = "frp-fun.top:56561")
    @ResponseBody
    public String test(String sessionId){
        // 这里可以加入验证逻辑，比如检查用户名密码是否正确
        String userid = SessionManager.getSession(sessionId);
        return userid;
    }

    @RequestMapping(value = "/registers", method = RequestMethod.POST)
    @ResponseBody
    public String registers(@RequestParam("username") String username,
        @RequestParam("password") String password) {
        User user = userService.findByUsername(username);
        if (password == null || password.length() < 6) {
            return "密码过短";
        }
        else if (user != null) {
            return "用户已存在";
        }
        else{
            long id = userService.getFirstNonExistingId();
            userService.insertUser(new User(id,username,password,1));
            return "success";
        }
    }

    @RequestMapping(value = "/getusername", method = RequestMethod.POST)
    @ResponseBody
    public String getusername(String userid) {
        User user = userService.findUserById(Long.parseLong(userid));
        return user.getUsername();
    }

    @RequestMapping(value = "/getuserauth", method = RequestMethod.POST)
    @ResponseBody
    public Integer getuserauth(String userid) {
        User user = userService.findUserById(Long.parseLong(userid));
        return user.getAuthority();
    }
}


