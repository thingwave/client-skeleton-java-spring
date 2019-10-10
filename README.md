# Arrowhead Client Skeletons (Java Spring-Boot)
##### The project provides client skeletons for the Arrowhead Framework 4.1.3

### How to use client skeletons?

Fork this repo and extend the skeletons with your own application code. ([check the best practice recommendations](https://github.com/arrowhead-f/client-skeleton-java-spring/blob/development/README.md#best-practices-to-start-with-the-skeletons))

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

### Best practices to start with the skeletons

##### (1st) apllicaction.properties
Location: `src/main/resources`
* Decide the required security level and set the `server.ssl.enabled` and `token.security.filter.enabled` properties accordingly.
* Create your own client certificate (or for demo purpuse use the provided one) and update the further `server.ssl...` properties accordingly. *(**Note** that `server.ssl.key-store-password` and `server.ssl.key-password` must be the same.)*
* Change the `client_system_name` property to your system name. *(**Note** that it should be in line with your certificate common name e.g.: when your certificate common name is `my_awesome_client.my_cloud.my_company.arrowhed.eu`, then your system name is  `my_awesome_client`)*
* Adjust the Service Registry Core System location by the `sr_address` and `sr_port` properties.
* In case of a provider you have to set it's web-server parameters by the `server.address` and `server.port` properties.
* In case of a consumer decide whether it should act as a web-server or not. If yes, then set the `spring.main.web-application-type` to 'servlet' and set the further server parameters like in the provider case. If not, just left these properties untouched.

##### (2nd) package structure
All the provided skeleton classes are located in the child packages of `eu.arrowhead` base package.
* You can create your own classes  under this base package or
* You can create your own packages like `com.my_company.my_awesome_project` to organize the skeleton and the application code separated. In the latter case if you wish to use spring beans at your custom packages, then you have to let spring framework known about your base package(s). This can be managed by adding the base package name(s) as a string value(s) in the `@ComponentScan` annotaion of the application's `Main.class` *(**Look for the 'TODO' mark** within the main class)*.

##### (3rd) security configuration
The skeletons provide a built-in arrowhed framework compatible security configuration located in `eu.arrowhead.client.skeleton.consumer|provider.security` package.
* The `ConsumerSecurityConfig.class` and the `ProviderSecurityConfig.class` extend the `DefaultSecurityConfig.class` which is imported by the client-library dependency and responsible for setting the `server.ssl.enabled` property declared in the `application.properties`. (**Note** that the `ConsumerSecurityConfig.class` is became effective only when your consumer is a web-server.)*
* The `ConsumerAccessControlFilter.class` and the `ProviderAccessControlFilter.class` extend the `AccessControlFilter.class` which is imported by the client-library dependency and responsible for setting the security level based on the `application.properties`.
  -  `ConsumerAccessControlFilter.class` is became effective only when your consumer is a web-server and `server.ssl.enabled` property is true. This filter is responsible for validating whether the received https request is coming from one of the local cloud's client based on its certificate. *(**Look for the 'TODO' mark** within this class if you want to implement additional access rules.)*
  - `ProviderAccessControlFilter.class` is doing the same as described in the consumer case, but became effective only when `server.ssl.enabled` property is true and `token.security.filter.enabled` property is false. When `token.security.filter.enabled` property is true, then `ProviderTokenSecurityFilter.class` became effective which is validating whether a token is received within the https request and whether it is a valid one ore not. *(**Note** that the token is created by the Authorization Core System during the orchestration process and the consumer have to put it into its https request as a query paramater.)*

##### (4th) start-up & shutdown configuration
The skeletons provide a built-in application start-up and shutdown configuration located in `eu.arrowhead.client.skeleton.consumer|provider` package.
The `ConsumerApplicationInitListener.class` and the `ProviderApplicationInitListener.class` contains the `customInit()` method which is executed automatically right after the application start-up and also the `customDestroy()` method which is executed automatically right after triggering the application shutdown, but still before the final stop. *(**Look for the 'TODO' marks** within these classes if you want to implement additional logics.)*

Already implemented Consumer start-up logics:
* Checking the Service Registry Core System reachability. *(Sends an 'echo' request to the server.)*
* Checking the Orchestrator Core System reachability. *(Sends an 'echo' request to the server.)*
* Querying and storing the public service URIs of Orchestrator Core System. *(Sends 'query' requests to the Service Registry.)*

Already implemented Provider start-up logics:
* Checking the Service Registry Core System reachability. *(Sends an 'echo' request to the server.)*
* Checking the Authorization Core System reachability if SSL enabled. *(Sends an 'echo' request to the server.)*
* Turning on the token security filter if it is enabled.
* Querying and storing the 'public-key' service URI of Authorization Core System if token security filter is enabled. *(Sends a 'query' request to the Service Registry.)*
Recommended Provider start-up logics:
* Registering the provided service into the Service Registry Core System. *(**Hint:** Use the `forceRegisterServiceToServiceRegistry()` method from `ArrowheadService.class`. It removes your current service registry entry from the database and register again, so it ensures that if your service interfaces or the meta data have been changed, then the freshest condition will be published.)*
