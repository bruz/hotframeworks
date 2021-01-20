# HotFrameworks

Pulls statistics about web frameworks daily. It uses an unusual approach with a Clojure web app that doesn't serve traffic directly, but instead is queried by a scheduled job that uploads the HTML and assets to an S3 bucket.

## Development

Prerequisites:

* [Leiningen](http://leiningen.org/)
* [PostgreSQL](http://www.postgresql.org/)

Installing:

```bash
git clone git@github.com:bruz/hotframeworks.git
cd hotframeworks

# Assuming no database password
createdb hotframeworks
psql hotframeworks < database/schema.sql

cp .lein-env.example .lein-env
```

Modify the configuration in .lein-env. AWS credentials are only necessary for publishing to S3.

Loading schema.sql only provides the database schema. In order to import statistics for some actual frameworks, adding some records to the `languages` and `frameworks` tables is required.

Import statistics from GitHub and Stack Overflow:

```bash
lein repl
```

In the REPL:

```clojure
(require '[hotframeworks.models.statistic-set :as stats])
(stats/pull-and-save!)
```

Run the web app to test it locally:

```bash
lein ring server
```

## Pull statistics and publish to S3

```bash
lein run
```
