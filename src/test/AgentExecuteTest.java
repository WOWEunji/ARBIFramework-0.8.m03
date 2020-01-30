package test;

import kr.ac.uos.ai.arbi.agent.ArbiAgent;
import kr.ac.uos.ai.arbi.agent.ArbiAgentExecutor;
import kr.ac.uos.ai.arbi.ltm.DataSource;

public class AgentExecuteTest {
	public static void main(String[] args) {
		
		ArbiAgent agent = new ArbiAgent() {
		};
		ArbiAgentExecutor.execute("Agent1.xml", agent);
		
		
		ArbiAgent agent2 = new ArbiAgent() { 
			@Override
			public String onRequest(String sender, String request) {
				System.out.println(sender +" "+ request);
				return "world";
			}
		};
		ArbiAgentExecutor.execute("Agent2.xml", agent2);
		
		System.out.println(agent.request("testAgent2", "hello"));
		System.out.println(agent.request("testAgent2", "hello"));
		//System.out.println("end");
		
		
		
		DataSource dc = new DataSource(){
		@Override
		public void onNotify(String content) {
			
			System.out.println("Notified! : "+content);
		}
			
		};
		dc.connect("tcp://169.254.5.157:61616", "dc://testdc2");
		dc.onNotify("test");
		
		dc.subscribe("(rule (fact (a $b $c)) --> (notify (a $b $c)))");
		dc.assertFact("(a $b $c)");
		
//		dc.assertFact("(a \"b\" \"c\")");
//		String id = dc.subscribe("(rule (fact (SystemLog $actor $type $action $content $time)) --> (notify (SystemLog $actor $type $action $content $time)))");
//		dc.assertFact("(SystemLog (Actor \"TaskManager\") (Type \"WorldModel\") (Action \"Assert\") (Content \"Tree(10,20)\") (Time \"456123456\"))");
//		dc.subscribe("(SystemLog $actor $type $content $time)");
		
//		long time = System.currentTimeMillis();
//		for(int i =0; i < 5; i++){
//			dc.assertFact("(a \"b\" \"c\")");
//			dc.retrieveFact("(a $a $b)");
//			dc.match("(a $a $b)");
//			dc.updateFact("(update (a $a \"c\") (a \"d\" \"e\"))");
//			dc.retrieveFact("(a $a $b)");
//			dc.match("(a $a $b)");
//			dc.retractFact("(a \"b\" \"c\")");
//			dc.retrieveFact("(a $a $b)");
//			dc.match("(a $a $b)");
//			dc.retractFact("(a \"d\" \"e\")");
//			dc.retrieveFact("(a $a $b)");
//			dc.match("(a $a $b)");
//			System.out.println(i + " : " + (System.currentTimeMillis()-time));
//			time = System.currentTimeMillis();
//		}			
//	
//			String id = dc.subscribe("(rule (fact (a $b $c)) --> (notify (a $b $c)))");
//			dc.assertFact("(a (b \"b1\") \"c\")");
//			dc.retractFact("(a (b \"b1\") \"c\")");
//			dc.unsubscribe(id);
//			dc.assertFact("(a (b \"b1\") \"c\")");
//			dc.retractFact("(a (b \"b1\") \"c\")");
			
			
			
	}
}
