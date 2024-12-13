# Order Management Service

Este é um serviço de gerenciamento de pedidos que integra com sistemas externos para processar e calcular pedidos em alta escala.

## 🎯 Requisitos do Projeto

- Gerenciar e calcular produtos do pedido
- Integrar com sistema externo A para recebimento dos pedidos
- Integrar com sistema externo B para disponibilizar pedidos calculados
- Capacidade de processamento: 150-200k pedidos/dia
- Alta disponibilidade e performance

## 🏗️ Arquitetura

### Tecnologias Utilizadas
- Java 17
- Spring Boot 3.2.0
- Spring Data JPA
- Spring Cloud OpenFeign
- PostgreSQL
- Apache Kafka
- Docker
- Resilience4j

### Padrões de Projeto
- Arquitetura Hexagonal (Ports & Adapters)
- Strategy Pattern (Cálculo de pedidos)
- Factory Pattern (Criação de objetos)
- Repository Pattern (Acesso a dados)
- Circuit Breaker (Resiliência)

### Estrutura do Projeto
```
src/
├── main/
│   ├── java/
│   │   └── com/orderservice/
│   │       ├── controller/       # REST Controllers
│   │       ├── domain/          # Entidades de domínio
│   │       ├── dto/             # Objetos de transferência
│   │       ├── external/        # Integrações externas
│   │       ├── repository/      # Repositórios
│   │       └── service/         # Lógica de negócio
│   └── resources/
│       └── application.yml     # Configurações
└── test/
    └── java/                  # Testes unitários
```

## 🚀 Como Executar

### Pré-requisitos
- Docker e Docker Compose
- Java 17
- Maven

### Passos para Execução

1. Clone o repositório
```bash
git clone https://github.com/seu-usuario/order-service.git
cd order-service
```

2. Build do projeto
```bash
mvn clean package -DskipTests
```

3. Inicie os containers
```bash
docker-compose up -d
```

### Verificação do Status
```bash
# Verificar containers em execução
docker ps

# Verificar logs
docker logs order-service
```

## 📝 Endpoints da API

### Criar Pedido
```bash
curl -X POST http://localhost:8080/api/orders \
-H "Content-Type: application/json" \
-d '{
    "items": [
        {
            "productCode": "PROD-1",
            "quantity": 2,
            "price": 10.00
        }
    ]
}'
```

### Consultar Pedido
```bash
curl http://localhost:8080/api/orders/{id}
```

### Processar Pedido
```bash
curl -X POST http://localhost:8080/api/orders/{id}/process
```

## 🧪 Testes

### Executar Testes Unitários
```bash
mvn test
```

### Executar Testes com Cobertura
```bash
mvn test jacoco:report
```
O relatório de cobertura estará disponível em `target/site/jacoco/index.html`

## 📊 Monitoramento

- Swagger UI: http://localhost:8080/swagger-ui.html
- API Docs: http://localhost:8080/v3/api-docs

## ⚙️ Configurações

As principais configurações estão no arquivo `application.yml`:
- Conexão com banco de dados
- URLs dos sistemas externos
- Configurações do Kafka
- Parâmetros de resilience4j

## 📈 Performance

O sistema foi projetado para:
- Processar 150-200k pedidos/dia
- Latência média < 200ms
- Disponibilidade 99.9%

### Otimizações
- Cache em memória para dados frequentes
- Connection pooling
- Bulk processing para operações em lote
- Circuit breaker para falhas de sistemas externos

## 🔒 Segurança

- Validação de entrada de dados
- Tratamento de exceções
- Circuit breaker para proteção de recursos
- Logs de auditoria

## 👥 Contribuição

1. Fork o projeto
2. Crie sua feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit suas mudanças (`git commit -m 'Add some AmazingFeature'`)
4. Push para a branch (`git push origin feature/AmazingFeature`)
5. Abra um Pull Request

## 📄 Licença

Este projeto está sob a licença MIT. Veja o arquivo [LICENSE](LICENSE) para mais detalhes.