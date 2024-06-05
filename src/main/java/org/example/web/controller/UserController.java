package org.example.web.controller;
import org.example.web.entity.User;
import org.example.web.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserMapper userMapper;

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public String login(@RequestParam("username") String username,
                        @RequestParam("password") String password) {
        // 这里可以加入验证逻辑，比如检查用户名密码是否正确
        User user = userMapper.findByUsernameAndPassword(username,password);
        if (user != null) {
            return "redirect:/user/success.html?username=" + user.getId();
        }
        else{
            return "redirect:/user/login.html?errmsg=no user found";
        }
    }
    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public String register(@RequestParam("username") String username,
                           @RequestParam("password") String password) {
        User user = userMapper.findByUsername(username);
        if (password == null || password.length() < 6) {
            return "redirect:/user/register.html?errmsg=no password";
        }
        else if (user != null) {
            return "redirect:/user/register.html?errmsg=user already exists";
        }
        else{
            long id = userMapper.getFirstNonExistingId();
            userMapper.insertUser(new User(id,username,password,1));
            return "redirect:/user/login.html?errmsg=register success";
        }
    }
}


