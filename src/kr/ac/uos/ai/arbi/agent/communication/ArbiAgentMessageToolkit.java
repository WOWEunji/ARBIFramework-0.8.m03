package kr.ac.uos.ai.arbi.agent.communication;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import kr.ac.uos.ai.arbi.agent.AgentMessageAction;
import kr.ac.uos.ai.arbi.agent.ArbiAgent;
import kr.ac.uos.ai.arbi.agent.ArbiAgentMessage;
import kr.ac.uos.ai.arbi.agent.communication.activemq.ActiveMQAdaptor;

public class ArbiAgentMessageToolkit extends Thread {
	private final int nThread = 5;

	private ArbiMessageAdaptor adaptor;
	private String agentURI;
	private ArbiMessageQueue queue;
	private ExecutorService messageThreadPool;
	private ArbiAgent agent;
	private ArrayList<ArbiAgentMessage> waitingResponse;

	public ArbiAgentMessageToolkit(String brokerURL, String agentURI, ArbiAgent agent) {
		this.agentURI = agentURI;
		this.queue = new ArbiMessageQueue();
		this.adaptor = new ActiveMQAdaptor(brokerURL, agentURI, queue);
		this.messageThreadPool = Executors.newFixedThreadPool(nThread);
		waitingResponse = new ArrayList<ArbiAgentMessage>();
		this.agent = agent;
		this.start();
	}

	public ArbiAgentMessage createMessage(String receiver, AgentMessageAction action, String content) {
		return new ArbiAgentMessage(agentURI, receiver, action, content);
	}

	public void run() {
		while (agent.isRunning()) {
			ArbiAgentMessage message = queue.blockingDequeue(null, 500);
			if (message != null) {
				dispatch(message);
			}
		}
	}

	private void dispatch(ArbiAgentMessage message) {
		Runnable task = null;
		AgentMessageAction action = message.getAction();
		switch (action) {
		case Inform:
			task = new DispatchDataTask(this, message);
			break;
		case Request:
			task = new DispatchRequestTask(this, message);
			break;
		case Query:
			task = new DispatchQueryTask(this, message);
			break;
		case Response:
			dispatchResponse(message);
			break;
		case Notify:
			task = new DispatchNotifyTask(this, message);
			break;
		case Subscribe:
			task = new DispatchSubscribeTask(this, message);
			break;
		case Unsubscribe:
			task = new DispatchUnsubscribeTask(this, message);
			break;
		case System:
			task = new DispatchSystemTask(this, message);
			break;
		case RequestStream:
			task = new DispatchRequestStreamTask(this, message);
			break;
		case ReleaseStream:
			task = new DispatchReleaseStreamTask(this, message);
			break;
		default:
			break;
		}
		if (task != null) {
			messageThreadPool.execute(task);
		} else if (!(action.equals(AgentMessageAction.Response))) {
			System.err.println("�옒紐삳맂 硫붿떆吏�: [" + message.getAction() + "] " + message.getContent());
		}
	}

	private void dispatchResponse(ArbiAgentMessage message) {
		ArbiAgentMessage responsedMessage = null;
		for (ArbiAgentMessage arbiAgentMessage : waitingResponse) {
			if (arbiAgentMessage.getConversationID().equals(message.getConversationID())) {
				responsedMessage = arbiAgentMessage;
			}
		}
		responsedMessage.setResponse(message);
		waitingResponse.remove(responsedMessage);
	}

	public void sendResponseMessage(String requestID, String sender, String response) {
		adaptor.send(new ArbiAgentMessage(this.agentURI, sender, AgentMessageAction.Response, response, requestID));
	}

	public String request(String receiver, String content) {
		ArbiAgentMessage message = createMessage(receiver, AgentMessageAction.Request, content);
		waitingResponse.add(message);
		adaptor.send(message);
		return message.getResponse();
	}

	public String query(String receiver, String content) {
		ArbiAgentMessage message = createMessage(receiver, AgentMessageAction.Query, content);
		waitingResponse.add(message);
		adaptor.send(message);
		return message.getResponse();
	}

	public void send(String receiver, String content) {
		ArbiAgentMessage message = createMessage(receiver, AgentMessageAction.Inform, content);
		adaptor.send(message);
	}

	public String subscribe(String receiver, String content) {
		ArbiAgentMessage message = createMessage(receiver, AgentMessageAction.Subscribe, content);
		waitingResponse.add(message);
		adaptor.send(message);
		return message.getResponse();
	}

	public void unsubscribe(String receiver, String content) {
		ArbiAgentMessage message = createMessage(receiver, AgentMessageAction.Unsubscribe, content);
		adaptor.send(message);
	}

	public void notify(String receiver, String content) {
		ArbiAgentMessage message = createMessage(receiver, AgentMessageAction.Notify, content);
		adaptor.send(message);
	}

	public void system(String receiver, String content) {
		ArbiAgentMessage message = createMessage(receiver, AgentMessageAction.System, content);
		adaptor.send(message);
	}
	
	public String requestStream(String receiver, String content) {
		ArbiAgentMessage message = createMessage(receiver, AgentMessageAction.RequestStream, content);
		waitingResponse.add(message);
		adaptor.send(message);
		return message.getResponse();
	}
	
	public void releaseStream(String receiver, String content) {
		ArbiAgentMessage message = createMessage(receiver, AgentMessageAction.ReleaseStream, content);
		adaptor.send(message);
	}

	public String onRequest(String sender, String request) {
		return agent.onRequest(sender, request);
	}

	public String onQuery(String sender, String query) {
		return agent.onQuery(sender, query);
	}

	public void onData(String sender, String data) {
		agent.onData(sender, data);
	}

	public void onNotify(String sender, String data) {
		agent.onNotify(sender, data);
	}

	public void onUnsubscribe(String sender, String unsubscribe) {
		agent.onUnsubscribe(sender, unsubscribe);
	}

	public String onSubscribe(String sender, String subscribe) {
		return agent.onSubscribe(sender, subscribe);
	}

	public void onSystem(String sender, String data) {
		agent.onSystem(sender, data);
	}
	
	public String onRequestStream(String sender, String rule) {
		return agent.onRequestStream(sender, rule);
	}

	public void onReleaseStream(String sender, String streamID) {
		agent.onReleaseStream(sender, streamID);
	}

}
