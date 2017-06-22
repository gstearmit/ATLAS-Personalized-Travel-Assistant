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

![alt text]()

## Dependencies and how to build ATLAS

ATLAS has been developed in Java, by using Eclipse as development IDE.
To import and run ATLAS, you need to download Eclipse (we suggest Luna or Neon), install Maven 3.2.x and install Java JDK 1.8.x.
NB: To use the Telegram chat-bot (which is required to run the demonstrator) you need to have a Telegram account. Both the mobile application
and the Desktop version of Telegram can be used.

The project dependencies are inside the lib folder. If required, add them to the project Build Path.

## How to configure

### General configuration
Please open the botProperties.txt and, in your Telegram, seach for the ATLAS chat-bot, by using the name parameter reported in this file.

### Mac configuration

In order to run the composer component inside the ATLAS demonstrator, wine (1.6.2) must be configured on te system, because the demostrator runs a small executable
included inside it, namely wsynth. Please see the composer-impl.jar dependency and modifiy the configuration.properties file inside it with the following properties 
(example values)

macWinePath=/opt/local/bin/wine macwsynthPath=/opt/local/bin/wsynth.exe

## How to run

The main entry point for the demonstrator is:

/demonstrator/src/main/java/eu/domainobjects/Demonstrator.java

when Demonstrator is started, select File -> Open Scenario from the window menu and the scenario will be loaded.

To run the demonstrator, which is sinchronized with the Telegram chat-bot please press STEP or PLAY on the demonstrator, and START on the chat-bot.




