package kr.ac.uos.ai.arbi.agent.logger;

import kr.ac.uos.ai.arbi.ltm.DataSource;

public class LoggingActionPrior implements ActionBody {
	
	private DataSource dataSource;
	private String actor;
	private String type;
	private String action;
	private ActionBody normalAction;
	
	public LoggingActionPrior(DataSource dataSource, String actor, String type, String action, ActionBody normalAction ) {
		this.dataSource = dataSource;
		this.actor = actor;
		this.type = type;
		this.action = action;
		this.normalAction = normalAction;
	}

	public Object execute(Object o){
		this.sendLog(o.toString());
		return normalAction.execute(o);
	}
	
	private void sendLog(String content) {
		String time = String.valueOf(System.currentTimeMillis());

		
		content = content.replace("\"", "\\\"");
		content = content.replace("\t", "\\t");
		content = content.replace("\r", "\\r");
		content = content.replace("\b", "\\b");
		content = content.replace("\f", "\\f");
		content = content.replace("\n", "\\n");
		
		System.out.println("[System Log]	(SystemLog (actor \""+ actor +"\") (type \""+ type +"\") "
				+ "(action \""+ action +"\") (content \""+ content +"\") (time \""+ time +"\"))");
		
		dataSource.assertFact("(SystemLog (actor \""+ actor +"\") (type \""+ type +"\") "
				+ "(action \""+ action +"\") (content \""+ content +"\") (time \""+ time +"\"))");
	}


}
