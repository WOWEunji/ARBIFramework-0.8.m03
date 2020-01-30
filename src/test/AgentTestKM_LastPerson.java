package test;

import java.util.ArrayList;
import java.util.Random;

import org.omg.PortableServer.RequestProcessingPolicyOperations;

import kr.ac.uos.ai.arbi.agent.ArbiAgent;
import kr.ac.uos.ai.arbi.agent.ArbiAgentExecutor;
import kr.ac.uos.ai.arbi.ltm.DataSource;

public class AgentTestKM_LastPerson extends ArbiAgent {

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

	AgentTestKM_LastPerson() {
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
		listService.add("로봇시작");
//		listService.add("식사");
//		listService.add("옷갈아입기");

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
				
//				(Sound_info $time $interval $pitch $intensity $count)

				dc.assertFact("(Sound_info \""+ getTime() +"\" 1 2 3 4)");
				System.out.println("음성 정보");
				sleepCommand(1000);

				
				dc.assertFact("(Robot_task \"" + getTime() + "\" \"" + user + "\" \"" + service + "\" \"start\")");
				sleepCommand(2000);
				dc.assertFact("(Robot_task \"" + getTime() + "\" \"" + user + "\" \"" + service + "\" \"end\")");
				sleepCommand(2000);
				
				dc.assertFact("(Robot_task \"" + getTime() + "\" \"" + user + "\" \"" + service + "\" \"end\")");
				sleepCommand(2000);
				
				dc.assertFact("(Sound_info \""+ getTime() +"\" 1 2 3 4)");
				System.out.println("음성 정보");
				sleepCommand(1000);

				
				dc.assertFact("(Sound_info \"" + getTime() + "\" 10.11 1000.11 100.11 5)");
				System.out.println("음성 정보");
				sleepCommand(1000);
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
		new AgentTestKM_LastPerson();
	}
}