package test;

import kr.ac.uos.ai.arbi.agent.ArbiAgent;
import kr.ac.uos.ai.arbi.agent.ArbiAgentExecutor;
import kr.re.kist.emma.mybom.*;

public class MybomConfigureTestAgent2 extends ArbiAgent {
	@Override
	public String onRequest(String sender, String request) {
		// TODO Auto-generated method stub
		return "(TM)"+sender + " : " + request;
	}

	public static void main(String[] args) {
		/**
		 * <!-- ARBIFramework-0.8.m02/mybom.xml -->
		 * 
		 * <agent> <alias>TM</alias> ... </agent>
		 */
		AgentAttribute TMattr = MybomConfigure.agentAlias("taskmanager");
		String TMname = "agent://www.arbi.com/taskmanager";
		String TMversion = "0";
		String TMauthor = "모름";
		String TMdescription = "TM 속성 획득 불가";
		if(TMattr != null) {
			TMname = TMattr.name();
			TMversion = TMattr.version();
			TMauthor = TMattr.author();
			TMdescription = TMattr.description();
		}
		
		System.out.println("TM name: " + TMname);
		System.out.println("TM version: " + TMversion);
		System.out.println("TM author: " + TMauthor);
		System.out.println("TM description: " + TMdescription);
		
		AgentAttribute DMattr = MybomConfigure.agentAlias("DM");
		String DMname = "agent://www.arbi.com/dialogue";
		String DMversion = "0";
		String DMauthor = "모름";
		String DMdescription = "DM 속성 획득 불가";
		if(DMattr != null) {
			DMname = DMattr.name();
			DMversion = DMattr.version();
			DMauthor = DMattr.author();
			DMdescription = DMattr.description();
		}

		String arbiHost = MybomConfigure.ArbiHost();
		if( arbiHost == null)
			arbiHost = "tcp://127.0.0.1:61616";
		
		MybomConfigureTestAgent2 TMagent = new MybomConfigureTestAgent2();
		MybomConfigureTestAgent2 DMagent = new MybomConfigureTestAgent2();
		
		ArbiAgentExecutor.execute(arbiHost, DMname, DMagent);
		ArbiAgentExecutor.execute(arbiHost, TMname, TMagent);
		
		DMagent.send(TMname, "(test \"hello TM\")");
	}

}
