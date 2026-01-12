package domain.model;

/**
 * Representa uma posição (linha, coluna) no tabuleiro de Sudoku.
 * Encapsula a lógica de conversão entre base 1 (usuário) e base 0 (interno).
 */
public class Posicao {
    private final int linha;  // 1-9 (base usuário)
    private final int coluna; // 1-9 (base usuário)

    public Posicao(int linha, int coluna) {
        if (!isValida(linha) || !isValida(coluna)) {
            throw new IllegalArgumentException("Linha e coluna devem estar entre 1 e 9");
        }
        this.linha = linha;
        this.coluna = coluna;
    }

    private boolean isValida(int valor) {
        return valor >= 1 && valor <= 9;
    }

    // Getters para base usuário
    public int getLinha() {
        return linha;
    }

    public int getColuna() {
        return coluna;
    }

    // Getters para base interna (array)
    public int getLinhaIndex() {
        return linha - 1;
    }

    public int getColunaIndex() {
        return coluna - 1;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Posicao posicao = (Posicao) obj;
        return linha == posicao.linha && coluna == posicao.coluna;
    }

    @Override
    public int hashCode() {
        return 31 * linha + coluna;
    }

    @Override
    public String toString() {
        return String.format("(%d,%d)", linha, coluna);
    }
}