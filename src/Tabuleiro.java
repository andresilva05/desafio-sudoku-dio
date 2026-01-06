import java.util.HashSet;

/**
 * Representa o tabuleiro 9x9 do Sudoku.
 * Gerencia a grade de células e contadores de estado.
 */
public class Tabuleiro {

    // Grade 9x9 de células
    private Celula[][] grade = new Celula[9][9];

    // Contadores de estado
    private int celulasPreenchidas = 0;
    private int celulasFixas = 0;
    private int celulasComErro = 0;

    /**
     * Construtor vazio - cria tabuleiro com todas células vazias.
     */
    public Tabuleiro() {
        for (int linha = 0; linha < 9; linha++) {
            for (int coluna = 0; coluna < 9; coluna++) {
                // Cria célula vazia, não fixa, editável, sem rascunhos, sem erro
                grade[linha][coluna] = new Celula(null, false, true, new HashSet<>(), false);
            }
        }
    }

    // ========== MÉTODOS DE ACESSO ==========

    /**
     * Obtém uma célula específica do tabuleiro.
     * @param linha Índice da linha (0-8)
     * @param coluna Índice da coluna (0-8)
     * @return A célula ou null se índices inválidos
     */
    public Celula getCelula(int linha, int coluna) {
        if (linha < 0 || linha >= 9 || coluna < 0 || coluna >= 9) {
            return null;
        }
        return grade[linha][coluna];
    }

    // ========== VERIFICAÇÕES DE ESTADO ==========

    /**
     * Verifica se o tabuleiro está completamente preenchido.
     */
    public boolean estaCompleto() {
        return celulasPreenchidas == 81;
    }

    /**
     * Verifica se o tabuleiro contém células com erro.
     */
    public boolean temErros() {
        return celulasComErro > 0;
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

    // ========== CONTROLE DE CONTADORES ==========

    public void incrementarCelulasPreenchidas() {
        celulasPreenchidas++;
    }

    public void decrementarCelulasPreenchidas() {
        celulasPreenchidas--;
    }

    public void incrementarCelulasFixas() {
        celulasFixas++;
    }

    public void incrementarCelulasComErro() {
        celulasComErro++;
    }

    public void decrementarCelulasComErro() {
        if (celulasComErro > 0) {
            celulasComErro--;
        }
    }

    /**
     * Atualiza contadores após inserção.
     * @param ehFixo true se a célula é fixa
     */
    public void atualizarContadoresAposInserir(boolean ehFixo) {
        celulasPreenchidas++;
        if (ehFixo) {
            celulasFixas++;
        }
    }
}