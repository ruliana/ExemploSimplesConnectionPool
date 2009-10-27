<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@page import="java.sql.Connection"%>
<%@page import="java.util.List"%>
<%@page import="java.util.Map"%>
<%@page import="database.FornecedorDeConexoes"%>
<%@page import="database.Consulta"%>

<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>Teste de pool de conexões</title>
</head>
<body>
	<% 
		Connection conn = FornecedorDeConexoes.getInstance().obterConexao();
	    Consulta consulta = new Consulta("select * from test");
	    List<Map<String, Object>> resultado = consulta.executar();
	%>
	<h1>Nomes!</h1>
	<table>	
	<% for(Map<String, Object> record : resultado) { %>
		<tr>
			<td><%= record.get("name") %></td>
		</tr>
	<% } %>
	</table>
</body>
</html>