package kr.re.kist.emma.mybom;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

public class MybomConfigure {
	private static List<AgentAttribute> mList;
	private static String mIP;
	private static String mArbiHost;

	static {
		mList = new ArrayList<AgentAttribute>();
		String jarPath = MybomConfigure.class.getProtectionDomain().getCodeSource().getLocation().getPath();
		File file = new File(jarPath);
//		jarPath = file.getParent().toString();
//		System.out.println(jarPath+"/mybom.xml");
//		MybomConfigure.parse(Paths.get(jarPath + "/mybom.xml"));

		localIP();
		mArbiHost = "tcp://" + mIP + ":61616";
	}

	public static String localIP() {
		if (mIP != null)
			return mIP;

		Map<String, String> table = new HashMap<String, String>();
		
		Enumeration<NetworkInterface> iNetworks;
		Pattern p = Pattern.compile("(1|2)?\\d?\\d(\\.(1|2)?\\d?\\d){3}");
		Matcher m = null;
		try {
			iNetworks = NetworkInterface.getNetworkInterfaces();
			while (iNetworks.hasMoreElements()) {
				NetworkInterface iNetwork = iNetworks.nextElement();
				String ipName = iNetwork.getDisplayName();

				Enumeration<InetAddress> iAddrs = iNetwork.getInetAddresses();
				while (iAddrs.hasMoreElements()) {
					InetAddress iAddr = iAddrs.nextElement();
					m = p.matcher(iAddr.getHostAddress());
					if (m.find()) {
						String ethernetIPv4 = m.group();
//						System.out.println(iNetwork.getDisplayName() + ":" + m.group());
						table.put(ipName, ethernetIPv4);
						break;
					}
				}
			}
			
			char[] startChs = {'e', 'w'};
			Set<String> names = table.keySet();
			for(int i=0, n=startChs.length; i<n; ++i) {
				for (String name : names) {
					if (startChs[i] == name.charAt(0)) {
						mIP = table.get(name);
						return mIP;
					}
				}
			}
		} catch (SocketException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static String ArbiHost() {
		return mArbiHost;
	}
	
	private static int parse(Path path) {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = null;
		Document doc = null;
		try {
			builder = factory.newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		}
		try {
			doc = builder.parse(path.toFile());
		} catch (SAXException | IOException e) {
			e.printStackTrace();
		}
		Node agents = doc.getElementsByTagName("mybom").item(0);
		for (Node agentTag = agents.getFirstChild(); agentTag != null; agentTag = agentTag.getNextSibling()) {
			/// < Traveling in 'mybom' tag
			String agentTagName = agentTag.getNodeName().toLowerCase();
			if (agentTagName.equals("agent")) {

				final AgentAttribute attr = new AgentAttribute();

				for (Node tagNode = agentTag.getFirstChild(); tagNode != null; tagNode = tagNode.getNextSibling()) {
					/// < Traveling in 'agent' tag

					String tagName = tagNode.getNodeName();
					if (tagName.equals("#text"))
						continue; /// < Pass \n\r

					/// < Getting the Tag, Value
					String tag = tagNode.getNodeName();
					String value = tagNode.getTextContent();

					attr.put(tag, value);
				}
				mList.add(attr);
			}
		}

		return 1;
	}

	public static AgentAttribute agentAlias(String alias) {
		Iterator<AgentAttribute> iter = mList.iterator();
		while (iter.hasNext()) {
			AgentAttribute attr = iter.next();
			if (attr.alias().equals(alias))
				return attr;
		}
		return null;
	}

	public static AgentAttribute find(String tag, String value) {
		AgentAttribute attr = null;

		Iterator<AgentAttribute> iterator = mList.iterator();
		while (iterator.hasNext()) {
			attr = iterator.next();
			if (attr.get(tag).equals(value))
				return attr;
		}
		return null;
	}
}
