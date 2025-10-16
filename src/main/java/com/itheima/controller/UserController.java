package com.itheima.controller;

import com.itheima.entity.User;
import com.itheima.mapper.UserMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {

    @Autowired
    private UserMapper userMapper;

    @PostMapping
    @CachePut(cacheNames = "userCache",key = "#user.id")//key的生成为cacheNames加两个冒号加key userCache::1 key的位置有很多用法
    public User save(@RequestBody User user){
        userMapper.insert(user);
        return user;
    }

    @DeleteMapping
    @CacheEvict(cacheNames = "userCache",key = "#id")
    public void deleteById(Long id){
        userMapper.deleteById(id);
    }

	@DeleteMapping("/delAll")
    @CacheEvict(cacheNames = "userCache",allEntries = true)
    public void deleteAll(){
        userMapper.deleteAll();
    }

    @GetMapping
    @Cacheable(cacheNames = "userCache",key = "#id")//先查询缓存看看有没有数据，如果有就直接返回，没有在调用方法，并放到缓存中
    public User getById(Long id){
        User user = userMapper.getById(id);
        return user;
    }

}
