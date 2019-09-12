package com.youyuan.jedis;

import redis.clients.jedis.Jedis;

/**
 * @author zhangyu
 * @version 1.0
 * @description 测试redis的连通性
 * @date 2018/11/21 16:00
 */
public class JedisTest {
    public static void main(String[] args) {
        Jedis jedis=new Jedis("192.168.32.131",6379);
        System.out.println(jedis.ping());
    }
}
