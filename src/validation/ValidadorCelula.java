package validation;

import domain.model.Celula;
import domain.model.Posicao;
import domain.model.Tabuleiro;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Validador para regras específicas de células.
 * Puramente funcional - não altera estado.
 */
public class ValidadorCelula {

    /**
     * Valida se uma célula pode receber um valor específico.
     * Verifica se a célula é editável e se o valor é válido.
     */
    public ResultadoValidacao validarInsercao(Celula celula, int valor, Posicao posicao) {
        List<String> erros = new ArrayList<>();

        // 1. Valida valor
        if (valor < 1 || valor > 9) {
            erros.add("Valor deve estar entre 1 e 9");
        }

        // 2. Valida célula
        if (celula.isFixo()) {
            erros.add("Célula fixa não pode ser alterada em " + posicao);
        }

        if (celula.isPreenchida()) {
            erros.add("Célula já está preenchida em " + posicao);
        }

        if (!erros.isEmpty()) {
            return ResultadoValidacao.erros(erros);
        }

        return ResultadoValidacao.sucesso();
    }

    /**
     * Valida se uma célula pode ser removida.
     */
    public ResultadoValidacao validarRemocao(Celula celula, Posicao posicao) {
        List<String> erros = new ArrayList<>();

        if (celula.isFixo()) {
            erros.add("Célula fixa não pode ser removida em " + posicao);
        }

        if (celula.isVazia()) {
            erros.add("Célula já está vazia em " + posicao);
        }

        if (!erros.isEmpty()) {
            return ResultadoValidacao.erros(erros);
        }

        return ResultadoValidacao.sucesso();
    }
}