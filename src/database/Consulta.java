package database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class Consulta {

    private final String sql;
    private final List<Object> parametros = new LinkedList<Object>();

    public Consulta(String sql) {
        this.sql = sql;
    }
    
    public Consulta param(Object parametro) {
        parametros.add(parametro);
        return this;
    }

    public List<Map<String, Object>> executar() throws SQLException {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {
            connection = FornecedorDeConexoes.getInstance().obterConexao();
            
            statement = connection.prepareStatement(sql);
            int x = 1;
            for (Object parametro : parametros) {
                statement.setObject(x++, parametro);                
            }
            
            resultSet = statement.executeQuery();
            
            ResultSetMetaData metaData = resultSet.getMetaData();
            
            List<Map<String, Object>> resultado = new LinkedList<Map<String, Object>>();
            while(resultSet.next()) {
                Map<String, Object> linha = new LinkedHashMap<String, Object>();
                for(int i = 1; i <= metaData.getColumnCount(); i++) {
                    linha.put(metaData.getColumnLabel(i).toLowerCase(), resultSet.getObject(i));
                }
                resultado.add(linha);
            }
            return resultado;
            
        } finally {
            try {
                if (resultSet != null) resultSet.close();
            } finally {
                try {
                    if (statement != null) statement.close();
                } finally {
                    if (connection != null) connection.close();
                }
            }
        }
    }
}
