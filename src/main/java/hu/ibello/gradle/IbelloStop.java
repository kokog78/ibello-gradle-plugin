package hu.ibello.gradle;

import java.util.List;

import org.gradle.api.tasks.TaskAction;

public class IbelloStop extends IbelloTask {

	@TaskAction
	public void run() {
		runProcess("stop");
	}
	
	@Override
	protected List<String> getCalculatedCommand(String command) {
		List<String> result = super.getCalculatedCommand(command);
		appendArgument(result, "--pid", getPidFile());
		return result;
	}
	
}