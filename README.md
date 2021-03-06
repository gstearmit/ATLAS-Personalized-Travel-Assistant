# ATLAS-Personalized-Travel-Assistant

Nowadays, users can count on a large amount of mobility services offering
disparate functionalities and providing all needed information. Yet, from a
user perspective, properly exploiting the available mobility services to organize
journeys that meet personal expectations, is becoming a complex task. Indeed,
discover, compare, and select the appropriate services in an open and constantly
expanding domain, is a challenging and time-consuming task. We claim that a
uniform and easy way for exploiting these services while moving around, getting
accurate and personalized information is still missing. In this paper we propose
a platform allowing for the definition of value-added mobility services by
(i) enhancing interoperability among the existing services, (ii) supporting their
execution via run-time adaptation, (iii) through the definition of multi-channel
front-end applications. The platform leverages on an adaptive by-design approach
for distributed service-based systems, called Domain Object model, which uses
advanced techniques for dynamic service adaptation. To show our platform in action,
we have implemented and evaluated a world-wide travel assistant supporting
users for the whole travel duration.

## ATLAS Overview

<p align="center">
  <img src="https://github.com/das-fbk/ATLAS-Personalized-Travel-Assistant/blob/master/DEMO_Overview.png" width="650"/>
</p>

## Contributors

This study has been designed, developed, and reported by the following investigators:

* <b> Antonio Bucchiarone </b> - Fondazione Bruno Kessler (FBK, Trento - Italy)

* <b> Martina De Sanctis </b> - Gran Sasso Science Institute - GSSI, L'Aquila, Italy

* <b> Michael Dolzani </b> - University of Trento, Italy

* <b> Annapaola Marconi </b> - Fondazione Bruno Kessler

For any information, interested researchers can contact us by writing an email to martina.desanctis@gssi.it.


## Dependencies and how to build ATLAS

ATLAS has been developed in Java, by using Eclipse as development IDE.
To import and run ATLAS, you need to download Eclipse (we suggest Luna or Neon), install Maven 3.2.x and install Java JDK 1.8.x.

NB: To use the Telegram chat-bot (which is required to run the demonstrator) you need to have a Telegram account. Both the mobile application and the Desktop version of Telegram can be used.

The project dependencies are inside the lib folder. If required, add them to the project Build Path.

## How to configure

### General configuration
Please open the <i>botProperties.txt</i> and, in your Telegram, seach for the ATLAS chat-bot, by using the name parameter reported in this file.

### Mac configuration

In order to run the composer component inside the ATLAS demonstrator, wine (1.6.2) must be configured on the system, because the demostrator runs a small executable
included inside it, namely wsynth. Please see the composer-impl.jar dependency and modifiy the <i>configuration.properties</i> file inside it with the following properties 
(example values)

macWinePath=/opt/local/bin/wine macwsynthPath=/opt/local/bin/wsynth.exe

## How to run

In order to run the demonstrator, a Google API key is required (please see https://developers.google.com/maps/documentation/geocoding/get-api-key). Once you get your own key, open the file <i>requiredKeys.txt</i> and insert your Google key in place of the string <i><put_your_key_here></i>.
  
The rome2rio key is available, but it would be better if you get and use your own rome2rio key, by updating the <i>requiredKeys.txt</i> file. See https://www.rome2rio.com .

The main entry point for the demonstrator is:

/demonstrator/src/main/java/eu/domainobjects/Demonstrator.java

when the ATLAS demonstrator is started, select <b>File</b> -> <b>Open Scenario</b> from the window menu and the scenario will be loaded.

To run the demonstrator, which is sinchronized with the Telegram chat-bot please press <i>STEP</i> or <i>PLAY</i> on the demonstrator, and <i>START</i> on the chat-bot.

## License

Please refer to the ATLAS/COPYRIGHT file.

## ATLAS demonstrator and Chat-bot execution Examples

### Demonstrator Interface

Domain Objects Models Tab: Example of a <i>fragment</i>

![alt text](https://github.com/das-fbk/ATLAS-Personalized-Travel-Assistant/blob/master/demoFragmentModel.PNG)

Domain Objects Models Tab: Example of a <i>domain property</i>

![alt text](https://github.com/das-fbk/ATLAS-Personalized-Travel-Assistant/blob/master/demoDomainPropertyModel.PNG)

Domain Objects Runtime Execution Tab

![alt text](https://github.com/das-fbk/ATLAS-Personalized-Travel-Assistant/blob/master/demoUserProcess.PNG)

<b>Travel Assistant Process</b>: Execution of the Selected Planning Mode (Global in the example)

![alt text](https://github.com/das-fbk/ATLAS-Personalized-Travel-Assistant/blob/master/demoGlobalPlanning.PNG)

<b>User Process</b>: Execution of the dynamically selected mobility transport service

![alt text](https://github.com/das-fbk/ATLAS-Personalized-Travel-Assistant/blob/master/demoRideSharing.PNG)

### Telegram chat-bot Interface

ATLAS chat-bot start

<p align="center">
  <img src="https://github.com/das-fbk/ATLAS-Personalized-Travel-Assistant/blob/master/1_ChatBotStart.png" width="350"/>
</p>

ATLAS user input insertion

<p align="center">
  <img src="https://github.com/das-fbk/ATLAS-Personalized-Travel-Assistant/blob/master/3_ChatBotInput.png" width="350"/>
</p>

ATLAS Planners Result

<p align="center">
  <img src="https://github.com/das-fbk/ATLAS-Personalized-Travel-Assistant/blob/master/ATLAS_resultList.png" width="350"/>
</p>

ATLAS Mobility Services Details results

<p align="center">
  <img src="https://github.com/das-fbk/ATLAS-Personalized-Travel-Assistant/blob/master/ATLAS_choosenSolution.png" width="350"/>
</p>








