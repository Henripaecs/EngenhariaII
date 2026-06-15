# 🏥 Saúde Web — Sistema de Gestão de Profissionais de Saúde

Projeto desenvolvido para a disciplina de Desenvolvimento de Software Web.

## 🔗 Links

| | URL |
|---|---|
| 🌐 **App em produção** | https://saude-frontend-x6mm.onrender.com |
| ⚙️ **API (backend)** | https://saude-backend-x6mm.onrender.com |
| 📄 **Swagger (documentação da API)** | https://saude-backend-x6mm.onrender.com/swagger-ui.html |
| 💻 **Repositório GitHub** | https://github.com/Henripaecs/EngenhariaII |

> ⚠️ O backend está hospedado no plano gratuito do Render — a primeira requisição após inatividade pode demorar ~1 minuto para responder (cold start).

---

## 👥 Equipe

| Integrante | Responsabilidade |
|---|---|
| Arthur Signorini Miranda | CRUD de Profissional de Saúde |
| Henrique Paes de Carvalho | CRUD de Atendimento |

---

## 🛠 Stack

| Camada | Tecnologia |
|---|---|
| Backend | Java 17 + Spring Boot 3.2 |
| Frontend | React 18 + React Router |
| Banco de Dados | PostgreSQL 18 |
| Build Backend | Maven |
| Build Frontend | Node.js 20 + npm |
| Containers | Docker + Docker Compose |
| CI/CD | GitHub Actions |
| Hospedagem | Render (gratuito) |

---

## 📋 Requisito Obrigatório — Entidades

### ProfissionalDeSaude
| Atributo | Tipo | Descrição |
|---|---|---|
| `id` | Long (PK) | Identificador único |
| `nome` | String | Nome do profissional (obrigatório) |
| `telefone` | String | Telefone de contato |
| `endereco` | String | Endereço completo |
| `categoria` | List\<String\> | MEDICO, PSICOLOGO, FISIOTERAPEUTA |

### Atendimento
| Atributo | Tipo | Descrição |
|---|---|---|
| `id` | Long (PK) | Identificador único |
| `data` | LocalDate | Data do atendimento (obrigatório) |
| `horario` | LocalTime | Horário do atendimento |
| `problema_texto` | Text | Descrição do problema/queixa |
| `receita_saude` | List\<String\> | Remédio (médico) / Atividade Física (fisio) / Atividade Mental (psicólogo) |
| `profissional` | FK | Profissional responsável |

---

## 🔌 Endpoints da API

### Profissionais `/api/profissionais`
| Método | Rota | Descrição |
|---|---|---|
| POST | `/api/profissionais` | Inserir novo profissional |
| PUT | `/api/profissionais/{id}` | Alterar profissional por ID |
| GET | `/api/profissionais` | Consultar todos / filtrar por nome |
| GET | `/api/profissionais/{id}` | Consultar por ID |
| GET | `/api/profissionais/categoria/{cat}` | Consultar por categoria |
| DELETE | `/api/profissionais/{id}` | Excluir por ID |

### Atendimentos `/api/atendimentos`
| Método | Rota | Descrição |
|---|---|---|
| POST | `/api/atendimentos` | Registrar atendimento |
| GET | `/api/atendimentos` | Listar todos |
| GET | `/api/atendimentos/{id}` | Buscar por ID |
| GET | `/api/atendimentos/profissional/{id}` | Listar por profissional |
| PUT | `/api/atendimentos/{id}` | Alterar atendimento |
| DELETE | `/api/atendimentos/{id}` | Excluir atendimento |

---

## 🚀 Como rodar localmente

### Pré-requisitos
- Java 17+
- Maven
- Node.js 20+
- PostgreSQL rodando na porta 5432

### Backend
```bash
cd backend
mvn spring-boot:run
```
Acesse: http://localhost:8080

### Frontend
```bash
cd frontend
npm install
npm start
```
Acesse: http://localhost:3000

### Com Docker Compose
```bash
docker compose up --build
```

---

## 📁 Estrutura do Projeto
```
EngenhariaII/
├── .github/workflows/ci-cd.yml   ← CI/CD GitHub Actions
├── backend/
│   ├── Dockerfile
│   ├── pom.xml
│   └── src/main/java/com/saude/
│       ├── SaudeApplication.java
│       ├── config/CorsConfig.java
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
│   ├── Dockerfile
│   ├── package.json
│   └── src/
│       ├── App.js / App.css
│       ├── services/api.js
│       └── components/
│           ├── ProfissionalList.js
│           ├── ProfissionalForm.js
│           ├── AtendimentoList.js
│           └── AtendimentoForm.js
├── docker-compose.yml
└── render.yaml                   ← Deploy automático no Render
```
