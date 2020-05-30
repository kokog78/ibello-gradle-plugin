package hu.ibello.gradle;

import java.util.List;

import org.gradle.api.tasks.TaskAction;

public class IbelloRun extends IbelloTestTask {

	private String java;
	
	public String getJava() {
		return java;
	}
	
	public void setJava(String java) {
		this.java = java;
	}

	@TaskAction
	public void run() {
		runProcess("run");
	}
	
	@Override
	protected List<String> getCalculatedCommand(String command) {
		List<String> result = super.getCalculatedCommand(command);
		appendArgument(result, "--java", java);
		return result;
	}
	
}
