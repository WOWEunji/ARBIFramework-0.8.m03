package kr.ac.uos.ai.arbi.interaction.adaptor;

import java.util.HashMap;

import org.zeromq.ZMQ;

import kr.ac.uos.ai.arbi.interaction.MonitorMessageQueue;

public class ZeroMQAdaptor extends Thread implements InteractionMessageAdaptor {

	private ZMQ.Context zmqContext;
	private ZMQ.Socket zmqSocket;
	private HashMap<String, ZMQ.Socket> zmqSocketMap;
	private MonitorMessageQueue queue;
	
	public ZeroMQAdaptor(String broker, MonitorMessageQueue queue) {
		this.queue = queue;
		String[] url = broker.split(":");
		zmqContext = ZMQ.context(1);
		zmqSocket = zmqContext.socket(ZMQ.REP);
		zmqSocket.bind(url[0]+":" +url[1] +":5560");
		
		this.start();
	}
	
	@Override
	public void run() {
		while(true) {
			String message = zmqSocket.recvStr();
			queue.enqueue(message);
		}
	}
	
	@Override
	public void close() {
		zmqSocket.close();
		zmqContext.term();
	}
	
	@Override
	public void send(String monitorID, String message) {
		// TODO Auto-generated method stub
		ZMQ.Context context = ZMQ.context(1);
		ZMQ.Socket socket = context.socket(ZMQ.REQ);
		socket.connect(monitorID);
		socket.send(message);
		socket.close();
		context.term();
	}

	@Override
	public void sendStatus(String status) {
		// TODO Auto-generated method stub
		
	}

	

}
