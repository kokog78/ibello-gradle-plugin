package hu.ibello.gradle;

import org.gradle.api.tasks.TaskAction;

public class IbelloHelp extends IbelloTask {

	@TaskAction
	public void run() {
		runProcess("help");
	}
	
}