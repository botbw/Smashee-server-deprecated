package server.controllers;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import server.mappers.UserMapper;
import server.pojos.User;

import java.util.List;

@RestController
@Component
@RequestMapping("/user")
public class UserController {
    UserMapper userMapper;

    public UserController(@Autowired UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    @RequestMapping("getAll")
    public List<User> getAll() {
        return userMapper.selectList(null);
    }

    @RequestMapping("getUser/{userId}")
    public User getUser(@PathVariable int userId) {
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("uid", userId);
    return userMapper.selectById(wrapper);
    }

    @RequestMapping("addUser/{userId}/{userName}/{password}")
    public int addUser(
            @PathVariable Integer userId,
            @PathVariable String userName,
            @PathVariable String password) {
        User user = new User(userId, userName, password);
        return userMapper.insert(user);
    }

    @RequestMapping("deleteUser/{userId}")
    public int deleteUser(@PathVariable int userId) {
        return userMapper.deleteById(userId);
    }
}
