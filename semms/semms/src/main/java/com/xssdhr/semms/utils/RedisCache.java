package com.xssdhr.semms.utils;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundSetOperations;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.TimeUnit;

@SuppressWarnings(value = {"unchecked","rawtypes"})
@Component
public class RedisCache {

    @Autowired
    public RedisTemplate redisTemplate;

    /**
     * 缓存基本的对象，Integer、String、实体类等
     * @param key 缓存的键值
     * @param value 缓存的值
     * @param <T>
     */
    public <T> void setCacheObject(final String key,final T value){
        redisTemplate.opsForValue().set(key,value);
    }

    /**
     * 缓存基本的对象，Integer、String、实体类等
     * @param key 缓存的键值
     * @param value 缓存的值
     * @param timeout 时间
     * @param timeUnit  时间颗粒度
     * @param <T>
     */
    public <T> void setCacheObject(final String key, final T value, final Integer timeout, final TimeUnit timeUnit){
        redisTemplate.opsForValue().set(key,value,timeout,timeUnit);
    }

    /**
     * 设置有效时间
     * @param key   redis键
     * @param timeout   超时时间
     * @param unit 时间单位
     * @return true=设置成功 false=设置失败
     */
    public boolean expire(final String key,final long timeout,final TimeUnit unit){
        return redisTemplate.expire(key,timeout,unit);
    }

    /**
     * 获得缓存的基本对象
     * @param key   缓存键值
     * @param <T>   缓存键值对应的数据
     * @return
     */
    public <T> T getCacheObject(final String key){
        ValueOperations<String,T> operations = redisTemplate.opsForValue();
        return operations.get(key);
    }

    /**
     * 删除单个对象
     * @param key
     * @return
     */
    public boolean deleteObject(final String key){
        return redisTemplate.delete(key);
    }

    /**
     * 缓存List数据
     * @param key 缓存的键值
     * @param dataList  待缓存的List数据
     * @param <T>
     * @return
     */
    public <T> long setCacheList(final String key, final List<T> dataList){
        Long count = redisTemplate.opsForList().rightPushAll(key,dataList);
        return count == null ? 0 : count;
    }

    /**
     * 获得缓存的List对象
     * @param key   缓存的键值
     * @param <T>
     * @return 缓存键值对应的数据
     */
    public <T> List<T> getCacheList(final String key){
        return redisTemplate.opsForList().range(key,0,-1);
    }

    /**
     * 缓存Set
     * @param key   缓存键值
     * @param dataSet   缓存的数据
     * @param <T> 缓存数据的对象
     * @return
     */
    public <T> BoundSetOperations<String,T> setCacheSet(final String key,final Set<T> dataSet){
        BoundSetOperations<String,T> setOperations = redisTemplate.boundSetOps(key);
        Iterator<T> it = dataSet.iterator();

        while (it.hasNext()){
            setOperations.add(it.next());
        }
        return setOperations;
    }

    /**
     * 获得缓存的set
     * @param key
     * @param <T>
     * @return
     */
    public <T> Set<T> getCacheSet(final String key){
        return redisTemplate.opsForSet().members(key);
    }

    /**
     * 缓存Map
     * @param key
     * @param dataMap
     * @param <T>
     */
    public  <T> void setCacheMap(final String key,final Map<String,T> dataMap){
        if(dataMap != null){
            redisTemplate.opsForHash().putAll(key,dataMap);
        }
    }

    /**
     * 获得缓存的Map
     * @param key
     * @param <T>
     * @return
     */
    public <T> Map<String,T> getCacheMap(final String key){
        return redisTemplate.opsForHash().entries(key);
    }

    /**
     * 让hash中存入数据
     * @param key
     * @param hKey
     * @param value
     * @param <T>
     */
    public <T> void setCacheMapValue(final String key,final String hKey,final T value){
        redisTemplate.opsForHash().put(key,hKey,value);
    }

    /**
     * 获取hash中的数据
     * @param key   redis键
     * @param hkey  hash键
     * @param <T>   hash中的对象
     * @return
     */
    public <T> T getCacheMapValue(final String key,final String hkey){
        HashOperations<String,String,T> opsForHash = redisTemplate.opsForHash();
        return opsForHash.get(key,hkey);
    }

    /**
     * 删除hash中的数据
     * @param key
     * @param hkey
     */
    public void delCacheMapValue(final String key,final String hkey){
        HashOperations hashOperations = redisTemplate.opsForHash();
        hashOperations.delete(key,hkey);
    }

    /**
     * 获取多个hash中的数据
     * @param key   redis键
     * @param hkeys hash键集合
     * @param <T>
     * @return  hash对象集合
     */
    public <T> List<T> getMultiCacheMapValue(final String key,final Collection<Object> hkeys){
        return redisTemplate.opsForHash().multiGet(key,hkeys);
    }

    /**
     * 获得缓存的基本对象操作
     * @param pattern   字符串前缀
     * @return  对象列表
     */
    public Collection<String> keys(final String pattern){
        return redisTemplate.keys(pattern);
    }
}
