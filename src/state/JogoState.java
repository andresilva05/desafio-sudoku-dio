package state;

import domain.model.Posicao;
import domain.model.Tabuleiro;
import validation.ResultadoValidacao;

/**
 * Interface comum para todos os estados do jogo (State Pattern).
 * Define as operações que podem ser realizadas em cada estado.
 */
public interface JogoState {

    // ========== OPERAÇÕES DO JOGO ==========

    /**
     * Tenta inserir um número em uma posição.
     * @return Resultado da validação
     */
    ResultadoValidacao inserirNumero(Posicao posicao, int valor, Tabuleiro tabuleiro);

    /**
     * Tenta remover um número de uma posição.
     * @return Resultado da validação
     */
    ResultadoValidacao removerNumero(Posicao posicao, Tabuleiro tabuleiro);

    /**
     * Verifica se o jogo pode ser finalizado.
     */
    ResultadoValidacao podeFinalizar(Tabuleiro tabuleiro);

    /**
     * Limpa células editáveis.
     * @return Resultado da operação
     */
    ResultadoValidacao limparCelulasEditaveis(Tabuleiro tabuleiro);

    /**
     * Finaliza o jogo se possível.
     * @return Próximo estado se finalizado, null se não
     */
    JogoState finalizarJogo(Tabuleiro tabuleiro);

    // ========== CONSULTAS ==========

    /**
     * Retorna o status textual do estado atual.
     */
    String getStatus(Tabuleiro tabuleiro);

    /**
     * Retorna o enum correspondente ao estado.
     */
    EstadoJogo getTipoEstado();

    /**
     * Verifica se o jogo está ativo (pode receber jogadas).
     */
    boolean isAtivo();

    /**
     * Verifica se o jogo foi vencido.
     */
    boolean isVencido();
}