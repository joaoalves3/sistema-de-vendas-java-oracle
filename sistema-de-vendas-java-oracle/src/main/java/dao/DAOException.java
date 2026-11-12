package dao;

/**
 * Erro de acesso ao banco, com uma mensagem amigável para mostrar ao usuário.
 */
public class DAOException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public DAOException(String mensagem, Throwable causa) {
        super(mensagem + ": " + causa.getMessage(), causa);
    }
}
