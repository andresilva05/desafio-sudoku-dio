package ui;

import domain.model.Posicao;
import domain.model.Tabuleiro;
import service.JogoService;  // ← ESTE É O IMPORT CORRETO
import validation.ResultadoValidacao;

import java.util.Scanner;
/**
 * Interface de usuário do jogo de Sudoku (versão refatorada).
 * Responsável apenas por interação com usuário - toda lógica de negócio
 * é delegada para o JogoService.
 *
 * <p><b>Princípios aplicados:</b></p>
 * <ul>
 *   <li><b>Single Responsibility Principle (SRP)</b>: Apenas cuida da UI</li>
 *   <li><b>Dependency Inversion</b>: Depende da abstração JogoService, não de detalhes</li>
 *   <li><b>Separation of Concerns</b>: Zero regras de negócio na UI</li>
 * </ul>
 */
public class Menu {
    private JogoService jogoService;
    private Scanner scanner;
    private boolean executando;

    /**
     * Construtor que recebe o serviço do jogo por injeção de dependência.
     * Isso permite testar o Menu com um serviço mock.
     */
    public Menu(JogoService jogoService) {
        this.jogoService = jogoService;
        this.scanner = new Scanner(System.in);
        this.executando = true;
    }

    /**
     * Exibe o tabuleiro formatado para o usuário.
     * Usa cores ANSI para melhor visualização:
     * - Negrito: células fixas
     * - Vermelho: células com erro
     * - Normal: células editáveis
     *
     * <p><b>Decisão de design:</b> A formatação visual é responsabilidade da UI,
     * mas os dados vêm do domínio através do serviço.</p>
     */
    private void mostrarTabuleiro() {
        Tabuleiro tabuleiro = jogoService.getTabuleiro();

        System.out.println("\n    1 2 3   4 5 6   7 8 9");
        System.out.println("  ┌───────┬───────┬───────┐");

        for (int linha = 0; linha < 9; linha++) {
            System.out.print((linha + 1) + " │ ");

            for (int coluna = 0; coluna < 9; coluna++) {
                var celula = tabuleiro.getCelula(linha, coluna);
                Integer valor = celula.getValorAtual();

                // Formatação baseada no estado da célula
                if (valor == null) {
                    System.out.print("· ");
                } else {
                    String valorStr = String.valueOf(valor);

                    if (celula.isFixo()) {
                        // Célula fixa em negrito
                        System.out.print("\u001B[1m" + valorStr + "\u001B[0m ");
                    } else if (celula.isEmErro()) {
                        // Célula com erro em vermelho
                        System.out.print("\u001B[31m" + valorStr + "\u001B[0m ");
                    } else {
                        // Célula normal
                        System.out.print(valorStr + " ");
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
        System.out.println("  Legenda: \u001B[1mNegrito\u001B[0m = fixo | \u001B[31mVermelho\u001B[0m = erro");
    }

    /**
     * Exibe o menu principal com todas opções disponíveis.
     * O menu muda dinamicamente baseado no estado do jogo.
     *
     * <p><b>Decisão de design:</b> A UI consulta o estado atual para
     * decidir quais opções mostrar, mas não toma decisões de negócio.</p>
     */
    public void exibirMenu() {
        System.out.println("\n=== SUDOKU REFATORADO ===");

        // 1. Mostra tabuleiro atual
        mostrarTabuleiro();

        // 2. Mostra informações do jogo
        System.out.println("\n--- INFORMAÇÕES ---");
        System.out.println("Status: " + jogoService.verificarStatus());
        System.out.println("Jogadas: " + jogoService.getJogadasRealizadas());
        System.out.println("Estado: " + jogoService.getTipoEstado().getDescricao());

        // 3. Mostra opções do menu
        System.out.println("\n--- MENU ---");
        System.out.println("1. Inserir número");
        System.out.println("2. Remover número");
        System.out.println("3. Verificar status do jogo");
        System.out.println("4. Limpar células editáveis");
        System.out.println("5. Finalizar jogo");

        // Opções condicionais baseadas no estado
        if (!jogoService.isAtivo()) {
            System.out.println("6. Iniciar jogo");
        }

        System.out.println("0. Mostrar menu novamente");
        System.out.println("9. Sair");
    }

    /**
     * Processa a opção escolhida pelo usuário.
     * Cada opção delega para um método específico.
     *
     * <p><b>Decisão de design:</b> Switch simples que atua como roteador.
     * Facilita adicionar novas opções no futuro.</p>
     */
    public void processarOpcao(int opcao) {
        switch (opcao) {
            case 1 -> processarInserir();
            case 2 -> processarRemover();
            case 3 -> processarVerificarStatus();
            case 4 -> processarLimpar();
            case 5 -> processarFinalizar();
            case 6 -> processarIniciar();
            case 0 -> {} // Apenas mostra menu novamente
            case 9 -> {
                System.out.println("Saindo do jogo...");
                executando = false;
            }
            default -> System.out.println("⚠️  Opção inválida! Digite 0 para ver o menu.");
        }
    }

    /**
     * Loop principal do menu.
     * Controla o fluxo da aplicação de forma limpa.
     */
    public void executar() {
        System.out.println("🎮 Bem-vindo ao Sudoku (Versão Refatorada)!");
        System.out.println("📚 Padrões aplicados: State, Factory, SRP");

        while (executando) {
            try {
                exibirMenu();
                int opcao = lerOpcao();
                processarOpcao(opcao);

                // Pausa para usuário ver o resultado
                if (executando && opcao != 0) {
                    System.out.print("\nPressione Enter para continuar...");
                    scanner.nextLine();
                }

            } catch (Exception e) {
                System.out.println("❌ Erro: " + e.getMessage());
                scanner.nextLine(); // Limpa buffer
            }
        }

        scanner.close();
        System.out.println("\nObrigado por jogar! 👋");
    }

    /**
     * Lê e valida a opção do menu.
     *
     * <p><b>Decisão de design:</b> Validação de entrada é responsabilidade da UI.
     * O serviço valida regras de negócio, a UI valida formato.</p>
     */
    private int lerOpcao() {
        System.out.print("\nEscolha uma opção: ");

        while (!scanner.hasNextInt()) {
            System.out.println("⚠️  Erro: Digite um número válido!");
            scanner.next(); // Descarta entrada inválida
            System.out.print("Escolha uma opção: ");
        }

        int opcao = scanner.nextInt();
        scanner.nextLine(); // Limpa buffer do enter
        return opcao;
    }

    // ========== MÉTODOS PARA CADA OPERAÇÃO ==========

    /**
     * Processa inserção de número.
     * Coleta dados do usuário e delega para o serviço.
     */
    private void processarInserir() {
        System.out.println("\n--- INSERIR NÚMERO ---");

        Posicao posicao = lerPosicao("Digite linha e coluna (ex: 1 2): ");
        if (posicao == null) return;

        int valor = lerNumero("Digite o valor (1-9): ", 1, 9);
        if (valor == -1) return;

        // DELEGAÇÃO: Toda lógica está no serviço
        ResultadoValidacao resultado = jogoService.inserirNumero(
                posicao.getLinha(),
                posicao.getColuna(),
                valor
        );

        exibirResultado(resultado, "✓ Número inserido com sucesso!");
    }

    /**
     * Processa remoção de número.
     */
    private void processarRemover() {
        System.out.println("\n--- REMOVER NÚMERO ---");

        Posicao posicao = lerPosicao("Digite linha e coluna para remover (ex: 1 2): ");
        if (posicao == null) return;

        ResultadoValidacao resultado = jogoService.removerNumero(
                posicao.getLinha(),
                posicao.getColuna()
        );

        exibirResultado(resultado, "✓ Número removido com sucesso!");
    }

    /**
     * Processa verificação de status.
     */
    private void processarVerificarStatus() {
        System.out.println("\n--- STATUS DO JOGO ---");
        System.out.println(jogoService.verificarStatus());

        // Verificação adicional se pode finalizar
        if (jogoService.getTabuleiro().estaCompleto()) {
            ResultadoValidacao validacao = jogoService.podeFinalizar();
            if (validacao.isValido()) {
                System.out.println("✅ Pronto para finalizar!");
            } else {
                System.out.println("❌ " + validacao.getMensagemErro());
            }
        }
    }

    /**
     * Processa limpeza de células editáveis.
     */
    private void processarLimpar() {
        System.out.print("\n⚠️  Tem certeza que quer limpar TODAS as células editáveis? (S/N): ");
        String resposta = scanner.next().toUpperCase();
        scanner.nextLine(); // Limpa buffer

        if (resposta.equals("S") || resposta.equals("SIM")) {
            ResultadoValidacao resultado = jogoService.limparCelulasEditaveis();
            exibirResultado(resultado, "✓ Jogo limpo! Células fixas mantidas.");
        } else {
            System.out.println("Operação cancelada.");
        }
    }

    /**
     * Processa finalização do jogo.
     */
    private void processarFinalizar() {
        System.out.println("\n--- FINALIZAR JOGO ---");

        ResultadoValidacao resultado = jogoService.finalizarJogo();

        if (resultado.isValido()) {
            System.out.println("🎉 PARABÉNS! Você completou o Sudoku com sucesso!");
            System.out.println("Total de jogadas: " + jogoService.getJogadasRealizadas());
            executando = false; // Encerra o jogo
        } else {
            System.out.println("❌ " + resultado.getMensagemErro());
        }
    }

    /**
     * Processa início do jogo (apenas se não iniciado).
     */
    private void processarIniciar() {
        if (!jogoService.isAtivo()) {
            ResultadoValidacao resultado = jogoService.iniciarJogo();
            exibirResultado(resultado, "✓ Jogo iniciado! Boa sorte!");
        } else {
            System.out.println("⚠️  Jogo já está em andamento.");
        }
    }

    // ========== MÉTODOS AUXILIARES ==========

    /**
     * Lê uma posição (linha e coluna) do usuário.
     * Retorna null se entrada inválida.
     */
    private Posicao lerPosicao(String mensagem) {
        System.out.print(mensagem);

        try {
            int linha = scanner.nextInt();
            int coluna = scanner.nextInt();
            scanner.nextLine(); // Limpa buffer

            return new Posicao(linha, coluna);
        } catch (Exception e) {
            System.out.println("⚠️  Formato inválido! Use: linha coluna (ex: 1 2)");
            scanner.nextLine(); // Limpa buffer
            return null;
        }
    }

    /**
     * Lê um número dentro de um intervalo.
     * Retorna -1 se entrada inválida.
     */
    private int lerNumero(String mensagem, int min, int max) {
        System.out.print(mensagem);

        if (!scanner.hasNextInt()) {
            System.out.println("⚠️  Erro: Digite um número!");
            scanner.next();
            return -1;
        }

        int valor = scanner.nextInt();
        scanner.nextLine(); // Limpa buffer

        if (valor < min || valor > max) {
            System.out.printf("⚠️  Valor deve estar entre %d e %d%n", min, max);
            return -1;
        }

        return valor;
    }

    /**
     * Exibe resultado de uma operação de forma padronizada.
     */
    private void exibirResultado(ResultadoValidacao resultado, String mensagemSucesso) {
        if (resultado.isValido()) {
            System.out.println(mensagemSucesso);
        } else {
            System.out.println("❌ " + resultado.getMensagemErro());
        }
    }
}