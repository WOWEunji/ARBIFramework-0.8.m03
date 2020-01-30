package test;

import org.omg.PortableServer.RequestProcessingPolicyOperations;

import kr.ac.uos.ai.arbi.agent.ArbiAgent;
import kr.ac.uos.ai.arbi.agent.ArbiAgentExecutor;
import kr.ac.uos.ai.arbi.ltm.DataSource;

public class Jang_query_test_km2 extends ArbiAgent{
	
	public void onStop(){}
	public String onRequest(String sender, String request){return "I love you";}
	public void onData(String sender, String data){}
	public String onSubscribe(String sender, String subscribe){return "Ignored";}
	public void onUnsubscribe(String sender, String subID){}
	public void onNotify(String sender, String notification){}
	
	Jang_query_test_km2(){
		ArbiAgentExecutor.execute("tcp://169.254.5.157:61616","sa://kist.re.kr/jang4", this);
	}
	
	public void onStart(){
		String user = "장우";
		System.out.println("start");
		//request("agent://AgentTest2", "hello");
		DataSource dc = new DataSource();
		dc.connect("tcp://169.254.5.157:61616", "dc://kist.re.kr/testdc");
		
		//request("sa://kist.re.kr/jang2","(RequestTest \"testValue\")");
			
		dc.assertFact("(SubscribeTest \"notificationValue\")");
		this.query("sa://kist.re.kr/km", "(Last_person \"장우\")");
		System.out.println("쿼리 TM->KM 마지막 사람 이름 요청");
		sleepCommand(2000);
				
		for(int i=0;i<50;i++)
		{
			request("sa://kist.re.kr/km","(Human_location \"20160718T170601.380455\" 305 \"기정\")");
			//this.query("sa://kist.re.kr/km","(Human_location \" 기정\")");
			System.out.println("리퀘스트 TM->KM 사람 마지막 위치 요청");
			sleepCommand(2000);
		}

		/*
		this.query("sa://kist.re.kr/km", "(Human_location \"20160718T170601.380455\" 305 \"기정\")");
		System.out.println("쿼리 TM->KM 사람 마지막 위치 요청");
		sleepCommand(2000);
		
		this.query("sa://kist.re.kr/km","(Task_preference_capability \"" +user+ "\" \"약먹기\")");
		System.out.println("선호/비선호"); 
		sleepCommand(2000);
		
		this.query("sa://kist.re.kr/km","(Human_personality \"20160718T172513.380455\" 302 1 \"kyonmo\")");
		System.out.println("성격"); 
		sleepCommand(2000);		
		
		request("sa://kist.re.kr/cm","(Location_name_axis \"" + getTime() + "\" 301 \"식탁\")");		System.out.println("장소 중심 좌표 요청"); 
		sleepCommand(1000);
		
		//request("sa://kist.re.kr/km","(Location_axis \"" +getTime()+ "\" 308 \"안방\")");
		this.query("sa://kist.re.kr/km","(Location_axis \"" +getTime()+ "\" 308 \"식탁\")");
		System.out.println("장소 중심 좌표 요청"); 
		sleepCommand(1000);
		//KM -> CM으로 넘어가는 과정에서 멈춤
*/
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
		//return query;
	}
	 private static String getTime() {
//	    	String time = null;
	    	String time = new java.text.SimpleDateFormat("yyyyMMdd'T'HHmmss.SSS'000'").format(new java.util.Date());

	    	return time;
	    }
	 
	private void sleepCommand(int time) {
		try {
			Thread.sleep(time);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

    }	
	
	public static void main(String[] args) {
		new Jang_query_test_km2();
	}
}