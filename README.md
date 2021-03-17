# ScalaCassandraAlpakka

Simplified spin off from my other repo ([ScalaCassandra](https://github.com/mveeprojects/ScalaCassandra)) to learn about streaming to Cassandra via Alpakka.

## Running the app

1. Run this from the root of the project `./docker/CassandraVanillaDocker.sh`, wait a few seconds (up to 10/20) for
   Cassandra to become ready.
2. Start the app up in IntelliJ.

## Cqlsh

### To access the command line (`cqlsh`) via docker

`docker exec -it cassandra cqlsh`

### To view all keyspaces

`describe keyspaces`

### To switch keyspace

`use keyspace_name;`

### To list all tables

`describe tables;`

### To select all records from a table (e.g. the video table)

`select * from video;`

## Sources

All sources from ([ScalaCassandra](https://github.com/mveeprojects/ScalaCassandra)), plus the below.

* https://blog.knoldus.com/alpakka/
* https://doc.akka.io/docs/alpakka/current/cassandra.html