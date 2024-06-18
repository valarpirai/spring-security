package com.example.security.controller;

import com.example.security.service.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/redis")
public class RedisController {

    @Autowired
    RedisService redisService;

    @PostMapping("/add")
    public String add(@RequestBody Map<String, String> map) {
        for (var key : map.keySet()) {
            redisService.set(key, map.get(key));
        }
        return "success";
    }

    @GetMapping("/get")
    public String get(@RequestParam("key") String key) {
        return redisService.get(key);
    }

    @DeleteMapping("/delete")
    public String delete(@RequestParam("key") String key) {
        return redisService.del(key) ? "success" : "fail";
    }
}
