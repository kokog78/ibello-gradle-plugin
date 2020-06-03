package hu.ibello.gradle;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.Executors;

import org.gradle.api.DefaultTask;
import org.gradle.api.tasks.TaskExecutionException;

public abstract class IbelloTask extends DefaultTask {
	
	private final static String PID_FILE = "./ibello/ibello.pid";
	
	private File directory;
	private File argumentsFile;
	
	public File getDirectory() {
		return directory;
	}
	
	public void setDirectory(File directory) {
		this.directory = directory;
	}
	
	public File getArgumentsFile() {
		return argumentsFile;
	}
	
	public void setArgumentsFile(File arguments) {
		this.argumentsFile = arguments;
	}

	protected void runProcess(String command) {
		ProcessBuilder builder = new ProcessBuilder();
		List<String> calculated = getCalculatedCommand(command);
		System.out.println("Running command: " + String.join(" ", calculated));
		builder.command(calculated);
		builder.directory(getProject().getProjectDir());
		builder.redirectErrorStream(true);
		Process process;
		try {
			process = builder.start();
		} catch (IOException ex) {
			throw new TaskExecutionException(this, ex);
		}
		StreamGobbler stdout = new StreamGobbler(process.getInputStream(), System.out::println);
		Executors.newSingleThreadExecutor().submit(stdout);
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
	
	protected List<String> getCalculatedCommand(String command) {
		List<String> result = new ArrayList<>();
		// script file
		File rootDir = calculateRootDirectory();
		if (rootDir != null) {
			File file = new File(rootDir, calculateCommandName());
			result.add(file.getAbsolutePath());
		} else {
			result.add(calculateCommandName());
		}
		// command
		result.add(command);
		// directory
		File dir = calculateDirectory();
		if (dir != null) {
			appendArgument(result, "--directory", dir.getAbsolutePath());
		}
		// arguments
		if (argumentsFile != null) {
			appendArgument(result, "--arguments", argumentsFile.getAbsolutePath());
		}
		// language
		String language = calculateLanguage();
		appendArgument(result, "--language", language);
		return result;
	}
	
	protected void appendArgument(List<String> arguments, String key, String value) {
		if (value != null) {
			value = value.trim();
			if (!value.isEmpty()) {
				arguments.add(key);
				if (value.contains(" ")) {
					value = String.format("\"%s\"", value);
				}
				arguments.add(value);
			}
		}
	}
	
	protected File calculateDirectory() {
		if (directory != null) {
			return directory;
		}
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
	
	protected String getPidFile() {
		return PID_FILE;
	}
	
	private IbelloPluginExtension getIbelloExtension() {
		return getProject().getExtensions().getByType(IbelloPluginExtension.class);
	}
	
	private boolean isWindows() {
		String os = System.getProperty("os.name", "generic").toLowerCase(Locale.ENGLISH);
		return os.indexOf("win") >= 0;
	}

}
