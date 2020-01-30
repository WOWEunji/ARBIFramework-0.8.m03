package kr.ac.uos.ai.arbi.agent.datastream;

public class DataStream extends Thread {

	private DataStreamToolkit dataStreamToolkit;
	private StreamDataQueue streamDataQueue;
	
	public DataStream() {
		dataStreamToolkit = new DataStreamToolkit();
		streamDataQueue = new StreamDataQueue();
		this.start();
	}

	public String requestStream(String rule) {
		return dataStreamToolkit.connect(rule);
	}
	
	public void releaseStream(String streamID) {
		dataStreamToolkit.disconnect(streamID);
	}

	public void push(String data) {
		streamDataQueue.enqueue(data);
	}
	
	@Override
	public void run() {
		while(true) {
			String data = streamDataQueue.blockingDequeue(500);
			if(data != null) 	
				dataStreamToolkit.checkRules(data);
		}
	}
}
