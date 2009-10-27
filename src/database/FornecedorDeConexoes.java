package database;

import static java.lang.String.format;
import static java.lang.System.currentTimeMillis;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

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
            return conexaoDireta();
            //return conexaoPorPoolDeConexoes();
        } finally {
            System.out.println(format("tempo: %dms", currentTimeMillis() - start));
        }
    }

    private Connection conexaoDireta() {
        try {
            // String de conexão, usuário, senha
            return DriverManager.getConnection("jdbc:h2:tcp://localhost/~/test", "sa", "");
        } catch (SQLException e) {
            throw new RuntimeException("Não foi possível conectar ao banco de dados", e);
        }
    }

    private Connection conexaoPorPoolDeConexoes() {
        try {
            Context contextoInicial = new InitialContext();
            Context ambiente = (Context) contextoInicial.lookup("java:comp/env");
            DataSource ds = (DataSource) ambiente.lookup("jdbc/pool_de_conexoes");
            return ds.getConnection();
            
        } catch (NamingException e) {
            throw new RuntimeException(e);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
