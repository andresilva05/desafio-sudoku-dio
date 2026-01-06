/**
 * Ponto de entrada do programa.
 * Responsável por inicializar o sistema e processar argumentos de linha de comando.
 */
public class Main {

    /**
     * Método principal - inicia o jogo de Sudoku.
     * @param args Argumentos para células fixas no formato: linha coluna valor linha coluna valor...
     */
    public static void main(String[] args) {
        System.out.println("=== SUDOKU ===");
        System.out.println("Bem-vindo ao jogo de Sudoku!");

        try {
            // 1. Criar instância do jogo
            Jogo jogo = new Jogo();

            // 2. Processar argumentos para células fixas (REQUISITO 1)
            if (args.length > 0) {
                System.out.println("Inicializando com células fixas...");
                processarArgumentos(jogo, args);
            }

            // 3. Iniciar o jogo
            boolean iniciado = jogo.iniciarJogo();
            if (!iniciado) {
                System.out.println("Erro: " + jogo.getUltimoErro());
                return;
            }

            // 4. Criar e executar menu
            Menu menu = new Menu(jogo);
            menu.executar();

        } catch (Exception e) {
            System.out.println("❌ Erro crítico: " + e.getMessage());
            e.printStackTrace();
        }

        System.out.println("\nAté a próxima! 👋");
    }

    /**
     * Processa os argumentos de linha de comando para criar células fixas.
     * Formato esperado: linha coluna valor linha coluna valor...
     * Exemplo: "1 1 5 2 3 7" = célula (1,1)=5 fixa, célula (2,3)=7 fixa
     */
    private static void processarArgumentos(Jogo jogo, String[] args) {
        // Verifica se número de argumentos é múltiplo de 3
        if (args.length % 3 != 0) {
            throw new IllegalArgumentException(
                    "Número inválido de argumentos. " +
                            "Use formato: linha coluna valor linha coluna valor..."
            );
        }

        System.out.println("Processando " + args.length + " argumentos (" + (args.length/3) + " células fixas)");

        // Processa cada trio: linha, coluna, valor
        for (int i = 0; i < args.length; i += 3) {
            try {
                int linha = Integer.parseInt(args[i]);
                int coluna = Integer.parseInt(args[i + 1]);
                int valor = Integer.parseInt(args[i + 2]);

                System.out.printf("  Célula fixa: (%d,%d) = %d%n", linha, coluna, valor);

                // Validações básicas
                if (linha < 1 || linha > 9 || coluna < 1 || coluna > 9 || valor < 1 || valor > 9) {
                    throw new IllegalArgumentException(
                            String.format("Valores inválidos: linha=%d coluna=%d valor=%d (devem ser 1-9)",
                                    linha, coluna, valor)
                    );
                }

                // Marca célula como fixa
                boolean sucesso = jogo.marcarComoFixo(linha, coluna, valor);
                if (!sucesso) {
                    System.out.printf("  ⚠️ Aviso: Não foi possível marcar (%d,%d)=%d como fixo%n",
                            linha, coluna, valor);
                }

            } catch (NumberFormatException e) {
                throw new IllegalArgumentException(
                        String.format("Argumentos devem ser números: %s %s %s",
                                args[i], args[i+1], args[i+2])
                );
            }
        }

        System.out.println("Inicialização concluída com sucesso!");
    }
}