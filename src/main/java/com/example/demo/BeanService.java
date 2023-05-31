package com.example.demo;

import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;


@Service
@CacheConfig(cacheManager = "conCacheManager")
public class BeanService {

    @Cacheable(cacheNames = "findById", key = "#id+'-'+#name")
    public Department findById(int id, String name) {
        System.out.println("查询的数据是ID为 "+id +" 的数据 " + name);
        if (id == 2) {
            return null;
        }
        Department d = new Department();
        d.setId(id);
        d.setName(name);
        return d;
    }


}
