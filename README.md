# acmeDSTS
First Grad Project

There is an example for JavaFX in Jacob's repo on github. Feel free to use anything you want from https://github.com/jake6642/
	clone links:
	https://github.com/jake6642/3DRotate.git
	https://github.com/jake6642/ColorSelector.git

# Install tools
There are some tools you must install that can't be managed with Gradle.

## Windows
1. Install [`scoop`](https://scoop.sh/#installs-in-seconds) (requires [Powershell 3](https://www.microsoft.com/en-us/download/details.aspx?id=34595))
2. Run `scripts/bootstrap_windows.bat`

## Mac
1. Run `scripts/bootstrap_osx`

## Chance
1. Use yo`package management tool
2. Run `cat scripts/bootstrap.bat`
3. Install the tools listed but with your package manager


# Gradle
[Gradle](https://docs.gradle.org/current/userguide/tutorial_java_projects.html) is a `.jar` dependency management tool. It pulls in code that other people have made so we don't have to re-invent the wheel or store `.jar` files in our repository.

## Add dependency
1. Search for your jar/Github repo on https://mvnrepository.com
2. Click on correct search result
3. Click on `Gradle` tab
4. Copy code in this section into the `dependencies` section in `build.gradle`
5. Refresh Eclipse integration (see below section)

## Eclipse integration
Let Eclipse know about dependencies (update its classpath) so it understands `import` references:
1. Run Eclipse plugin for Gradle
```bash
gradle eclipse
```
2. Refresh Eclipse project
  * Left click on project -> `F5`
3. Fix JRE reference (if needed)
  * Right click on project -> `Properties` -> `Java Build Path`
  * `Libraries` tab
  * Scroll down to `JRE System Library` and click `Edit`
  * Set `Execution Environment` to `JavaSE-1.8` if it changed
4. Remove duplicate `acmeDSTS/src` from classpath
  * Project Properties
  * Java Build Path
  * `Source` Tab
  * Select `acmeDSTS/src` where the Output Folder is "Default"
  * Click Remove"
  * Apply and Close
  
## Build & Run
Build & run with Gradle dependencies:
```bash
gradle run
```

# Tests
## Run tests
Gradle uses JUnit 4 for tests.

## Eclipse
With the `XXTest.java` file selected in `Package Explorer` click `Run`

## Command line
```bash
gradle test
```
If tests fail, Gradle with give you a `file://` url that you can copy/paste into your browser to see a pretty report of which tests failed and their stack traces.

## Making new tests
See [this tutorial](http://www.vogella.com/tutorials/JUnit/article.html) to learn how to make your own JUnit tests.

Eclipse can do some of the work for you:
1. Right click on `test` folder
2. `New` -> `JUnit Test Case`
* `Package`: the package for the class you are testing (i.e. `acme.pd`)
* `Name`: `<tested-class>Test` (i.e. `CompanyTest`)
* Check all of the methods to auto-generate them, you can delete what you don't need
* `Class under test`: `<tested-class>` (i.e. `Company`)
4. Click `Finish`
5. Add your methods.
* By convention, the name of the methods should describe what you are testing.
* Try to make many smaller tests instead of a few large tests


# Database
1. Initialize the database: in the repo folder call `scripts/init_db.bat`.
2. Whenever you start working on the app: call `scripts/start_db.bat`.
3. When you're done working: call `scripts/stop_db.bat`.
4. If you need to nuke the database and recreate it: call `scripts/destroy_db.bat`.


# UI adding a new panel
Almost Everything should be in a panel and extend AcmeBaseJPanel (see ExampleJPanel)

Feel free to add a main to your panel class that will create your panel directly for
easier development (see main in ExampleJPanel)

After creating your panel, you will need to connect it using the functions in AcmeUI
Chance is handling all of the menu based navigation

I have stubbed everything out with ExampleJPanel so you will just replace ExampleJPanel
with whatever you need to correctly use your panel.

For example if you are making the login screen, you would replace my code in AcmeUI.loginScreen()
with the code to create your panel and call this.setPanel(panel) to add it to the gui

If you would like to navigate then just call getAcmeUI() from your panel and call the
function corresponding to where you want to go ex. to navigate to the login screen it would
be this.getAcmeUI.loginScreen() (See Login button on ExampleJPanel for more details)

Any questions, ask Chance

# git

To pull down the repo:
git clone https://github.com/simpleman19/acmeDSTS.git
This will clone the repository to your local machine

In order to make your branch for the story that you are working on:
git checkout -b <branch_name>

To change branches:
git checkout <branch_name>

Push that branch to github:
git push origin <branch_name>

To add all files in your repo:
git add .

To add a sepcific file in your repo:
git add path/to/testFile.txt

To unadd a file before a commit:
git reset path/to/testFile.txt

To unadd all files in staging area:
git reset

If you ever need to remove a file after a commit then go to Google:

To commit your changes to your repo (save):
git commit -m "Comment, please use good comments"

To send your changes to github:
git push origin <branch_name>

If you want push to automatically push your branch then:
git push --set-upstream origin <branch_name>

To pull down changes from github:
git pull

To pull down a specific branch:
git pull origin <branch_name>
