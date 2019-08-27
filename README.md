# How to build REST API with Arrow, Ktor and requery in Kotlin

Sample REST API using [Ktor](https://ktor.io/), [Arrow](https://arrow-kt.io/)
and [requery](https://github.com/requery/requery).

## Requirements

 * [JDK 11](https://www.oracle.com/technetwork/java/javase/downloads/jdk11-downloads-5066655.html)

## Running

```
./gradlew run
```

## API

List of employees using repository based on IO from Arrow:
```
curl http://localhost:8080/io/employees
```

List of employees using repository based on Single from Java Rx 2.0:
```
curl http://localhost:8080/rx2/employees
```
