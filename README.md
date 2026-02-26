# API de Gerenciamento de Tarefas (To-Do List)

### Objetivo

Desenvolver uma API RESTful que permita criar, listar, atualizar e excluir tarefas,
com autenticação de usuários e persistência em banco de dados.

 Tecnologias e Ferramentas 

- **Java 11+**
- **Spring Boot 2.x**
- **Spring Data JPA**
- **Banco de dados: H2 (dev)** 
- **Maven**
- **Lombok**
- **Git**

## Funcionalidades (Requisitos)

### 1. **Autenticação e Usuários**

- Cadastro de usuário (nome, e-mail, senha) – senha deve ser hash (BCrypt)
- Login com e-mail e senha, retornando um token JWT
- Acesso às tarefas somente com token válido (rotas protegidas)
- Cada usuário vê apenas suas próprias tarefas

### 2. **CRUD de Tarefas**

- Criar tarefa: título, descrição, data de vencimento, status (pendente/concluída)
- Listar todas as tarefas do usuário (com filtros opcionais por status/data)
- Buscar uma tarefa por ID
- Atualizar tarefa (parcial ou total)
- Excluir tarefa
- Marcar tarefa como concluída (ou reabrir)
