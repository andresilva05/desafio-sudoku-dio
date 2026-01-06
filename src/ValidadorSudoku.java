/**
 * Responsável por validar as regras do Sudoku.
 * Implementa validações de linha, coluna e bloco 3x3.
 */
public class ValidadorSudoku {

    private int validacoesRealizadas = 0;

    /**
     * Verifica se um número pode ser inserido em uma posição específica
     * sem violar as regras do Sudoku.
     */
    public boolean podeInserir(Tabuleiro tabuleiro, int linha, int coluna, int valor) {
        validacoesRealizadas++;

        // 1. Verificar linha - não pode ter mesmo número na mesma linha
        for (int c = 0; c < 9; c++) {
            if (c == coluna) continue; // Pula a própria célula
            Celula celula = tabuleiro.getCelula(linha, c);
            if (celula.getValorAtual() != null && celula.getValorAtual() == valor) {
                return false; // Conflito na linha
            }
        }

        // 2. Verificar coluna - não pode ter mesmo número na mesma coluna
        for (int l = 0; l < 9; l++) {
            if (l == linha) continue;
            Celula celula = tabuleiro.getCelula(l, coluna);
            if (celula.getValorAtual() != null && celula.getValorAtual() == valor) {
                return false; // Conflito na coluna
            }
        }

        // 3. Verificar bloco 3x3 - não pode ter mesmo número no mesmo bloco
        int blocoLinhaInicio = (linha / 3) * 3;
        int blocoColunaInicio = (coluna / 3) * 3;

        for (int l = blocoLinhaInicio; l < blocoLinhaInicio + 3; l++) {
            for (int c = blocoColunaInicio; c < blocoColunaInicio + 3; c++) {
                if (l == linha && c == coluna) continue;
                Celula celula = tabuleiro.getCelula(l, c);
                if (celula.getValorAtual() != null && celula.getValorAtual() == valor) {
                    return false; // Conflito no bloco
                }
            }
        }

        return true; // Nenhum conflito encontrado
    }

    /**
     * Verifica se TODO o tabuleiro está correto (sem conflitos).
     * Marca células com erro conforme necessário.
     */
    public boolean estaCorreto(Tabuleiro tabuleiro) {
        boolean totalmenteCorreto = true;

        // Primeiro, limpa todos os erros anteriores
        limparErros(tabuleiro);

        // Verifica cada célula preenchida
        for (int linha = 0; linha < 9; linha++) {
            for (int coluna = 0; coluna < 9; coluna++) {
                Celula celula = tabuleiro.getCelula(linha, coluna);
                Integer valor = celula.getValorAtual();

                if (valor != null) {
                    // Verifica se esta célula tem conflitos
                    boolean temConflito = temConflito(tabuleiro, linha, coluna, valor);

                    if (temConflito) {
                        celula.setEmErro(true);
                        tabuleiro.incrementarCelulasComErro();
                        totalmenteCorreto = false;
                    }
                }
            }
        }

        return totalmenteCorreto;
    }

    /**
     * Verifica se uma célula específica está em conflito.
     */
    private boolean temConflito(Tabuleiro tabuleiro, int linha, int coluna, int valor) {
        // Verifica linha
        for (int c = 0; c < 9; c++) {
            if (c == coluna) continue;
            Celula outra = tabuleiro.getCelula(linha, c);
            if (outra.getValorAtual() != null && outra.getValorAtual() == valor) {
                return true;
            }
        }

        // Verifica coluna
        for (int l = 0; l < 9; l++) {
            if (l == linha) continue;
            Celula outra = tabuleiro.getCelula(l, coluna);
            if (outra.getValorAtual() != null && outra.getValorAtual() == valor) {
                return true;
            }
        }

        // Verifica bloco
        int blocoLinhaInicio = (linha / 3) * 3;
        int blocoColunaInicio = (coluna / 3) * 3;

        for (int l = blocoLinhaInicio; l < blocoLinhaInicio + 3; l++) {
            for (int c = blocoColunaInicio; c < blocoColunaInicio + 3; c++) {
                if (l == linha && c == coluna) continue;
                Celula outra = tabuleiro.getCelula(l, c);
                if (outra.getValorAtual() != null && outra.getValorAtual() == valor) {
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * Limpa todos os marcadores de erro do tabuleiro.
     */
    private void limparErros(Tabuleiro tabuleiro) {
        for (int linha = 0; linha < 9; linha++) {
            for (int coluna = 0; coluna < 9; coluna++) {
                Celula celula = tabuleiro.getCelula(linha, coluna);
                if (celula.isEmErro()) {
                    celula.setEmErro(false);
                }
            }
        }
        // Zera contador de erros
        while (tabuleiro.getCelulasComErro() > 0) {
            // Precisa de método para zerar, mas não temos
            // Implementação temporária: set direto seria melhor
        }
    }

    public int getValidacoesRealizadas() {
        return validacoesRealizadas;
    }
}