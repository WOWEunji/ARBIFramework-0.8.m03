package test;

import com.lambdaworks.redis.RedisClient;
import com.lambdaworks.redis.api.StatefulRedisConnection;
import com.lambdaworks.redis.api.sync.RedisCommands;

public class Test {

	public Test(){
		RedisClient client = RedisClient.create("redis://169.254.5.157:6379/0");
		StatefulRedisConnection<String, String> connection = client.connect();
		RedisCommands<String, String> syncCommands = connection.sync();
		syncCommands.set("test", "Hello Worlds");
		String worlds = syncCommands.get("test");
		System.out.println(worlds);
	}
	
	public static void main(String[] args) {
		new Test();
	}
}
