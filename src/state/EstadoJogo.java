package state;

/**
 * Enum que representa os possíveis estados do jogo.
 * Mantido para compatibilidade, mas a lógica de estado agora está nas classes State.
 */
public enum EstadoJogo {
    NAO_INICIADO("Jogo não iniciado"),
    EM_ANDAMENTO("Jogo em andamento"),
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