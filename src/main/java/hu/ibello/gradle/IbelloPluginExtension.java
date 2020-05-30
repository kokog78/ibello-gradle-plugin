package hu.ibello.gradle;

import java.io.File;

public class IbelloPluginExtension {

	private File installDir;
	private File directory;
	private String language;
	
	public File getInstallDir() {
		return installDir;
	}
	
	public void setInstallDir(File installDir) {
		this.installDir = installDir;
	}
	
	public File getDirectory() {
		return directory;
	}
	
	public void setDirectory(File directory) {
		this.directory = directory;
	}
	
	public String getLanguage() {
		return language;
	}
	
	public void setLanguage(String language) {
		this.language = language;
	}
}
