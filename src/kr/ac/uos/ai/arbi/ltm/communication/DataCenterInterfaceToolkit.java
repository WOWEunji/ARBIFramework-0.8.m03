package kr.ac.uos.ai.arbi.ltm.communication;

import java.util.ArrayList;

import kr.ac.uos.ai.arbi.ltm.LTMMessageAction;
import kr.ac.uos.ai.arbi.agent.datastream.DataStreamToolkit;
import kr.ac.uos.ai.arbi.ltm.DataSource;
import kr.ac.uos.ai.arbi.ltm.communication.activemq.ActiveMQAdaptor;
import kr.ac.uos.ai.arbi.ltm.communication.message.LTMMessage;

public class DataCenterInterfaceToolkit extends Thread {

	
	private final LTMMessageQueue					queue;
	private final DataSource						dataSource;
	private final ArrayList<LTMMessage>				waitingResult;
	private final ActiveMQAdaptor					adaptor;
	
	private final LTMMessageFactory					factory;

	public DataCenterInterfaceToolkit(String brokerURL, String dataSourceURI,
			DataSource dataSource) {

		this.factory = LTMMessageFactory.getInstance();
		this.dataSource = dataSource;
		this.queue = new LTMMessageQueue();
		this.adaptor = new ActiveMQAdaptor(brokerURL, dataSourceURI, queue);
		this.waitingResult = new ArrayList<LTMMessage>();
		this.start();
	}

	public void run() {
		while(dataSource.isRunning()) {
			LTMMessage message = queue.blockingDequeue(null, 500);
			if (message != null) {
				dispatch(message);
			}
		}
	}
		
	private void dispatch(LTMMessage message) {
		if(message.getAction()==LTMMessageAction.Notify){
			dataSource.onNotify(message.getContent());
			return;
		}
		
		LTMMessage responsedMessage = null;
		for (LTMMessage ltmMessage : waitingResult) {
			if(ltmMessage.getConversationID().equals(message.getConversationID())){
				responsedMessage = ltmMessage;
			}
		}
		responsedMessage.setResponse(message);
		waitingResult.remove(responsedMessage);
		
	}
	

	public void assertFact(String uri, String fact) {
		LTMMessage message = factory.newAssertFactMessage(uri, fact);
		waitingResult.add(message);
		adaptor.send(message);
	}

	public String retractFact(String uri, String fact) {
		LTMMessage message = factory.newRetractFactMessage(uri, fact);
		waitingResult.add(message);
		adaptor.send(message);
		return message.getResponse();
	}
	

	public String retrieveFact(String uri, String fact) {
		LTMMessage message = factory.newRetrieveFactMessage(uri, fact);
		waitingResult.add(message);
		adaptor.send(message);
		return message.getResponse();

	}

	public void updateFact(String uri, String fact) {
		LTMMessage message = factory.newUpdateFactMessage(uri, fact);
		waitingResult.add(message);
		adaptor.send(message);
	}

	public String match(String uri, String fact) {
		LTMMessage message = factory.newMatchMessage(uri, fact);
		waitingResult.add(message);
		adaptor.send(message);
		return message.getResponse();

	}

	public String subscribe(String uri, String rule) {
		LTMMessage message = factory.newSubscribeMessage(uri, rule);
		waitingResult.add(message);
		adaptor.send(message);
		return message.getResponse();
	}

	public void unsubscribe(String uri, String subID) {
		LTMMessage message = factory.newUnsubscribeMessage(uri, subID);
		waitingResult.add(message);
		adaptor.send(message);
		
	}

	public DataStreamToolkit registerStream(String uri, String rule) {
		LTMMessage message = factory.newRequestStreamMessage(uri, rule);
		adaptor.send(message);
		return null;
	}

}
