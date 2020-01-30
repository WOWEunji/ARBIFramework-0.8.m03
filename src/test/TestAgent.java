package test;


import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Random;

import org.omg.PortableServer.RequestProcessingPolicyOperations;

import kr.ac.uos.ai.arbi.agent.ArbiAgent;
import kr.ac.uos.ai.arbi.agent.ArbiAgentExecutor;
import kr.ac.uos.ai.arbi.agent.logger.AgentAction;
import kr.ac.uos.ai.arbi.agent.logger.LogTiming;
import kr.ac.uos.ai.arbi.agent.logger.LoggerManager;
import kr.ac.uos.ai.arbi.ltm.DataSource;

public class TestAgent extends ArbiAgent {

	private AgentAction ReasonResultLogAction;
	
	public void onStop() {
	}

	public String onRequest(String sender, String request) {
		return "Ignored";
	}

	public String onQuery(String sender, String query) {
		return "Ignored";
	}

	public void onData(String sender, String data) {
	}

	public String onSubscribe(String sender, String subscribe) {
		return "Ignored";
	}

	public void onUnsubscribe(String sender, String subID) {
	}

	public void onNotify(String sender, String notification) {
	}
	public TestAgent() {
		System.out.println("Agent Test started");
		ArbiAgentExecutor.execute("tcp://169.254.5.157:61616", "sa://kist.re.kr/test", this);
		System.out.println("agent1");
	}

	public void onStart() {
		System.out.println("Agent Test started");
		 DataSource dc = new DataSource();
		System.out.println("Agent Test started2");
		dc.connect("tcp://169.254.5.157:61616", "dc://kist.re.kr/testdc");
		System.out.println("Agent Test started3");
		
		
		
		UpdateReasonResultLogger updateReasonResultlog = new UpdateReasonResultLogger();
		ReasonResultLogAction = new AgentAction("Reasoning_History", updateReasonResultlog);
		LoggerManager reasonResultloggerManager = LoggerManager.getInstance();
		reasonResultloggerManager.registerAction("Output", ReasonResultLogAction, LogTiming.NonAction);

		
		int count = 0;
		do{
			
			String time = new java.text.SimpleDateFormat("yyyy-MM-dd HH시 mm분 ss.SSS초").format(new java.util.Date());

			String message = "(Test (time \""+time+"\") (position "+count+"))";
			System.out.println(message);
			dc.assertFact(message);
			
			ReasonResultLog(time, String.valueOf(count));
			count++;

			
		}while(count != 50);


	}
	


	private static String getTime() {
		// String time = null;
		String time = new java.text.SimpleDateFormat("yyyyMMdd'T'HHmmss.SSS'000'").format(new java.util.Date());

		return time;
	}

	public static void main(String[] args) {
		new TestAgent();
		
	}
	private void ReasonResultLog(String time, String result) {
		ReasonResultArgument argResult = new ReasonResultArgument();
		argResult.setResonResult(result);
		argResult.setTime(time);
		ReasonResultLogAction.execute(argResult);
	}
}