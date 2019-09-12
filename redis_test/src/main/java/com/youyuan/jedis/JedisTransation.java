package com.youyuan.jedis;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.Transaction;

import java.util.List;

/**
 * @author zhangyu
 * @version 1.0
 * @description jedis��������
 * ģ�ⳡ��
 * ���ÿ��Ŀ������balance,���ÿ��Ļ�����dept,��ͨ����������������balance��dept�ĳ�ʼֵ������������balance
 * ����������һ��ģ���ڴ���ִ�и���ʱû�б���߳��޸ģ�Ȼ���ڴ�������ģ���ӳ�Thread.sleep,��ʱ�ڷ��������޸�balance
 * �������Ƿ���ִ�гɹ�
 * @date 2018/11/21 17:10
 */
public class JedisTransation {

    /**
     * ģ�����ÿ��ۿ�
     * @return true ִ�гɹ�  falseִ��ʧ��
     */
    public boolean execMethod() throws InterruptedException {
        Jedis jedis=new Jedis("192.168.32.131",6381);
        //���ÿ��������
        int balance=0;
        //���ÿ������
        int dept=0;
        //����balance
        jedis.watch("balance");
        //ģ�������ӳ٣���ʱ�ڷ��������ֶ��޸�balance,Ȼ�������Exception���������µ���
        Thread.sleep(10000);
        balance=Integer.valueOf(jedis.get("balance"));
        dept=Integer.valueOf(jedis.get("dept"));
        //���ѽ��
        int pay=10;
        //�������
        if (balance<pay){
            jedis.unwatch();
            System.out.println("���㳡��balance:"+balance+" dept:"+dept+" pay:"+pay);
            return true;
        }
        Transaction transaction=null;
        //��������
        transaction=jedis.multi();
        //��������ȥ���ѽ��,�����Ѷ�Ⱥͻ����ȷŵ�һ������ִ��
        transaction.decrBy("balance",pay);
        transaction.incrBy("dept",pay);
        //�ύ����
        List<Object> exec= transaction.exec();
        System.out.println("����ִ�г���balance:"+balance+" dept:"+dept+" pay:"+pay+" exec:"+exec);
        //execΪ�մ�������ִ��ʧ��
        if (exec==null){
            execMethod();
        }
        return true;
    }

    public boolean transMethod() throws InterruptedException {
        Jedis jedis = new Jedis("192.168.32.131", 6381);
        int balance;// �������
        int dept;// Ƿ��
        int amtToSubtract = 10;// ʵˢ���

        jedis.watch("balance");
        //jedis.set("balance","5");//�˾䲻�ó��֣����η��㡣ģ�����������Ѿ��޸��˸���Ŀ
        Thread.sleep(7000);
        balance = Integer.parseInt(jedis.get("balance"));
        if (balance < amtToSubtract) {
            jedis.unwatch();
            System.out.println("modify");
            return false;
        } else {
            System.out.println("***********transaction");
            Transaction transaction = jedis.multi();
            transaction.decrBy("balance", amtToSubtract);
            transaction.incrBy("dept", amtToSubtract);
            transaction.exec();
            balance = Integer.parseInt(jedis.get("balance"));
            dept = Integer.parseInt(jedis.get("dept"));

            System.out.println("*******" + balance);
            System.out.println("*******" + dept);
            return true;
        }
    }

    public static void main(String[] args) throws InterruptedException {
        JedisTransation jedisTransation=new JedisTransation();
        jedisTransation.execMethod();
        //jedisTransation.transMethod();
    }
}
