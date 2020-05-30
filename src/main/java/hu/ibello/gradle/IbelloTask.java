package hu.ibello.gradle;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.gradle.api.DefaultTask;
import org.gradle.api.tasks.TaskExecutionException;

public abstract class IbelloTask extends DefaultTask {

	protected void runProcess(String ... args) {
		ProcessBuilder builder = new ProcessBuilder();
		builder.command(getCalculatedCommand(args));
		builder.directory(getProject().getProjectDir());
		Process process;
		try {
			process = builder.start();
		} catch (IOException ex) {
			throw new TaskExecutionException(this, ex);
		}
		try {
			int exitCode = process.waitFor();
			if (exitCode > 0) {
				throw new TaskExecutionException(this, null);
			}
		} catch (InterruptedException ex) {
			// TODO stop ibello
			ex.printStackTrace();
		}
	}
	
	private List<String> getCalculatedCommand(String ... args) {
		List<String> result = new ArrayList<>();
		// command
		File rootDir = calculateRootDirectory();
		if (rootDir != null) {
			File file = new File(rootDir, calculateCommandName());
			result.add(file.getAbsolutePath());
		} else {
			result.add(calculateCommandName());
		}
		// directory
		File dir = calculateDirectory();
		if (dir != null) {
			result.add("--directory");
			result.add(dir.getAbsolutePath());
		}
		// language
		String language = calculateLanguage();
		if (language != null && !language.trim().isEmpty()) {
			result.add("--language");
			result.add(language.trim());
		}
		// other arguments
		for (String arg : args) {
			result.add(arg);
		}
		return result;
	}
	
	protected File calculateDirectory() {
		IbelloPluginExtension extension = getIbelloExtension();
		if (extension != null && extension.getDirectory() != null) {
			return extension.getDirectory();
		}
		return null;
	}
	
	protected File calculateRootDirectory() {
		IbelloPluginExtension extension = getIbelloExtension();
		if (extension != null) {
			return extension.getInstallDir();
		}
		return null;
	}
	
	protected String calculateLanguage() {
		IbelloPluginExtension extension = getIbelloExtension();
		if (extension != null) {
			return extension.getLanguage();
		}
		return null;
	}
	
	private String calculateCommandName() {
		if (isWindows()) {
			return "ibello.cmd";
		} else {
			return "ibello";
		}
	}
	
	private IbelloPluginExtension getIbelloExtension() {
		return getProject().getExtensions().getByType(IbelloPluginExtension.class);
	}
	
	private boolean isWindows() {
		String OS = System.getProperty("os.name", "generic").toLowerCase(Locale.ENGLISH);
		return OS.indexOf("win") >= 0;
	}

}
