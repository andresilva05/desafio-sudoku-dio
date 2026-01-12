package domain.model;

import java.util.HashSet;
import java.util.Set;

/**
 * Representa o tabuleiro 9x9 do Sudoku.
 * Gerencia a grade de células e mantém consistência interna.
 */
public class Tabuleiro {
    // Grade 9x9 de células
    private Celula[][] grade = new Celula[9][9];

    // Contadores de estado - calculados dinamicamente
    private int celulasPreenchidas = 0;
    private int celulasFixas = 0;
    private int celulasComErro = 0;

    /**
     * Construtor vazio - cria tabuleiro com todas células vazias.
     */
    public Tabuleiro() {
        inicializarGradeVazia();
        recalcularContadores();
    }

    /**
     * Construtor com grade personalizada (para testes/fábrica).
     */
    Tabuleiro(Celula[][] grade) {
        if (grade == null || grade.length != 9 || grade[0].length != 9) {
            throw new IllegalArgumentException("Grade deve ser 9x9");
        }
        this.grade = grade;
        recalcularContadores();
    }

    private void inicializarGradeVazia() {
        for (int linha = 0; linha < 9; linha++) {
            for (int coluna = 0; coluna < 9; coluna++) {
                grade[linha][coluna] = Celula.criarVazia();
            }
        }
    }

    // ========== MÉTODOS DE ACESSO ==========

    /**
     * Obtém uma cópia imutável da célula em uma posição específica.
     */
    public Celula getCelula(int linha, int coluna) {
        validarIndices(linha, coluna);
        return grade[linha][coluna];
    }

    /**
     * Obtém uma cópia imutável da célula usando Posicao.
     */
    public Celula getCelula(Posicao posicao) {
        return getCelula(posicao.getLinhaIndex(), posicao.getColunaIndex());
    }

    /**
     * Define uma célula em uma posição específica.
     * Recalcula contadores automaticamente.
     */
    public void setCelula(int linha, int coluna, Celula celula) {
        validarIndices(linha, coluna);
        if (celula == null) {
            throw new IllegalArgumentException("Célula não pode ser nula");
        }

        Celula antiga = grade[linha][coluna];
        grade[linha][coluna] = celula;

        // Atualiza contadores
        atualizarContadoresAoSubstituir(antiga, celula);
    }

    /**
     * Define uma célula usando Posicao.
     */
    public void setCelula(Posicao posicao, Celula celula) {
        setCelula(posicao.getLinhaIndex(), posicao.getColunaIndex(), celula);
    }

    // ========== OPERAÇÕES DE ATUALIZAÇÃO ==========

    /**
     * Insere um valor em uma célula editável.
     * Retorna true se a inserção foi bem-sucedida.
     */
    public boolean inserirNumero(int linha, int coluna, int valor) {
        validarIndices(linha, coluna);

        Celula atual = grade[linha][coluna];

        // Verifica se pode inserir
        if (atual.isFixo()) {
            return false; // Célula fixa não pode ser alterada
        }
        if (valor < 1 || valor > 9) {
            return false; // Valor inválido
        }

        // Cria nova célula com o valor
        Celula nova = atual.comValor(valor);
        grade[linha][coluna] = nova;

        // Atualiza contadores
        atualizarContadoresAoSubstituir(atual, nova);
        return true;
    }

    /**
     * Insere um valor usando Posicao.
     */
    public boolean inserirNumero(Posicao posicao, int valor) {
        return inserirNumero(posicao.getLinhaIndex(), posicao.getColunaIndex(), valor);
    }

    /**
     * Remove o valor de uma célula editável.
     */
    public boolean removerNumero(int linha, int coluna) {
        validarIndices(linha, coluna);

        Celula atual = grade[linha][coluna];

        // Verifica se pode remover
        if (atual.isFixo()) {
            return false; // Célula fixa não pode ser alterada
        }
        if (atual.isVazia()) {
            return false; // Já está vazia
        }

        // Cria nova célula vazia
        Celula nova = atual.comValor(null);
        grade[linha][coluna] = nova;

        // Atualiza contadores
        atualizarContadoresAoSubstituir(atual, nova);
        return true;
    }

    /**
     * Remove o valor usando Posicao.
     */
    public boolean removerNumero(Posicao posicao) {
        return removerNumero(posicao.getLinhaIndex(), posicao.getColunaIndex());
    }

    /**
     * Marca uma célula como fixa com um valor.
     * Usado apenas na inicialização.
     */
    public boolean marcarComoFixo(int linha, int coluna, int valor) {
        validarIndices(linha, coluna);

        Celula atual = grade[linha][coluna];

        // Só pode marcar como fixa se estiver vazia
        if (!atual.isVazia()) {
            return false;
        }

        // Cria célula fixa
        Celula nova = Celula.criarFixa(valor);
        grade[linha][coluna] = nova;

        // Atualiza contadores
        atualizarContadoresAoSubstituir(atual, nova);
        return true;
    }

    /**
     * Marca uma célula como com erro.
     */
    public void marcarErro(int linha, int coluna) {
        validarIndices(linha, coluna);

        Celula atual = grade[linha][coluna];
        if (atual.isPreenchida() && !atual.isEmErro()) {
            Celula nova = atual.marcarErro();
            grade[linha][coluna] = nova;
            celulasComErro++;
        }
    }

    /**
     * Remove marcação de erro de uma célula.
     */
    public void limparErro(int linha, int coluna) {
        validarIndices(linha, coluna);

        Celula atual = grade[linha][coluna];
        if (atual.isEmErro()) {
            Celula nova = atual.limparErro();
            grade[linha][coluna] = nova;
            celulasComErro--;
        }
    }

    /**
     * Limpa todos os erros do tabuleiro.
     */
    public void limparTodosErros() {
        for (int linha = 0; linha < 9; linha++) {
            for (int coluna = 0; coluna < 9; coluna++) {
                Celula atual = grade[linha][coluna];
                if (atual.isEmErro()) {
                    grade[linha][coluna] = atual.limparErro();
                }
            }
        }
        celulasComErro = 0;
    }

    /**
     * Limpa todas as células editáveis.
     * @return número de células limpas
     */
    public int limparCelulasEditaveis() {
        int celulasLimpas = 0;

        for (int linha = 0; linha < 9; linha++) {
            for (int coluna = 0; coluna < 9; coluna++) {
                Celula atual = grade[linha][coluna];
                if (atual.isEditavel() && atual.isPreenchida()) {
                    Celula nova = atual.comValor(null);
                    grade[linha][coluna] = nova;
                    celulasLimpas++;

                    // Atualiza contador
                    celulasPreenchidas--;
                }
            }
        }

        return celulasLimpas;
    }

    // ========== VERIFICAÇÕES DE ESTADO ==========

    public boolean estaCompleto() {
        return celulasPreenchidas == 81;
    }

    public boolean temErros() {
        return celulasComErro > 0;
    }

    public boolean estaVazio() {
        return celulasPreenchidas == 0;
    }

    // ========== CONTADORES ==========

    private void atualizarContadoresAoSubstituir(Celula antiga, Celula nova) {
        // Atualiza contador de preenchidas
        if (antiga.isPreenchida() && !nova.isPreenchida()) {
            celulasPreenchidas--;
        } else if (!antiga.isPreenchida() && nova.isPreenchida()) {
            celulasPreenchidas++;
        }

        // Atualiza contador de fixas
        if (antiga.isFixo() && !nova.isFixo()) {
            celulasFixas--;
        } else if (!antiga.isFixo() && nova.isFixo()) {
            celulasFixas++;
        }

        // Atualiza contador de erros
        if (antiga.isEmErro() && !nova.isEmErro()) {
            celulasComErro--;
        } else if (!antiga.isEmErro() && nova.isEmErro()) {
            celulasComErro++;
        }
    }

    private void recalcularContadores() {
        celulasPreenchidas = 0;
        celulasFixas = 0;
        celulasComErro = 0;

        for (int linha = 0; linha < 9; linha++) {
            for (int coluna = 0; coluna < 9; coluna++) {
                Celula celula = grade[linha][coluna];
                if (celula.isPreenchida()) {
                    celulasPreenchidas++;
                }
                if (celula.isFixo()) {
                    celulasFixas++;
                }
                if (celula.isEmErro()) {
                    celulasComErro++;
                }
            }
        }
    }

    // ========== GETTERS ==========

    public int getCelulasPreenchidas() {
        return celulasPreenchidas;
    }

    public int getCelulasFixas() {
        return celulasFixas;
    }

    public int getCelulasComErro() {
        return celulasComErro;
    }

    public Celula[][] getGrade() {
        // Retorna cópia defensiva
        Celula[][] copia = new Celula[9][9];
        for (int i = 0; i < 9; i++) {
            System.arraycopy(grade[i], 0, copia[i], 0, 9);
        }
        return copia;
    }

    // ========== VALIDAÇÃO ==========

    private void validarIndices(int linha, int coluna) {
        if (linha < 0 || linha >= 9 || coluna < 0 || coluna >= 9) {
            throw new IllegalArgumentException(
                    String.format("Índices inválidos: linha=%d, coluna=%d (deve ser 0-8)", linha, coluna)
            );
        }
    }

    // ========== UTILITÁRIOS ==========

    /**
     * Cria uma cópia profunda do tabuleiro.
     */
    public Tabuleiro copiar() {
        Celula[][] copiaGrade = new Celula[9][9];
        for (int linha = 0; linha < 9; linha++) {
            for (int coluna = 0; coluna < 9; coluna++) {
                // Como Celula é imutável, podemos reutilizar as instâncias
                copiaGrade[linha][coluna] = grade[linha][coluna];
            }
        }
        return new Tabuleiro(copiaGrade);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Tabuleiro: ").append(celulasPreenchidas).append("/81 preenchidas\n");
        for (int linha = 0; linha < 9; linha++) {
            for (int coluna = 0; coluna < 9; coluna++) {
                sb.append(grade[linha][coluna].toString()).append(" ");
                if (coluna == 2 || coluna == 5) {
                    sb.append("│ ");
                }
            }
            sb.append("\n");
            if (linha == 2 || linha == 5) {
                sb.append("──────┼───────┼──────\n");
            }
        }
        return sb.toString();
    }
}