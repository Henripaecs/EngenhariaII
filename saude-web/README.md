# 🏥 Saúde Web — Sistema de Gestão de Profissionais de Saúde

Projeto desenvolvido com base na aula de Desenvolvimento de Software Web (Java + React).

## Stack

| Camada        | Tecnologia                    |
|---------------|-------------------------------|
| Backend       | Java 17 + Spring Boot 3.2     |
| Frontend      | React 18 + React Router       |
| Banco de Dados| PostgreSQL 15                 |
| Build Backend | Maven                         |
| Build Frontend| Node.js 20 + npm              |
| Containers    | Docker + Docker Compose       |
| CI/CD         | GitHub Actions                |

## Entidades

### ProfissionalDeSaude
- `id` (Long, PK)
- `nome` (String, obrigatório)
- `telefone` (String)
- `endereco` (String)
- `categoria` (List<String>: `MEDICO`, `PSICOLOGO`, `FISIOTERAPEUTA`)

### Atendimento
- `id` (Long, PK)
- `data` (LocalDate, obrigatório)
- `horario` (LocalTime)
- `problema_texto` (Text)
- `receita_saude` (List<String>) — Remédio (médico), Atividade Física (fisio), Atividade Mental (psicólogo)
- `profissional` (FK → ProfissionalDeSaude)

## Endpoints da API

### Profissionais `/api/profissionais`
| Método | Rota                          | Descrição                    |
|--------|-------------------------------|------------------------------|
| POST   | `/api/profissionais`          | Inserir novo profissional     |
| PUT    | `/api/profissionais/{id}`     | Alterar profissional por ID   |
| GET    | `/api/profissionais`          | Consultar todos / por nome   |
| GET    | `/api/profissionais/{id}`     | Consultar por ID              |
| GET    | `/api/profissionais/categoria/{cat}` | Consultar por categoria |
| DELETE | `/api/profissionais/{id}`     | Excluir por ID                |

### Atendimentos `/api/atendimentos`
| Método | Rota                          | Descrição                    |
|--------|-------------------------------|------------------------------|
| POST   | `/api/atendimentos`           | Registrar atendimento         |
| GET    | `/api/atendimentos`           | Listar todos                  |
| GET    | `/api/atendimentos/{id}`      | Buscar por ID                 |
| GET    | `/api/atendimentos/profissional/{id}` | Listar por profissional |
| PUT    | `/api/atendimentos/{id}`      | Alterar atendimento           |
| DELETE | `/api/atendimentos/{id}`      | Excluir atendimento           |

## Como rodar

### Com Docker Compose (recomendado)
```bash
docker-compose up --build
```
- Frontend: http://localhost:3000
- Backend: http://localhost:8080
- Swagger: http://localhost:8080/swagger-ui.html

### Desenvolvimento local
```bash
# Backend (requer PostgreSQL local na porta 5432)
cd backend
mvn spring-boot:run

# Frontend
cd frontend
npm install
npm start
```

## Testes
```bash
# Backend
cd backend && mvn test

# Frontend
cd frontend && npm test
```

## Estrutura do Projeto
```
saude-web/
├── .github/workflows/ci-cd.yml
├── backend/
│   ├── pom.xml
│   ├── Dockerfile
│   └── src/main/java/com/saude/
│       ├── SaudeApplication.java
│       ├── model/
│       │   ├── ProfissionalDeSaude.java
│       │   └── Atendimento.java
│       ├── repository/
│       │   ├── ProfissionalDeSaudeRepository.java
│       │   └── AtendimentoRepository.java
│       └── controller/
│           ├── ProfissionalDeSaudeController.java
│           └── AtendimentoController.java
├── frontend/
│   ├── package.json
│   ├── Dockerfile
│   └── src/
│       ├── App.js / App.css
│       ├── index.js
│       ├── services/api.js
│       └── components/
│           ├── ProfissionalList.js
│           ├── ProfissionalForm.js
│           ├── AtendimentoList.js
│           └── AtendimentoForm.js
└── docker-compose.yml
```
