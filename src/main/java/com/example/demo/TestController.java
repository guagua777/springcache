package com.example.demo;


import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
public class TestController {

    @Resource
    private BeanService cacheService;

    @RequestMapping("/test")
    public Department getDep(int id, String name){
        final Object conCacheManager = ApplicationContextUtils.getBean("conCacheManager");
        System.out.println(conCacheManager);
        Department de = cacheService.findById(id, name);
        return de;
    }


}
