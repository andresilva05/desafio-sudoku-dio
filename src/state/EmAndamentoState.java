package state;

import domain.model.Posicao;
import domain.model.Tabuleiro;
import validation.ResultadoValidacao;

/**
 * Estado quando o jogo está em andamento.
 * Permite todas as operações de jogo.
 */
public class EmAndamentoState implements JogoState {

    @Override
    public ResultadoValidacao inserirNumero(Posicao posicao, int valor, Tabuleiro tabuleiro) {
        // Validações básicas já feitas pelo Tabuleiro
        boolean sucesso = tabuleiro.inserirNumero(posicao, valor);

        if (sucesso) {
            return ResultadoValidacao.sucesso();
        } else {
            return ResultadoValidacao.erro("Não foi possível inserir número na posição " + posicao);
        }
    }

    @Override
    public ResultadoValidacao removerNumero(Posicao posicao, Tabuleiro tabuleiro) {
        boolean sucesso = tabuleiro.removerNumero(posicao);

        if (sucesso) {
            return ResultadoValidacao.sucesso();
        } else {
            return ResultadoValidacao.erro("Não foi possível remover número da posição " + posicao);
        }
    }

    @Override
    public ResultadoValidacao podeFinalizar(Tabuleiro tabuleiro) {
        if (!tabuleiro.estaCompleto()) {
            return ResultadoValidacao.erro("Jogo incompleto. Preencha todas as células.");
        }

        // Aqui verificaria se não há erros (validação será feita por serviço)
        return ResultadoValidacao.sucesso();
    }

    @Override
    public ResultadoValidacao limparCelulasEditaveis(Tabuleiro tabuleiro) {
        int celulasLimpas = tabuleiro.limparCelulasEditaveis();

        if (celulasLimpas > 0) {
            return ResultadoValidacao.sucesso();
        } else {
            return ResultadoValidacao.erro("Nenhuma célula editável para limpar");
        }
    }

    @Override
    public JogoState finalizarJogo(Tabuleiro tabuleiro) {
        ResultadoValidacao resultado = podeFinalizar(tabuleiro);

        if (resultado.isValido()) {
            // Transição para estado vencido
            return new VencidoState();
        }

        // Se não pode finalizar, retorna null (mantém estado atual)
        return null;
    }

    @Override
    public String getStatus(Tabuleiro tabuleiro) {
        StringBuilder status = new StringBuilder();
        status.append("Jogo em andamento");
        status.append(" - ").append(tabuleiro.getCelulasPreenchidas()).append("/81 células");

        if (tabuleiro.temErros()) {
            status.append(" - CONTÉM ERROS");
        } else {
            status.append(" - sem erros");
        }

        return status.toString();
    }

    @Override
    public EstadoJogo getTipoEstado() {
        return EstadoJogo.EM_ANDAMENTO;
    }

    @Override
    public boolean isAtivo() {
        return true;
    }

    @Override
    public boolean isVencido() {
        return false;
    }

    /**
     * Verifica se deve transicionar para estado Completo.
     */
    public JogoState verificarTransicao(Tabuleiro tabuleiro) {
        if (tabuleiro.estaCompleto()) {
            return new CompletoState();
        }
        return this; // Mantém estado atual
    }
}