package com.youyuan.jedis;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.Tuple;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author zhangyu
 * @version 1.0
 * @description jedis API����
 * @date 2018/11/21 16:13
 */
public class TestAPI {
    public static void main(String[] args) {
        Jedis jedis=new Jedis("192.168.32.131",6381);
        jedis.flushDB();
        //String���Ͳ���
        jedis.set("k1","v1");
        jedis.set("k2","v2");
        jedis.set("k3","v3");
        //key�Ĳ���
        jedis.keys("*");
        //List���Ͳ���
        jedis.lpush("list01","1","2","3","5","6","7","8","9");
        List<String> list= jedis.lrange("list01",0,-1);
        System.out.println(list);
        System.out.println(jedis.type("list01"));
        //Set���Ͳ���
        jedis.sadd("set01","a","b","c","d","e","f");
        System.out.println(jedis.smembers("set01"));
        //Hash���Ͳ���
        jedis.hset("hash01","company","������Ե��������Ƽ����޹�˾");
        System.out.println(jedis.hget("hash01","company"));
        //ZSet���Ͳ���
        Map<String,Double> map=new HashMap<String, Double>();
        map.put("A",91.2);
        map.put("B",86.9);
        map.put("C",102.8);
        map.put("D",99.7);
        map.put("E",89.2);
        map.put("F",98.8);
        jedis.zadd("zset01",map);
        Set<Tuple> tuples=jedis.zrangeWithScores("zset01",0,-1);
        for (Tuple tuple:tuples){
            System.out.println(tuple.getElement()+"����"+tuple.getScore());
        }
    }
}
