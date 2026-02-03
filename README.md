# Hold — Gestão de Pessoas

Sistema de cadastro de pessoas desenvolvido para a **Global Tech Holding** (prova de recrutamento). Backend em **Spring Boot** (Java) e frontend em **Angular**, com interface para incluir, alterar, excluir e pesquisar pessoas, além do cálculo de peso ideal.

---

## Tecnologias utilizadas

### Backend (Java)

| Tecnologia | Versão | Descrição |
|------------|--------|-----------|
| **Java** | 21 | Linguagem e JDK |
| **Spring Boot** | 3.5.6 | Framework da aplicação |
| **Spring Boot Web** | - | API REST (controllers, JSON) |
| **Spring Data JPA** | - | Persistência e repositórios |
| **Spring Boot Validation** | - | Validação de DTOs (@Valid, @NotBlank, etc.) |
| **Spring Boot Actuator** | - | Endpoints de health, info e metrics |
| **Flyway** | (gerenciada pelo Spring Boot) | Migrações de banco (versionamento do schema) |
| **Flyway MySQL** | - | Driver Flyway para MySQL |
| **MySQL Connector J** | - | Driver JDBC para MySQL |
| **Lombok** | - | Redução de boilerplate (getters, setters, etc.) |
| **SpringDoc OpenAPI (WebMVC UI)** | 2.8.13 | Documentação da API (Swagger UI) |
| **Apache Commons Lang3** | 3.14.0 | Utilitários (strings, etc.) |
| **JUnit 5 + Mockito** | (spring-boot-starter-test) | Testes unitários (service e controller) |
| **JaCoCo** | 0.8.12 | Relatório de cobertura de testes |
| **H2** | (scope test) | Banco em memória para testes |

### Frontend (Angular)

| Tecnologia | Versão | Descrição |
|------------|--------|-----------|
| **Angular** | 19 | Framework SPA |
| **Angular Router** | 19 | Roteamento e lazy load |
| **Angular Forms** | 19 | Formulários reativos e template-driven |
| **Angular Common** | 19 | Diretivas, pipes e utilitários |
| **RxJS** | 7.8 | Programação reativa (Observables) |
| **TypeScript** | 5.6 | Linguagem |
| **Tailwind CSS** | 3.4 | Estilos utilitários (layout, cores, responsivo) |
| **PostCSS** | 8.4 | Processamento de CSS |
| **Autoprefixer** | 10.4 | Prefixos automáticos para compatibilidade de navegadores |

### Infraestrutura e ferramentas

| Item | Uso |
|------|-----|
| **Maven** | Build e dependências do backend |
| **npm** | Dependências e scripts do frontend |
| **MySQL** | Banco de dados em desenvolvimento e produção |

---

## Pré-requisitos

Antes de subir o projeto, instale:

- **JDK 21** — [Adoptium](https://adoptium.net/) ou [Oracle](https://www.oracle.com/java/technologies/downloads/)
- **Maven 3.8+** — [Maven](https://maven.apache.org/download.cgi) (ou use o wrapper `mvnw` se existir na pasta `backend`)
- **Node.js 18+** e **npm** — [Node.js](https://nodejs.org/)
- **MySQL 8** (ou 5.7) em execução na porta **3306**

Verifique no terminal:

```bash
java -version    # deve mostrar versão 21
mvn -v           # Maven 3.x
node -v          # v18 ou superior
npm -v
```

---

## Como subir o projeto

### 1. Banco de dados (MySQL)

- O MySQL deve estar **rodando** (serviço iniciado).
- O banco **`hold`** é criado automaticamente na primeira execução do backend (`createDatabaseIfNotExist=true`).
- **Usuário e senha padrão** usados pelo projeto: **`root`** / **`root`**.

Se no seu ambiente o usuário ou a senha forem outros, edite o arquivo:

**`backend/src/main/resources/application.properties`**

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/hold?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC&createDatabaseIfNotExist=true
spring.datasource.username=root
spring.datasource.password=root
```

Ajuste `username` e `password` conforme seu MySQL. Se o MySQL estiver em outra máquina ou porta, altere a URL.

---

### 2. Backend (Spring Boot)

Abra um terminal na pasta **raiz do repositório** (onde estão as pastas `backend` e `frontend`).

**Windows (PowerShell ou CMD):**

```powershell
cd backend
mvn spring-boot:run
```

**Linux / macOS:**

```bash
cd backend
./mvnw spring-boot:run
# ou, se tiver Maven instalado globalmente:
mvn spring-boot:run
```

Aguarde até aparecer algo como: **`Started BackendApplication in X seconds`**.

Endpoints úteis:

| URL | Descrição |
|-----|-----------|
| http://localhost:8080 | Base da API |
| http://localhost:8080/swagger-ui.html | Documentação interativa (Swagger) |
| http://localhost:8080/actuator/health | Status de saúde da aplicação |

---

### 3. Frontend (Angular)

Em **outro terminal**, na raiz do repositório:

**Windows:**

```powershell
cd frontend
npm install
npm start
```

**Linux / macOS:**

```bash
cd frontend
npm install
npm start
```

O comando `npm start` roda o `ng serve`. A aplicação ficará disponível em:

| URL | Descrição |
|-----|-----------|
| http://localhost:4200 | Interface do usuário (Cadastro de Pessoas) |

As requisições do frontend para **`/api`** são redirecionadas para **http://localhost:8080** pelo proxy configurado em `frontend/proxy.conf.json`, então não é necessário configurar CORS manualmente no dia a dia.

---

### 4. Testando no navegador

1. Certifique-se de que **backend** (porta 8080) e **frontend** (porta 4200) estão rodando.
2. Acesse **http://localhost:4200**.
3. **Pesquisar:** clique em **Pesquisar** (com ou sem nome) para listar pessoas. Os botões **Alterar**, **Excluir** e **Peso Ideal** só ficam habilitados após uma pesquisa que retorne resultados.
4. **Incluir:** clique em **Incluir**, preencha Nome, Sexo e Altura (obrigatórios), opcionalmente CPF, data de nascimento e e-mail, e salve.
5. **Alterar:** na tabela, clique em uma linha para selecionar (fica destacada). Depois clique em **Alterar**, edite e salve.
6. **Excluir:** selecione uma pessoa na tabela e clique em **Excluir**; confirme na caixa de diálogo.
7. **Peso ideal:** selecione uma pessoa e clique em **Peso Ideal**; o resultado aparece em um popup (nome e peso ideal em kg).

---

## Rodar os testes (backend)

Os testes estão apenas em **service** e **controller**, além da classe principal **BackendApplicationTests** (que carrega o contexto Spring e valida que a aplicação sobe).

**Os testes não precisam de MySQL:** usam **H2 em memória**, configurado em `backend/src/test/resources/application.properties` (Flyway desativado nos testes, schema criado pelo JPA com `ddl-auto=create-drop`).

**No terminal (Maven):**

```bash
cd backend
mvn test
```

O relatório de cobertura (JaCoCo) é gerado em:

**`backend/target/site/jacoco/index.html`**

Abra esse arquivo no navegador para ver a cobertura por pacote/classe.

**No IntelliJ IDEA:**

- Clique com o botão direito na pasta **`src/test/java`** (ou na pasta `com.hold.backend`) e escolha **Run 'Tests in com.hold.backend'**.
- Ou abra uma classe de teste (ex.: `BackendApplicationTests`, `PessoaControllerTest`, `PessoaServiceImplTest`) e use o ícone ▶ ao lado da classe ou do método.

---

## Estrutura do projeto

```
workspace-hold/
├── backend/                    # API REST (Spring Boot)
│   ├── src/main/java/com/hold/backend/
│   │   ├── BackendApplication.java
│   │   ├── config/              # CORS, Swagger
│   │   ├── controller/          # REST controllers
│   │   ├── dto/                 # Request/Response
│   │   ├── exception/           # Tratamento global de erros
│   │   ├── mapper/              # Entity ↔ DTO
│   │   ├── model/               # Entidades JPA
│   │   ├── repository/          # JPA repositories
│   │   ├── service/             # Interface e implementação
│   │   ├── task/                # Lógica de negócio (CRUD, peso ideal)
│   │   └── validation/          # Validação customizada (ex.: CPF)
│   ├── src/main/resources/
│   │   ├── application.properties
│   │   └── db/migration/mysql/  # Scripts Flyway (V1, V2, ...)
│   ├── src/test/java/          # Testes: BackendApplicationTests, controller, service/impl
│   ├── src/test/resources/
│   │   └── application.properties  # H2 em memória (testes sem MySQL)
│   └── pom.xml
│
├── frontend/                   # SPA Angular
│   ├── src/app/
│   │   ├── app.config.ts
│   │   ├── app.routes.ts
│   │   ├── pessoas/             # Módulo de pessoas (listagem, formulário)
│   │   └── services/            # Serviço HTTP e modelos
│   ├── proxy.conf.json         # /api → localhost:8080
│   ├── package.json
│   ├── tailwind.config.js
│   └── angular.json
└── README.md                   # Este arquivo
```

---

## Regras de negócio (resumo)

- **Pessoa:** nome (obrigatório), CPF (opcional, validado), data de nascimento, sexo (obrigatório), altura (obrigatória), e-mail (opcional, validado). ID gerado automaticamente.
- **Fluxo na tela:** Pesquisar lista pessoas (por nome ou todas). Após pesquisa com resultado, o usuário pode selecionar uma linha e usar **Alterar**, **Excluir** ou **Peso Ideal**. O botão **Incluir** está sempre disponível.
- **Peso ideal:** calculado no backend e exibido em popup no frontend. Homens: (72,7 × altura) − 58; Mulheres: (62,1 × altura) − 44,7 (altura em metros, resultado em kg).

---

## Resumo rápido de comandos

| Ação | Comando |
|------|---------|
| Subir backend | `cd backend` → `mvn spring-boot:run` |
| Subir frontend | `cd frontend` → `npm install` → `npm start` |
| Rodar testes | `cd backend` → `mvn test` |
| Build do frontend | `cd frontend` → `npm run build` |

Aplicação: **http://localhost:4200**  
API / Swagger: **http://localhost:8080** / **http://localhost:8080/swagger-ui.html**


