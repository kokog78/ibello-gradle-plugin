package hu.ibello.gradle;

import java.io.File;
import java.util.List;

import org.gradle.api.tasks.TaskAction;

public class IbelloDocgen extends IbelloTask {

	private File inputFile;
	
	private File outputFile;
	
	private boolean overwrite;
	
	public File getInputFile() {
		return inputFile;
	}
	
	public void setInputFile(File inputFile) {
		this.inputFile = inputFile;
	}
	
	public File getOutputFile() {
		return outputFile;
	}
	
	public void setOutputFile(File outputFile) {
		this.outputFile = outputFile;
	}
	
	public boolean isOverwrite() {
		return overwrite;
	}
	
	public void setOverwrite(boolean overwrite) {
		this.overwrite = overwrite;
	}
	
	@TaskAction
	public void run() {
		runProcess("docgen");
	}
	
	@Override
	protected List<String> getCalculatedCommand(String command) {
		List<String> result = super.getCalculatedCommand(command);
		if (inputFile != null) {
			appendArgument(result, "--input", inputFile.getAbsolutePath());
		}
		if (outputFile != null) {
			appendArgument(result, "--output", outputFile.getAbsolutePath());
		}
		if (overwrite) {
			result.add("--overwrite");
		}
		return result;
	}
}
