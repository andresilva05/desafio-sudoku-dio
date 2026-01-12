package domain.exception;

/**
 * Exceção base para erros específicos do domínio Sudoku.
 * Permite tratar erros de negócio separadamente de erros técnicos.
 */
public class SudokuException extends RuntimeException {

    public SudokuException(String message) {
        super(message);
    }

    public SudokuException(String message, Throwable cause) {
        super(message, cause);
    }
}