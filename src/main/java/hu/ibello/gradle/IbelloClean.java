package hu.ibello.gradle;

import java.util.List;

import org.gradle.api.tasks.TaskAction;

public class IbelloClean extends IbelloTask {

	private int keep = -1;
	
	public int getKeep() {
		return keep;
	}
	
	public void setKeep(int keep) {
		this.keep = keep;
	}
	
	@TaskAction
	public void run() {
		runProcess("clean");
	}
	
	@Override
	protected List<String> getCalculatedCommand(String command) {
		List<String> result = super.getCalculatedCommand(command);
		if (keep >= 0) {
			appendArgument(result, "--keep", Integer.toString(keep));
		}
		return result;
	}
	
}