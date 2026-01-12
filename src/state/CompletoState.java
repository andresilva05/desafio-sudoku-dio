package state;

import domain.model.Posicao;
import domain.model.Tabuleiro;
import validation.ResultadoValidacao;

/**
 * Estado quando o tabuleiro está completamente preenchido,
 * mas ainda não foi validado como correto.
 */
public class CompletoState implements JogoState {

    @Override
    public ResultadoValidacao inserirNumero(Posicao posicao, int valor, Tabuleiro tabuleiro) {
        // Em estado completo, ainda pode alterar células (para corrigir erros)
        return new EmAndamentoState().inserirNumero(posicao, valor, tabuleiro);
    }

    @Override
    public ResultadoValidacao removerNumero(Posicao posicao, Tabuleiro tabuleiro) {
        // Pode remover para corrigir erros
        ResultadoValidacao resultado = new EmAndamentoState().removerNumero(posicao, tabuleiro);

        if (resultado.isValido()) {
            // Se removeu, volta para estado EmAndamento
            // Essa transição será gerenciada pelo contexto
        }

        return resultado;
    }

    @Override
    public ResultadoValidacao podeFinalizar(Tabuleiro tabuleiro) {
        // Precisa verificar se está correto (sem conflitos)
        // Isso será feito pelo serviço de validação
        return ResultadoValidacao.sucesso();
    }

    @Override
    public ResultadoValidacao limparCelulasEditaveis(Tabuleiro tabuleiro) {
        // Pode limpar para recomeçar
        return new EmAndamentoState().limparCelulasEditaveis(tabuleiro);
    }

    @Override
    public JogoState finalizarJogo(Tabuleiro tabuleiro) {
        // A transição para Vencido depende da validação
        // Retorna null aqui - a decisão será do serviço
        return null;
    }

    @Override
    public String getStatus(Tabuleiro tabuleiro) {
        StringBuilder status = new StringBuilder();
        status.append("Tabuleiro completo!");
        status.append(" - ").append(tabuleiro.getCelulasPreenchidas()).append("/81 células");

        if (tabuleiro.temErros()) {
            status.append(" - CONTÉM ERROS (corrija para vencer)");
        } else {
            status.append(" - sem erros (pronto para finalizar!)");
        }

        return status.toString();
    }

    @Override
    public EstadoJogo getTipoEstado() {
        return EstadoJogo.COMPLETO;
    }

    @Override
    public boolean isAtivo() {
        return true; // Ainda pode fazer alterações
    }

    @Override
    public boolean isVencido() {
        return false;
    }

    /**
     * Transição para estado Vencido (se validação passar).
     */
    public JogoState marcarComoVencido() {
        return new VencidoState();
    }

    /**
     * Volta para estado EmAndamento (se alterar algo).
     */
    public JogoState voltarParaAndamento() {
        return new EmAndamentoState();
    }
}