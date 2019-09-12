package com.youyuan.jedis;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisSentinelPool;

/**
 * @author zhangyu
 * @version 1.0
 * @description jedis���ӳع����࣬JedisPoolֻ����һ�ݣ��ǵ���ģʽ
 * @date 2018/11/21 21:17
 */
public class JedisPoolUtil {
    //���Ϊstatic�ڴ���ֻ��һ�ݣ�volatile���߳������޸�ʱ�ǿɼ��ģ��޸���֮������д�뵽���ڴ�
    private static volatile JedisPool jedisPool=null;

    private JedisPoolUtil(){}

    /**
     * ������ȡjedis���ӳض���
     * @return
     */
    public static JedisPool getInstance(){
        //Ϊ������������жϲ�Ϊ��ֱ�ӻ�ȡ
        if (jedisPool!=null){
            return jedisPool;
        }
        //Ϊ���ж�˫�ؼ��
        if (jedisPool==null){
            synchronized (JedisPoolUtil.class){
                if (jedisPool==null){
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
                    jedisPool=new JedisPool(poolConfig,"192.168.32.131",6381);
                }
            }
        }
        return jedisPool;
    }

    /**
     * ���������Żس���
     * @param jedisPool ���ӳ�
     * @param jedis ����
     */
    public static void relace(JedisPool jedisPool, Jedis jedis){
        if (jedis!=null){
            if (jedisPool==null){
                jedisPool=getInstance();
            }
            jedisPool.returnResource(jedis);
        }
    }
}
