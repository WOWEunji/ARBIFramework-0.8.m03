package test;

import org.omg.PortableServer.RequestProcessingPolicyOperations;

import kr.ac.uos.ai.arbi.agent.ArbiAgent;
import kr.ac.uos.ai.arbi.agent.ArbiAgentExecutor;
import kr.ac.uos.ai.arbi.ltm.DataSource;

public class AgentTest3 extends ArbiAgent{
	
	public void onStop(){}
	public String onRequest(String sender, String request){return "Ignored";}
	public void onData(String sender, String data){}
	public String onSubscribe(String sender, String subscribe){return "Ignored";}
	public void onUnsubscribe(String sender, String subID){}
	public void onNotify(String sender, String notification){}
	
	AgentTest3(){
		ArbiAgentExecutor.execute("tcp://169.254.5.157:61616","sa://kist.re.kr/test3", this);
	}
	
	public void onStart(){
		System.out.println("start");
		//request("agent://AgentTest2", "hello");
		DataSource dc = new DataSource();
		dc.connect("tcp://169.254.5.157:61616", "dc://kist.re.kr/testdc");
		
		request("sa://kist.re.kr/ae","(RequestTest \"testValue\")");
		
		dc.assertFact("(SubscribeTest \"notificationValue\")");
		this.query("sa://kist.re.kr/test1", "(testQuery)");
//		while(true) {
//			String time = new java.text.SimpleDateFormat("yyyy-MM-dd HH시 mm분 ss.ms초").format(new java.util.Date());
//			dc.assertFact("(Test (time \""+time+"\") (position 10 20))");
//		}
		//System.out.println(dc.retrieveFact("(Robot_position $time $position)"));
	}
	@Override
	public String onQuery(String sender, String query){
		
		System.out.println("OK");
		
		return "(Ignored)";
	}
	
	
	public static void main(String[] args) {
		new AgentTest3();
	}
}