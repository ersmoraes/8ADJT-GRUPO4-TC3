# 🏥 Sistema Hospitalar - Backend

## 📋 Visão Geral

Sistema modular para gestão de consultas médicas em ambiente hospitalar, desenvolvido com **Spring Boot**, **Spring Security**, **GraphQL** e **RabbitMQ**.

## 🏗️ Arquitetura

O sistema é composto por **3 microsserviços**:

### 1. 🗓️ **Agendamento Service** (Porta 8081)
- **Responsabilidade**: Gestão de consultas e autenticação
- **Tecnologias**: Spring Boot, Spring Security, JWT, JPA/Hibernate
- **Funcionalidades**:
  - Autenticação de usuários (médicos, enfermeiros, pacientes)
  - CRUD de consultas com validações
  - Controle de acesso baseado em roles
  - Publicação de eventos no RabbitMQ

### 2. 📊 **Histórico Service** (Porta 8082)
- **Responsabilidade**: Armazenamento e consulta do histórico médico
- **Tecnologias**: Spring Boot, GraphQL, JPA/Hibernate
- **Funcionalidades**:
  - API GraphQL para consultas flexíveis
  - Histórico completo por paciente/médico
  - Interface GraphiQL para testes
  - Consumo de eventos via RabbitMQ

### 3. 📧 **Notificação Service** (Porta 8083)
- **Responsabilidade**: Envio de notificações e lembretes
- **Tecnologias**: Spring Boot, Spring Mail, Scheduler
- **Funcionalidades**:
  - Envio automático de emails
  - Lembretes diários programados
  - Reprocessamento de falhas
  - Consumo de eventos via RabbitMQ

## 🔐 Segurança

### Níveis de Acesso
- **👨‍⚕️ Médicos**: Visualizar e editar consultas próprias
- **👩‍⚕️ Enfermeiros**: Registrar e acessar todas as consultas
- **🏥 Pacientes**: Visualizar apenas suas consultas

### Autenticação
- **JWT** stateless com expiração de 24h
- **BCrypt** para hash de senhas
- **Roles** baseadas em enum: MEDICO, ENFERMEIRO, PACIENTE

## 🚀 Como Executar

### Pré-requisitos
- **Java 11+**
- **MySQL 8.0+**
- **RabbitMQ**
- **Maven 3.6+**

### 1. Com Docker (Recomendado)
```bash
# Subir infraestrutura
cd docker
docker-compose up -d

# Verificar se serviços estão rodando
docker ps
```

### 2. Manual

#### Configurar Bancos de Dados
```sql
CREATE DATABASE hospital_agendamento;
CREATE DATABASE hospital_historico;
CREATE DATABASE hospital_notificacao;
```

#### Configurar Email (Notificação Service)
Editar `notificacao-service/src/main/resources/application.yml`:
```yaml
spring:
  mail:
    username: seu-email@gmail.com
    password: sua-senha-app
```

#### Executar Serviços
```bash
# Terminal 1 - Build do projeto
mvn clean install

# Terminal 2 - Agendamento Service
cd agendamento-service
mvn spring-boot:run

# Terminal 3 - Histórico Service  
cd historico-service
mvn spring-boot:run

# Terminal 4 - Notificação Service
cd notificacao-service
mvn spring-boot:run
```

## 📡 APIs Disponíveis

### 🔑 Autenticação (8081)
```http
POST /api/auth/login
GET /api/auth/me
```

### 📅 Consultas (8081)
```http
POST /api/consultas        # Criar consulta
GET /api/consultas         # Listar consultas
GET /api/consultas/{id}    # Obter consulta
PUT /api/consultas/{id}    # Atualizar consulta
DELETE /api/consultas/{id} # Cancelar consulta
GET /api/consultas/futuras # Consultas futuras
```

### 📊 GraphQL (8082)
- **Endpoint**: `http://localhost:8082/graphql`
- **GraphiQL**: `http://localhost:8082/graphiql`

### 📧 Notificações (8083)
```http
GET /api/notificacoes/pendentes
POST /api/notificacoes/reprocessar
POST /api/notificacoes/lembretes
```

## 💻 Exemplos de Uso

### 1. Login
```bash
curl -X POST http://localhost:8081/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "username": "dr.silva",
    "password": "password123"
  }'
```

### 2. Criar Consulta
```bash
curl -X POST http://localhost:8081/api/consultas \
  -H "Authorization: Bearer SEU_TOKEN_JWT" \
  -H "Content-Type: application/json" \
  -d '{
    "pacienteId": 6,
    "medicoId": 2,
    "dataHora": "2024-12-25T14:30:00",
    "observacoes": "Consulta de retorno"
  }'
```

### 3. Consultar Histórico (GraphQL)
```graphql
query {
  consultasPorPaciente(pacienteId: "6") {
    consultaId
    pacienteNome
    medicoNome
    dataHora
    status
    observacoes
  }
}
```

## 📊 Monitoramento

### URLs Importantes
- **RabbitMQ Management**: `http://localhost:15672` (guest/guest)
- **GraphiQL Interface**: `http://localhost:8082/graphiql`

### Logs
```bash
# Ver logs do agendamento
tail -f agendamento-service/logs/application.log

# Ver logs do RabbitMQ
docker logs hospital-rabbitmq

# Ver logs do MySQL
docker logs hospital-mysql
```

## 👥 Usuários Padrão

| Username | Password | Role | Nome |
|----------|----------|------|------|
| admin | password123 | MEDICO | Administrador |
| dr.silva | password123 | MEDICO | Dr. João Silva |
| dra.santos | password123 | MEDICO | Dra. Maria Santos |
| enf.ana | password123 | ENFERMEIRO | Ana Enfermeira |
| enf.carlos | password123 | ENFERMEIRO | Carlos Enfermeiro |
| paciente1 | password123 | PACIENTE | Pedro Paciente |
| paciente2 | password123 | PACIENTE | Julia Paciente |

## 🔄 Fluxo do Sistema

1. **Médico/Enfermeiro** autentica no sistema
2. **Cria/Edita** uma consulta via API REST
3. **Evento** é publicado no RabbitMQ
4. **Histórico Service** consome evento e armazena dados
5. **Notificação Service** consome evento e envia email
6. **Paciente** recebe notificação automática
7. **Sistema** envia lembretes programados

## 🛠️ Estrutura do Projeto

```
hospital-system/
├── common/                    # Módulo compartilhado
├── agendamento-service/       # Gestão de consultas
├── historico-service/         # GraphQL + Histórico
├── notificacao-service/       # Emails + Lembretes
├── docker/                    # Docker compose
└── README.md
```

## 🧪 Testes

```bash
# Executar todos os testes
mvn test

# Executar testes de um serviço específico
cd agendamento-service
mvn test
```

## 🔧 Configurações Adicionais

### Alterar Portas
Editar nos arquivos `application.yml`:
```yaml
server:
  port: NOVA_PORTA
```

### Configurar Ambiente de Produção
1. Alterar senhas padrão
2. Configurar SSL/HTTPS
3. Usar banco de dados externo
4. Configurar monitoring com Actuator

## 🚀 Deploy

### Docker (Recomendado)
```bash
# Build das imagens
mvn clean package
docker build -t hospital/agendamento:latest agendamento-service/
docker build -t hospital/historico:latest historico-service/
docker build -t hospital/notificacao:latest notificacao-service/

# Deploy
docker-compose -f docker/docker-compose.prod.yml up -d
```

**Desenvolvido com ❤️ para gestão hospitalar eficiente**