package domain.model;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Representa uma célula individual do tabuleiro de Sudoku.
 * Cada célula pode estar vazia ou conter um número de 1 a 9.
 * Imutável em seus atributos básicos - alterações requerem novo objeto.
 */
public class Celula {
    // O valor atual da célula (null se vazia)
    private final Integer valorAtual;

    // Indica se o número é fixo (veio do puzzle inicial)
    private final boolean fixo;

    // Indica se a célula contém um valor em conflito
    private final boolean emErro;

    // Números de rascunho/anotações do usuário
    private final Set<Integer> rascunhos;

    /**
     * Construtor privado - use o Builder para criar instâncias.
     */
    Celula(Integer valorAtual, boolean fixo, boolean emErro, Set<Integer> rascunhos) {
        this.valorAtual = valorAtual;
        this.fixo = fixo;
        this.emErro = emErro;
        this.rascunhos = rascunhos != null ?
                Collections.unmodifiableSet(new HashSet<>(rascunhos)) :
                Collections.emptySet();

        // Validação de invariantes
        if (valorAtual != null && (valorAtual < 1 || valorAtual > 9)) {
            throw new IllegalArgumentException("Valor deve estar entre 1 e 9");
        }
        if (fixo && valorAtual == null) {
            throw new IllegalArgumentException("Célula fixa deve ter um valor");
        }
    }

    // ========== FACTORY METHODS ==========

    /**
     * Cria uma nova célula vazia e editável.
     */
    public static Celula criarVazia() {
        return new Celula(null, false, false, null);
    }

    /**
     * Cria uma nova célula fixa (inicial do puzzle).
     */
    public static Celula criarFixa(int valor) {
        return new Celula(valor, true, false, null);
    }

    /**
     * Cria uma nova célula preenchida pelo usuário.
     */
    public static Celula criarPreenchida(int valor) {
        return new Celula(valor, false, false, null);
    }

    // ========== GETTERS ==========

    public Integer getValorAtual() {
        return valorAtual;
    }

    public boolean isFixo() {
        return fixo;
    }

    public boolean isEditavel() {
        return !fixo;
    }

    public boolean isEmErro() {
        return emErro;
    }

    public Set<Integer> getRascunhos() {
        return rascunhos;
    }

    public boolean isVazia() {
        return valorAtual == null;
    }

    public boolean isPreenchida() {
        return valorAtual != null;
    }

    // ========== OPERAÇÕES QUE RETORNAM NOVAS INSTÂNCIAS ==========

    /**
     * Retorna uma nova célula com o valor atualizado.
     * Mantém o estado de fixo e rascunhos.
     */
    public Celula comValor(Integer novoValor) {
        if (fixo) {
            throw new IllegalStateException("Célula fixa não pode ser alterada");
        }
        if (novoValor != null && (novoValor < 1 || novoValor > 9)) {
            throw new IllegalArgumentException("Valor deve estar entre 1 e 9");
        }
        return new Celula(novoValor, fixo, false, rascunhos); // Reseta erro ao alterar
    }

    /**
     * Retorna uma nova célula marcada com erro.
     */
    public Celula marcarErro() {
        if (valorAtual == null) {
            throw new IllegalStateException("Célula vazia não pode estar em erro");
        }
        return new Celula(valorAtual, fixo, true, rascunhos);
    }

    /**
     * Retorna uma nova célula sem erro.
     */
    public Celula limparErro() {
        return new Celula(valorAtual, fixo, false, rascunhos);
    }

    /**
     * Retorna uma nova célula com rascunho adicionado.
     */
    public Celula adicionarRascunho(int numero) {
        if (numero < 1 || numero > 9) {
            throw new IllegalArgumentException("Número de rascunho deve estar entre 1 e 9");
        }
        Set<Integer> novosRascunhos = new HashSet<>(rascunhos);
        novosRascunhos.add(numero);
        return new Celula(valorAtual, fixo, emErro, novosRascunhos);
    }

    /**
     * Retorna uma nova célula com rascunho removido.
     */
    public Celula removerRascunho(int numero) {
        Set<Integer> novosRascunhos = new HashSet<>(rascunhos);
        novosRascunhos.remove(numero);
        return new Celula(valorAtual, fixo, emErro, novosRascunhos);
    }

    /**
     * Retorna uma nova célula sem rascunhos.
     */
    public Celula limparRascunhos() {
        return new Celula(valorAtual, fixo, emErro, null);
    }

    // ========== EQUALS & HASHCODE ==========

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Celula celula = (Celula) o;

        if (fixo != celula.fixo) return false;
        if (emErro != celula.emErro) return false;
        if (valorAtual != null ? !valorAtual.equals(celula.valorAtual) : celula.valorAtual != null) return false;
        return rascunhos.equals(celula.rascunhos);
    }

    @Override
    public int hashCode() {
        int result = valorAtual != null ? valorAtual.hashCode() : 0;
        result = 31 * result + (fixo ? 1 : 0);
        result = 31 * result + (emErro ? 1 : 0);
        result = 31 * result + rascunhos.hashCode();
        return result;
    }

    @Override
    public String toString() {
        String valorStr = valorAtual != null ? String.valueOf(valorAtual) : "·";
        String fixoStr = fixo ? "F" : "E";
        String erroStr = emErro ? "!" : " ";
        return String.format("%s%s%s", valorStr, fixoStr, erroStr);
    }

    // ========== BUILDER (OPCIONAL MAS ÚTIL) ==========

    /**
     * Builder para criar células com configurações complexas.
     * Útil para testes e criação de células específicas.
     */
    public static class Builder {
        private Integer valorAtual = null;
        private boolean fixo = false;
        private boolean emErro = false;
        private Set<Integer> rascunhos = new HashSet<>();

        public Builder comValor(Integer valor) {
            this.valorAtual = valor;
            return this;
        }

        public Builder fixo(boolean fixo) {
            this.fixo = fixo;
            return this;
        }

        public Builder emErro(boolean emErro) {
            this.emErro = emErro;
            return this;
        }

        public Builder adicionarRascunho(int rascunho) {
            this.rascunhos.add(rascunho);
            return this;
        }

        public Builder comRascunhos(Set<Integer> rascunhos) {
            this.rascunhos = rascunhos != null ? new HashSet<>(rascunhos) : new HashSet<>();
            return this;
        }

        public Celula build() {
            return new Celula(valorAtual, fixo, emErro, rascunhos);
        }
    }
}