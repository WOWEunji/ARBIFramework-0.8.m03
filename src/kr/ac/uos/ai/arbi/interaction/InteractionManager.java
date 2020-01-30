package kr.ac.uos.ai.arbi.interaction;

import java.util.ArrayList;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import kr.ac.uos.ai.arbi.agent.ArbiAgent;
import kr.ac.uos.ai.arbi.interaction.proxy.MonitorProxy;
import kr.ac.uos.ai.arbi.ltm.DataSource;
import kr.ac.uos.ai.arbi.model.GLFactory;
import kr.ac.uos.ai.arbi.model.GeneralizedList;

public class InteractionManager extends ArbiAgent {

	public static final String interactionAgentURI = "agent://www.arbi.com/interactionManager";
	public static final String interactionManagerURI = "http://www.arbi.com/interactionManager";
	private static final String interactionManagerDS = "ds://www.arbi.com/interactionManager";

	private static final String CONTEXTMANAGER_ADDRESS = "agent://www.arbi.com/contextManager";
	private static final String CONTEXTMONITORMANAGER_ADDRESS = "agent://www.arbi.com/contextMonitorManager";
	private static final String TASKMANAGER_ADDRESS = "agent://www.arbi.com/taskManager";
	private static final String KNOWLEDGEMANAGER_ADDRESS = "agent://www.arbi.com/knowledgeManager";
	
	private ArrayList<MonitorProxy> monitorProxyList;
	private LogManager logManager;

	private MonitorMessageToolkit monitorMessageToolkit;

	public InteractionManager() {
		monitorProxyList = new ArrayList<>();
		logManager = new LogManager(this);
	}
	
	public void start(String brokerURI){
		initMessageToolkit(brokerURI);
		monitorMessageToolkit.sendStatus("ON");
		initDataSource(brokerURI);
	}
	
	public void stop() {
		monitorMessageToolkit.stopThread();
	}

	private void initMessageToolkit(String brokerURI) {
		monitorMessageToolkit = new MonitorMessageToolkit(brokerURI, this);
	}

	private void initDataSource(String brokerURI) {
		DataSource dc = new DataSource() {
			@Override
			public void onNotify(String content) {
				sendMessage(content);
				System.out.println("[ LogData ]\t" +"<SystemLog>"+"\t" +content);
			}
		};
		dc.connect(brokerURI, interactionManagerDS);
		String id = dc.subscribe(
				"(rule (fact (SystemLog $Actor $Type $Action $Content $Time)) --> (notify (SystemLog $Actor $Type $Action $Content $Time)))");
	}

	public void createProxy(String monitorID, String protocol, JSONArray filter) {
		for(int i=0; i<monitorProxyList.size(); i++) {
			if(monitorProxyList.get(i).getMonitorID().equals(monitorID))
				return;
		}
		MonitorProxy monitorProxy = new MonitorProxy(monitorID, protocol, filter);
		monitorProxyList.add(monitorProxy);
	}

	private void changeProxyFilter(String monitorID, JSONArray filter) {

		for (int i = 0; i < monitorProxyList.size(); i++) {
			MonitorProxy monitorProxy = monitorProxyList.get(i);
			if (monitorProxy.getMonitorID().equals(monitorID))
				monitorProxy.setFilter(filter);
		}
	}

	private void deleteProxy(String monitorID) {
		for (int i = 0; i < monitorProxyList.size(); i++) {
			MonitorProxy monitorProxy = monitorProxyList.get(i);
			if (monitorProxy.getMonitorID().equals(monitorID)) {
				JSONArray filter = monitorProxy.getFilterToJSON();
				monitorProxyList.remove(monitorProxy);
				setGlobalFilter(filter);
			}
		}
	}

	@Override
	public void onSystem(String sender, String data) {
		sendMessage(data);
		System.out.println("[ LogData ]\t" +"<MessageLog>"+"\t" +data);
	}
	
	private void sendMessage(String data) {
		JSONObject message = logManager.logParseToJSON(data);
		for (int i = 0; i < monitorProxyList.size(); i++) {
			MonitorProxy monitorProxy = monitorProxyList.get(i);
			if (monitorProxy.isFilterON(message)) {
				monitorMessageToolkit.sendMessage(monitorProxy.getMonitorID(), monitorProxy.getMonitorProtocol(),
						message.toJSONString());
			}
		}
	}

	public void setGlobalFilter(JSONArray filter) {
		logManager.setGlobalFilter(filter);
	}

	public int getProxySize() {
		return this.monitorProxyList.size();
	}

	public boolean checkProxyFilter(int i, JSONObject filterObject) {
		return monitorProxyList.get(i).isFilterON(filterObject);
	}

	public void globalFilterChange(JSONObject filterObject) {
		String actor = filterObject.get("Actor").toString().toUpperCase();
		System.out.println(filterObject.toJSONString());
		if (actor.equals("TM") || actor.equals("TASKMANAGER")) {
			system(TASKMANAGER_ADDRESS, filterObject.toJSONString());
		} else if (actor.equals("CM") || actor.equals("CONTEXTMANAGER")) {
			system(CONTEXTMANAGER_ADDRESS, filterObject.toJSONString());
		} else if (actor.equals("KM") || actor.equals("KNOWLEDGEMANAGER")) {
			system(KNOWLEDGEMANAGER_ADDRESS, filterObject.toJSONString());
		} else if(actor.equals("CMM") || actor.equals("CONTEXTMONITORMANAGER")) {
			system(CONTEXTMONITORMANAGER_ADDRESS, filterObject.toJSONString());
		}
	}
	
	public void sendCommand(JSONObject jsonObject) {
		
		String sender = jsonObject.get("Sender").toString();
		System.out.println("sender = " +sender);
		JSONObject glObject = (JSONObject)jsonObject.get("GeneralizedList");
		GeneralizedList gl = GLFactory.newGLFromJSON(glObject);

		if(gl == null)
			return;
		
		String receiver = jsonObject.get("Receiver").toString().toUpperCase();
		String receiverURL = null;

		if (receiver.equals("TM") || receiver.equals("TASKMANAGER")) {
			receiverURL = TASKMANAGER_ADDRESS;
		} else if (receiver.equals("CM") || receiver.equals("CONTEXTMANAGER")) {
			receiverURL = CONTEXTMANAGER_ADDRESS;
		} else if (receiver.equals("KM") || receiver.equals("KNOWLEDGEMANAGER")) {
			receiverURL = KNOWLEDGEMANAGER_ADDRESS;
		} else if(receiver.equals("CMM") || receiver.equals("CONTEXTMONITORMANAGER")) {
			receiverURL = CONTEXTMONITORMANAGER_ADDRESS;
		}
		
		if(receiverURL == null)
			return;
		
		String messageType = jsonObject.get("MessageType").toString().toLowerCase();
		
		String result = null;
		if(messageType.equals("request"))
			result = request(receiverURL, gl.toString());
		else if(messageType.equals("query"))
			result = query(receiverURL, gl.toString());
		else if(messageType.equals("send"))
			send(receiverURL, gl.toString());
		else if(messageType.equals("subscribe"))
			result = subscribe(receiverURL, gl.toString());
		else if(messageType.equals("unsubscribe"))
			unsubscribe(receiverURL, gl.toString());
		else if(messageType.equals("notify"))
			notify(receiverURL, gl.toString());
	
		if(result != null) {
			JSONObject resultObject = GLFactory.newJSONObjectFromGLString(result);
			resultObject.put("Sender", receiver);
			resultObject.put("Receiver", sender);
			for(int i=0; i<monitorProxyList.size(); i++) {
				MonitorProxy monitorProxy = monitorProxyList.get(i);
				if(sender.equals(monitorProxy.getMonitorID())) {
					monitorMessageToolkit.sendMessage(monitorProxy.getMonitorID(), monitorProxy.getMonitorProtocol(),
							resultObject.toJSONString());
				}
			}
		}
	}

	public void messageRecieved(String monitorAction) {
		try {
			//System.out.println("[ Monitor Action ] " +monitorAction);
			JSONParser jsonParser = new JSONParser();
			JSONObject jsonObject = (JSONObject) jsonParser.parse(monitorAction);
			String action = jsonObject.get("Action").toString();
			
			if (action.toLowerCase().equals("create monitor")) {
				String monitorID = jsonObject.get("ID").toString();
				String protocol = jsonObject.get("Protocol").toString();
				JSONArray filter = (JSONArray) jsonObject.get("Filter");
				createProxy(monitorID, protocol, filter);
				setGlobalFilter(filter);
			} else if (action.toLowerCase().equals("change filter")) {
				String monitorID = jsonObject.get("ID").toString();
				JSONArray filter = (JSONArray) jsonObject.get("Filter");
				changeProxyFilter(monitorID, filter);
				setGlobalFilter(filter);
			} else if (action.toLowerCase().equals("delete monitor")) {
				String monitorID = jsonObject.get("ID").toString();
				deleteProxy(monitorID);
			} else if(action.toLowerCase().equals("command")) {
				sendCommand(jsonObject);
				
			}
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
