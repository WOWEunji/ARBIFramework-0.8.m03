package kr.ac.uos.ai.arbi.agent;

import java.io.File;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class ArbiAgentExecutor {

	public static void execute(String agentXML, ArbiAgent agent) {

		try {
			File inputFile = new File(agentXML);
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(inputFile);
			doc.getDocumentElement().normalize();
			
			Element agentElement = (Element)doc.getElementsByTagName("Agent-Property").item(0);
			if(agentElement != null) {
				Element serverURLElement = (Element)agentElement.getElementsByTagName("ServerURL").item(0);
				Element agentNameElement = (Element)agentElement.getElementsByTagName("AgentName").item(0);
			
				if(serverURLElement != null && agentNameElement != null) {
					agent.initialize(serverURLElement.getTextContent(), agentNameElement.getTextContent());
				} else {
					System.out.println("Execute Error : AgentXML file was lacked");
				}
			} else {
				System.out.println("Execute Error : AgentXML file was wrong");
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void execute(String agentURL, String agentName, ArbiAgent agent) {
		agent.initialize(agentURL, agentName);
	}

}
