package test; 

import kr.ac.uos.ai.arbi.agent.ArbiAgent;
import kr.ac.uos.ai.arbi.agent.ArbiAgentExecutor;
import kr.ac.uos.ai.arbi.ltm.DataSource; 

public class AgentTestCR extends ArbiAgent{ 
	 
	public void onStop(){} 
	public String onRequest(String sender, String request){return "Ignored";} 
	public String onQuery(String sender, String query){return "Ignored";} 
	public void onData(String sender, String data){} 
	public String onSubscribe(String sender, String subscribe){return "Ignored";} 
	public void onUnsubscribe(String sender, String subID){} 
	public void onNotify(String sender, String notification){} 
	 
	AgentTestCR(){ 
		ArbiAgentExecutor.execute("Agent1.xml", this); 
		System.out.println("agent1"); 
	} 
	 
	public void onStart(){ 

		
		System.out.println("Context Reasoner Agent Test started"); 
		//request("agent://AgentTest2", "hello"); 
		DataSource dc = new DataSource(); 
		//dc.connect("tcp://169.254.5.157:61616", "dc://kist.re.kr/testdc1"); 
		dc.connect("tcp://169.254.5.157:61616", "dc://kist.re.kr/testdc1");
		
		
		///////////////////////init//////////////////////////

//		

		
//		dc.assertFact("(Object_recognition \"20160718T130601.380455\" 308 \"약통\" \"true\")"); 
//		System.out.println("물체 인식"); 
//		sleepCommand(2000);
		
//		TM
//		dc.assertFact("(Robot_task \"20160718T130601.380455\" 308 \"kyonmo\" \"약먹기\" \"end\")"); 
//		System.out.println("로봇 작업 기록-약먹기 종료"); 
//		sleepCommand(2000);
//		
//		dc.assertFact("(Robot_dialog \"20160718T130601.380455\" \"약먹기\" \"견모 어르신 안녕하세요.\")"); 
//		System.out.println("로봇 발화기록"); 
//		sleepCommand(2000);
		
//		PM
		

		
		////////////////////////////////////////////////////////////////////////////////
		
		// TM-> PM
		dc.assertFact("(Human_recognition \"20160718T172603.380455\" 307 \"유정\" \"true\")"); 
        System.out.println("사람 인식"); 
        sleepCommand(300);
        //*/
		dc.assertFact("(Human_tracking \"20160718T130601.380455\" \"유정\" 4.0 0.1 4.0)"); 
        System.out.println("사람 위치 추적"); 
        sleepCommand(500);
		
		request("sa://kist.re.kr/cm","(Reason_spacial 4 \"유정\" \"수저통\")");
		System.out.println("1번 사람 Spatial Reasoning");
		sleepCommand(500);
		/*/
		dc.assertFact("(Human_tracking \"20170712T130601.380455\" \"기정\" 2.0 0.1 2.0)"); 
		System.out.println("사람 위치 추적"); 
		sleepCommand(2000);
		
		request("sa://kist.re.kr/cm","(Reason_spacial 4 \"기정\" \"수저통\")");
		System.out.println("1번 사람 Spatial Reasoning");
		sleepCommand(2000);//*/
				
		
		
//		
		//TM -> CR
//		request("sa://kist.re.kr/cm","(Human_personality \"20160718T172513.380455\" 302 1 \"kyonmo\")");
//		System.out.println("성격"); 
//		sleepCommand(2000);

//		request("sa://kist.re.kr/km","(Weather_information \"20160718T179601.380455\" 304)");
//		System.out.println("날씨정보"); 
//		sleepCommand(2000);
//				
//		request("sa://kist.re.kr/km","(Human_medicine_log \"20160718T140601.380455\" 305 \"kyonmo\")");
//		System.out.println("약먹기 기록 확인(3시간 이내 True)");
//		sleepCommand(2000);
//		
//		request("sa://kist.re.kr/km","(Human_medicine_log \"20160718T170601.380455\" 305 \"kyonmo\")");
//		System.out.println("약먹기 기록 확인(3시간 이상 False)");
//		sleepCommand(2000);
//		
//		request("sa://kist.re.kr/km","(Human_location \"20160718T170601.380455\" 305 \"kyonmo\")");
//		System.out.println("CR->KM 사람 마지막 위치 요청");
//		sleepCommand(2000);
		
		System.out.println("agent Test Ended"); 



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
		new AgentTestCR(); 
	} 
} 