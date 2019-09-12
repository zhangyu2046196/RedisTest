package com.youyuan.jedis;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.Transaction;

import java.util.List;

/**
 * @author zhangyu
 * @version 1.0
 * @description jedis操作事务
 * 模拟场景
 * 信用卡的可用余额balance,信用卡的还款额度dept,先通过服务器命令设置balance和dept的初始值，代码里面监控balance
 * 先正常调用一次模拟在代码执行更改时没有别的线程修改，然后在代码里面模拟延迟Thread.sleep,这时在服务器上修改balance
 * 看代码是否能执行成功
 * @date 2018/11/21 17:10
 */
public class JedisTransation {

    /**
     * 模拟信用卡扣款
     * @return true 执行成功  false执行失败
     */
    public boolean execMethod() throws InterruptedException {
        Jedis jedis=new Jedis("192.168.32.131",6381);
        //信用卡可用余额
        int balance=0;
        //信用卡还款额
        int dept=0;
        //监听balance
        jedis.watch("balance");
        //模拟网络延迟，此时在服务器上手动修改balance,然程序进入Exception里面在重新调用
        Thread.sleep(10000);
        balance=Integer.valueOf(jedis.get("balance"));
        dept=Integer.valueOf(jedis.get("dept"));
        //消费金额
        int pay=10;
        //余额不够情况
        if (balance<pay){
            jedis.unwatch();
            System.out.println("余额不足场景balance:"+balance+" dept:"+dept+" pay:"+pay);
            return true;
        }
        Transaction transaction=null;
        //开启事务
        transaction=jedis.multi();
        //可用余额减去消费金额,可消费额度和还款额度放到一个事务执行
        transaction.decrBy("balance",pay);
        transaction.incrBy("dept",pay);
        //提交事务
        List<Object> exec= transaction.exec();
        System.out.println("正常执行场景balance:"+balance+" dept:"+dept+" pay:"+pay+" exec:"+exec);
        //exec为空代表事务执行失败
        if (exec==null){
            execMethod();
        }
        return true;
    }

    public boolean transMethod() throws InterruptedException {
        Jedis jedis = new Jedis("192.168.32.131", 6381);
        int balance;// 可用余额
        int dept;// 欠额
        int amtToSubtract = 10;// 实刷额度

        jedis.watch("balance");
        //jedis.set("balance","5");//此句不该出现，讲课方便。模拟其他程序已经修改了该条目
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
