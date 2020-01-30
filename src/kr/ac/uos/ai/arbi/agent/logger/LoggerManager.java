package kr.ac.uos.ai.arbi.agent.logger;

import java.util.HashMap;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import kr.ac.uos.ai.arbi.ltm.DataSource;

public class LoggerManager {

	private static LoggerManager instance;
	
	private DataSource dataSource;
	private String actor;
	private HashMap<String, AgentAction> actionMap;

	private LoggerManager() {
		this.dataSource = new DataSource();
		this.actionMap = new HashMap<>();
	}
		
	public static LoggerManager getInstance() {
		if(instance == null) 
			instance = new LoggerManager();
		
		return instance;
	}
	
	public void initLoggerManager(String brokerURL, String agentURI) {
		
		String[] splitURI = agentURI.split("/");
		String actor = splitURI[splitURI.length-1];
		
		this.actor = actor;
		this.connectDataSource(brokerURL, "dc://www.arbi.com/" + actor + "Log");
		
	}
	
	private void connectDataSource(String brokerURL, String dataSourceURI) {
		dataSource.connect(brokerURL, dataSourceURI);
	}

	public void registerAction(String actionName, AgentAction action, LogTiming logTiming) {

		for (String key : actionMap.keySet()) {
			if (key.toLowerCase().equals(actionName.toLowerCase())) {
				AgentAction tmp = actionMap.get(key);
				if (action.getType().toLowerCase().equals(tmp.getType().toLowerCase())) {
					return;
				}
			}
		}
		
		action.initLoggingFunction(this.dataSource, this.actor, actionName, logTiming);
		actionMap.put(actionName, action);

	}

	public void freeAction(String actionName) {

		if (actionMap.containsKey(actionName))
			actionMap.remove(actionName);

	}

	public AgentAction getAction(String actionName) {

		if(actionMap.containsKey(actionName))
			return actionMap.get(actionName);
		
		return null;
	}
	
	public void changeFilterOption(String data) {
		
		try {
			
			JSONParser parser = new JSONParser();
			JSONObject filter;
			filter = (JSONObject) parser.parse(data);

			String action = filter.get("Action").toString().replaceAll("\"", "");
			String type = filter.get("Type").toString().replaceAll("\"", "");
			boolean flag = Boolean.parseBoolean(filter.get("Flag").toString().replaceAll("\"", ""));

			for (String key : actionMap.keySet()) {
				if (key.toLowerCase().equals(action.toLowerCase())) {
					AgentAction agentAction = actionMap.get(key);
					if (agentAction.getType().toLowerCase().equals(type.toLowerCase())) {
						agentAction.changeAction(flag);
					}
				}
			}
			
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}

}
