package test;

public class ReasonResultArgument {
	private String time;
	private String resonResult;

	
	public ReasonResultArgument(){
		time = "";
		resonResult = "";

	}
	
	public void setResonResult(String resonResult){
		this.resonResult = resonResult;
	}
	public String getResonResult(){
		return resonResult;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}
	
	public String toString() {
		//String result = "(Index \""+time+"\") (Output \""+resonResult+"\")";
		//String result = "{\"Time\": \""+time+"\", \"Action\":\""+action+"\", \"Result\" : \""+action_result+"\"}";
		resonResult = resonResult.replace("\"", "");
		String result = "{\"Time\": \""+time+"\", \"Output\":\""+resonResult+"\"}";
		return result;
	}
}
