package service;

import domain.model.Posicao;
import domain.model.Tabuleiro;
import validation.RegraSudoku;
import validation.ResultadoValidacao;
import validation.ValidadorCelula;

import java.util.List;

/**
 * Serviço que coordena todas as validações do Sudoku.
 * Separa detecção de conflitos (pura) de marcação de erros (com efeito).
 */
public class ValidadorService {
    private final ValidadorCelula validadorCelula;
    private final RegraSudoku regraSudoku;

    public ValidadorService() {
        this.validadorCelula = new ValidadorCelula();
        this.regraSudoku = new RegraSudoku();
    }

    /**
     * Valida se um número pode ser inserido em uma posição.
     * Combina validação de célula com regras do Sudoku.
     */
    public ResultadoValidacao validarInsercao(Tabuleiro tabuleiro, Posicao posicao, int valor) {
        // 1. Valida a célula em si
        ResultadoValidacao validacaoCelula = validadorCelula.validarInsercao(
                tabuleiro.getCelula(posicao), valor, posicao
        );

        if (!validacaoCelula.isValido()) {
            return validacaoCelula;
        }

        // 2. Valida regras do Sudoku
        return regraSudoku.podeInserir(tabuleiro, posicao, valor);
    }

    /**
     * Valida se um número pode ser removido de uma posição.
     */
    public ResultadoValidacao validarRemocao(Tabuleiro tabuleiro, Posicao posicao) {
        return validadorCelula.validarRemocao(tabuleiro.getCelula(posicao), posicao);
    }

    /**
     * Valida todo o tabuleiro e marca células com erro.
     * Retorna resultado da validação.
     */
    public ResultadoValidacao validarTabuleiro(Tabuleiro tabuleiro) {
        // Primeiro limpa erros anteriores
        tabuleiro.limparTodosErros();

        // Encontra conflitos
        List<Posicao> conflitos = regraSudoku.encontrarConflitos(tabuleiro);

        // Marca células com erro
        for (Posicao posicao : conflitos) {
            tabuleiro.marcarErro(posicao.getLinhaIndex(), posicao.getColunaIndex());
        }

        // Retorna resultado
        if (conflitos.isEmpty()) {
            return ResultadoValidacao.sucesso();
        } else {
            return ResultadoValidacao.erro("Tabuleiro contém " + conflitos.size() + " conflitos");
        }
    }

    /**
     * Valida se o tabuleiro completo está correto.
     * Usado para finalização do jogo.
     */
    public ResultadoValidacao validarTabuleiroCompleto(Tabuleiro tabuleiro) {
        if (!tabuleiro.estaCompleto()) {
            return ResultadoValidacao.erro("Tabuleiro não está completo");
        }

        boolean correto = regraSudoku.estaCorreto(tabuleiro);

        if (correto) {
            return ResultadoValidacao.sucesso();
        } else {
            // Valida para marcar os erros
            validarTabuleiro(tabuleiro);
            return ResultadoValidacao.erro("Tabuleiro completo mas com erros");
        }
    }

    /**
     * Verifica se o tabuleiro está pronto para ser finalizado.
     */
    public ResultadoValidacao podeFinalizar(Tabuleiro tabuleiro) {
        if (!tabuleiro.estaCompleto()) {
            return ResultadoValidacao.erro("Jogo incompleto. Preencha todas as células.");
        }

        return validarTabuleiroCompleto(tabuleiro);
    }
}