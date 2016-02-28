# RgAdmin
Really Good Admin: SQL Client for browser

### Run

Fetch dependencies

`npm install`
`mvn install`

Compile React.js front end

`npm run watch`

Start application

`mvn exec:java -Dexec.mainClass="org.rgadmin.AppKt"`

Open browser

`open http://localhost:8080`

Optionally run embedded PostgreSQL server. Starts in port 5432

`mvn exec:java -Dexec.mainClass="org.rgadmin.embedded.EmbeddedPostgreSQLKt" -Dexec.classpathScope="test"`
