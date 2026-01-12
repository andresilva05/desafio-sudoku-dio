package service;

import domain.model.Posicao;
import domain.model.Tabuleiro;
import state.*;
import validation.ResultadoValidacao;

/**
 * Serviço principal que orquestra o jogo de Sudoku.
 * Substitui a antiga classe Jogo, aplicando separação de responsabilidades.
 * Gerencia estado do jogo usando State Pattern.
 */
public class JogoService {
    private Tabuleiro tabuleiro;
    private JogoState estadoAtual;
    private int jogadasRealizadas;

    // Serviços especializados
    private final ValidadorService validadorService;

    /**
     * Construtor privado - use a fábrica para criar instâncias.
     */
    public JogoService(Tabuleiro tabuleiro, JogoState estadoInicial, ValidadorService validadorService) {
        this.tabuleiro = tabuleiro;
        this.estadoAtual = estadoInicial;
        this.validadorService = validadorService;
        this.jogadasRealizadas = 0;
    }

    // ========== OPERAÇÕES PRINCIPAIS ==========

    /**
     * Tenta inserir um número em uma posição do tabuleiro.
     * Valida através do estado atual e executa se permitido.
     */
    public ResultadoValidacao inserirNumero(int linha, int coluna, int valor) {
        Posicao posicao = new Posicao(linha, coluna);

        // 1. Validação básica de entrada
        ResultadoValidacao validacaoBasica = validarEntrada(posicao, valor);
        if (!validacaoBasica.isValido()) {
            return validacaoBasica;
        }

        // 2. Validação pelo estado atual (State Pattern)
        ResultadoValidacao validacaoEstado = estadoAtual.inserirNumero(posicao, valor, tabuleiro);
        if (!validacaoEstado.isValido()) {
            return validacaoEstado;
        }

        // 3. Executa a inserção (já feita pelo estado)
        jogadasRealizadas++;

        // 4. Verifica transições de estado
        verificarTransicaoEstado();

        // 5. Valida se há conflitos após a inserção
        if (tabuleiro.getCelulasPreenchidas() > 0) {
            validadorService.validarTabuleiro(tabuleiro);
        }

        return ResultadoValidacao.sucesso();
    }

    /**
     * Remove um número de uma posição (apenas células editáveis).
     */
    public ResultadoValidacao removerNumero(int linha, int coluna) {
        Posicao posicao = new Posicao(linha, coluna);

        // 1. Validação básica
        if (linha < 1 || linha > 9 || coluna < 1 || coluna > 9) {
            return ResultadoValidacao.erro("Linha e coluna devem ser entre 1 e 9");
        }

        // 2. Validação pelo estado atual
        ResultadoValidacao validacaoEstado = estadoAtual.removerNumero(posicao, tabuleiro);
        if (!validacaoEstado.isValido()) {
            return validacaoEstado;
        }

        // 3. Executa a remoção (já feita pelo estado)
        jogadasRealizadas++;

        // 4. Verifica transições de estado
        verificarTransicaoEstado();

        return ResultadoValidacao.sucesso();
    }

    /**
     * Marca uma célula como fixa (usado apenas na inicialização).
     */
    public ResultadoValidacao marcarComoFixo(int linha, int coluna, int valor) {
        // Só permite em estado não iniciado
        if (!(estadoAtual instanceof NaoIniciadoState)) {
            return ResultadoValidacao.erro("Células fixas só podem ser definidas antes de iniciar o jogo");
        }

        Posicao posicao = new Posicao(linha, coluna);

        // Validação básica
        ResultadoValidacao validacaoBasica = validarEntrada(posicao, valor);
        if (!validacaoBasica.isValido()) {
            return validacaoBasica;
        }

        // Marca como fixa
        boolean sucesso = tabuleiro.marcarComoFixo(posicao.getLinhaIndex(), posicao.getColunaIndex(), valor);

        if (sucesso) {
            return ResultadoValidacao.sucesso();
        } else {
            return ResultadoValidacao.erro("Não foi possível marcar célula como fixa");
        }
    }

    /**
     * Inicia o jogo (transição de NaoIniciado para EmAndamento).
     */
    public ResultadoValidacao iniciarJogo() {
        if (estadoAtual instanceof NaoIniciadoState) {
            NaoIniciadoState estadoNaoIniciado = (NaoIniciadoState) estadoAtual;
            estadoAtual = estadoNaoIniciado.iniciarJogo();
            return ResultadoValidacao.sucesso();
        } else {
            return ResultadoValidacao.erro("Jogo já foi iniciado");
        }
    }

    /**
     * Limpa todas as células editáveis.
     */
    public ResultadoValidacao limparCelulasEditaveis() {
        ResultadoValidacao validacaoEstado = estadoAtual.limparCelulasEditaveis(tabuleiro);

        if (validacaoEstado.isValido()) {
            jogadasRealizadas++;
            // Após limpar, sempre volta para EmAndamento
            if (estadoAtual instanceof CompletoState) {
                estadoAtual = ((CompletoState) estadoAtual).voltarParaAndamento();
            }
            return ResultadoValidacao.sucesso();
        }

        return validacaoEstado;
    }

    /**
     * Finaliza o jogo se possível.
     */
    public ResultadoValidacao finalizarJogo() {
        // 1. Verifica se pode finalizar pelo estado
        ResultadoValidacao podeFinalizar = estadoAtual.podeFinalizar(tabuleiro);
        if (!podeFinalizar.isValido()) {
            return podeFinalizar;
        }

        // 2. Valida se o tabuleiro está correto
        ResultadoValidacao validacao = validadorService.validarTabuleiroCompleto(tabuleiro);
        if (!validacao.isValido()) {
            return validacao;
        }

        // 3. Transição para estado vencido
        JogoState novoEstado = estadoAtual.finalizarJogo(tabuleiro);
        if (novoEstado != null) {
            estadoAtual = novoEstado;
        } else if (estadoAtual instanceof CompletoState) {
            // Se estava completo e passou na validação, marca como vencido
            estadoAtual = ((CompletoState) estadoAtual).marcarComoVencido();
        }

        return ResultadoValidacao.sucesso();
    }

    // ========== CONSULTAS ==========

    /**
     * Retorna o status atual do jogo.
     */
    public String verificarStatus() {
        return estadoAtual.getStatus(tabuleiro);
    }

    /**
     * Verifica se o jogo pode ser finalizado.
     */
    public ResultadoValidacao podeFinalizar() {
        // Primeiro valida pelo estado
        ResultadoValidacao validacaoEstado = estadoAtual.podeFinalizar(tabuleiro);
        if (!validacaoEstado.isValido()) {
            return validacaoEstado;
        }

        // Depois valida o conteúdo
        return validadorService.validarTabuleiroCompleto(tabuleiro);
    }

    // ========== GETTERS ==========

    public Tabuleiro getTabuleiro() {
        return tabuleiro;
    }

    public JogoState getEstadoAtual() {
        return estadoAtual;
    }

    public int getJogadasRealizadas() {
        return jogadasRealizadas;
    }

    public boolean isAtivo() {
        return estadoAtual.isAtivo();
    }

    public boolean isVencido() {
        return estadoAtual.isVencido();
    }

    public EstadoJogo getTipoEstado() {
        return estadoAtual.getTipoEstado();
    }

    // ========== MÉTODOS PRIVADOS ==========

    private ResultadoValidacao validarEntrada(Posicao posicao, int valor) {
        // Validações básicas que não dependem do estado
        if (valor < 1 || valor > 9) {
            return ResultadoValidacao.erro("Valor deve estar entre 1 e 9");
        }

        // Posicao já valida linha/coluna no construtor

        return ResultadoValidacao.sucesso();
    }

    private void verificarTransicaoEstado() {
        // Verifica transições automáticas baseadas no tabuleiro
        if (estadoAtual instanceof EmAndamentoState) {
            JogoState possivelNovoEstado = ((EmAndamentoState) estadoAtual).verificarTransicao(tabuleiro);
            if (possivelNovoEstado != estadoAtual) {
                estadoAtual = possivelNovoEstado;
            }
        } else if (estadoAtual instanceof CompletoState) {
            // Se em estado completo e removemos algo, volta para em andamento
            if (!tabuleiro.estaCompleto()) {
                estadoAtual = ((CompletoState) estadoAtual).voltarParaAndamento();
            }
        }
    }
}