package hu.ibello.gradle;

import java.io.File;
import java.util.Map;
import java.util.Set;

import org.gradle.api.DefaultTask;
import org.gradle.api.Project;
import org.gradle.api.artifacts.Configuration;
import org.gradle.api.artifacts.Dependency;
import org.gradle.api.artifacts.PublishArtifact;
import org.gradle.api.file.FileCollection;
import org.gradle.api.plugins.JavaPluginConvention;
import org.gradle.api.tasks.SourceSet;
import org.gradle.api.tasks.SourceSetContainer;
import org.gradle.api.tasks.TaskAction;

public class IbelloInternal extends DefaultTask {

	
	@TaskAction
	public void run() {
		Project project = getProject();
		
		System.out.println("Extension properties:");
		Map<String, Object> props = project.getExtensions().getExtraProperties().getProperties();
		for (String name : props.keySet()) {
			String line = String.format(" - %s: %s", name, props.get(name).getClass().getCanonicalName());
			System.out.println(line);
		}
		
		System.out.println("Configurations:");
		Set<String> configNames = project.getConfigurations().getNames();
		for (String name : configNames) {
			String line = String.format(" - %s", name);
			System.out.println(line);
			
			Configuration config = project.getConfigurations().getByName(name);
			
			for (PublishArtifact artifact : config.getArtifacts()) {
				line = String.format("   - artifact %s %s", artifact.getType(), artifact.getName());
				System.out.println(line);
			}
			
			for (Dependency dep : config.getDependencies()) {
				line = String.format("   - dependency %s:%s:%s", dep.getGroup(), dep.getName(), dep.getVersion());
				System.out.println(line);
			}
			
			if (config.isCanBeResolved()) {
				for (File file : config.getFiles()) {
					line = String.format("   - file %s", file.getAbsolutePath());
					System.out.println(line);
				}
			}
		}
		
		JavaPluginConvention java = project.getConvention().getPlugin(JavaPluginConvention.class);
		if (java != null) {
			System.out.println("Source sets:");
			SourceSetContainer sourceSets = java.getSourceSets();
			if (sourceSets != null) {
				for (String name : sourceSets.getNames()) {
					String line = String.format(" - %s", name);
					System.out.println(line);
					SourceSet src = sourceSets.getByName(name);
					if (src.getOutput() != null) {
						FileCollection coll = src.getOutput().getClassesDirs();
						if (coll != null) {
							for (File dir : coll) {
								line = String.format("    - classes %s", dir.getAbsoluteFile());
								System.out.println(line);
							}
						}
						File res = src.getOutput().getResourcesDir();
						if (res != null) {
							line = String.format("    - resources %s", res.getAbsoluteFile());
							System.out.println(line);
						}
					}
				}
			}
		}
	}
}
