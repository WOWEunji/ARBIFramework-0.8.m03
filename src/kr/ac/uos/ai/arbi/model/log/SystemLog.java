package kr.ac.uos.ai.arbi.model.log;

public class SystemLog {

	private String actor;
	private String type;
	private String action;
	private long time;
	
	public SystemLog(String actor, String type, String action) {
		this.actor = actor;
		this.type = type;
		this.action = action;
		this.time = 0;
	}
	
	public SystemLog(String actor, String type, String action, long time) {
		this.actor = actor;
		this.type = type;
		this.action = action;
		this.time = time;
	}
	
	public String getActor() {
		return this.actor;
	}
	
	public String getType() {
		return this.type;
	}
	
	public String getAction() {
		return this.action;
	}
	
	public long getTime() {
		return this.time;
	}
	
}
