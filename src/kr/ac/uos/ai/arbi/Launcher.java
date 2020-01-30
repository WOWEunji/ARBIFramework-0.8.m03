package kr.ac.uos.ai.arbi;

import java.io.File;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import kr.ac.uos.ai.arbi.agent.ArbiAgentExecutor;
import kr.ac.uos.ai.arbi.framework.ArbiFrameworkServer;
import kr.ac.uos.ai.arbi.framework.broker.ActiveMQBroker;
import kr.ac.uos.ai.arbi.framework.broker.ApolloBroker;
import kr.ac.uos.ai.arbi.interaction.InteractionManager;
import kr.ac.uos.ai.arbi.monitor.ArbiFrameworkMonitor;
import kr.re.kist.emma.mybom.MybomConfigure;

public class Launcher {

	public static final int DefaultBrokerPort = 61616;
//	public static final String DefualtBrokerHost = "127.0.0.1";
	public static final String DefualtBrokerHost = MybomConfigure.localIP();	///< 20191002 박동현 수정
	
	private InteractionManager interactionManager;

	public Launcher(String[] args) {

		int brokerPort = 0;
		String host = null;
		boolean brokerStart = false;
		boolean serverStart = false;
		boolean activeMQBroker = true;
		boolean interactionManagerStart = false;
		String interactionManagerURI = null;
		boolean arbiFrameworkMonitorStart = false;

		try {

			File inputFile = new File("Configuration.xml");
			System.out.println(inputFile.getAbsoluteFile());
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(inputFile);
			doc.getDocumentElement().normalize();

			Element configurationElement = (Element) doc.getElementsByTagName("ARBIFrameworkConfiguration").item(0);
			if (configurationElement != null) {
				Element brokerStartElement = (Element) configurationElement.getElementsByTagName("MessageBrokerStart")
						.item(0);
				Element frameworkStartElement = (Element) configurationElement.getElementsByTagName("FrameworkStart")
						.item(0);
				Element brokerTypeElement = (Element) configurationElement.getElementsByTagName("MessageBrokerType")
						.item(0);
				Element brokerPropertyElement = (Element) configurationElement
						.getElementsByTagName("MessageBrokerProperty").item(0);
				Element interactionManagerStartElement = (Element) configurationElement
						.getElementsByTagName("InteractionManagerStart").item(0);
				Element arbiAgentMonitorStartElement = (Element) configurationElement
						.getElementsByTagName("ARBIFrameworkMonitorStart").item(0);

				if (brokerStartElement != null && brokerStartElement.getTextContent().toLowerCase().equals("true"))
					brokerStart = true;

				if (frameworkStartElement != null
						&& frameworkStartElement.getTextContent().toLowerCase().equals("true"))
					serverStart = true;

				if (brokerTypeElement != null && brokerTypeElement.getTextContent().toLowerCase().equals("apollo"))
					activeMQBroker = false;

				if (brokerPropertyElement != null) {
					Element portElement = (Element) configurationElement.getElementsByTagName("Port").item(0);
					Element hostElement = (Element) configurationElement.getElementsByTagName("Host").item(0);

					if (portElement != null) {
						brokerPort = Integer.parseInt(portElement.getTextContent());
					}
					if (hostElement != null) {
						host = hostElement.getTextContent();
						if(host.toLowerCase().equals("auto"))	///< 20191002 박동현 수정
							host = MybomConfigure.localIP();	///< 20191002 박동현 수정
					}
				}

				if (interactionManagerStartElement != null
						&& interactionManagerStartElement.getTextContent().toLowerCase().equals("true"))
					interactionManagerStart = true;

				if (arbiAgentMonitorStartElement != null
						&& arbiAgentMonitorStartElement.getTextContent().toLowerCase().equals("true"))
					arbiFrameworkMonitorStart = true;

			}

			if (brokerPort == 0) {
				brokerPort = DefaultBrokerPort;
			}
			if (host == null) {
				host = DefualtBrokerHost;
			}

			String serverURL = "tcp://" + host + ":" + brokerPort;

			if (brokerStart) {
				if (activeMQBroker) {
					ActiveMQBroker broker = new ActiveMQBroker();
					broker.setURL(serverURL);
					broker.start();
					
					ActiveMQBroker stompBroker = new ActiveMQBroker();
					stompBroker.setURL("stomp://"+host+":"+61613);
					stompBroker.start();
					
				} else {
					ApolloBroker broker = new ApolloBroker();
					broker.setURL(serverURL);
					broker.start();

					ActiveMQBroker stompBroker = new ActiveMQBroker();
					stompBroker.setURL("stomp://"+host+":"+61613);
					stompBroker.start();
				}

				System.out.println("Connect to " + serverURL);

			}

			if (serverStart) {
				ArbiFrameworkServer server = new ArbiFrameworkServer();
				server.start(serverURL);
			}

			if (interactionManagerStart) {

				interactionManager = new InteractionManager();
				ArbiAgentExecutor.execute(serverURL, InteractionManager.interactionAgentURI, interactionManager);
				interactionManager.start(serverURL);

			}

			if (arbiFrameworkMonitorStart) {
				ArbiFrameworkMonitor m = new ArbiFrameworkMonitor();
				m.start();
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void stop() {
		// TODO Auto-generated method stub
		interactionManager.stop();
	}
	
	public static void main(String[] args){
		Launcher launcher = new Launcher(args);
		
		Runtime.getRuntime().addShutdownHook(new Thread(){
			public void run() {
				launcher.stop();
			}
		});
	}

	
}
