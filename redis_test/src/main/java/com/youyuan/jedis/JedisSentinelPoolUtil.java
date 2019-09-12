package com.youyuan.jedis;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisSentinelPool;

import java.util.HashSet;
import java.util.Set;

/**
 * @author zhangyu
 * @version 1.0
 * @description jedis连接哨兵模式
 * @date 2018/11/21 21:56
 */
public class JedisSentinelPoolUtil {
    //设置哨兵连接池
    private static volatile JedisSentinelPool jedisSentinelPool=null;

    private JedisSentinelPoolUtil(){}

    /**
     * 获取jedis哨兵连接池
     * @return 返回哨兵连接
     */
    public static JedisSentinelPool getJedisSentinelPool(){
        if (jedisSentinelPool!=null){
            return jedisSentinelPool;
        }
        if (jedisSentinelPool==null){
            synchronized (JedisSentinelPoolUtil.class){
                if (jedisSentinelPool==null){
                    Set<String> sentinels = new HashSet<String>();
                    String hostAndPort1 = "192.168.32.131:26379";
                    sentinels.add(hostAndPort1);
                    String clusterName = "host6379";

                    //jedispool连接池的配置信息
                    GenericObjectPoolConfig poolConfig=new GenericObjectPoolConfig();
                    //连接池最大创建连接数
                    poolConfig.setMaxTotal(1000);
                    //设置连接池最多有多少个连接是空闲的
                    poolConfig.setMaxIdle(32);
                    //设置连接池最少有多少个连接是空闲的
                    poolConfig.setMinIdle(10);
                    //设置连接超时等待时间,单位秒
                    poolConfig.setMaxWaitMillis(100*1000);
                    //设置连接拿到后是否测试连通性,true测试
                    poolConfig.setTestOnBorrow(true);
                    jedisSentinelPool=new JedisSentinelPool(clusterName,sentinels,poolConfig);
                }
            }
        }
        return jedisSentinelPool;
    }

    /**
     * 将连接放回连接池
     * @param jedisSentinelPool 哨兵连接池
     * @param jedis 连接
     */
    public static void close(JedisSentinelPool jedisSentinelPool, Jedis jedis){
        if (jedis!=null){
            if (jedisSentinelPool==null){
                jedisSentinelPool=getJedisSentinelPool();
            }
            jedisSentinelPool.returnResource(jedis);
        }
    }
}
