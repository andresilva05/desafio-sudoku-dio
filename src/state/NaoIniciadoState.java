package state;

import domain.model.Posicao;
import domain.model.Tabuleiro;
import validation.ResultadoValidacao;

/**
 * Estado inicial do jogo - antes de começar a jogar.
 * Permite apenas iniciar o jogo.
 */
public class NaoIniciadoState implements JogoState {

    @Override
    public ResultadoValidacao inserirNumero(Posicao posicao, int valor, Tabuleiro tabuleiro) {
        return ResultadoValidacao.erro("Jogo não iniciado. Use 'iniciarJogo()' primeiro.");
    }

    @Override
    public ResultadoValidacao removerNumero(Posicao posicao, Tabuleiro tabuleiro) {
        return ResultadoValidacao.erro("Jogo não iniciado. Use 'iniciarJogo()' primeiro.");
    }

    @Override
    public ResultadoValidacao podeFinalizar(Tabuleiro tabuleiro) {
        return ResultadoValidacao.erro("Jogo não iniciado.");
    }

    @Override
    public ResultadoValidacao limparCelulasEditaveis(Tabuleiro tabuleiro) {
        return ResultadoValidacao.erro("Jogo não iniciado.");
    }

    @Override
    public JogoState finalizarJogo(Tabuleiro tabuleiro) {
        // Não pode finalizar jogo não iniciado
        return null;
    }

    @Override
    public String getStatus(Tabuleiro tabuleiro) {
        return "Jogo não iniciado - pronto para começar!";
    }

    @Override
    public EstadoJogo getTipoEstado() {
        return EstadoJogo.NAO_INICIADO;
    }

    @Override
    public boolean isAtivo() {
        return false;
    }

    @Override
    public boolean isVencido() {
        return false;
    }

    /**
     * Transição para estado EmAndamento.
     */
    public JogoState iniciarJogo() {
        return new EmAndamentoState();
    }
}