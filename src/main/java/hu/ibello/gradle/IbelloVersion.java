package hu.ibello.gradle;

import org.gradle.api.tasks.TaskAction;

public class IbelloVersion extends IbelloTask {

	@TaskAction
	public void run() {
		runProcess("version");
	}
	
}