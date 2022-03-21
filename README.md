# work-scheduler

## How to run the app

You should have docker installed.

Run `docker-compose up --build` from the project root directory.

Then you can execute [http requests](./http) using VS Code with [Rest Client](https://marketplace.visualstudio.com/items?itemName=humao.rest-client) plugin.

## How to run the tests

To run unit tests:

`./gradlew clean check`

To run integration tests:

`./gradlew clean integrationTest`

To run linter:

`./gradlew spotlessApply`

## Roadmap:
- liquibase
- swagger
- kubernetes, helm
- apm integration
- grafana metrics
- logback
- sleuth
- cicd pipelines on repo
- role based authorization
- paging get requests

business:
- filters to get requests
- eventing workers about incoming shifts
