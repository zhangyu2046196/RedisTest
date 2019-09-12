package com.youyuan.jedis;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisSentinelPool;

/**
 * @author zhangyu
 * @version 1.0
 * @description ÉÚ±øÁ¬½Ó³Ø²âÊÔ
 * @date 2018/11/21 22:04
 */
public class JedisSentinelPoolTest {
    public static void main(String[] args) {
        JedisSentinelPool jedisSentinelPool=JedisSentinelPoolUtil.getJedisSentinelPool();
        Jedis jedis=null;
        try {
            jedis=jedisSentinelPool.getResource();
            jedis.set("abc","vvv");
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            JedisSentinelPoolUtil.close(jedisSentinelPool,jedis);
        }

    }
}
