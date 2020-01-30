package test;

import java.util.ArrayList;
import java.util.Random;

import org.omg.PortableServer.RequestProcessingPolicyOperations;

import kr.ac.uos.ai.arbi.agent.ArbiAgent;
import kr.ac.uos.ai.arbi.agent.ArbiAgentExecutor;
import kr.ac.uos.ai.arbi.ltm.DataSource;

public class AgentTestKM_SemanticMemory extends ArbiAgent {

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

	AgentTestKM_SemanticMemory() {
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
		listService.add("약먹기");
		listService.add("식사");
		listService.add("옷갈아입기");

		ArrayList<String> listUser = new ArrayList<String>();
//		listUser.add("기정");
		listUser.add("유정");
		listUser.add("준호");

		Random generator = new Random();
//		for (String user : listUser) {
//			for (String service : listService) {
//				int intNum = generator.nextInt();
//				float floatNum = generator.nextFloat();
//				
//				System.out.println("=============================================");
//				System.out.println("사람 인식 : " + user);
//				dc.assertFact("(Human_recognition \"" + getTime() + "\" 300 \"" + user + "\" \"true\")");
//				sleepCommand(2000);
//
//				System.out.println("사람 위치 추적");
//				dc.assertFact("(Human_tracking \"" + getTime() + "\" \"" + user + "\"" + 0 +" "+ floatNum*0.5 +" "+floatNum*3 +")");
//				sleepCommand(1000);
//				
//				dc.assertFact("(Human_dialog \"" + getTime() + "\" 308 \"어르신을 도와 드려.\")");
//				System.out.println("사람 발화기록");
//				sleepCommand(1000);
//
//				System.out.println("=============================================");
//				System.out.println("로봇 서비스 시작 : " + service);
////				(Robot_task $time $person_name $task_name $task_type)
//				dc.assertFact("(Robot_task \"" + getTime() + "\" \"" + user + "\" \"" + service + "\" \"start\")");
//				sleepCommand(1000);
//				dc.assertFact("(Human_tracking \"" + getTime() + "\" \"" + user + "\"" + 0 +" "+ floatNum*0.5 +" "+floatNum*3 +")");
//				System.out.println("사람 위치 추적");
//				sleepCommand(1000);
//
//				dc.assertFact("(Object_recognition \"" + getTime() + "\" 308 \"약통\" \"true\")");
//				System.out.println("물체 인식");
//				sleepCommand(1000);
//
//				dc.assertFact("(Robot_dialog \"" + getTime() + "\"" + service + "\" \"기상알람 서비스를 시작합니다.\")");
//				System.out.println("로봇 발화기록");
//				sleepCommand(1000);
//
//				dc.assertFact("(Human_dialog \"" + getTime() + "\" \"어 그래.\")");
//				System.out.println("사람 발화기록");
//				sleepCommand(1000);
//				float floatNum2 = generator.nextFloat();
//
//				dc.assertFact("(Human_tracking \"" + getTime() + "\" \"" + user + "\"" + 0 +" "+ floatNum2*0.5 +" "+floatNum2*3 +")");
//				System.out.println("사람 위치 추적");
//				sleepCommand(1000);
//
//				dc.assertFact("(Human_physiology \"" + getTime() + "\" 80.11 36.6 11.11 22.22 33.33)");
//				System.out.println("생채 신호 정보");
//				sleepCommand(1000);
//
//				dc.assertFact("(Sound_info \"" + getTime() + "\" 10.11 1000.11 100.11 5)");
//				System.out.println("음성 정보");
//				sleepCommand(1000);
//
//				dc.assertFact("(Robot_task \"" + getTime() + "\" \"" + user + "\" \"" + service + "\" \"end\")");
//				System.out.println("로봇 서비스 종료 : " + service);
//				sleepCommand(5000);
//
//			}
//		}
		// TM-> PM
		String user = "유정";
		request("sa://kist.re.kr/km", "(Semantic_memory_generation \"" + getTime() + "\" 308 \"" + user + "\")");
		System.out.println("Semantic Memory 생성 요청");
		sleepCommand(1000);
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

	private static String getTime() {
		// String time = null;
		String time = new java.text.SimpleDateFormat("yyyyMMdd'T'HHmmss.SSS'000'").format(new java.util.Date());

		return time;
	}

	public static void main(String[] args) {
		new AgentTestKM_SemanticMemory();
	}
}