package hu.ibello.gradle;

import java.io.File;
import java.util.List;

import org.gradle.api.tasks.TaskAction;

public class IbelloCucumber extends IbelloTestTask {

	private File featuresDir;
	private String java;
	
	public File getFeaturesDir() {
		return featuresDir;
	}
	
	public void setFeaturesDir(File features) {
		this.featuresDir = features;
	}
	
	public String getJava() {
		return java;
	}
	
	public void setJava(String java) {
		this.java = java;
	}
	
	@TaskAction
	public void run() {
		runProcess("cucumber");
	}
	
	@Override
	protected List<String> getCalculatedCommand(String command) {
		List<String> result = super.getCalculatedCommand(command);
		appendArgument(result, "--java", java);
		if (featuresDir != null) {
			appendArgument(result, "--features", featuresDir.getAbsolutePath());
		}
		return result;
	}
	
}
