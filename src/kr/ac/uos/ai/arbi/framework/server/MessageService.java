package kr.ac.uos.ai.arbi.framework.server;

import kr.ac.uos.ai.arbi.agent.ArbiAgentMessage;
import kr.ac.uos.ai.arbi.ltm.communication.message.LTMMessage;

public class MessageService {
	private MessageDeliverAdaptor					deliverAdaptor;
	private LTMMessageAdaptor						ltmAdaptor;
	private LTMMessageListener						listener;
	private boolean interactionManagerStatus;
	
	public MessageService(LTMMessageListener listener) {
		this.listener = listener;
		this.deliverAdaptor = new ActiveMQMessageAdaptor(this);
		this.ltmAdaptor = (LTMMessageAdaptor)deliverAdaptor;
	}


	public void messageRecieved(ArbiAgentMessage agentMessage) {
		System.out.println("[Agent Message]\t<"+agentMessage.getAction().toString()+">\t"+agentMessage.getSender()+" --> "+agentMessage.getReceiver()+" : "+agentMessage.getContent());
		deliverAdaptor.deliver(agentMessage);
		
		if(interactionManagerStatus) {
			deliverAdaptor.deliverTOmonitor(agentMessage);
		}
		
	}
	
	public void send(LTMMessage message){
		
		String time = new java.text.SimpleDateFormat("yyyy-MM-dd HH시 mm분 ss.SSS초").format(new java.util.Date());

		System.out.println("[LTM Message]\t<"+time+": "+message.getAction().toString()+">\t"+message.getClient()+" : "+message.getContent());
		
		ltmAdaptor.send(message);
		
		
		
		if(interactionManagerStatus) {
			deliverAdaptor.deliverTOmonitor(message);
		}
	}


	public void messageRecieved(LTMMessage ltmMessage) {
		String time = new java.text.SimpleDateFormat("yyyy-MM-dd HH시 mm분 ss.SSS초").format(new java.util.Date());
		System.out.println("[LTM Message]\t<"+time+": "+ltmMessage.getAction().toString()+">\t"+ltmMessage.getClient()+" : "+ltmMessage.getContent());
		
		if(interactionManagerStatus) {
			deliverAdaptor.deliverTOmonitor(ltmMessage);
		}
		
		listener.messageRecieved(ltmMessage);
	}
	
	public void messageRecieved(String status) {
		if(status.equals("ON"))
			interactionManagerStatus = true;
		else if(status.equals("OFF"))
			interactionManagerStatus = false;
		
	}

	public void initialize(String brokerURL) {
		deliverAdaptor.initialize(brokerURL);
		
	}

	
}
