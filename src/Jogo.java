/**
 * Classe principal que gerencia a lógica do jogo de Sudoku.
 * Controla estado, validações e coordena as operações.
 */
public class Jogo {

    private Tabuleiro tabuleiro;
    private ValidadorSudoku validador;
    private EstadoJogo estadoJogo = EstadoJogo.NAO_INICIADO;
    private boolean ultimaJogadaValida;
    private int jogadasRealizadas = 0;
    private String ultimoErro;

    /**
     * Construtor - inicializa jogo vazio.
     */
    public Jogo() {
        this.tabuleiro = new Tabuleiro();
        this.validador = new ValidadorSudoku();
        this.estadoJogo = EstadoJogo.NAO_INICIADO;
        this.ultimaJogadaValida = false;
        this.jogadasRealizadas = 0;
        this.ultimoErro = null;
    }

    /**
     * Converte índice de base 1 (usuário) para base 0 (array).
     */
    public static int converterParaBase0(int indiceBase1) {
        if (indiceBase1 < 1 || indiceBase1 > 9) {
            throw new IllegalArgumentException("Índice deve estar entre 1 e 9");
        }
        return indiceBase1 - 1;
    }

    // ========== OPERAÇÕES PRINCIPAIS ==========

    /**
     * Tenta inserir um número em uma posição do tabuleiro.
     * Valida todas as regras antes de permitir a inserção.
     */
    public boolean inserirNumero(int linha, int coluna, int valor) {
        ultimoErro = null;

        // 1. Validar estado do jogo
        if (estadoJogo != EstadoJogo.EM_ANDAMENTO) {
            ultimoErro = "Jogo não está em andamento";
            return false;
        }

        // 2. Validar coordenadas (1-9)
        if (linha < 1 || linha > 9) {
            ultimoErro = "Linha deve ser entre 1 e 9";
            return false;
        }
        if (coluna < 1 || coluna > 9) {
            ultimoErro = "Coluna deve ser entre 1 e 9";
            return false;
        }
        if (valor < 1 || valor > 9) {
            ultimoErro = "Valor deve ser entre 1 e 9";
            return false;
        }

        // 3. Converter para índices internos (0-8)
        int linhaIdx = linha - 1;
        int colunaIdx = coluna - 1;

        // 4. Obter célula
        Celula celula = tabuleiro.getCelula(linhaIdx, colunaIdx);
        if (celula == null) {
            ultimoErro = "Posição inválida";
            return false;
        }

        // 5. Validar se célula é fixa (REQUISITO 2)
        if (celula.isFixo()) {
            ultimoErro = "Célula fixa não pode ser alterada";
            return false;
        }

        if (celula.getValorAtual() != null) {
            ultimoErro = "Célula já está preenchida";
            return false;
        }

        // 6. Validar regras do Sudoku (sem conflitos)
        if (!validador.podeInserir(tabuleiro, linhaIdx, colunaIdx, valor)) {
            ultimoErro = "Número causa conflito no Sudoku";
            return false;
        }

        // 7. TUDO VALIDADO - Inserir número
        celula.setValorAtual(valor);
        tabuleiro.incrementarCelulasPreenchidas();
        jogadasRealizadas++;
        ultimaJogadaValida = true;

        // 8. Atualizar estado do jogo
        atualizarEstadoAposJogada();

        return true;
    }

    /**
     * Remove um número de uma posição (apenas células não fixas).
     */
    public boolean removerNumero(int linha, int coluna) {
        ultimoErro = null;

        // Validações similares a inserir
        if (estadoJogo != EstadoJogo.EM_ANDAMENTO) {
            ultimoErro = "Jogo não está em andamento";
            return false;
        }

        if (linha < 1 || linha > 9 || coluna < 1 || coluna > 9) {
            ultimoErro = "Linha e coluna devem ser entre 1 e 9";
            return false;
        }

        int linhaIdx = linha - 1;
        int colunaIdx = coluna - 1;

        Celula celula = tabuleiro.getCelula(linhaIdx, colunaIdx);
        if (celula == null) {
            ultimoErro = "Posição inválida";
            return false;
        }

        // REQUISITO 3: Não pode remover célula fixa
        if (celula.isFixo()) {
            ultimoErro = "Célula fixa não pode ser removida";
            return false;
        }

        if (celula.getValorAtual() == null) {
            ultimoErro = "Célula já está vazia";
            return false;
        }

        // Remover
        celula.setValorAtual(null);
        celula.setEmErro(false);
        tabuleiro.decrementarCelulasPreenchidas();
        jogadasRealizadas++;
        ultimaJogadaValida = true;

        // Atualizar estado
        if (estadoJogo == EstadoJogo.COMPLETO || estadoJogo == EstadoJogo.VENCIDO) {
            estadoJogo = EstadoJogo.EM_ANDAMENTO;
        }

        return true;
    }

    /**
     * Marca uma célula como fixa (usado na inicialização com args).
     */
    public boolean marcarComoFixo(int linha, int coluna, int valor) {
        // Validações básicas
        if (linha < 1 || linha > 9 || coluna < 1 || coluna > 9 || valor < 1 || valor > 9) {
            return false;
        }

        int linhaIdx = linha - 1;
        int colunaIdx = coluna - 1;

        Celula celula = tabuleiro.getCelula(linhaIdx, colunaIdx);
        if (celula == null) {
            return false;
        }

        // Marcar como fixa
        celula.setValorAtual(valor);
        celula.setFixo(true);
        tabuleiro.incrementarCelulasPreenchidas();
        tabuleiro.incrementarCelulasFixas();

        return true;
    }

    /**
     * Limpa todas as células editáveis (REQUISITO 6).
     */
    public boolean limparCelulasEditaveis() {
        ultimoErro = null;

        if (estadoJogo != EstadoJogo.EM_ANDAMENTO &&
                estadoJogo != EstadoJogo.COMPLETO &&
                estadoJogo != EstadoJogo.VENCIDO) {
            ultimoErro = "Jogo não está ativo";
            return false;
        }

        int celulasLimpas = 0;

        // Percorre todas células
        for (int linha = 0; linha < 9; linha++) {
            for (int coluna = 0; coluna < 9; coluna++) {
                Celula celula = tabuleiro.getCelula(linha, coluna);

                // Limpa apenas células editáveis com valor
                if (!celula.isFixo() && celula.getValorAtual() != null) {
                    celula.setValorAtual(null);
                    celula.setEmErro(false);
                    celulasLimpas++;
                    tabuleiro.decrementarCelulasPreenchidas();
                }
            }
        }

        if (celulasLimpas > 0) {
            jogadasRealizadas++;
            ultimaJogadaValida = true;
            estadoJogo = EstadoJogo.EM_ANDAMENTO;
            return true;
        } else {
            ultimoErro = "Nenhuma célula editável para limpar";
            return false;
        }
    }

    /**
     * Verifica se o jogo pode ser finalizado (REQUISITO 7).
     * @return true se completo e sem erros, false caso contrário
     */
    public boolean podeFinalizar() {
        if (!tabuleiro.estaCompleto()) {
            ultimoErro = "Jogo incompleto. Preencha todas as células.";
            return false;
        }

        // Valida se está correto (sem conflitos)
        boolean correto = validador.estaCorreto(tabuleiro);
        if (!correto) {
            ultimoErro = "Jogo completo mas com erros. Corrija os conflitos.";
            return false;
        }

        return true;
    }

    /**
     * Finaliza o jogo se possível (REQUISITO 7).
     */
    public boolean finalizarJogo() {
        if (podeFinalizar()) {
            estadoJogo = EstadoJogo.VENCIDO;
            return true;
        }
        return false;
    }

    /**
     * Atualiza o estado do jogo após uma jogada.
     */
    private void atualizarEstadoAposJogada() {
        if (tabuleiro.estaCompleto()) {
            // Valida se está correto
            boolean correto = validador.estaCorreto(tabuleiro);
            estadoJogo = correto ? EstadoJogo.VENCIDO : EstadoJogo.COMPLETO;
        } else {
            estadoJogo = EstadoJogo.EM_ANDAMENTO;
        }
    }

    // ========== CONSULTAS DE ESTADO ==========

    /**
     * Retorna o status detalhado do jogo (REQUISITO 5).
     * Inclui: estado, células preenchidas, e se tem erros.
     */
    public String verificarStatus() {
        StringBuilder status = new StringBuilder();

        switch (estadoJogo) {
            case NAO_INICIADO:
                status.append("Jogo não iniciado");
                break;

            case EM_ANDAMENTO:
                status.append("Jogo em andamento (incompleto)");
                break;

            case COMPLETO:
                status.append("Tabuleiro completo");
                break;

            case VENCIDO:
                status.append("Jogo finalizado com sucesso!");
                break;
        }

        // Adiciona informações de preenchimento
        status.append(String.format(" - %d/81 células", tabuleiro.getCelulasPreenchidas()));

        // REQUISITO 5: Informa se contém erros
        if (estadoJogo != EstadoJogo.NAO_INICIADO) {
            if (tabuleiro.temErros()) {
                status.append(" - CONTÉM ERROS");
            } else {
                status.append(" - sem erros");
            }
        }

        return status.toString();
    }

    // ========== GETTERS ==========

    public Tabuleiro getTabuleiro() {
        return tabuleiro;
    }

    public String getUltimoErro() {
        return ultimoErro;
    }

    public EstadoJogo getEstadoJogo() {
        return estadoJogo;
    }

    public boolean iniciarJogo() {
        if (estadoJogo != EstadoJogo.NAO_INICIADO) {
            ultimoErro = "Jogo já foi iniciado";
            return false;
        }
        estadoJogo = EstadoJogo.EM_ANDAMENTO;
        return true;
    }

    public boolean isUltimaJogadaValida() {
        return ultimaJogadaValida;
    }

    public int getJogadasRealizadas() {
        return jogadasRealizadas;
    }
}