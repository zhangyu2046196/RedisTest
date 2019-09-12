package com.youyuan.jedis;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisSentinelPool;

import java.util.HashSet;
import java.util.Set;

/**
 * @author zhangyu
 * @version 1.0
 * @description jedis�����ڱ�ģʽ
 * @date 2018/11/21 21:56
 */
public class JedisSentinelPoolUtil {
    //�����ڱ����ӳ�
    private static volatile JedisSentinelPool jedisSentinelPool=null;

    private JedisSentinelPoolUtil(){}

    /**
     * ��ȡjedis�ڱ����ӳ�
     * @return �����ڱ�����
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

                    //jedispool���ӳص�������Ϣ
                    GenericObjectPoolConfig poolConfig=new GenericObjectPoolConfig();
                    //���ӳ���󴴽�������
                    poolConfig.setMaxTotal(1000);
                    //�������ӳ�����ж��ٸ������ǿ��е�
                    poolConfig.setMaxIdle(32);
                    //�������ӳ������ж��ٸ������ǿ��е�
                    poolConfig.setMinIdle(10);
                    //�������ӳ�ʱ�ȴ�ʱ��,��λ��
                    poolConfig.setMaxWaitMillis(100*1000);
                    //���������õ����Ƿ������ͨ��,true����
                    poolConfig.setTestOnBorrow(true);
                    jedisSentinelPool=new JedisSentinelPool(clusterName,sentinels,poolConfig);
                }
            }
        }
        return jedisSentinelPool;
    }

    /**
     * �����ӷŻ����ӳ�
     * @param jedisSentinelPool �ڱ����ӳ�
     * @param jedis ����
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
