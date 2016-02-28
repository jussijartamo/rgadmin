# RgAdmin
Really Good Admin: SQL Client for browser

### Run

1. `npm install`
2. `mvn install`

Compile React.js front end
3. `npm run watch`

Start application
4. `mvn exec:java -Dexec.mainClass="org.rgadmin.AppKt"`

Optionally run embedded PostgreSQL server. Starts in port 5432
3. `mvn exec:java -Dexec.mainClass="org.rgadmin.embedded.EmbeddedPostgreSQLKt" -Dexec.classpathScope="test"`
