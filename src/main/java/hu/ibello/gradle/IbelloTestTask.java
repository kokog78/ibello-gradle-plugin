package hu.ibello.gradle;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.gradle.api.artifacts.Configuration;
import org.gradle.api.artifacts.ConfigurationContainer;
import org.gradle.api.file.FileCollection;
import org.gradle.api.plugins.JavaPluginConvention;
import org.gradle.api.tasks.SourceSet;

public abstract class IbelloTestTask extends IbelloTask {
	
	private String[] tags;
	private boolean headless;
	private String browser;
	private int[] size;
	private int repeat = 0;
	
	public String[] getTags() {
		return tags;
	}
	
	public void setTags(String ... tags) {
		this.tags = tags;
	}
	
	public boolean isHeadless() {
		return headless;
	}
	
	public void setHeadless(boolean headless) {
		this.headless = headless;
	}
	
	public String getBrowser() {
		return browser;
	}
	
	public void setBrowser(String browser) {
		this.browser = browser;
	}
	
	public int[] getSize() {
		return size;
	}
	
	public void setSize(int x, int y) {
		this.size = new int[] {x, y};
	}
	
	public int getRepeat() {
		return repeat;
	}
	
	public void setRepeat(int repeat) {
		this.repeat = repeat;
	}
	
	@Override
	protected List<String> getCalculatedCommand(String command) {
		List<String> result = super.getCalculatedCommand(command);
		if (headless) {
			result.add("--headless");
		}
		if (tags != null) {
			for (String tag : tags) {
				appendArgument(result, "--tag", tag);
			}
		}
		appendArgument(result, "--browser", browser);
		if (size != null) {
			String value = String.format("%dx%d", size[0], size[1]);
			appendArgument(result, "--size", value);
		}
		if (repeat > 1) {
			appendArgument(result, "--repeat", Integer.toString(repeat));
		}
		appendArgument(result, "--pid", getPidFile());
		for (File file : getDependencyFiles()) {
			appendArgument(result, "--classpath", file.getAbsolutePath());
		}
		return result;
	}
	
	private List<File> getDependencyFiles() {
		List<File> result = new ArrayList<>();
		List<Configuration> configurations = getConfigurations();
		for (Configuration config : configurations) {
			Set<File> files = config.getFiles();
			if (files != null) {
				for (File file : files) {
					if (file.getName().startsWith("ibello")) {
						// skip this file
					} else {
						result.add(file);
					}
				}
			}
		}
		SourceSet sourceSet = getMainSourceSet();
		if (sourceSet != null && sourceSet.getOutput() != null) {
			FileCollection files = sourceSet.getOutput().getClassesDirs();
			if (files != null) {
				for (File dir : files) {
					result.add(dir);
				}
			}
			if (sourceSet.getOutput().getResourcesDir() != null) {
				result.add(sourceSet.getOutput().getResourcesDir());
			}
		}
		Collections.sort(result);
		return result;
	}
	
	private List<Configuration> getConfigurations() {
		List<Configuration> result = new ArrayList<>();
		ConfigurationContainer configurations = getProject().getConfigurations();
		if (configurations != null) {
			Configuration config = getResolvedConfiguration(configurations, "runtime");
			if (config != null) {
				result.add(config);
			}
			config = getResolvedConfiguration(configurations, "runtimeClasspath");
			if (config != null) {
				result.add(config);
			}
			config = getResolvedConfiguration(configurations, "implementation");
			if (config != null) {
				result.add(config);
			}
			config = getResolvedConfiguration(configurations, "default");
			if (config != null) {
				result.add(config);
			}
		}
		return result;
	}
	
	private Configuration getResolvedConfiguration(ConfigurationContainer configurations, String name) {
		Configuration config = configurations.getByName(name);
		if (config != null && config.isCanBeResolved()) {
			return config;
		} else {
			return null;
		}
	}
	
	private SourceSet getMainSourceSet() {
		JavaPluginConvention java = getProject().getConvention().getPlugin(JavaPluginConvention.class);
		if (java != null) {
			return java.getSourceSets().getByName("main");
		}
		return null;
	}
	
}
