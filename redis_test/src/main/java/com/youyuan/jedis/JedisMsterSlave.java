package com.youyuan.jedis;

import redis.clients.jedis.Jedis;

/**
 * @author zhangyu
 * @version 1.0
 * @description ����ʵ�����Ӹ��ƶ�д����
 * @date 2018/11/21 20:55
 */
public class JedisMsterSlave {
    public static void main(String[] args) {
        Jedis jedis_M=new Jedis("192.168.32.131",6381);
        Jedis jedis_S=new Jedis("192.168.32.131",6379);
        //����slave����Ӳ�����
        jedis_S.slaveof("192.168.32.131",6381);
        jedis_M.set("class","beijing");
        String classStr=jedis_S.get("class");
        System.out.println(classStr);
    }
}
