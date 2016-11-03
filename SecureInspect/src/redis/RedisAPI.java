package redis;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisShardInfo;
import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPool;
import utils.ChineseToSpellUtil;

public class RedisAPI {
	//private Jedis jedis;
	//private JedisPool jedisPool;
	//private ShardedJedis shardedJedis;
	private ShardedJedisPool shardedJedisPool;
	
	/*public JedisPool getPool(){
		if(jedisPool == null){
			JedisPoolConfig config = new JedisPoolConfig();
			
			config.setMaxActive(500);
			config.setMaxIdle(5);
			config.setMaxWait(10001);
			config.setTestOnBorrow(true);
			jedisPool = new JedisPool(config, "127.0.0.1", 6379);
		}
		return jedisPool;
	}*/
	
	/*public void returnResource(JedisPool pool, Jedis redis){
		if(redis != null){
			pool.returnResource(redis);
		}
	}*/
	/*	public String get(){
	JedisPool pool = null;
	Jedis jedis = null;
	String inf = null;
	try{
		pool = getPool();
		jedis = pool.getResource();
		inf = jedis.get("resultqueue");
		if(inf != null){
			System.out.println(inf);
		}
		else{
			System.out.println("Пе");
		}
		
	} catch(Exception e){
		pool.returnBrokenResource(jedis);
	} finally {
		returnResource(pool, jedis);
	}
	return inf;
}*/

/*	public void set(String text){
		JedisPool pool = null;
		Jedis jedis = null;
		try{
			pool = getPool();
			jedis = pool.getResource();
			jedis.set("textqueue", text);
		} catch(Exception e){
			pool.returnBrokenResource(jedis);
		} finally {
			returnResource(pool, jedis);
		}
	}*/
	
	public ShardedJedisPool getShardedPool(){
		if(shardedJedisPool == null){
			JedisPoolConfig config = new JedisPoolConfig();
			config.setMaxActive(20);
			config.setMaxIdle(5);
			config.setMaxWait(10001);
			config.setTestOnBorrow(false);
			
			List<JedisShardInfo> shards = new ArrayList<JedisShardInfo>();
			shards.add(new JedisShardInfo("10.10.41.45", 6379, "master"));
			
			shardedJedisPool = new ShardedJedisPool(config, shards);
		}
		return shardedJedisPool;
	}
	
	public void returnResource(ShardedJedisPool shardedPool, ShardedJedis shardedRedis){
		if(shardedRedis != null){
			shardedPool.returnResource(shardedRedis);
		}
	}
	
	public String get(String obj){
		ShardedJedisPool shardedPool = null;
		ShardedJedis shardedJedis = null;
		String result = null;
		
		shardedPool = getShardedPool();
		shardedJedis = shardedPool.getResource();

		JSONObject jsonobj = JSONObject.fromObject(obj);
		String id =  ChineseToSpellUtil.getSpell(jsonobj.getString("id"), false).toLowerCase();
		String status = shardedJedis.get("Spider_" + id);
		String num = shardedJedis.get("Spider_" + id + "_Num");
		
		
		jsonobj.put("status", "True".equals(status) ? "True" : "False");
		jsonobj.put("num", num == null ? "0" : num);
		jsonobj.put("id", id);
		shardedJedis.del("Spider_" + id);
		shardedJedis.del("Spider_" + id + "_Num");
		result = jsonobj.toString();
		returnResource(shardedPool, shardedJedis);
		return result;
	}
	
	public String get(){
		ShardedJedisPool shardedPool = null;
		ShardedJedis shardedJedis = null;
		String result = null;
		
		shardedPool = getShardedPool();
		shardedJedis = shardedPool.getResource();

		 Set s = shardedJedis.hkeys("*");
		 Iterator it = s.iterator();
		 JSONArray jsonArray = new JSONArray();
		 int i=0;
		 while (it.hasNext()) {
			i++;
		   String key = (String) it.next();
		   String value = shardedJedis.get(key);
		   System.out.println(key + value);
		   JSONObject obj = new JSONObject();
		   obj.put("key" +i , key);
		   obj.put("value" +i, value);
		   jsonArray.add(obj);
		  }
		  result = jsonArray.toString();
		  
		returnResource(shardedPool, shardedJedis);
		return result;
	}
	
	public void set(String text){
		ShardedJedisPool shardedPool = null;
		ShardedJedis shardedJedis = null;
		
		shardedPool = getShardedPool();
		shardedJedis = shardedPool.getResource();
		shardedJedis.lpush("task_descriptor", text);
		returnResource(shardedPool, shardedJedis);
	}
}
