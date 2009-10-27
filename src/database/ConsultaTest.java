package database;

import java.sql.Connection;
import java.sql.Statement;
import java.util.List;
import java.util.Map;

import junit.framework.TestCase;

public class ConsultaTest extends TestCase {

    public void testConsultaSimples() throws Exception {
        Connection conexao = FornecedorDeConexoes.getInstance().obterConexao();
        Statement stmt = conexao.createStatement();
        try {
            stmt.execute("create table teste (id serial primary key, nome varchar(255))");
            stmt.execute("insert into teste (nome) values ('teste 1')");
            stmt.execute("insert into teste (nome) values ('teste 2')");
            stmt.execute("insert into teste (nome) values ('nada')");
            
            Consulta consulta = new Consulta("select * from teste where nome like ?");
            consulta.param("teste %");
            List<Map<String, Object>> resultado = consulta.executar();
            
            assertEquals(2, resultado.size());
            assertEquals("teste 1", resultado.get(0).get("nome"));
            assertEquals("teste 2", resultado.get(1).get("nome"));
        } finally {
            stmt.execute("drop table teste");
            stmt.close();
            conexao.close();
        }
    }
}
