package crmjuridico.database;

import crmjuridico.model.enums.EstadoProcesso;
import crmjuridico.model.processo.Cliente;
import crmjuridico.model.processo.Processo;
import crmjuridico.model.utilizador.Advogado;
import crmjuridico.model.utilizador.Utilizador;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;

public class ProcessoDAO
{
    public void inserirProcesso(Processo p) throws  SQLException
    {
        Connection conexao = null;
        PreparedStatement stmt = null;
        try {
            conexao = new ConexaoDB().getConexao();
            stmt = conexao.prepareStatement(
                    "INSERT INTO processo (numProcesso, nomeAcao, estado, id_cliente, id_advogado, dataDistribuicao, dataFinalizacao)"+
                            "VALUES (?,?,?,?,?,?,?)"
            );
            stmt.setString(1, p.getNumProcesso());
            stmt.setString(2, p.getNomeAcao());
            stmt.setString(3, p.getEstado().name());
            stmt.setInt(4, p.getCliente().getId());
            stmt.setInt(5, p.getAdvogado().getId());
            stmt.setString(6, p.getDataDistribuicao().toString());
            if(p.getDataFinalizacao() != null)
                stmt.setString(7, p.getDataFinalizacao().toString());
            else
                stmt.setString(7, null); // ver outra forma de fazer isso
            stmt.executeUpdate();

        } finally {
            try { if (stmt != null) stmt.close(); } catch (SQLException e) {}
            try { if (conexao != null) conexao.close(); } catch (SQLException e) {}
        }
    }
    public Processo processoPorId(int id) throws SQLException{
        Processo processo = null;
        Connection conexao = null;
        PreparedStatement stmt = null;
        try {
            conexao = new ConexaoDB().getConexao();
            stmt = conexao.prepareStatement(
                    "SELECT * FROM processo WHERE id = ? "

            );
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery(); // para ler
            if (rs.next()){
                processo = construirProcesso(rs);

            }
        } finally {
            try { if (stmt != null) stmt.close(); } catch (SQLException e) {}
            try { if (conexao != null) conexao.close(); } catch (SQLException e) {}
        }
        return processo;
    }
    public ArrayList<Processo> processoPorEstado(EstadoProcesso estado) throws SQLException{
        ArrayList<Processo> listaEstado = new ArrayList<>();
        Connection conexao = null;
        PreparedStatement stmt = null;
        try {
            conexao = new ConexaoDB().getConexao();
            stmt = conexao.prepareStatement(
                    "SELECT * FROM processo WHERE estado = ? "

            );
            stmt.setString(1, estado.name());
            ResultSet rs = stmt.executeQuery(); // para ler
            while (rs.next()){
                Processo p = construirProcesso(rs);
                listaEstado.add(p);
            }
        } finally {
            try { if (stmt != null) stmt.close(); } catch (SQLException e) {}
            try { if (conexao != null) conexao.close(); } catch (SQLException e) {}
        }
        return listaEstado;
    }
    public ArrayList<Processo> processoPorCliente(Cliente cliente) throws  SQLException {
        ArrayList<Processo> listaProcessoCliente = new ArrayList<>();
        Connection conexao = null;
        PreparedStatement stmt = null;
        try {
            conexao = new ConexaoDB().getConexao();
            stmt = conexao.prepareStatement(
                    "SELECT * FROM processo WHERE id_cliente = ? AND estado NOT IN ('ARQUIVADO', 'FINALIZADO')"

            );
            stmt.setInt(1, cliente.getId());
            ResultSet rs = stmt.executeQuery(); // para ler
            while (rs.next()){
                Processo p = construirProcesso(rs);
                listaProcessoCliente.add(p);
            }
        } finally {
            try { if (stmt != null) stmt.close(); } catch (SQLException e) {}
            try { if (conexao != null) conexao.close(); } catch (SQLException e) {}
        }
        return listaProcessoCliente;
    }
    public ArrayList<Processo> processoPorAdvogado(Utilizador advogado) throws SQLException{
        ArrayList<Processo> listaProcessoAdvogado = new ArrayList<>();
        Connection conexao = null;
        PreparedStatement stmt = null;
        try {
            conexao = new ConexaoDB().getConexao();
            stmt = conexao.prepareStatement(
                    "SELECT * FROM processo WHERE id_advogado = ? AND estado NOT IN ('ARQUIVADO', 'FINALIZADO')"

            );
            stmt.setInt(1, advogado.getId());
            ResultSet rs = stmt.executeQuery(); // para ler
            while (rs.next()){
                Processo p = construirProcesso(rs);
                listaProcessoAdvogado.add(p);
            }
        } finally {
            try { if (stmt != null) stmt.close(); } catch (SQLException e) {}
            try { if (conexao != null) conexao.close(); } catch (SQLException e) {}
        }
        return listaProcessoAdvogado;
    }
    public boolean atualizarProcessoPorEstado(Processo p) throws  SQLException
    {
        Connection conexao = null;
        PreparedStatement stmt = null;
        try {
            conexao = new ConexaoDB().getConexao();
            stmt = conexao.prepareStatement(
                    "UPDATE processo SET estado = ? WHERE id = ?");

            stmt.setString(1, p.getEstado().name());
            stmt.setInt(2, p.getId());
            return stmt.executeUpdate() >0;

        } finally {
            try { if (stmt != null) stmt.close(); } catch (SQLException e) {}
            try { if (conexao != null) conexao.close(); } catch (SQLException e) {}
        }
    }

    private Processo construirProcesso(ResultSet rs) throws SQLException {
        int id = rs.getInt("id");
        String numProcesso = rs.getString("numProcesso");
        String nomeAcao = rs.getString("nomeAcao");
        EstadoProcesso estado = EstadoProcesso.valueOf(rs.getString("estado"));
        int id_cliente = rs.getInt("id_cliente");
        Cliente cliente = new ClienteDAO().clientePorId(id_cliente);
        int id_advogado = rs.getInt("id_advogado");
        Utilizador u = new UtilizadorDAO().utilizadorPorId(id_advogado);
        Advogado advogado = (u instanceof Advogado) ? (Advogado) u : null;
        if (advogado == null)
            return null;
        LocalDate dataDistribuicao = LocalDate.parse(rs.getString("dataDistribuicao"));
        LocalDate dataFinalizacao = null;
        if(rs.getString("dataFinalizacao") != null)
            dataFinalizacao = LocalDate.parse(rs.getString("dataFinalizacao"));

        Processo p = new Processo (id, numProcesso, nomeAcao, estado, cliente, advogado, dataDistribuicao, dataFinalizacao);
        return p;
    }
}
