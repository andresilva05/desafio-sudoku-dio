package validation;

import domain.model.Celula;
import domain.model.Posicao;
import domain.model.Tabuleiro;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Implementa as regras clássicas do Sudoku.
 * Puramente funcional - não altera estado.
 */
public class RegraSudoku {

    /**
     * Verifica se um número pode ser inserido em uma posição
     * sem violar as regras do Sudoku.
     */
    public ResultadoValidacao podeInserir(Tabuleiro tabuleiro, Posicao posicao, int valor) {
        List<String> conflitos = new ArrayList<>();

        // Verifica linha
        if (temConflitoNaLinha(tabuleiro, posicao, valor)) {
            conflitos.add("Conflito na linha " + (posicao.getLinha()));
        }

        // Verifica coluna
        if (temConflitoNaColuna(tabuleiro, posicao, valor)) {
            conflitos.add("Conflito na coluna " + (posicao.getColuna()));
        }

        // Verifica bloco 3x3
        if (temConflitoNoBloco(tabuleiro, posicao, valor)) {
            conflitos.add("Conflito no bloco 3x3");
        }

        if (!conflitos.isEmpty()) {
            return ResultadoValidacao.erros(conflitos);
        }

        return ResultadoValidacao.sucesso();
    }

    /**
     * Valida todo o tabuleiro em busca de conflitos.
     * Retorna lista de posições com conflitos.
     */
    public List<Posicao> encontrarConflitos(Tabuleiro tabuleiro) {
        List<Posicao> conflitos = new ArrayList<>();

        for (int linhaIdx = 0; linhaIdx < 9; linhaIdx++) {
            for (int colunaIdx = 0; colunaIdx < 9; colunaIdx++) {
                Celula celula = tabuleiro.getCelula(linhaIdx, colunaIdx);

                if (celula.isPreenchida()) {
                    Posicao posicao = new Posicao(linhaIdx + 1, colunaIdx + 1);

                    // Verifica se este valor causa conflito
                    if (temConflito(tabuleiro, posicao, celula.getValorAtual())) {
                        conflitos.add(posicao);
                    }
                }
            }
        }

        return conflitos;
    }

    /**
     * Verifica se o tabuleiro está totalmente correto (sem conflitos).
     */
    public boolean estaCorreto(Tabuleiro tabuleiro) {
        return encontrarConflitos(tabuleiro).isEmpty();
    }

    // ========== MÉTODOS PRIVADOS ==========

    private boolean temConflito(Tabuleiro tabuleiro, Posicao posicao, int valor) {
        return temConflitoNaLinha(tabuleiro, posicao, valor) ||
                temConflitoNaColuna(tabuleiro, posicao, valor) ||
                temConflitoNoBloco(tabuleiro, posicao, valor);
    }

    private boolean temConflitoNaLinha(Tabuleiro tabuleiro, Posicao posicao, int valor) {
        int linhaIdx = posicao.getLinhaIndex();

        for (int colunaIdx = 0; colunaIdx < 9; colunaIdx++) {
            if (colunaIdx == posicao.getColunaIndex()) {
                continue; // Pula a própria célula
            }

            Celula outra = tabuleiro.getCelula(linhaIdx, colunaIdx);
            if (outra.isPreenchida() && outra.getValorAtual() == valor) {
                return true;
            }
        }

        return false;
    }

    private boolean temConflitoNaColuna(Tabuleiro tabuleiro, Posicao posicao, int valor) {
        int colunaIdx = posicao.getColunaIndex();

        for (int linhaIdx = 0; linhaIdx < 9; linhaIdx++) {
            if (linhaIdx == posicao.getLinhaIndex()) {
                continue; // Pula a própria célula
            }

            Celula outra = tabuleiro.getCelula(linhaIdx, colunaIdx);
            if (outra.isPreenchida() && outra.getValorAtual() == valor) {
                return true;
            }
        }

        return false;
    }

    private boolean temConflitoNoBloco(Tabuleiro tabuleiro, Posicao posicao, int valor) {
        int linhaIdx = posicao.getLinhaIndex();
        int colunaIdx = posicao.getColunaIndex();

        int blocoLinhaInicio = (linhaIdx / 3) * 3;
        int blocoColunaInicio = (colunaIdx / 3) * 3;

        for (int l = blocoLinhaInicio; l < blocoLinhaInicio + 3; l++) {
            for (int c = blocoColunaInicio; c < blocoColunaInicio + 3; c++) {
                if (l == linhaIdx && c == colunaIdx) {
                    continue; // Pula a própria célula
                }

                Celula outra = tabuleiro.getCelula(l, c);
                if (outra.isPreenchida() && outra.getValorAtual() == valor) {
                    return true;
                }
            }
        }

        return false;
    }
}