package kr.ac.uos.ai.arbi.interaction;

import java.util.HashMap;
import java.util.Map;

import kr.ac.uos.ai.arbi.interaction.adaptor.ActiveMQAdaptor;
import kr.ac.uos.ai.arbi.interaction.adaptor.ActiveMQStompAdaptor;
import kr.ac.uos.ai.arbi.interaction.adaptor.InteractionMessageAdaptor;
import kr.ac.uos.ai.arbi.interaction.adaptor.SocketAdaptor;
import kr.ac.uos.ai.arbi.interaction.adaptor.ZeroMQAdaptor;


public class MonitorMessageToolkit extends Thread {

	private final int nThread = 5;
	private MonitorMessageQueue queue;
	private HashMap<String, InteractionMessageAdaptor> adaptorMap;
	private InteractionMessageAdaptor activeMQAdaptor;
	private InteractionMessageAdaptor socketAdaptor;
	private InteractionMessageAdaptor activeMQStompAdaptor;
	private InteractionMessageAdaptor zeroMQAdaptor;
	
	private InteractionManager interactionManager;
	
	public MonitorMessageToolkit(String broker , InteractionManager interactionManager) {
		this.interactionManager = interactionManager;
		this.queue = new MonitorMessageQueue();
		this.adaptorMap = new HashMap<>();
		initAdaptors(broker);
		this.start();
	}
	
	private void initAdaptors(String broker) {
		this.activeMQAdaptor = new ActiveMQAdaptor(broker, queue);
		this.socketAdaptor = new SocketAdaptor(queue);
		this.activeMQStompAdaptor = new ActiveMQStompAdaptor(broker, queue);
		this.zeroMQAdaptor = new ZeroMQAdaptor(broker, queue);
		
		this.adaptorMap.put("ActiveMQ", activeMQAdaptor);
		this.adaptorMap.put("Socket", socketAdaptor);
		this.adaptorMap.put("Stomp", activeMQStompAdaptor);
		this.adaptorMap.put("ZeroMQ", zeroMQAdaptor);
		
	}
	
	public void stopThread() {
		for(Map.Entry<String, InteractionMessageAdaptor> elem : adaptorMap.entrySet()) {
			elem.getValue().close();
		}
	}
	
	public void run() {
		while(true){
			String message = queue.blockingDequeue(500);
			if(message != null) {
				interactionManager.messageRecieved(message);
			}
		}
	}
	
	public void sendMessage(String monitorID, String protocol, String message) {
		if(adaptorMap.containsKey(protocol))
			adaptorMap.get(protocol).send(monitorID, message);
	}
	
	public void sendStatus(String status) {
		activeMQAdaptor.sendStatus(status);
	}
}
