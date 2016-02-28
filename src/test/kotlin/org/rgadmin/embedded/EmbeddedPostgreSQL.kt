package org.rgadmin.embedded

import org.dalesbred.Database
import org.postgresql.ds.PGSimpleDataSource
import ru.yandex.qatools.embed.postgresql.PostgresProcess
import ru.yandex.qatools.embed.postgresql.PostgresStarter
import ru.yandex.qatools.embed.postgresql.config.AbstractPostgresConfig
import ru.yandex.qatools.embed.postgresql.config.PostgresConfig
import ru.yandex.qatools.embed.postgresql.distribution.Version
import java.sql.DriverManager
import java.util.*

class EmbeddedPostgreSQL(val port: Int) {
    val username = "test"
    val password = "test"
    val config =
            PostgresConfig(Version.Main.PRODUCTION, AbstractPostgresConfig.Net("127.0.0.1", port), AbstractPostgresConfig.Storage("test"),
                    AbstractPostgresConfig.Timeout(), AbstractPostgresConfig.Credentials(username, password))
    val url = "jdbc:postgresql://${config.net().host()}:${config.net().port()}/${config.storage().dbName()}?user=$username&password=$password"

    init {
        val runtime = PostgresStarter.getDefaultInstance();
        val exec = runtime.prepare(config);
        val mainThread = Thread.currentThread();
        var process: PostgresProcess? = null
        val runnable: Runnable = Runnable {
            process!!.stop();
            mainThread.join();
        };
        Runtime.getRuntime().addShutdownHook(Thread(runnable));
        process = exec.start();
    }
}

fun main(args: Array<String>) {
    val port = Integer.parseInt(System.getProperty("port") ?: "5432");
    val postgre = EmbeddedPostgreSQL(port)

    val datasource = PGSimpleDataSource()
    datasource.url = postgre.url
    datasource.user = postgre.username
    datasource.password = postgre.password

    val db = Database.forDataSource(datasource);

    db.update(
            """
            CREATE sequence serial;

            CREATE TABLE films (
                code        char(5) CONSTRAINT firstkey PRIMARY KEY,
                title       varchar(40) NOT NULL,
                did         integer NOT NULL,
                date_prod   date,
                kind        varchar(10)
            );

            CREATE TABLE distributors (
                 did    integer PRIMARY KEY DEFAULT nextval('serial'),
                 name   varchar(40) NOT NULL CHECK (name <> '')
            );
            """
    );

    for (i in 1..100) {
        val distributorName = "distributor_name_$i"
        val did = db.update("insert into distributors (name) values (?)", distributorName);
        val code = "cd$i"
        val title = "title_$i";
        val kind = "kind_$i";
        db.update("insert into films (code,title,did,date_prod,kind) values (?, ?, ?, ?, ?)",
                code, title, did, Date(), kind);
    }

    Thread.currentThread().join()
}
