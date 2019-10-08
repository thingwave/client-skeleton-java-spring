# Arrowhead Client Skeletons (Java Spring-Boot)
##### The project provides client skeletons for the Arrowhead Framework 4.1.3

### How to use client skeletons?

Fork this repo and extend the skeletons with your own application code

### Requirements

The project has the following dependencies:
* JRE/JDK 11 [Download from here](https://www.oracle.com/technetwork/java/javase/downloads/jdk11-downloads-5066655.html)
* Maven 3.5+ [Download from here](http://maven.apache.org/download.cgi) | [Install guide](https://www.baeldung.com/install-maven-on-windows-linux-mac)

### Project structure

This is a multi module maven project relying on the [parent `pom.xml`](https://github.com/arrowhead-f/client-skeleton-java-spring/blob/master/pom.xml) which lists all the modules and common dependencies.

##### Modules:

* **client-skeleton-consumer**: client skeleton module with the purpose of initiating an orchestration request and consume the service from the chosen provider. This consumer project also contains a simple example about how to orchestrate and consume the service afterward.

* **client-skeleton-provider**: client skeleton module with the purpose of registrating a specific service into the Service Registry and running a web server where the service is available.

Skeletons are built on the [`Arrowhead Client Library`](https://github.com/arrowhead-f/client-library-java-spring) which is also imported to this project as a maven dependency. The client library provides the `ArrowheadService.class` which is a singleton spring managed bean and designed with the purpose of interacting with Arrowhead Framework. Use its methods by [autowiring](https://www.baeldung.com/spring-autowire) into your spring managed custom classes or use `ArrowheadBeans.getArrowheadService()` if your custom class is not spring managed.

Both client skeleton have a default 'ApplicationInitListener' and a defult 'SecurityConfig' what you can change or extend. The essential configuration has to be managed by customizing the `application.properties` file, located in `src/main/resources` folder.

##### Check [`sos-examples-spring`](https://github.com/arrowhead-f/sos-examples-spring) repository for full demo client implementations.
