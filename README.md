**Dropwizard + Vaadin + Bootstrap + Liquibase example**

**Prerequisites**

Look for [detailed instructions](http://blog.onewip.com/dropwizard-vaadin-bootstrap) on prerequisites, compile and running.

**Compile**

`mvn vaadin:update-widgetset`

`mvn vaadin:update-theme`

`mvn vaadin:compile`

`mvn package`

**Run**

`java -jar target/event-1.0-SNAPSHOT.jar server src/configuration.yml`

