package test;

import kr.ac.uos.ai.arbi.agent.logger.ActionBody;


public class UpdateReasonResultLogger implements ActionBody{
	private ReasonResultArgument Contents;



	@Override
	public Object execute(Object o) {

		
		Contents = (ReasonResultArgument) o;
		
		return Contents.toString();
	}

}