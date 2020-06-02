# ibello-gradle-plugin

This gradle plugin helps us to execute and manage ibello tests. To read more about ibello, please visit

https://github.com/kokog78/ibello-api

and

https://ibello.hu

Version: 1.0.

## Configuration

In the `build.gradle` file, you need to add these lines:

```groovy
buildscript {
    repositories {
        mavenCentral()
    }
    dependencies {
        classpath 'hu.ibello:ibello-gradle-plugin:1.0'
    }
}

apply plugin: 'hu.ibello.gradle'
```

## General parameters

You have a top-level configuration block for general parameters:

```groovy
ibello {
	installDir file('/path/to/my/ibello/installation')
    directory file('/path/to/my/project')
	language 'hu'
}
```

A few words about these parameters:

- `installDir`: you need to specify a file here which points to the ibello installation directory (where the `ibello.sh` and `ibello.cmd` scripts are). You can skip this if you added the ibello to the PATH of your operating system.
- `directory`: you need to specify a file here which points to the ibello project (where the `ibello` folder is). You can skip this and the plugin will use the current working directory.
- `language`: language code, really optional. If you do not specify it then the default 'en' will be used.

## Predefined tasks

The plugin will add a few tasks to your gradle environment. All of them are in the "ibello" group.

- `ibelloVersion`: prints the ibello version.
- `ibelloHelp`: prints help about the ibello command line options.
- `ibelloCleanAll`: removes all test results from the project.
- `ibelloUpdateAll`: updates all available webdrivers and removes old ones. If you installed ibello as administrator then you need to have administrative privileges to run this task.
- `ibelloStop`: stops are ongoing test runs and webdrivers. Some CI systems are stopping ibello too forcefully if you manually stops the build process. You can add this task to the end of your continuous integration plan to be sure that no webdrivers are left behind.

## Predefined task types

### General task types

Many task types are available in the plugin. Each of them has some common properties:

- `directory`: same as in the global settings, you just can specify the ibello project for each tasks if you want.
- `argumentsFile`: should be a file, ibello will load it's arguments from this file. You can read more about it in the documentation: https://ibello.hu/ibello/public/#documentation-cli.

Here are the task types:

- `IbelloVersion`: just prints the ibello version.
- `IbelloHelp`: prints the help message.
- `IbelloClean`: removes old test results from the project. It has a parameter:
  - `keep`: how many test results you want to keep? The newest ones will survive.
- `IbelloUpdate`: updates webdrivers. If you installed ibello as administrator then you need to have administrative privileges to run task with this type. It has 2 additional parameters:
  - `browser`: the type of the browser, it can be: `CHROME`, `FIREFOX`, `OPERA`, `EDGE`. The plugin defines these variable for you. If you do not specify it, all available webdrivers will be updated.
  - `remove`: set it to `true` if you want to remove unused webdrivers.
- `IbelloStop`: stops all tests started from the same ibello directory.
- `IbelloDocgen`: documentation generation with ibello. You can read more about it here: https://ibello.hu/ibello/public/#documentation-docgen. Additional parameters:
  - `inputFile`: the input file (as a `File` instance).
  - `outputFile`: the output file (as a `File` instance). Optional, if you do not specify it then ibello will find out automatically, and the generated documentation will be in the `ibello/output` folder.
  - `overwrite`: a boolean parameter, you need to specify it if you want to forcefully overwrite existing files.

Examples:

This task removes old test results, only the latest 20 will be available.

```groovy
task cleanOldResults(type: IbelloClean, group: 'ibello') {
	keep 20
}
```

This task generates documentation:

```groovy
task generateFancyDocs(type: IbelloDocgen, group: 'ibello') {
	inputFile file('ibello/docs/project.json')
	overwrite true
}
```

### Testing task types

The tasks below are running tests and they have some extra parameters. These ones:

- `tags`: list of ibello tags. Read more about tags in the documentation: https://ibello.hu/ibello/public/#documentation-configuration.
- `browser`: the type of the browser you want to use. It's value can be: `CHROME`, `FIREFOX`, `OPERA`, `EDGE`. The plugin defines these variable for you.
- `headless`: a boolean parameter, if you want to run your tests in headless mode (without a browser window), you need to set it to `true`.
- `size`: default size of the browser window (as a numeric list), for example "1024, 768".
- `repeat`: repeat number of your tests, an integer value.

The task types are:

- `IbelloRun`: starts a java test. It has one more parameter:
  - `java`: you can specify the class/package names of you specifications, for example "\*Smoke\*". The "\*" character is a wildchar.
- `IbelloCucumber`: starts a cucumber test. It has 2 additional parameters:
  - `java`: you can specify the java package name of your glue-code here as a string.
  - `featuresDir`: you can specify the directory of your feature files if it is not the default one (the `features` subdirectory of you project).

Example:

```groovy
task mySmokeTestsWithIbello(type: IbelloRun, group: 'ibello') {
	java '*Smoke*'
	browser CHROME
	headless true
    tags 'one', 'two', 'three'
}
```

