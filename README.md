# 🧩 Sudoku Game — Desafio DIO

Jogo de Sudoku desenvolvido em **Java**, via terminal, como parte do **Bootcamp da DIO**.  
O projeto implementa o motor completo do Sudoku, com validações rigorosas, controle de estado do jogo e separação clara de responsabilidades.

---

## 📋 Funcionalidades Implementadas

### Menu Interativo
O jogo oferece um menu no terminal com as seguintes opções:

1. **Iniciar jogo**  
   Exibe o tabuleiro inicial com números fixos informados via argumentos (`args`).

2. **Colocar número**  
   Solicita linha, coluna e valor, validando regras do Sudoku.

3. **Remover número**  
   Permite remover apenas números inseridos pelo usuário.

4. **Verificar jogo**  
   Exibe o estado atual do tabuleiro.

5. **Verificar status do jogo**  
   Informa se o jogo está não iniciado, incompleto ou completo, e se há erros.

6. **Limpar**  
   Remove todos os números inseridos pelo usuário, mantendo os fixos.

7. **Finalizar jogo**  
   Encerra o jogo apenas se o tabuleiro estiver completo e sem conflitos.

---

## ✅ Regras e Validações

- Apenas valores de **1 a 9** são permitidos
- Células fixas **não podem ser alteradas ou removidas**
- Não é possível inserir número em célula já preenchida
- Validação de conflitos:
  - Linha
  - Coluna
  - Bloco 3×3
- O jogo diferencia **tabuleiro completo** de **tabuleiro correto**

---

## 📌 Status do Jogo

- **Não iniciado** (sempre sem erros)
- **Incompleto** (com ou sem erros)
- **Completo** (com ou sem erros)
- **Vencido** (completo e sem erros)

---

## 🏗️ Arquitetura do Projeto

### Estrutura de Classes

```text
Sudoku/
├── Main.java              # Ponto de entrada e leitura dos args
├── Menu.java              # Interface do usuário (terminal)
├── Jogo.java              # Lógica principal e orquestração
├── Tabuleiro.java         # Estado do tabuleiro 9x9
├── Celula.java            # Unidade básica do jogo
├── ValidadorSudoku.java   # Regras do Sudoku (linha, coluna, bloco)
└── EstadoJogo.java        # Enum de estados do jogo
```

### Princípios Aplicados

- **Separação de responsabilidades**
  - Menu: entrada e saída
  - Jogo: regras e fluxo
  - Tabuleiro: estado
  - Validador: regras puras

- **Encapsulamento**
  - O menu não acessa células diretamente
  - O tabuleiro protege sua estrutura interna
  - A célula gerencia seu próprio estado

- **Validação em dois níveis**
  - Menu: valida formato da entrada
  - Jogo/Validador: valida regras do Sudoku

---

## 🚀 Execução

### Compilação e Execução

```bash
javac *.java
java Main
```

### Execução com números fixos

```bash
java Main 3 5 5 1 1 1 9 9 9
```

Formato:

```text
linha coluna valor linha coluna valor ...
```

Exemplo acima define como fixos:

- (3,5) = 5
- (1,1) = 1
- (9,9) = 9

---

## 🎮 Exemplo de Uso

```text
=== SUDOKU ===
    1 2 3   4 5 6   7 8 9
  ┌───────┬───────┬───────┐
1 │ · · · │ · · · │ · · · │
...
```

```text
--- MENU ---
1. Inserir número
2. Remover número
3. Verificar jogo
4. Verificar status do jogo
5. Limpar células editáveis
6. Finalizar jogo
7. Sair
```

---

## 🧪 Casos de Teste

### Células Fixas

```bash
java Main 1 1 5 5 5 5 9 9 9
```

Células fixas não podem ser removidas ou alteradas.

---

### Conflitos

Inserir:

```text
(1,1) = 5
```

Tentar:

- (1,2) = 5 → conflito de linha  
- (2,1) = 5 → conflito de coluna  
- (2,2) = 5 → conflito de bloco  

---

### Limpeza

1. Inserir números pelo menu  
2. Executar opção **Limpar**  
3. Apenas números fixos permanecem  

---

## 📚 Aprendizados

Este projeto demonstra:

- Modelagem orientada a objetos
- Separação clara entre interface e regra de negócio
- Validação consistente de estado
- Organização de código para manutenção e extensão
- Pensamento arquitetural aplicado a um problema clássico

---

## 📄 Licença

Projeto educacional desenvolvido para fins de aprendizado no Bootcamp Java e Aws da DIO.
