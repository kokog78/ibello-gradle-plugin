package hu.ibello.gradle;

import org.gradle.api.Plugin;
import org.gradle.api.Project;

public class IbelloPlugin implements Plugin<Project>{

	@Override
	public void apply(Project project) {
		IbelloPluginExtension extension = project.getExtensions().create("ibello", IbelloPluginExtension.class);
		
		registerTaskType(project, IbelloVersion.class);
		registerTaskType(project, IbelloCucumber.class);
		registerTaskType(project, IbelloRun.class);
		registerTaskType(project, IbelloClean.class);
		registerTaskType(project, IbelloHelp.class);
	}
	
	private void registerTaskType(Project project, Class<?> type) {
		project.getExtensions().getExtraProperties().set(type.getSimpleName(), type);
	}

}
