# Domain Objects Demonstrator

IMPORTANT NOTE: this version have manual dependencies inside lib folder, please read with attention following notes


## Dependencies

Project dependencies are inside lib folder, please add them to your Eclipse project


## How to build

To build Domain Objects Demonstrator you need to install Maven 3.2.x, Java JDK 1.8.x, Eclipse and set
dependencies.


## How to configure

### General configuration

Please open src/main/resources/demonstrator.properties and change value of property *workingDir* to your current resource directory if you run inside eclipse or if you run outside it just configure to point using absolute path.


### Mac configuration

In order to run composer component inside Domain Objects demonstrator, on system must be configured wine (1.6.2), because runs 
a small executable included inside demonstrator, wsynth.
Please see composer-impl.jar dependency and modify configuration.properties inside with following properties (example values)

macWinePath=/opt/local/bin/wine
macwsynthPath=/opt/local/bin/wsynth.exe



# How to run

Main entry point for demonstrator is here:

/demonstrator/src/main/java/eu/domainobjects/Demonstrator.java

when Demonstrator is started, select File-> Open from menu and select scenario file inside repository:

<WORKSPACE>\demonstrator\src\main\resources\storyboard1\Storyboard1-main.xml

To run example process, please press STEP

