import factory.JogoFactory;
import service.JogoService;
import ui.Menu;

/**
 * Ponto de entrada principal do Sudoku Refatorado.
 *
 * <p><b>Arquitetura Nova:</b></p>
 * <ul>
 *   <li><b>Main</b>: Apenas inicialização (Single Responsibility)</li>
 *   <li><b>JogoFactory</b>: Cria todas dependências (Factory Pattern)</li>
 *   <li><b>JogoService</b>: Orquestra o jogo (Service Layer)</li>
 *   <li><b>Menu</b>: Interface com usuário (UI Layer)</li>
 *   <li><b>State Pattern</b>: Estados gerenciam regras por situação</li>
 * </ul>
 */
public class Main {

    /**
     * Método principal refatorado - foca apenas em inicialização.
     *
     * @param args Argumentos para células fixas no formato: linha coluna valor...
     */
    public static void main(String[] args) {
        System.out.println("=== SUDOKU REFATORADO ===");
        System.out.println("🏗️  Arquitetura: State Pattern + Factory + Services");
        System.out.println("📦 Pacotes: domain, service, state, validation, ui, factory");

        try {
            // 1. DECISÃO: Qual jogo criar baseado nos argumentos
            JogoService jogoService = criarJogoAdequado(args);

            // 2. INICIALIZAÇÃO: Começa no estado "Não Iniciado"
            System.out.println("🎮 Estado inicial: " + jogoService.getTipoEstado());

            // 3. UI: Menu recebe o serviço (Dependency Injection)
            Menu menu = new Menu(jogoService);

            // 4. EXECUÇÃO: Controle passa para o Menu
            menu.executar();

        } catch (IllegalArgumentException e) {
            // Erro de argumentos inválidos
            System.out.println("❌ Argumentos inválidos: " + e.getMessage());
            System.out.println("📋 Formato correto: linha coluna valor linha coluna valor...");
            System.out.println("   Exemplo: 1 1 5 2 3 7");

        } catch (Exception e) {
            // Erro inesperado
            System.out.println("💥 Erro crítico: " + e.getMessage());
            e.printStackTrace();
            System.out.println("\n📞 Reporte este erro com a mensagem acima.");
        }

        System.out.println("\n✨ Jogo encerrado. Obrigado! 👋");
    }

    /**
     * Factory method que decide qual jogo criar.
     * Demonstra o uso do Factory Pattern.
     */
    private static JogoService criarJogoAdequado(String[] args) {
        if (args.length > 0) {
            System.out.println("🔧 Criando jogo personalizado com " + (args.length / 3) + " células fixas...");
            return JogoFactory.criarJogoComFixos(args);
        } else {
            System.out.println("🔧 Criando jogo vazio (sem células fixas)...");
            return JogoFactory.criarJogoVazio();
        }
    }

    /**
     * Método auxiliar para testes rápidos.
     * Pode ser usado para criar um jogo de exemplo.
     */
    private static JogoService criarJogoExemplo() {
        System.out.println("🔧 Criando jogo de exemplo (puzzle fácil)...");
        return JogoFactory.criarJogoExemplo();
    }
}