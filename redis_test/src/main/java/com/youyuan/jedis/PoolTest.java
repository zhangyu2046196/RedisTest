package com.youyuan.jedis;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

/**
 * @author zhangyu
 * @version 1.0
 * @description jedis连接池测试
 * @date 2018/11/21 21:36
 */
public class PoolTest {
    public static void main(String[] args) {
        JedisPool jedisPool=JedisPoolUtil.getInstance();
        Jedis jedis=null;
        try {
            jedis=jedisPool.getResource();
            jedis.set("pool","连接池");
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            JedisPoolUtil.relace(jedisPool,jedis);
        }
    }
}
