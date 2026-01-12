package factory;

import domain.model.Celula;
import domain.model.Tabuleiro;

/**
 * Factory para criação de tabuleiros de Sudoku.
 * Centraliza a lógica de inicialização.
 */
public class TabuleiroFactory {

    /**
     * Cria um tabuleiro vazio (todas células editáveis e vazias).
     */
    public static Tabuleiro criarTabuleiroVazio() {
        return new Tabuleiro(); // Construtor já cria grade vazia
    }

    /**
     * Cria um tabuleiro com células fixas a partir de argumentos.
     * Formato: linha1 coluna1 valor1 linha2 coluna2 valor2 ...
     */
    public static Tabuleiro criarTabuleiroComFixos(String[] args) {
        Tabuleiro tabuleiro = criarTabuleiroVazio();

        if (args == null || args.length == 0) {
            return tabuleiro;
        }

        // Verifica se número de argumentos é múltiplo de 3
        if (args.length % 3 != 0) {
            throw new IllegalArgumentException(
                    "Número inválido de argumentos. Use formato: linha coluna valor linha coluna valor..."
            );
        }

        // Processa cada trio: linha, coluna, valor
        for (int i = 0; i < args.length; i += 3) {
            try {
                int linha = Integer.parseInt(args[i]);
                int coluna = Integer.parseInt(args[i + 1]);
                int valor = Integer.parseInt(args[i + 2]);

                // Validações básicas
                if (linha < 1 || linha > 9 || coluna < 1 || coluna > 9 || valor < 1 || valor > 9) {
                    throw new IllegalArgumentException(
                            String.format("Valores inválidos: linha=%d coluna=%d valor=%d (devem ser 1-9)",
                                    linha, coluna, valor)
                    );
                }

                // Marca célula como fixa
                boolean sucesso = tabuleiro.marcarComoFixo(linha - 1, coluna - 1, valor);

                if (!sucesso) {
                    System.err.printf("⚠️ Aviso: Não foi possível marcar (%d,%d)=%d como fixo%n",
                            linha, coluna, valor);
                }

            } catch (NumberFormatException e) {
                throw new IllegalArgumentException(
                        String.format("Argumentos devem ser números: %s %s %s",
                                args[i], args[i+1], args[i+2])
                );
            }
        }

        return tabuleiro;
    }

    /**
     * Cria um tabuleiro de exemplo para testes.
     */
    public static Tabuleiro criarTabuleiroExemplo() {
        Tabuleiro tabuleiro = criarTabuleiroVazio();

        // Define algumas células fixas (um puzzle fácil)
        int[][] fixos = {
                {1, 1, 5}, {1, 2, 3}, {1, 5, 7},
                {2, 1, 6}, {2, 4, 1}, {2, 5, 9}, {2, 6, 5},
                {3, 2, 9}, {3, 3, 8}, {3, 8, 6},
                {4, 1, 8}, {4, 5, 6}, {4, 9, 3},
                {5, 1, 4}, {5, 4, 8}, {5, 6, 3}, {5, 9, 1},
                {6, 1, 7}, {6, 5, 2}, {6, 9, 6},
                {7, 2, 6}, {7, 7, 2}, {7, 8, 8},
                {8, 4, 4}, {8, 5, 1}, {8, 6, 9}, {8, 9, 5},
                {9, 5, 8}, {9, 8, 7}, {9, 9, 9}
        };

        for (int[] fixo : fixos) {
            tabuleiro.marcarComoFixo(fixo[0] - 1, fixo[1] - 1, fixo[2]);
        }

        return tabuleiro;
    }

    /**
     * Cria um tabuleiro quase completo para testes de finalização.
     */
    public static Tabuleiro criarTabuleiroQuaseCompleto() {
        Tabuleiro tabuleiro = criarTabuleiroExemplo();

        // Preenche algumas células editáveis
        int[][] preenchimentos = {
                {1, 3, 4}, {1, 4, 6}, {1, 6, 8}, {1, 7, 9}, {1, 8, 1}, {1, 9, 2},
                {2, 2, 7}, {2, 3, 2}, {2, 7, 4}, {2, 8, 3}, {2, 9, 8},
                {3, 1, 1}, {3, 4, 3}, {3, 5, 4}, {3, 6, 2}, {3, 7, 5}, {3, 9, 7},
                {4, 2, 5}, {4, 3, 7}, {4, 4, 9}, {4, 6, 1}, {4, 7, 6}, {4, 8, 2},
                {5, 2, 2}, {5, 3, 6}, {5, 5, 5}, {5, 7, 7}, {5, 8, 9},
                {6, 2, 1}, {6, 3, 9}, {6, 4, 5}, {6, 6, 7}, {6, 7, 8}, {6, 8, 4},
                {7, 1, 9}, {7, 3, 1}, {7, 4, 7}, {7, 5, 3}, {7, 6, 4}, {7, 9, 6},
                {8, 1, 2}, {8, 2, 8}, {8, 3, 5}, {8, 7, 1}, {8, 8, 7},
                {9, 1, 3}, {9, 2, 4}, {9, 3, 6}, {9, 4, 2}, {9, 6, 5}, {9, 7, 3}
        };

        for (int[] preenchimento : preenchimentos) {
            tabuleiro.inserirNumero(preenchimento[0] - 1, preenchimento[1] - 1, preenchimento[2]);
        }

        return tabuleiro;
    }
}