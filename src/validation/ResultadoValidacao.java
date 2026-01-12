package validation;

import java.util.ArrayList;
import java.util.List;

/**
 * Resultado de uma operação de validação.
 * Contém se é válido e mensagens de erro se houver.
 * Puramente funcional - sem efeitos colaterais.
 */
public class ResultadoValidacao {
    private final boolean valido;
    private final List<String> mensagensErro;

    private ResultadoValidacao(boolean valido, List<String> mensagensErro) {
        this.valido = valido;
        this.mensagensErro = mensagensErro != null ? new ArrayList<>(mensagensErro) : new ArrayList<>();
    }

    public static ResultadoValidacao sucesso() {
        return new ResultadoValidacao(true, null);
    }

    public static ResultadoValidacao erro(String mensagemErro) {
        List<String> erros = new ArrayList<>();
        erros.add(mensagemErro);
        return new ResultadoValidacao(false, erros);
    }

    public static ResultadoValidacao erros(List<String> mensagensErro) {
        return new ResultadoValidacao(false, mensagensErro);
    }

    public boolean isValido() {
        return valido;
    }

    public List<String> getMensagensErro() {
        return new ArrayList<>(mensagensErro);
    }

    public String getMensagemErro() {
        if (mensagensErro.isEmpty()) {
            return "";
        }
        return mensagensErro.get(0);
    }

    @Override
    public String toString() {
        if (valido) {
            return "Validação bem-sucedida";
        } else {
            return "Erros: " + String.join(", ", mensagensErro);
        }
    }
}