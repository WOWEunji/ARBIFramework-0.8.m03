package test;

import kr.ac.uos.ai.arbi.agent.ArbiAgent;
import kr.ac.uos.ai.arbi.agent.ArbiAgentExecutor;
import kr.re.kist.emma.mybom.*;

public class MybomConfigureTestAgent1 extends ArbiAgent{
	AgentAttribute DM;
	AgentAttribute TM;
	public MybomConfigureTestAgent1(){
		DM = MybomConfigure.agentAlias("DM");
		TM = MybomConfigure.agentAlias("taskmanager");
		
		/**
		 * <!-- ARBIFramework-0.8/mybom.xml -->
		 * 
		 * <agent>
		 *     <alias>DM</alias>
		 *     ...
		 * </agent>
		 */
		ArbiAgentExecutor.execute(MybomConfigure.ArbiHost(), DM.name(), this);
		System.out.println("ARBI host: "+MybomConfigure.ArbiHost());
		System.out.println("DM alias: "+DM.alias());
		System.out.println("DM name: "+DM.name());
		System.out.println("DM version: "+DM.version());
		System.out.println("DM author: "+DM.author());
		System.out.println("DM description: "+DM.description());
	}
	
	@Override
	public String onRequest(String sender, String request) {
		// TODO Auto-generated method stub
		return "(DM)"+sender + " : " + request;
	}
	
	public static void main(String[] args) {
		MybomConfigureTestAgent1 DMAgent = new MybomConfigureTestAgent1();
	}

}
