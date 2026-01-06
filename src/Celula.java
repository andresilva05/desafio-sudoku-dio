import java.util.Set;
import java.util.HashSet;

/**
 * Representa uma célula individual do tabuleiro de Sudoku.
 * Cada célula pode estar vazia ou conter um número de 1 a 9.
 */
public class Celula {

    // O valor atual da célula (null se vazia)
    private Integer valorAtual = null;

    // Indica se o número é fixo (veio do puzzle inicial)
    private boolean fixo = false;

    // Indica se a célula pode ser editada pelo usuário
    private boolean editavel = true;

    // Números de rascunho/anotações do usuário
    private Set<Integer> rascunhos = new HashSet<>();

    // Indica se a célula contém um valor em conflito
    private boolean emErro = false;

    /**
     * Construtor para criar uma célula com estado específico.
     */
    public Celula(Integer valorAtual, boolean fixo, boolean editavel,
                  Set<Integer> rascunhos, boolean emErro) {
        this.valorAtual = valorAtual;
        this.fixo = fixo;
        this.editavel = editavel;
        this.rascunhos = rascunhos;
        this.emErro = emErro;
    }

    // ========== GETTERS ==========

    public Integer getValorAtual() {
        return valorAtual;
    }

    public boolean isFixo() {
        return fixo;
    }

    public boolean isEditavel() {
        return editavel;
    }

    public Set<Integer> getRascunhos() {
        return rascunhos;
    }

    public boolean isEmErro() {
        return emErro;
    }

    // ========== SETTERS ==========

    public void setValorAtual(Integer valorAtual) {
        this.valorAtual = valorAtual;
    }

    public void setFixo(boolean fixo) {
        this.fixo = fixo;
        this.editavel = !fixo; // Se é fixo, não é editável
    }

    public void setEmErro(boolean emErro) {
        this.emErro = emErro;
    }

    /**
     * Adiciona um número aos rascunhos (anotações).
     */
    public void adicionarRascunho(int numero) {
        if (numero >= 1 && numero <= 9) {
            rascunhos.add(numero);
        }
    }

    /**
     * Remove um número dos rascunhos.
     */
    public void removerRascunho(int numero) {
        rascunhos.remove(numero);
    }

    /**
     * Limpa todos os rascunhos.
     */
    public void limparRascunhos() {
        rascunhos.clear();
    }
}