# API Restful para operações bancárias

Esta API foi desenvolvida com o propósito de gerenciamento de operações bancárias tais como: __cadastro de bancos, cadastro de agências, cadastro de clientes, transferência entre contas, depósitos e saques__.

A mesma foi projetada utilizando o padrão Oauth2 como método de autenticação que provê um _token_ para acesso aos recursos protegidos.

Toda a aplicação é disponibilizada em um arquivo compactado `maida-bank-api-0.0.1-SNAPSHOT.jar`.

## Requerimentos
- JDK11 e Maven

## Especificações
- Spring Boot v2.2.5.RELEASE (Web, Security and Data)
- H2 Database (in memory)
- Lombok (automate getters and setters)
- Spring Security Oauth2
- Maven (Dependency management)

## Outras considerações
Porta padrão utilizada: `5000`

# REST API

Os recusros da API estão descritos abaixo.

## OBTER TOKEN

### Request

`POST /api/oauth/token`

#### Form Data
    grant_type = password
    username = admin@email.com
    password = 123456
### Authentication Data
    username = myapp
    password = myappsecret

### Response
    HTTP/1.1 200
    Cache-Control: no-store
    Pragma: no-cache
    X-Content-Type-Options: nosniff
    X-XSS-Protection: 1; mode=block
    X-Frame-Options: DENY
    Content-Type: application/json;charset=UTF-8
    Transfer-Encoding: chunked

    {
        "access_token": "70857270-e545-4bc4-a9a1-7a30e530e18f",
        "token_type": "bearer",
        "expires_in": 85832,
        "scope": "all"
    }
    

## CRIAR UMA INSTITUIÇÃO BANCÁRIA

### Request

`POST /api/banks`

### Body
    {
        "name": "Caixa Econômica Federal",
        "code": "104"
    }

### Response

    HTTP/1.1 200 
    X-Content-Type-Options: nosniff
    X-XSS-Protection: 1; mode=block
    Cache-Control: no-cache, no-store, max-age=0, must-revalidate
    Pragma: no-cache
    Expires: 0
    X-Frame-Options: DENY
    Content-Type: application/json
    Transfer-Encoding: chunked

    {
      "id": 3,
      "code": "033",
      "name": "Banco Santander"
    }
## LISTAR INSTITUIÇÕES BANCÁRIAS

### Request

`GET /api/banks`

### Response

    HTTP/1.1 200 
    X-Content-Type-Options: nosniff
    X-XSS-Protection: 1; mode=block
    Cache-Control: no-cache, no-store, max-age=0, must-revalidate
    Pragma: no-cache
    Expires: 0
    X-Frame-Options: DENY
    Content-Type: application/json
    Transfer-Encoding: chunked

    [
      {"id":1, "code":"001", "name":"Banco do Brasil SA"},
      {"id":2, "code":"237", "name":"Banco Bradesco SA"},
      {"id":3, "code":"033", "name":"Banco Santander"}
    ]   


## CRIAR UMA AGÊNCIA

### Request

`POST /api/branches`

### Body
    {
    	"name": "Agencia Jockey",
    	"code": "3178",
    	"bank": 1
    }

### Response

    HTTP/1.1 200 
    X-Content-Type-Options: nosniff
    X-XSS-Protection: 1; mode=block
    Cache-Control: no-cache, no-store, max-age=0, must-revalidate
    Pragma: no-cache
    Expires: 0
    X-Frame-Options: DENY
    Content-Type: application/json
    Transfer-Encoding: chunked

    {
      "id": 6,
      "code": "3178",
      "name": "Agencia Jockey",
      "bank": {"id": 1, "code": "001", "name": "Banco do Brasil SA"}
    }

## CRIAR CONTA BANCÁRIA PARA UM CLIENTE

### Request
`POST /api/banks/:bankId/branches/:branchId/accounts`

### Body
    {
    	"name": "Mary Jhonson",
    	"login": "mary",
    	"password": "123456"
    }

### Response

    HTTP/1.1 200 
    X-Content-Type-Options: nosniff
    X-XSS-Protection: 1; mode=block
    Cache-Control: no-cache, no-store, max-age=0, must-revalidate
    Pragma: no-cache
    Expires: 0
    X-Frame-Options: DENY
    Content-Type: application/json
    Transfer-Encoding: chunked

    {
      "id": 1,
      "name": "John Doe",
      "code": "94182-X",
      "branch": {
        "id": 2,
        "code": "405",
        "name": "Agencia Centro",
        "bank": {"id": 2, "code": "237", "name": "Banco Bradesco SA"}
      }
    }

## CRIAR TRANSAÇÃO   DEPÓSITO

### Request
`POST /api/accounts/:accountId/transactions`

### Body
    {
        "value": 1000,
        "type": "DEPOSIT",
        "entry": "CREDIT"
    }

### Response
    HTTP/1.1 200 
    X-Content-Type-Options: nosniff
    X-XSS-Protection: 1; mode=block
    Cache-Control: no-cache, no-store, max-age=0, must-revalidate
    Pragma: no-cache
    Expires: 0
    X-Frame-Options: DENY
    Content-Type: application/json
    Transfer-Encoding: chunked

    {
        "id": 1,
        "date": "2020-03-10T13:25:45.2129036",
        "value": 1000,
        "type": "DEPOSIT",
        "entry": "CREDIT",
        "account": {
            "id": 1,
            "code": "94182-X",
            "balance": 1000.00,
            "customer": {"id": 3, "name": "John Doe"},
            "branch": {
                "id": 2,
                "code": "405",
                "name": "Agencia Centro",
                "bank": {"id": 2, "code": "237", "name": "Banco Bradesco SA"}
            }
        }
    }

## CRIAR TRANSAÇÃO - SAQUE

### Request
`POST /api/accounts/:accountId/transactions`

### Body
    {
        "value": 500,
        "type": "WITHDRAWL",
        "entry": "DEBIT"
    }

### Response
    HTTP/1.1 200 
    X-Content-Type-Options: nosniff
    X-XSS-Protection: 1; mode=block
    Cache-Control: no-cache, no-store, max-age=0, must-revalidate
    Pragma: no-cache
    Expires: 0
    X-Frame-Options: DENY
    Content-Type: application/json
    Transfer-Encoding: chunked

    {
        "id": 1,
        "date": "2020-03-10T13:25:45.2129036",
        "value": 500,
        "type": "WITHDRAWL",
        "entry": "DEBIT",
        "account": {
            "id": 1,
            "code": "94182-X",
            "balance": 500.00,
            "customer": {"id": 3, "name": "John Doe"},
            "branch": {
                "id": 2,
                "code": "405",
                "name": "Agencia Centro",
                "bank": {"id": 2, "code": "237", "name": "Banco Bradesco SA"}
            }
        }
    }

## CRIAR TRANSAÇÃO - TRANSFERÊNCIA ENTRE CONTAS

### Request
`POST /api/accounts/:accountId/transactions`

### Body
    {
        "account": 2,
        "value": 250,
        "type": "TRANSFER",
        "entry": "DEBIT"
    }

### Response
    HTTP/1.1 200 
    X-Content-Type-Options: nosniff
    X-XSS-Protection: 1; mode=block
    Cache-Control: no-cache, no-store, max-age=0, must-revalidate
    Pragma: no-cache
    Expires: 0
    X-Frame-Options: DENY
    Content-Type: application/json
    Transfer-Encoding: chunked

    {
        "id": 1,
        "date": "2020-03-10T13:25:45.2129036",
        "value": 250,
        "type": "TRANSFER",
        "entry": "DEBIT",
        "account": {
            "id": 1,
            "code": "94182-X",
            "balance": 250.00,
            "customer": {"id": 3, "name": "John Doe"},
            "branch": {
                "id": 2,
                "code": "405",
                "name": "Agencia Centro",
                "bank": {"id": 2, "code": "237", "name": "Banco Bradesco SA"}
            }
        }
    }


## LISTAR TRANSAÇÕES DE UMA CONTA BANCÁRIA

### Request
`GET /api/accounts/:accountId/transactions`


### Response
    HTTP/1.1 200 
    X-Content-Type-Options: nosniff
    X-XSS-Protection: 1; mode=block
    Cache-Control: no-cache, no-store, max-age=0, must-revalidate
    Pragma: no-cache
    Expires: 0
    X-Frame-Options: DENY
    Content-Type: application/json
    Transfer-Encoding: chunked

    [
        {
            "id": 3,
            "date": "2020-03-10T13:26:16.064371",
            "value": 1000.00,
            "type": "TRANSFER",
            "entry": "DEBIT"
        },
        {
            "id": 1,
            "date": "2020-03-10T13:25:45.212904",
            "value": 1000.00,
            "type": "DEPOSIT",
            "entry": "CREDIT"
        }
    ]

## LISTAR CONTAS BANCÁRIAS

### Request
`GET /api/accounts`


### Response
    HTTP/1.1 200 
    X-Content-Type-Options: nosniff
    X-XSS-Protection: 1; mode=block
    Cache-Control: no-cache, no-store, max-age=0, must-revalidate
    Pragma: no-cache
    Expires: 0
    X-Frame-Options: DENY
    Content-Type: application/json
    Transfer-Encoding: chunked

    [
        {
            "id": 1,
            "code": "94182-X",
            "balance": 0.00,
            "customer": {"id": 3, "name": "John Doe"},
            "branch": {
                "id": 2,
                "code": "405",
                "name": "Agencia Centro",
                "bank": {"id": 2, "code": "237", "name": "Banco Bradesco SA"}
            }
        },
        {
            "id": 2,
            "code": "62254-4",
            "balance": 1000.00,
            "customer": {"id": 4, "name": "Mary Jhonson"},
            "branch": {
                "id": 1,
                "code": "3178",
                "name": "Agencia Jockey",
                "bank": {"id": 1, "code": "001", "name": "Banco do Brasil SA"}
            }
        }
    ]

