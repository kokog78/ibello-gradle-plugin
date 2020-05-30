package hu.ibello.gradle;

import org.gradle.api.tasks.TaskAction;

public class IbelloHelp extends IbelloTask {

	@TaskAction
	public void run() {
		System.out.println("root dir: " + calculateRootDirectory());
		System.out.println("base dir: " + calculateDirectory());
		System.out.println("language: " + calculateLanguage());
	}
	
}