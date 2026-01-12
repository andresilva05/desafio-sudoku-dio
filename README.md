# Refatoração de Projeto Sudoku com Padrões de Projeto

Projeto desenvolvido como parte do **Desafio de Padrões de Projeto** do **Bootcamp Java e AWS – Digital Innovation One (DIO)**.

O objetivo foi refatorar um sistema Sudoku já funcional, aplicando **Padrões de Projeto** e **princípios de boa arquitetura**, melhorando organização, legibilidade e manutenibilidade **sem alterar o comportamento do sistema**.

---

## 🎯 Objetivo

O principal objetivo foi melhorar a estrutura do código, separando melhor as responsabilidades e aplicando conceitos aprendidos durante o bootcamp, como:

* **State Pattern** para controle dos estados do jogo
* **Factory Pattern** para criação centralizada de objetos
* **Service Layer** para separar regras de negócio
* **Princípios SOLID**, com foco em responsabilidade única e baixo acoplamento

---

## 🔍 Problemas do Código Original

* A classe Jogo concentrava muitas responsabilidades
* Uso de muitos if e switch para controlar o estado do jogo
* As validações alteravam diretamente o estado do sistema
* A interface com o usuário conhecia detalhes internos da lógica do jogo

---

## 🏗️ Arquitetura Refatorada

### Estrutura de Pacotes

```text
src/
├── command/        # Comandos e invocador (Command Pattern)
├── domain/         # Entidades do domínio e exceções
│   ├── exception/
│   └── model/
├── factory/        # Criação centralizada de objetos
├── service/        # Regras de negócio e orquestração
├── state/          # State Pattern (estados do jogo)
├── ui/             # Interface com o usuário (console)
├── validation/     # Validações puras (sem efeitos colaterais)
└── Main.java       # Ponto de entrada da aplicação
```

---

## 🧩 Padrões de Projeto Aplicados

### State Pattern (Principal)

Responsável por controlar o ciclo de vida do jogo, substituindo condicionais complexas por estados explícitos.

**Estados implementados:**

* `NaoIniciadoState`
* `EmAndamentoState`
* `CompletoState`
* `VencidoState`

**Benefícios:**

* Isso deixou o código mais organizado e facilitou o entendimento do comportamento do jogo em cada fase.

---

### Factory Pattern

* O Factory Pattern foi aplicado para centralizar a criação de objetos como o jogo e o tabuleiro, reduzindo o acoplamento entre as classes.

---

### Service Layer

* A camada de service ficou responsável por concentrar as regras de negócio, evitando que a interface ou as entidades do domínio tenham lógica em excesso.

---

## 🔄 Fluxo da Aplicação

### Antes da Refatoração

```
Main chamava diretamente a classe Jogo, que fazia praticamente tudo.
```

### Depois da Refatoração

```
Main → Factory → Service → State Atual
                ↳ Validações Puras
                ↳ Domínio
```

---

## ✅ Funcionalidades Mantidas

* Inserção e remoção de valores
* Proteção de células fixas
* Validação completa das regras do Sudoku
* Verificação de conclusão do jogo
* Interface via terminal
* Comportamento original preservado

---

## 🚀 Benefícios da Refatoração

* Código mais organizado e legível
* Baixo acoplamento entre componentes
* Facilidade de manutenção e evolução
* Padrões de projeto aplicados de forma consciente
* Arquitetura alinhada com boas práticas profissionais


## 🏁 Conclusão

Este projeto foi muito importante para consolidar os conceitos de padrões de projeto e boa organização de código.
A refatoração mostrou como é possível melhorar bastante a estrutura de um sistema sem alterar seu comportamento, tornando o código mais claro e preparado para evoluções futuras.
