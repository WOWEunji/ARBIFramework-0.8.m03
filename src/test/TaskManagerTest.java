package test;

import kr.ac.uos.ai.arbi.agent.ArbiAgent;
import kr.ac.uos.ai.arbi.agent.ArbiAgentExecutor;
import kr.ac.uos.ai.arbi.ltm.DataSource;

public class TaskManagerTest {

	public static void main(String[] args) {
		ArbiAgent taskManager = new ArbiAgent() {
			@Override
			public String onRequest(String sender, String request) {
				// TODO Auto-generated method stub
				return "(ok)";
			}
		};
		ArbiAgentExecutor.execute("tcp://127.0.0.1:61616", "agent://www.arbi.com/TaskManager", taskManager);

		DataSource ds = new DataSource();
		ds.connect("tcp://127.0.0.1:61616", "ds://www.arbi.com/TaskManager");
		ds.assertFact("(serviceModel \"ServiceModel.xml\")");
		
		taskManager.request("agent://www.arbi.com/TaskReasoner", "");
	}
}
