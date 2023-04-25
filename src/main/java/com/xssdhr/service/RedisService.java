package com.xssdhr.service;

/**
 * @author xssdhr
 * @10/4/2023 下午5:55
 * @Company 南京点子跳跃传媒有限公司
 */
public interface RedisService {
    /**
     * 存储数据
     * @param key
     * @param value
     */
    void set(String key, String value);


    /**
     * 获取数据
     * @param key
     * @return
     */
    String get(String key);

    /**
     * 设置超期时间
     * @param key
     * @param expire
     * @return
     */
    boolean expire(String key, long expire);

    /**
     * 删除数据
     * @param key
     */
    void remove(String key);

    /**
     * 自增操作
     * @param key
     * @param delta 自增步长
     * @return
     */
    Long increment(String key, long delta);
}
