import java.util.Scanner;

/**
 * Interface de usuário do jogo de Sudoku.
 * Responsável por mostrar opções, coletar entradas e exibir resultados.
 * NÃO contém lógica de negócio - apenas delega para a classe Jogo.
 */
public class Menu {

    private Jogo jogo;
    private Scanner scanner;
    private boolean executando;

    /**
     * Construtor - recebe instância do jogo para delegar operações.
     */
    public Menu(Jogo jogo) {
        this.jogo = jogo;
        this.scanner = new Scanner(System.in);
        this.executando = true;
    }

    /**
     * Mostra o tabuleiro atual formatado.
     * Células fixas aparecem em negrito.
     */
    private void mostrarTabuleiro() {
        Tabuleiro tabuleiro = jogo.getTabuleiro();

        System.out.println("\n    1 2 3   4 5 6   7 8 9");
        System.out.println("  ┌───────┬───────┬───────┐");

        for (int linha = 0; linha < 9; linha++) {
            System.out.print((linha + 1) + " │ ");

            for (int coluna = 0; coluna < 9; coluna++) {
                Celula celula = tabuleiro.getCelula(linha, coluna);
                Integer valor = celula.getValorAtual();

                // Formatação: ponto para vazio, número normal ou em negrito para fixos
                if (valor == null) {
                    System.out.print("· ");
                } else {
                    if (celula.isFixo()) {
                        System.out.print("\u001B[1m" + valor + "\u001B[0m "); // Negrito
                    } else {
                        // Mostra em vermelho se estiver em erro
                        if (celula.isEmErro()) {
                            System.out.print("\u001B[31m" + valor + "\u001B[0m ");
                        } else {
                            System.out.print(valor + " ");
                        }
                    }
                }

                // Separadores visuais entre blocos 3x3
                if (coluna == 2 || coluna == 5) {
                    System.out.print("│ ");
                }
            }

            System.out.println("│");

            // Linhas separadoras entre blocos 3x3
            if (linha == 2 || linha == 5) {
                System.out.println("  ├───────┼───────┼───────┤");
            }
        }

        System.out.println("  └───────┴───────┴───────┘");
        System.out.println("  (Negrito = números fixos)");
    }

    /**
     * Exibe o menu principal com todas opções.
     */
    public void exibir() {
        System.out.println("\n=== SUDOKU ===");

        // 1. Mostra tabuleiro atual
        mostrarTabuleiro();

        // 2. Mostra opções do menu
        System.out.println("\n--- MENU ---");
        System.out.println("1. Inserir número");
        System.out.println("2. Remover número");
        System.out.println("3. Verificar jogo (mostrar tabuleiro)");
        System.out.println("4. Verificar status do jogo");
        System.out.println("5. Limpar células editáveis");
        System.out.println("6. Finalizar jogo");
        System.out.println("7. Sair");
        System.out.println("0. Mostrar este menu novamente");
    }

    /**
     * Processa a opção escolhida pelo usuário.
     */
    public void processarOpcao(int opcao) {
        switch (opcao) {
            case 1:
                processarInserir();
                break;

            case 2:
                processarRemover();
                break;

            case 3:
                // Verificar jogo = mostrar tabuleiro (já feito no exibir())
                System.out.println("Tabuleiro atual:");
                break;

            case 4:
                processarVerificarStatus();
                break;

            case 5:
                processarLimpar();
                break;

            case 6:
                processarFinalizar();
                break;

            case 7:
                System.out.println("Saindo do jogo...");
                executando = false;
                break;

            case 0:
                // Apenas mostra menu novamente no próximo loop
                break;

            default:
                System.out.println("Opção inválida! Digite 0 para ver o menu.");
        }
    }

    /**
     * Loop principal do menu.
     */
    public void executar() {
        while (executando) {
            exibir();
            int opcao = lerOpcao();
            processarOpcao(opcao);
        }
        scanner.close();
    }

    /**
     * Lê e valida a opção do menu.
     */
    private int lerOpcao() {
        System.out.print("\nEscolha uma opção: ");

        // Valida se é número
        while (!scanner.hasNextInt()) {
            System.out.println("Erro: Digite um número válido!");
            scanner.next();
            System.out.print("Escolha uma opção: ");
        }

        int opcao = scanner.nextInt();
        scanner.nextLine(); // Limpa buffer do enter
        return opcao;
    }

    // ========== MÉTODOS PARA CADA OPERAÇÃO ==========

    /**
     * Processa inserção de número (REQUISITO 2).
     */
    private void processarInserir() {
        System.out.println("\n--- INSERIR NÚMERO ---");

        int linha = lerCoordenada("Linha (1-9): ");
        if (linha == -1) return;

        int coluna = lerCoordenada("Coluna (1-9): ");
        if (coluna == -1) return;

        int valor = lerCoordenada("Valor (1-9): ");
        if (valor == -1) return;

        // Delega para o Jogo (que fará todas validações)
        boolean sucesso = jogo.inserirNumero(linha, coluna, valor);

        if (sucesso) {
            System.out.println("✓ Número inserido com sucesso!");
        } else {
            System.out.println("✗ Não foi possível inserir: " + jogo.getUltimoErro());
        }
    }

    /**
     * Processa remoção de número (REQUISITO 3).
     */
    private void processarRemover() {
        System.out.println("\n--- REMOVER NÚMERO ---");

        int linha = lerCoordenada("Linha para remover (1-9): ");
        if (linha == -1) return;

        int coluna = lerCoordenada("Coluna para remover (1-9): ");
        if (coluna == -1) return;

        boolean sucesso = jogo.removerNumero(linha, coluna);

        if (sucesso) {
            System.out.println("✓ Número removido com sucesso!");
        } else {
            System.out.println("✗ Não foi possível remover: " + jogo.getUltimoErro());
        }
    }

    /**
     * Processa verificação de status (REQUISITO 5).
     */
    private void processarVerificarStatus() {
        System.out.println("\n--- STATUS DO JOGO ---");
        String status = jogo.verificarStatus();
        System.out.println("Status: " + status);
    }

    /**
     * Processa limpeza de células editáveis (REQUISITO 6).
     */
    private void processarLimpar() {
        System.out.print("\nTem certeza que quer limpar todas células editáveis? (S/N): ");
        String resposta = scanner.next().toUpperCase();
        scanner.nextLine(); // Limpa buffer

        if (resposta.equals("S") || resposta.equals("SIM")) {
            boolean sucesso = jogo.limparCelulasEditaveis();
            if (sucesso) {
                System.out.println("✓ Jogo limpo! Células fixas mantidas.");
            } else {
                System.out.println("✗ " + jogo.getUltimoErro());
            }
        } else {
            System.out.println("Operação cancelada.");
        }
    }

    /**
     * Processa finalização do jogo (REQUISITO 7).
     */
    private void processarFinalizar() {
        System.out.println("\n--- FINALIZAR JOGO ---");

        boolean sucesso = jogo.finalizarJogo();
        if (sucesso) {
            System.out.println("🎉 PARABÉNS! Você completou o Sudoku com sucesso!");
            System.out.println("Jogo finalizado. Obrigado por jogar!");
            executando = false;
        } else {
            System.out.println("✗ " + jogo.getUltimoErro());
        }
    }

    /**
     * Método auxiliar para ler coordenadas/números com validação.
     * @return valor lido ou -1 se entrada inválida
     */
    private int lerCoordenada(String mensagem) {
        System.out.print(mensagem);

        if (!scanner.hasNextInt()) {
            System.out.println("Erro: Digite um número!");
            scanner.next();
            return -1;
        }

        int valor = scanner.nextInt();
        scanner.nextLine(); // Limpa buffer

        return valor;
    }
}