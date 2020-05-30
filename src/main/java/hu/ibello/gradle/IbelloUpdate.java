package hu.ibello.gradle;

import java.util.List;

import org.gradle.api.tasks.TaskAction;

public class IbelloUpdate extends IbelloTask {

	private String browser;
	
	private boolean remove;
	
	public String getBrowser() {
		return browser;
	}
	
	public void setBrowser(String browser) {
		this.browser = browser;
	}
	
	public boolean isRemove() {
		return remove;
	}
	
	public void setRemove(boolean remove) {
		this.remove = remove;
	}
	
	@TaskAction
	public void run() {
		runProcess("update");
	}
	
	@Override
	protected List<String> getCalculatedCommand(String command) {
		List<String> result = super.getCalculatedCommand(command);
		appendArgument(result, "--browser", browser);
		if (remove) {
			result.add("--remove");
		}
		return result;
	}
}
