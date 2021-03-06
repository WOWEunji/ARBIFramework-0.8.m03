package kr.ac.uos.ai.arbi.agent;

import kr.ac.uos.ai.arbi.agent.communication.ArbiAgentMessageToolkit;
import kr.ac.uos.ai.arbi.agent.datastream.DataStream;
import kr.ac.uos.ai.arbi.agent.datastream.StreamReceiver;
import kr.ac.uos.ai.arbi.agent.logger.LoggerManager;
import kr.ac.uos.ai.arbi.model.GLFactory;
import kr.ac.uos.ai.arbi.model.GeneralizedList;
import kr.ac.uos.ai.arbi.model.parser.ParseException;

public abstract class ArbiAgent {
	private String							ArbiAgentURI;
	private ArbiAgentMessageToolkit			messageToolkit;
	private DataStream				dataStream;
	
	private boolean							running;
	
	public void setRunning(boolean running) {
		this.running = running;
	}

	public ArbiAgent() {
		
	}
	
	public final void initialize(String brokerURL, String agentURI){
		ArbiAgentURI = agentURI;
		String broker = brokerURL;
		running = true;
		messageToolkit = new ArbiAgentMessageToolkit(broker, ArbiAgentURI, this);
		System.out.println("====================================messageToolkit = " +messageToolkit +"=================================================");
		System.out.println("====================================================ArbiAgentURI = "+ArbiAgentURI+"========================================");
		dataStream = new DataStream();
		LoggerManager.getInstance().initLoggerManager(brokerURL, agentURI);
		this.onStart();
	}
	
	public void onStart(){}
	public void onStop(){}
	public String onRequest(String sender, String request){return "Ignored";}
	public String onQuery(String sender, String query){return "Ignored";}
	public void onData(String sender, String data){}
	public String onSubscribe(String sender, String subscribe){return "Ignored";}
	public void onUnsubscribe(String sender, String unsubcribe){}
	public void onNotify(String sender, String notification){}
	public void onSystem(String sender, String data) {
		System.out.println("[ Data ] " +data);
		LoggerManager.getInstance().changeFilterOption(data);
	}

	public String onRequestStream(String sender, String rule) {
		System.out.println("sender = " +sender +", rule = " +rule);
		return dataStream.requestStream(rule);
	}
	
	public void onReleaseStream(String sender, String streamID) {
		dataStream.releaseStream(streamID); 
	}
	
	public void onStream(String data) {	
		System.out.println("on stream data = " +data);
	}
	
	public String request(String receiver, String request){
		return messageToolkit.request(receiver, request);
	}
	public String query(String receiver, String query){
		return messageToolkit.query(receiver, query);
	}
	public void send(String receiver, String data){
		messageToolkit.send(receiver, data);
	}
	public String subscribe(String receiver, String subscribe){
		return messageToolkit.subscribe(receiver, subscribe);
	}
	public void unsubscribe(String receiver, String content){
		messageToolkit.unsubscribe(receiver, content);
	}
	public void notify(String receiver, String notification){
		messageToolkit.notify(receiver, notification);
	}
	
	public void system(String receiver, String data) {
		messageToolkit.system(receiver, data);
	}
	
	public String requestStream(String receiver, String rule){
		try {
			GeneralizedList gl = GLFactory.newGLFromGLString(rule);
			for(int i=0; i<gl.getExpressionsSize(); i++) {
				GeneralizedList tmp = gl.getExpression(i).asGeneralizedList();
				if(tmp.getName().equals("url")) {
					new StreamReceiver(this, tmp.getExpression(0).toString().replaceAll("\"", ""));
					break;
				}
			}
			return messageToolkit.requestStream(receiver, rule);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public void releaseStream(String receiver, String streamID) {
		messageToolkit.releaseStream(receiver, streamID);
	}
	
	public DataStream getDataStream() {
		return dataStream;
	}

	public boolean isRunning() {
		return running;
	}

	
}
