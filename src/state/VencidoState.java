package state;

import domain.model.Posicao;
import domain.model.Tabuleiro;
import validation.ResultadoValidacao;

/**
 * Estado final - jogo vencido com sucesso.
 * Não permite mais alterações.
 */
public class VencidoState implements JogoState {

    @Override
    public ResultadoValidacao inserirNumero(Posicao posicao, int valor, Tabuleiro tabuleiro) {
        return ResultadoValidacao.erro("Jogo já finalizado! Não é possível fazer mais jogadas.");
    }

    @Override
    public ResultadoValidacao removerNumero(Posicao posicao, Tabuleiro tabuleiro) {
        return ResultadoValidacao.erro("Jogo já finalizado! Não é possível fazer mais jogadas.");
    }

    @Override
    public ResultadoValidacao podeFinalizar(Tabuleiro tabuleiro) {
        return ResultadoValidacao.erro("Jogo já está finalizado.");
    }

    @Override
    public ResultadoValidacao limparCelulasEditaveis(Tabuleiro tabuleiro) {
        return ResultadoValidacao.erro("Jogo já finalizado! Não é possível alterar.");
    }

    @Override
    public JogoState finalizarJogo(Tabuleiro tabuleiro) {
        // Já está finalizado
        return this;
    }

    @Override
    public String getStatus(Tabuleiro tabuleiro) {
        return "🎉 PARABÉNS! Você completou o Sudoku com sucesso!";
    }

    @Override
    public EstadoJogo getTipoEstado() {
        return EstadoJogo.VENCIDO;
    }

    @Override
    public boolean isAtivo() {
        return false;
    }

    @Override
    public boolean isVencido() {
        return true;
    }
}