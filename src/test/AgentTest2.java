package test;

import kr.ac.uos.ai.arbi.agent.ArbiAgent;
import kr.ac.uos.ai.arbi.agent.ArbiAgentExecutor;
import kr.ac.uos.ai.arbi.ltm.DataSource;
import kr.re.kist.emma.mybom.MybomConfigure;

public class AgentTest2 extends ArbiAgent{
	
	public void onStop(){}
	public String onRequest(String sender, String request){return"(robotSTT (actionID 1) (type  \"on\") (param \"param\") 1)";}
	public void onData(String sender, String data){}
	public String onSubscribe(String sender, String subscribe){return "Ignored";}
	public void onUnsubscribe(String sender, String subID){}
	public void onNotify(String sender, String notification){}
	
	AgentTest2(){
		ArbiAgentExecutor.execute(MybomConfigure.ArbiHost(),"sa://kist.re.kr/perception", this);
	}
	
	public void onStart(){
		System.out.println("start");
		//request("agent://AgentTest2", "hello");
//		DataSource dc = new DataSource();
//		dc.connect("tcp://169.254.5.157:61616", "dc://kist.re.kr/testdc");
//		
//		request("sa://kist.re.kr/test3","(RequestTest \"testValue\")");
//		
//		dc.assertFact("(SubscribeTest \"notificationValue\")");
//		this.query("sa://kist.re.kr/test", "(testQuery)");
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
		new AgentTest2();
	}
}
