package kr.ac.uos.ai.arbi.agent.logger;

import kr.ac.uos.ai.arbi.ltm.DataSource;

public class LoggingActionNonAction implements ActionBody {
	
	private DataSource dataSource;
	private String actor;
	private String type;
	private String action;
	
	public LoggingActionNonAction(DataSource dataSource, String actor, String type, String action) {
		this.dataSource = dataSource;
		this.actor = actor;
		this.type = type;
		this.action = action;
	}

	public Object execute(Object o){
		
		this.sendLog(o.toString());
		return o.toString();
	}
	
	private void sendLog(String content) {
		//String time = String.valueOf(System.currentTimeMillis());
		String time = new java.text.SimpleDateFormat("yyyy-MM-dd HH시 mm분 ss.SSS초").format(new java.util.Date());
		
		content = content.replace("\"", "\\\"");
		content = content.replace("\t", "\\t");
		content = content.replace("\r", "\\r");
		content = content.replace("\b", "\\b");
		content = content.replace("\f", "\\f");
		content = content.replace("\n", "\\n");
		//content = content.replace("\"","&quot;");
		//time= time.replace("\"","&quot;");
		
		
		//System.out.println("[System Log]	(SystemLog (actor \""+ actor +"\") (type \""+ type +"\") "
		//		+ "(action \""+ action +"\") (content \""+ content +"\") (time \""+ time +"\"))");
		System.out.println("check actor: "+actor);
		System.out.println("check type: "+type);
		System.out.println("check action: "+action);
		System.out.println("check content: "+content);
		System.out.println("check time: "+time);
		
		System.out.println("[System Log]	(SystemLog (actor \""+ actor +"\") (type \""+ type +"\") "
				+ "(action \""+ action +"\") (content \""+ content +"\") (time \""+ time +"\"))");
		
		dataSource.assertFact("(SystemLog (actor \""+ actor +"\") (type \""+ type +"\") "
				+ "(action \""+ action +"\") (content \""+ content +"\") (time \""+ time +"\"))");
	}


}
