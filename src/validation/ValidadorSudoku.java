package validation;

import domain.model.Tabuleiro;
import service.ValidadorService;

/**
 * Classe de compatibilidade durante a transição.
 * Encapsula o novo ValidadorService mantendo a interface antiga.
 */
public class ValidadorSudoku {
    private final ValidadorService validadorService;
    private int validacoesRealizadas = 0;

    public ValidadorSudoku() {
        this.validadorService = new ValidadorService();
    }


    public boolean podeInserir(Tabuleiro tabuleiro, int linha, int coluna, int valor) {
        validacoesRealizadas++;

        // Converter para nova API
        domain.model.Posicao posicao = new domain.model.Posicao(linha + 1, coluna + 1);
        var resultado = validadorService.validarInsercao(tabuleiro, posicao, valor);
        return resultado.isValido();
    }

    public boolean estaCorreto(Tabuleiro tabuleiro) {
        validacoesRealizadas++;
        var resultado = validadorService.validarTabuleiroCompleto(tabuleiro);
        return resultado.isValido();
    }

    public int getValidacoesRealizadas() {
        return validacoesRealizadas;
    }

}