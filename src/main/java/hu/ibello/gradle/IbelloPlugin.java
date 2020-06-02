package hu.ibello.gradle;

import org.gradle.api.Plugin;
import org.gradle.api.Project;

public class IbelloPlugin implements Plugin<Project> {

	@Override
	public void apply(Project project) {
		project.getExtensions().create("ibello", IbelloPluginExtension.class);
		
		registerTaskType(project, IbelloVersion.class);
		registerTaskType(project, IbelloCucumber.class);
		registerTaskType(project, IbelloRun.class);
		registerTaskType(project, IbelloClean.class);
		registerTaskType(project, IbelloUpdate.class);
		registerTaskType(project, IbelloDocgen.class);
		registerTaskType(project, IbelloStop.class);
		registerTaskType(project, IbelloHelp.class);
		
		registerObject(project, "CHROME", "chrome");
		registerObject(project, "FIREFOX", "firefox");
		registerObject(project, "OPERA", "opera");
		registerObject(project, "EDGE", "edge");
		
		createTask(project, "ibelloStop", IbelloStop.class, "Stops all running ibello processes and webdrivers.");
		
		createTask(project, "ibelloHelp", IbelloHelp.class, "Prints ibello help.");
		
		createTask(project, "ibelloVersion", IbelloVersion.class, "Prints ibello version number.");
		
		IbelloClean clean = createTask(project, "ibelloCleanAll", IbelloClean.class, "Clean all ibello test results.");
		clean.setKeep(0);
		
		IbelloUpdate update = createTask(project, "ibelloUpdateAll", IbelloUpdate.class, "Update all webdrivers and remove unused ones.");
		update.setRemove(true);
	}
	
	private void registerTaskType(Project project, Class<?> type) {
		registerObject(project, type.getSimpleName(), type);
	}

	private void registerObject(Project project, String name, Object value) {
		project.getExtensions().getExtraProperties().set(name, value);
	}
	
	private <T extends IbelloTask> T createTask(Project project, String name, Class<T> type, String description) {
		T task = project.getTasks().create(name, type);
		task.setGroup("ibello");
		task.setDescription(description);
		return task;
	}

}
