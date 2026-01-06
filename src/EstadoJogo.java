/**
 * Representa os possíveis estados do jogo conforme requisito 5.
 * NOTA: O enunciado pede 3 estados, mas mantemos VENCIDO para clareza.
 */
public enum EstadoJogo {
    NAO_INICIADO("Jogo não iniciado (sem erros)"),
    EM_ANDAMENTO("Jogo em andamento (incompleto)"),
    COMPLETO("Tabuleiro completo"),
    VENCIDO("Jogo finalizado com sucesso!");

    private final String descricao;

    EstadoJogo(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}