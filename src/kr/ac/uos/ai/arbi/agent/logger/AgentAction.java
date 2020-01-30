package kr.ac.uos.ai.arbi.agent.logger;

import kr.ac.uos.ai.arbi.ltm.DataSource;

public class AgentAction implements ActionBody {

	private String type;

	private ActionBody currentAction;

	private ActionBody normalAction;
	private ActionBody loggingAction;

	public AgentAction(String type, ActionBody normalAction) {
		this.type = type;
		this.normalAction = normalAction;
		this.currentAction = normalAction;
	}

	public void initLoggingFunction(DataSource dataSource, String actor, String action, LogTiming logTiming) {
		switch (logTiming) {
		case Prior:
			loggingAction = new LoggingActionPrior(dataSource, actor, this.type, action, this.normalAction);
			break;
		case Later:
			loggingAction = new LoggingActionLater(dataSource, actor, this.type, action, this.normalAction);
			break;
		case Both:
			loggingAction = new LoggingActionBoth(dataSource, actor, this.type, action, this.normalAction);
			break;
		case NonAction:
			loggingAction = new LoggingActionNonAction(dataSource, actor, this.type, action);
			currentAction = loggingAction;
			break;
		default:
			loggingAction = new LoggingActionPrior(dataSource, actor, this.type, action, this.normalAction);
		}
	}

	public String getType() {
		return this.type;
	}

	public void changeAction(boolean flag) {
		if (flag) {
			currentAction = loggingAction;
		} else {
			currentAction = normalAction;
		}
	}

	public ActionBody getFunction() {
		return currentAction;
	}

	@Override
	public Object execute(Object o) {
		// TODO Auto-generated method stub
		return currentAction.execute(o);
	}

}
