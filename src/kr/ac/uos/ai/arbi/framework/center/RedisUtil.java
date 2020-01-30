package kr.ac.uos.ai.arbi.framework.center;

import java.util.List;

import com.lambdaworks.redis.api.sync.RedisCommands;

import kr.ac.uos.ai.arbi.model.Binding;
import kr.ac.uos.ai.arbi.model.ExpressionList;
import kr.ac.uos.ai.arbi.model.GLFactory;
import kr.ac.uos.ai.arbi.model.GeneralizedList;

public class RedisUtil {
	public static final String PredicatePrefix = "predicate:";
	public static final String PredicateNamePrefix = "predicateName:";
	public static final String CreateTimeKey = "CreateTime";
	public static final String PredicateNameKey = "Predicate";
	public static final String AuthorKey = "Author";
	public static final String ExpressionNumberKey = "ExpressionNumber";
	public static final String ExpressionPrefix = "Expression:";
	public static final String SubscribeChannel = "LTMSubscribe";

	private static RedisCommands<String, String> command;

	public static void setCommand(RedisCommands<String, String> command) {
		RedisUtil.command = command;
		
	}

	public static PredicateContainer createContainer(String author,
			GeneralizedList predicateGL) {
		return createContainer(author, System.nanoTime(), predicateGL);
	}

	public static PredicateContainer createContainer(String author,
			long createTime, GeneralizedList predicateGL) {
		return new PredicateContainer(author, createTime, predicateGL);
	}

	public static PredicateContainer createContainer(String author,
			String predicateString) {
		return createContainer(author, System.nanoTime(), predicateString);
	}

	public static PredicateContainer createContainer(String author,
			long createTime, String predicateString) {
		return new PredicateContainer(author, createTime, predicateString);
	}

	public static PredicateContainer queryMatchData(PredicateContainer predicate) {
		String name = predicate.getPredicate().getName();
		List<String> predicateKeyList = command.zrange(PredicateNamePrefix
				+ name, 0, -1);
		for (int i = 0; i < predicateKeyList.size(); i++) {
			
			PredicateContainer queriedGL = queryPredicateDataByKey(predicateKeyList
					.get(i));
			Binding b = predicate.getPredicate().unify(
					queriedGL.getPredicate(), null);
			if (b != null) {
				return queriedGL;
			}
		}
		return null;
	}

	public static PredicateContainer queryPredicateDataByKey(String key) {
		PredicateContainer container = null;
		if (!key.startsWith(PredicatePrefix)) {
			key = PredicatePrefix + key;
		}
		int size = Integer.valueOf(command.hget(key, ExpressionNumberKey));
		ExpressionList exps = new ExpressionList();

		for (int i = 0; i < size; i++) {
			String expression = command.hget(key, ExpressionPrefix + i);

			if (expression.startsWith(PredicatePrefix)) {
				exps.add(GLFactory.newExpression(queryPredicateDataByKey(
						expression).getPredicate()));
			} else {
				exps.add(GLFactory.newValueExpression(expression));
			}
		}
		GeneralizedList predicate = GLFactory.newGL(
				command.hget(key, PredicateNameKey), exps);
		container = new PredicateContainer(command.hget(key, AuthorKey),
				Long.valueOf(command.hget(key, CreateTimeKey)), predicate);

		return container;
	}

	public static void retractData(PredicateContainer queried) {
		if (queried == null) {
			return;
		}
		retractGL(queried.getCreateTime(), queried.getPredicate());
		command.zrem(
				PredicateNamePrefix + queried.getPredicate().getName(),
				makePredicateKey(queried.getPredicate().getName(),
						queried.getCreateTime()));
	}

	private static void retractGL(long createTime, GeneralizedList predicate) {
		for (int i = 0; i < predicate.getExpressionsSize(); i++) {
			if (predicate.getExpression(i).isGeneralizedList()) {
				retractGL(createTime, predicate.getExpression(i)
						.asGeneralizedList());
			}
			String key = makePredicateKey(predicate.getName(), createTime);
			command.del(key);
		}

	}

	public static void assertData(PredicateContainer data) {
		assertGL(data.getAuthor(), data.getCreateTime(), data.getPredicate());
		command.zadd(
				PredicateNamePrefix + data.getPredicate().getName(),
				Long.MAX_VALUE - data.getCreateTime(),
				makePredicateKey(data.getPredicate().getName(),
						data.getCreateTime()));
	}

	private static String assertGL(String author, long createTime,
			GeneralizedList predicate) {
		String key = makePredicateKey(predicate.getName(), createTime);
		command.hset(key, AuthorKey, author);
		command.hset(key, CreateTimeKey, String.valueOf(createTime));
		command.hset(key, PredicateNameKey, predicate.getName());
		command.hset(key, ExpressionNumberKey,
				String.valueOf(predicate.getExpressionsSize()));
		for (int i = 0; i < predicate.getExpressionsSize(); i++) {
			if (predicate.getExpression(i).isGeneralizedList()) {
				String subkey = assertGL(author, createTime, predicate
						.getExpression(i).asGeneralizedList());
				command.hset(key, ExpressionPrefix + i, subkey);
			} else {
				command.hset(key, ExpressionPrefix + i, predicate
						.getExpression(i).toString().replaceAll("\"", ""));
			}
		}
		return key;
	}

	private static String makePredicateKey(String name, long time) {
		return PredicatePrefix + name + ":" + time;
	}

}
