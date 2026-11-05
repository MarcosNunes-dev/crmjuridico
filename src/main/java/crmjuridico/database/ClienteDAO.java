package crmjuridico.database;

import crmjuridico.model.enums.TipoCliente;
import crmjuridico.model.processo.Cliente;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;

public class ClienteDAO
{
    public void inserirCliente(Cliente c) throws SQLException
    {
        Connection conexao = null;
        PreparedStatement stmt = null;
        try {
            conexao = new ConexaoDB().getConexao();
            stmt = conexao.prepareStatement(
                    "INSERT INTO cliente(nome, morada, telefone, email, nif, tipoCliente, dataCadastro)"+
                            "VALUES (?,?,?,?,?,?,?)"
            );
            stmt.setString(1, c.getNome());
            stmt.setString(2, c.getMorada());
            stmt.setInt(3, c.getTelefone());
            stmt.setString(4, c.getEmail());
            stmt.setInt(5, c.getNif());
            stmt.setString(6, c.getTipoCliente().name());
            stmt.setString(7, c.getDataCadastro().toString()); // transforma a data em string
            stmt.executeUpdate();

        } finally {
            try { if (stmt != null) stmt.close(); } catch (SQLException e) {}
            try { if (conexao != null) conexao.close(); } catch (SQLException e) {}
        }
    }
    public ArrayList<Cliente> listarClienteAtivo() throws SQLException {
        ArrayList<Cliente> listaClientes = new ArrayList<>();
        Connection conexao = null;
        PreparedStatement stmt = null;
        try {
            conexao = new ConexaoDB().getConexao();
            stmt = conexao.prepareStatement(
                    "SELECT * FROM cliente WHERE ativo = 1 ORDER BY nome ASC "
            );
            ResultSet rs = stmt.executeQuery(); // para ler
            while (rs.next()) {
                Cliente cliente = construirCliente(rs);
                listaClientes.add(cliente);
            }
        } finally {
            try { if (stmt != null) stmt.close(); } catch (SQLException e) {}
            try { if (conexao != null) conexao.close(); } catch (SQLException e) {}
        }
        return listaClientes;
    }
    public ArrayList<Cliente> listarClienteInativo() throws SQLException {
        ArrayList<Cliente> listaClientes = new ArrayList<>();
        Connection conexao = null;
        PreparedStatement stmt = null;
        try {
            conexao = new ConexaoDB().getConexao();
            stmt = conexao.prepareStatement(
                    "SELECT * FROM cliente WHERE ativo = 0 ORDER BY nome ASC "
            );
            ResultSet rs = stmt.executeQuery(); // para ler
            while (rs.next()) {
                Cliente cliente = construirCliente(rs);
                listaClientes.add(cliente);
            }
        } finally {
            try { if (stmt != null) stmt.close(); } catch (SQLException e) {}
            try { if (conexao != null) conexao.close(); } catch (SQLException e) {}
        }
        return listaClientes;
    }
    private Cliente construirCliente(ResultSet rs) throws SQLException {
        int id = rs.getInt("id");
        String nome = rs.getString("nome");
        String morada = rs.getString("morada");
        int telefone = rs.getInt("telefone");
        String email = rs.getString("email");
        int nif = rs.getInt("nif");
        TipoCliente tipoCliente = TipoCliente.valueOf(rs.getString("tipoCliente"));
        LocalDate dataCadastro = LocalDate.parse(rs.getString("dataCadastro"));
        boolean ativo = rs.getInt("ativo") == 1;

        Cliente c = new Cliente(id, nome, morada, telefone, email, nif, tipoCliente, dataCadastro, ativo);
        return c;
    }
    public Cliente clientePorId(int id) throws SQLException{
        Cliente c = null;
        Connection conexao = null;
        PreparedStatement stmt = null;
        try {
            conexao = new ConexaoDB().getConexao();
            stmt = conexao.prepareStatement(
                    "SELECT * FROM cliente WHERE id = ? "

            );
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery(); // para ler
            if (rs.next()){
                c = construirCliente(rs);
            }
        } finally {
            try { if (stmt != null) stmt.close(); } catch (SQLException e) {}
            try { if (conexao != null) conexao.close(); } catch (SQLException e) {}
        }
        return c;
    }
    public Cliente clientePorNif(int nif) throws SQLException{
        Cliente c = null;
        Connection conexao = null;
        PreparedStatement stmt = null;
        try {
            conexao = new ConexaoDB().getConexao();
            stmt = conexao.prepareStatement(
                    "SELECT * FROM cliente WHERE nif = ? "

            );
            stmt.setInt(1, nif);
            ResultSet rs = stmt.executeQuery(); // para ler
            if (rs.next()){
                c = construirCliente(rs);
            }
        } finally {
            try { if (stmt != null) stmt.close(); } catch (SQLException e) {}
            try { if (conexao != null) conexao.close(); } catch (SQLException e) {}
        }
        return c;
    }
    public void atualizarCliente(Cliente cliente) throws SQLException{
        Connection conexao = null;
        PreparedStatement stmt = null;
        try {
            conexao = new ConexaoDB().getConexao();
            stmt = conexao.prepareStatement(
                    "UPDATE cliente SET nome = ?, morada = ?, telefone = ?, email = ?, nif = ?, tipoCliente = ?, ativo = ? WHERE id = ?");
            stmt.setString(1, cliente.getNome());
            stmt.setString(2, cliente.getMorada());
            stmt.setInt(3, cliente.getTelefone());
            stmt.setString(4, cliente.getEmail());
            stmt.setInt(5, cliente.getNif());
            stmt.setString(6, cliente.getTipoCliente().name());
            stmt.setInt(7, cliente.getAtivo() ? 1 : 0);
            stmt.setInt(8, cliente.getId());
            stmt.executeUpdate();
        } finally {
            try { if (stmt != null) stmt.close(); } catch (SQLException e) {}
            try { if (conexao != null) conexao.close(); } catch (SQLException e) {}
        }
    }
}
