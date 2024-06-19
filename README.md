# Accounts Payable API Documentation

Welcome to the Accounts Payable API. This guide will help you get started with setting up the environment using Docker and provide examples on how to interact with the API.

## Getting Started

### Prerequisites

- Docker and Docker Compose installed on your machine.

### Setup and Launch

To set up and launch the API, run the following command in your terminal:

```bash
sudo docker-compose up --build
```
This command will build the Docker images and start the containers as defined in your docker-compose.yml file.

## Account Status definition in Database

| Status ID| Description |
|----------|-------------|
|      1   | 'Em aberto' |
|      2   | 'Pago'      |
|      3   | 'Em atraso' |
|      4   | 'Cancelado' |

## API Usage

Here are some examples of how you can interact with the API using curl commands.

### Create Account

To create a new account, use the following command:

```bash
curl --location 'http://localhost:8080/api/accounts' \
--header 'Authorization: Basic YWRtaW46YWRtaW4=' \
--header 'Content-Type: application/json' \
--data '{ "dueDate" : "2024-01-19", "paymentDate" : "2024-01-10", "value" : 200.00, "description" : "compras de insumo", "status" : 2 }'
```

### List All Accounts

To list all accounts with a specific description, use:

```bash
curl --location 'http://localhost:8080/api/accounts?description=Pagamento%20numero%202' \
--header 'Authorization: Basic YWRtaW46YWRtaW4='
```
### Get Account by Id

To get a specific account by its ID, use:

```bash
curl --location 'http://localhost:8080/api/accounts/3' \
--header 'Authorization: Basic YWRtaW46YWRtaW4='
```

### Update Account

To update an existing account, use:

```bash
curl --location --request PUT 'http://localhost:8080/api/accounts/3' \
--header 'Authorization: Basic YWRtaW46YWRtaW4=' \
--header 'Content-Type: application/json' \
--data '{ "dueDate" : "2024-01-19", "paymentDate" : "2024-01-18", "value" : 200.00, "description" : "compras de insumo", "status" : 1 }'
```

### Update Account Status

To update the status of an account, use:

```bash
curl --location --request PATCH 'http://localhost:8080/api/accounts/3/status/4' \
--header 'Authorization: Basic YWRtaW46YWRtaW4='
```

### Get Total Pay by Period

To get the total payment within a specific period, use:

```bash
curl --location 'http://localhost:8080/api/accounts/total-pay?startDate=2024-01-01&endDate=2024-10-01' \
--header 'Authorization: Basic YWRtaW46YWRtaW4='
```

### Import Accounts from CSV

To import accounts from a CSV file, use:

```bash
curl -X POST -H 'Authorization: Basic YWRtaW46YWRtaW4=' -F "file=@/path/to/your/csv/example.csv" http://localhost:8080/api/accounts/import
```
In application directory  ***'resouces/db/csv'*** exists a file example to use or
use your CSV file.

## Suggestions for future improvements

1. Implementing a more robust authentication method like OAuth2 + JWT
2. Implementation of documentation using Swagger
3. Implementing authorization by diferents rules