package test;

import java.util.ArrayList;
import java.util.Random;

import org.omg.PortableServer.RequestProcessingPolicyOperations;

import kr.ac.uos.ai.arbi.agent.ArbiAgent;
import kr.ac.uos.ai.arbi.agent.ArbiAgentExecutor;
import kr.ac.uos.ai.arbi.ltm.DataSource;

public class AgentTestKM_EpisodicMemory extends ArbiAgent {

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

	AgentTestKM_EpisodicMemory() {
		ArbiAgentExecutor.execute("Agent1.xml", this);
		System.out.println("agent1");
	}

	public void onStart() {
		System.out.println("Agent Test started");
		DataSource dc = new DataSource();
		dc.connect("tcp://169.254.5.157:61616", "dc://kist.re.kr/testdc1");

		sleepCommand(3000);
		System.out.println("현재시간 : " + getTime());

		ArrayList<String> listService = new ArrayList<String>();
		listService.add("기상");
		listService.add("식사");
		listService.add("옷갈아입기");

		ArrayList<String> listUser = new ArrayList<String>();
//		listUser.add("기정");
		listUser.add("유정");
//		listUser.add("준호");

		Random generator = new Random();

		
		for (String user : listUser) {
			for (String service : listService) {
				int intNum = generator.nextInt();
				float floatNum = generator.nextFloat();
				
				System.out.println("=============================================");
				System.out.println("사람 인식 : " + user);
				dc.assertFact("(Human_recognition \"" + getTime() + "\" 300 \"" + user + "\" \"true\")");
				sleepCommand(2000);

				System.out.println("사람 위치 추적");
				floatNum = generator.nextFloat();
				dc.assertFact("(Human_tracking \"" + getTime() + "\" \"" + user + "\"" + 0 +" "+ floatNum*0.5 +" "+floatNum*3 +")");
				sleepCommand(1000);
				
				dc.assertFact("(Human_dialog \"" + getTime() + "\" \"어르신을 도와드려"+floatNum*30+"\")");
				System.out.println("사람 발화기록");
				sleepCommand(1000);
				
				System.out.println("=============================================");
				System.out.println("로봇 서비스 시작 : " + service);
//				(Robot_task $time $person_name $task_name $task_type)
				dc.assertFact("(Robot_task \"" + getTime() + "\" \"" + user + "\" \"" + service + "\" \"start\")");
				sleepCommand(1000);
				
				dc.assertFact("(Speak_gesture_face (current_mode 1) (next_mode 1) (target 1 1 1) (user_personality_E 1) (user_personality_ES 1) (robot_sentence \"안녕하세요?"+floatNum*30+"\") (actionID 1))");
				System.out.println("로봇 발화기록");
				sleepCommand(1000);
				
				floatNum = generator.nextFloat();
				dc.assertFact("(Human_tracking \"" + getTime() + "\" \"" + user + "\"" + 0 +" "+ floatNum*0.5 +" "+floatNum*3 +")");
				System.out.println("사람 위치 추적");
				sleepCommand(1000);

				dc.assertFact("(Object_recognition \"" + getTime() + "\" 308 \"약통"+floatNum*30+"\" \"true\")");
				System.out.println("물체 인식");
				
				sleepCommand(1000);
				floatNum = generator.nextFloat();
				dc.assertFact("(Human_tracking \"" + getTime() + "\" \"" + user + "\"" + 0 +" "+ floatNum*0.5 +" "+floatNum*3 +")");
				sleepCommand(1000);
			
				
				dc.assertFact("(Speak_gesture_face (current_mode 1) (next_mode 1) (target 1 1 1) (user_personality_E 1) (user_personality_ES 1) (robot_sentence \"안녕하세요?"+floatNum*30+"\") (actionID 1))");
				System.out.println("로봇 발화기록");
				sleepCommand(1000);

				floatNum = generator.nextFloat();
				dc.assertFact("(Human_tracking \"" + getTime() + "\" \"" + user + "\"" + 0 +" "+ floatNum*0.5 +" "+floatNum*3 +")");
				sleepCommand(500);
				
				dc.assertFact("(Human_dialog \"" + getTime() + "\" \"어 그래."+floatNum*30+"\")");
				System.out.println("사람 발화기록");
				sleepCommand(1000);

				floatNum = generator.nextFloat();
				dc.assertFact("(Human_tracking \"" + getTime() + "\" \"" + user + "\"" + 0 +" "+ floatNum*0.5 +" "+floatNum*3 +")");
				System.out.println("사람 위치 추적");
				sleepCommand(1000);

				dc.assertFact("(Human_physiology \"" + getTime() + "\" 80.11 36.6 11.11 22.22 33.33 11.11 22.22 33.33)");
				System.out.println("생채 신호 정보");
				sleepCommand(1000);

				dc.assertFact("(Sound_info \"" + getTime() + "\" 10.11 1000.11 100.11 5)");
				System.out.println("음성 정보");
				sleepCommand(1000);
				
				dc.assertFact("(Speak_gesture_face (current_mode 1) (next_mode 1) (target 1 1 1) (user_personality_E 1) (user_personality_ES 1) (robot_sentence \"안녕하세요?"+floatNum*30+"\") (actionID 1))");
				System.out.println("로봇 발화기록");
				sleepCommand(1000);
				
				dc.assertFact("(Human_dialog \"" + getTime() + "\" \"어 그래."+floatNum*30+"\")");
				System.out.println("사람 발화기록");
				sleepCommand(1000);
				
				floatNum = generator.nextFloat();
				dc.assertFact("(Human_tracking \"" + getTime() + "\" \"" + user + "\"" + 0 +" "+ floatNum*0.5 +" "+floatNum*3 +")");
				sleepCommand(500);
				
				dc.assertFact("(Human_dialog \"" + getTime() + "\" \"어 그래."+floatNum*30+"\")");
				System.out.println("사람 발화기록");
				sleepCommand(1000);
				
				request("sa://kist.re.kr/km","(Weather_information \"" +getTime() + "\" 304 \"today\" \"weather\")");
				System.out.println("날씨정보"); 
				sleepCommand(2000);

				dc.assertFact("(Speak_gesture_face (current_mode 1) (next_mode 1) (target 1 1 1) (user_personality_E 1) (user_personality_ES 1) (robot_sentence \"안녕하세요?"+floatNum*30+"\") (actionID 1))");
				System.out.println("로봇 발화기록");
				sleepCommand(1000);
				
				
				floatNum = generator.nextFloat();
				dc.assertFact("(Human_tracking \"" + getTime() + "\" \"" + user + "\"" + 0 +" "+ floatNum*0.5 +" "+floatNum*3 +")");
				sleepCommand(500);
				
				dc.assertFact("(Robot_task \"" + getTime() + "\" \"" + user + "\" \"" + service + "\" \"end\")");
				System.out.println("로봇 서비스 종료 : " + service);
				sleepCommand(5000);

			}
		}
	}

	private void sleepCommand(int time) {
		try {
			Thread.sleep(time);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private static String getTime() {
		// String time = null;
		String time = new java.text.SimpleDateFormat("yyyyMMdd'T'HHmmss.SSS'000'").format(new java.util.Date());

		return time;
	}

	public static void main(String[] args) {
		new AgentTestKM_EpisodicMemory();
	}
}