package kr.ac.uos.ai.arbi.framework.center;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import kr.ac.uos.ai.arbi.model.Binding;
import kr.ac.uos.ai.arbi.model.BindingFactory;
import kr.ac.uos.ai.arbi.model.Expression;
import kr.ac.uos.ai.arbi.model.rule.Rule;
import kr.ac.uos.ai.arbi.model.rule.action.Action;
import kr.ac.uos.ai.arbi.model.rule.condition.Condition;
import kr.ac.uos.ai.arbi.model.rule.condition.ConditionFactory;
import static kr.ac.uos.ai.arbi.framework.center.RedisUtil.*;

import com.lambdaworks.redis.RedisClient;
import com.lambdaworks.redis.pubsub.RedisPubSubListener;
import com.lambdaworks.redis.pubsub.StatefulRedisPubSubConnection;
import com.lambdaworks.redis.pubsub.api.async.RedisPubSubAsyncCommands;

public class RedisSubscriber implements RedisPubSubListener<String, String> {
	private HashMap<String, List<Rule>> subscribedRulesByPredicateName;
	private HashMap<String, Rule> subscribedRuleByID;
	private LTMNotificationHandler handler;
	private RedisPubSubAsyncCommands<String, String> subsriptionCommand;

	public RedisSubscriber() {
		RedisClient queryClient = RedisClient
				.create("redis://localhost/0");
		StatefulRedisPubSubConnection<String, String> psConnection = queryClient
				.connectPubSub();
		psConnection.addListener(this);
		this.subsriptionCommand = psConnection.async();
		this.subscribedRulesByPredicateName = new HashMap<String, List<Rule>>();
		this.subscribedRuleByID = new HashMap<String, Rule>();
		this.subsriptionCommand.subscribe(SubscribeChannel);
	}

	public void addNotificationHandler(LTMNotificationHandler handler) {
		this.handler = handler;
	}

	// [adru]-n:[*],1:[*],2:[*]

	@Override
	public void psubscribed(String event, long arg1) {
	}

	private void checkSubscribedRules(Condition c) {
		String predicateName = c.getPredicateName();
		List<Rule> rules = subscribedRulesByPredicateName.get(predicateName);
		if (rules == null || rules.isEmpty()) {
			return;
		}
		boolean conditionSatisfied;
		Binding b = BindingFactory.newBinding();
		for (Rule rule : rules) {
			conditionSatisfied = true;
			for (Condition con : rule.getConditions()) {
				Binding tempBind = null;
				if ((tempBind = evaluate(con)) != null) {
					b.copy(tempBind);
				} else {
					conditionSatisfied = false;
					break;
				}
			}
			if (conditionSatisfied) {
				for (Action a : rule.getActions()) {
					a.bind(b);
					handler.notify(a);
				}
			}
		}
	}

	private Binding evaluate(Condition con) {
		PredicateContainer data = createContainer(null, con.toString());
		PredicateContainer queriedData = queryMatchData(data);

		if (queriedData != null) {
			Binding b = data.getPredicate().unify(queriedData.getPredicate(),
					null);
			return b;
		}
		return null;
	}

	public String addRule(Rule r) {
		synchronized (this) {
			Condition[] condition = r.getConditions();
			String subID = "Subscribe:" + System.nanoTime();
			for (int i = 0; i < condition.length; i++) {
				if (subscribedRulesByPredicateName.get(condition[i]
						.getPredicateName()) == null) {

					subscribedRulesByPredicateName.put(
							condition[i].getPredicateName(),
							new ArrayList<Rule>());
				}
				subscribedRulesByPredicateName.get(
						condition[i].getPredicateName()).add(r);
				String pattern = makePatternString(condition[i]);
				subsriptionCommand.psubscribe(pattern);
			}
			subscribedRuleByID.put(subID, r);
			return subID;
		}
	}

	private String makePatternString(Condition condition) {
		StringBuilder sb = new StringBuilder();
		sb.append("(fact (").append(condition.getPredicateName());
		for (Expression e : condition.getExpressions()) {
			sb.append(" ");
			if (e.isVariable()) {
				sb.append("*");
			} else {
				sb.append(e.toString());
			}
		}
		sb.append("))");
		return sb.toString();
	}

	public void removeRule(String subID) {
		synchronized (this) {
			Rule r = subscribedRuleByID.remove(subID);
			Condition[] conditions = r.getConditions();
			for (Condition condition : conditions) {
				subscribedRulesByPredicateName
						.get(condition.getPredicateName()).remove(r);
				// String pattern = makePatternString(condition);
				// subsriptionCommand.punsubscribe(pattern);
			}
		}
	}

	@Override
	public void message(String arg0, String event) {
		synchronized (this) {
			Condition c = ConditionFactory.newConditionFromGLString(event);
			checkSubscribedRules(c);
		}
	}

	@Override
	public void message(String pattern, String channel, String event) {

	}

	@Override
	public void punsubscribed(String arg0, long arg1) {

	}

	@Override
	public void subscribed(String arg0, long arg1) {
	}

	@Override
	public void unsubscribed(String arg0, long arg1) {
	}

}
