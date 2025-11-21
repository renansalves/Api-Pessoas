# projeto pessoa crud.
---

api desenvolvida para controle do cadastro de pessoas a seus endereços.

### 💻 tecnologias utilizadas

* **linguagem:** java 21
* **framework:** spring boot 3.x
* **gerenciador de dependências:** Gradle
* **banco de dados:** Banco em memoria H2 
* **persistência:** spring data jpa / hibernate
* **Testes:** JUnit 5, Mockito (com 'org.mockito:mockito-core:5.+')
* **documentação:** springdoc openapi (swagger ui)
### ⚙️ pré-requisitos

para executar a api localmente, você precisará ter instalado:

1.  **java development kit (jdk):** versão 17 ou superior.
    ```bash
    java -version
    ```
2.  **Gradlew:** Gerenciador de dependencias.

### 🚀 instalação e execução

1.  **clonar o repositório:**
    ```bash
    git clone [https://github.com/renansalves/api-pessoas.git](https://github.com/renansalves/api-pessoas.git)
    cd api-pessoas
    ```

2.  **Configurar Banco de Dados (Opcional):**
    O projeto utiliza o **H2 em memória** por padrão. Para usar o PostgreSQL ou outro banco de sua preferencia, edite o arquivo `src/main/resources/application.yml` e configure as credenciais:
    
    ```yaml
    # Exemplo de configuração (YAML):
    spring:
      datasource:
        url: jdbc:postgresql://localhost:5432/api_pessoas_db 
        username: seu_usuario 
        password: sua_senha
    ```
3.  **compilar e empacotar (build):**
    ```bash
    ./gradlew build
    ```

4.  **executar a aplicação:**
    ```bash
    ./gradlew bootRun
    # a aplicação estará rodando em http://localhost:8080
    ```

### 🧭 uso da api (endpoints principais)

a api é acessível em `http://localhost:8080` (porta padrão).

| método | endpoint | descrição |
| :--- | :--- | :--- |
| **post** | `/api/v1/pessoas` | cria um novo registro de pessoa. |
| **get** | `/api/v1/pessoas/{id}` | busca uma pessoa pelo id. |
| **put** | `/api/v1/pessoas/{id}` | atualiza todas as informações de uma pessoa. |
| **delete** | `/api/v1/pessoas/{id}` | deleta uma pessoa pelo id. |

**exemplo de requisição (post /api/v1/pessoas):**

```bash
curl -x post http://localhost:8080/api/v1/pessoas \
-h "content-type: application/json" \
-d '{
  "nome": "renan alves",
  "datanascimento": "1990-01-01",
  "cpf": "123.456.789-00"
}'
```

### 📄 Documentação (Swagger UI)

* A documentação interativa da API, gerada automaticamente pelo Springdoc, pode ser acessada em: `http://localhost:8080/swagger-ui.html`

### 🎯 Funcionalidades e Requisitos

* Listar todas as pessoas e seus respectivos endereços.
* Criar uma nova pessoa com um ou mais endereços.
* Atualizar os dados de uma pessoa e/ou seu(s) endereço(s).
* Excluir uma pessoa e todos os seus endereços.
* Mostrar a idade da pessoa.

### 📝 Trabalho Futuro (TODO)

* [ ] 🚧 Ajustar o cálculo de idade.
* [ ] 📈 Implementar paginação na consulta.

