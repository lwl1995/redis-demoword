package cn.com.taiji.redis;

import org.apache.commons.lang3.RandomStringUtils;
import redis.clients.jedis.Jedis;

//模拟手机验证
public class MobileMessage {
    public static void main(String[] args) {
        //初始化
        Jedis jedis = new Jedis("localhost");
        System.out.println("redis本地服务连接成功！");
        //判断是否发过消息，如果没有则创建
        if (!jedis.exists("SmsMap")){
            //输出手机号
            String mobile = "13512818095";
            System.out.println("您的手机号为"+mobile);
            //生成验证码
            String code = RandomStringUtils.randomNumeric(5);
            //记录次数
            String count = "1";
            //存入redis
            jedis.hset("SmsMap","mobile",mobile);
            jedis.hset("SmsMap","code",code);
            jedis.hset("SmsMap","count",count);
            //设置过期时间
            jedis.expire("SmsMap",30);
            System.out.println("您的验证码为："+code+",30秒内有效");
        }else{
           String s =  jedis.hget("SmsMap","count");
            //获取次数，并判断是否超过3次
           int count =  Integer.parseInt(s);
           if(count<3){
               String code =   RandomStringUtils.randomNumeric(5);
               count++;
               jedis.hset("SmsMap","code",code);
               jedis.hset("SmsMap","count",Integer.toString(count));
               System.out.println("您的验证码为："+code+",30秒内有效");
           }else{
               System.out.println("请求次数过多，请10秒后再试");
           }
        }




    }
}
