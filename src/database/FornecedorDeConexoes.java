package database;

import static java.lang.String.format;
import static java.lang.System.currentTimeMillis;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.concurrent.atomic.AtomicBoolean;

public class FornecedorDeConexoes {

    private static final FornecedorDeConexoes euMesmo = new FornecedorDeConexoes();
    private AtomicBoolean iniciado = new AtomicBoolean(false);

    private FornecedorDeConexoes() {
    }

    public static FornecedorDeConexoes getInstance() {
        return euMesmo;
    }

    private void iniciarDriverDeConexaoSeNecessario() {
        if (iniciado.getAndSet(true)) return;
        try {
            Class.forName("org.h2.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Driver para o banco H2 não encontrado", e);
        }
    }

    public Connection obterConexao() {
        iniciarDriverDeConexaoSeNecessario();
        long start = currentTimeMillis();
        try {
            // String de conexão, usuário, senha
            return DriverManager.getConnection("jdbc:h2:localhost/~/test", "sa", "");
        } catch (SQLException e) {
            throw new RuntimeException("Não foi possível conectar ao banco de dados", e);
        } finally {
            System.out.println(format("tempo: %dms", currentTimeMillis() - start));
        }
    }
}
