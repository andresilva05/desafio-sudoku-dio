package factory;

import domain.model.Tabuleiro;
import service.JogoService;
import service.ValidadorService;
import state.NaoIniciadoState;

/**
 * Factory para criação do serviço principal do jogo.
 * Centraliza a montagem das dependências.
 */
public class JogoFactory {

    /**
     * Cria um jogo vazio (sem células fixas).
     */
    public static JogoService criarJogoVazio() {
        Tabuleiro tabuleiro = TabuleiroFactory.criarTabuleiroVazio();
        ValidadorService validadorService = new ValidadorService();
        NaoIniciadoState estadoInicial = new NaoIniciadoState();

        return new JogoService(tabuleiro, estadoInicial, validadorService);
    }

    /**
     * Cria um jogo com células fixas a partir de argumentos de linha de comando.
     */
    public static JogoService criarJogoComFixos(String[] args) {
        Tabuleiro tabuleiro = TabuleiroFactory.criarTabuleiroComFixos(args);
        ValidadorService validadorService = new ValidadorService();
        NaoIniciadoState estadoInicial = new NaoIniciadoState();

        return new JogoService(tabuleiro, estadoInicial, validadorService);
    }

    /**
     * Cria um jogo de exemplo (pré-configurado).
     */
    public static JogoService criarJogoExemplo() {
        Tabuleiro tabuleiro = TabuleiroFactory.criarTabuleiroExemplo();
        ValidadorService validadorService = new ValidadorService();
        NaoIniciadoState estadoInicial = new NaoIniciadoState();

        return new JogoService(tabuleiro, estadoInicial, validadorService);
    }

    /**
     * Cria um jogo quase completo para testes de finalização.
     */
    public static JogoService criarJogoQuaseCompleto() {
        Tabuleiro tabuleiro = TabuleiroFactory.criarTabuleiroQuaseCompleto();
        ValidadorService validadorService = new ValidadorService();
        NaoIniciadoState estadoInicial = new NaoIniciadoState();

        JogoService jogo = new JogoService(tabuleiro, estadoInicial, validadorService);
        jogo.iniciarJogo(); // Inicia automaticamente

        return jogo;
    }

    /**
     * Cria um jogo com configuração personalizada.
     * Útil para testes.
     */
    public static JogoService criarJogoPersonalizado(Tabuleiro tabuleiro) {
        ValidadorService validadorService = new ValidadorService();
        NaoIniciadoState estadoInicial = new NaoIniciadoState();

        return new JogoService(tabuleiro, estadoInicial, validadorService);
    }
}