package test;

import kr.ac.uos.ai.arbi.agent.ArbiAgentExecutor;
import kr.ac.uos.ai.arbi.framework.ArbiFrameworkServer;
import kr.ac.uos.ai.arbi.framework.broker.ApolloBroker;
import kr.ac.uos.ai.arbi.interaction.InteractionManager;

public class ApolloServerStart {

	public static void main(String[] args) {
		System.out.println("-------------Agent Server Start-------------");
		ApolloBroker broker = new ApolloBroker();
		//broker.setURL("169.254.5.157", 61616);
		broker.start();
		
		ArbiFrameworkServer server = new ArbiFrameworkServer();
		
		if(args.length == 0) {
			server.start("tcp://169.254.5.157:61616");
		} else if(args.length == 1) {
			server.start(args[0]);
		}
		
		InteractionManager interactionManager = new InteractionManager();
		interactionManager.start("tcp://169.254.5.157:61616");
		ArbiAgentExecutor.execute("tcp://169.254.5.157:61616", InteractionManager.interactionAgentURI, interactionManager);
		
	}
}
