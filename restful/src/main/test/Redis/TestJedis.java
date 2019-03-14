package Redis;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.web.context.support.XmlWebApplicationContext;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPool;

import java.util.HashSet;

public class TestJedis {

    @Test
    public void testJedisSingle(){
        //创建一个jedis对象
        Jedis jedis = new Jedis("127.0.0.1",6379);
        //调用jedis对象的方法
        jedis.set("key1","jedis test");
        String str = jedis.get("key1");
        System.out.println(str);
        //关闭jedis
        jedis.close();
    }

    //使用连接池
    @Test
    public void testJedisPool(){
        //创建jedis连接池
        JedisPool pool = new JedisPool("127.0.0.1",6379);

        //从连接池中获取jedis对象
        Jedis jedis = pool.getResource();
        String str = jedis.get("key1");
        System.out.println(str);
        //关闭jedis对象
        jedis.close();
        pool.close();

    }

    //测试集群
    @Test
    public void testJedisCluster(){

        HashSet<HostAndPort> nodes = new HashSet<>();
        nodes.add(new HostAndPort("127.0.0.1",7001));
        nodes.add(new HostAndPort("127.0.0.1",7002));
        nodes.add(new HostAndPort("127.0.0.1",7003));
        nodes.add(new HostAndPort("127.0.0.1",7004));
        nodes.add(new HostAndPort("127.0.0.1",7005));
        nodes.add(new HostAndPort("127.0.0.1",7006));
        JedisCluster cluster = new JedisCluster(nodes);

        cluster.set("key1","1000");
        String str = cluster.get("key1");
        System.out.println(str);

        cluster.close();

    }

    @Test
    public void testSpringJedisSingle(){
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:spring/applicationContext-*.xml");
        JedisPool pool = (JedisPool) applicationContext.getBean("redisClient");
        Jedis jedis = pool.getResource();
        String str = jedis.get("key1");
        System.out.println(str);
        jedis.close();
        pool.close();
    }

    @Test
    public void testSpringJedisCluster(){
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:spring/applicationContext-*.xml");
        JedisCluster jedisCluster = (JedisCluster) applicationContext.getBean("redisClient");
        String str = jedisCluster.get("key1");
        System.out.println(str);
        jedisCluster.close();

    }
}
