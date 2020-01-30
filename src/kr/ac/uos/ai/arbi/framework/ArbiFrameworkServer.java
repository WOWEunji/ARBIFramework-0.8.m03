package kr.ac.uos.ai.arbi.framework;

import kr.ac.uos.ai.arbi.framework.center.LTMMessageProcessor;
import kr.ac.uos.ai.arbi.framework.center.LTMService;
import kr.ac.uos.ai.arbi.framework.server.MessageService;

public class ArbiFrameworkServer {
	public static final String URL = "arbi.server";
	private MessageService				messageService;
	private LTMService					ltmService;
	public ArbiFrameworkServer() {
		ltmService = new LTMService();
		LTMMessageProcessor msgProcessor = new LTMMessageProcessor(ltmService);
		messageService = new MessageService(msgProcessor);
		msgProcessor.setMessageService(messageService);
	}
	
	public void start(String string) {
		messageService.initialize(string);
	}

}
