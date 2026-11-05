package crmjuridico.database;

import crmjuridico.model.enums.EstadoTarefa;
import crmjuridico.model.enums.Prioridade;
import crmjuridico.model.processo.Processo;
import crmjuridico.model.tarefa.Tarefa;
import crmjuridico.model.utilizador.Utilizador;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;

public class TarefaDAO
{
    public void   inserirTarefa(Tarefa tarefa) throws SQLException
    {
        Connection conexao = null;
        PreparedStatement stmt = null;
        try {
            conexao = new ConexaoDB().getConexao();
            stmt = conexao.prepareStatement(
                    "INSERT INTO tarefa (titulo, descricao, prioridade, estado, dataLimite, id_processo, id_utilizador)"+
                            "VALUES (?,?,?,?,?,?,?)"
            );
            stmt.setString(1, tarefa.getTitulo());
            stmt.setString(2, tarefa.getDescricao());
            stmt.setString(3, tarefa.getPrioridade().name());
            stmt.setString(4, tarefa.getEstado().name());
            stmt.setString(5, tarefa.getDataLimite().toString());
            stmt.setInt(6, tarefa.getProcesso().getId());
            stmt.setInt(7, tarefa.getResponsavel().getId());
            stmt.executeUpdate();

        } finally {
            try { if (stmt != null) stmt.close(); } catch (SQLException e) {}
            try { if (conexao != null) conexao.close(); } catch (SQLException e) {}
        }
    }
    public Tarefa tarefaPorId(int id) throws SQLException{
        Tarefa tarefa = null;
        Connection conexao = null;
        PreparedStatement stmt = null;
        try {
            conexao = new ConexaoDB().getConexao();
            stmt = conexao.prepareStatement(
                    "SELECT * FROM tarefa WHERE id = ? "

            );
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery(); // para ler
            if (rs.next()){
                tarefa = construirTarefa(rs);

            }
        } finally {
            try { if (stmt != null) stmt.close(); } catch (SQLException e) {}
            try { if (conexao != null) conexao.close(); } catch (SQLException e) {}
        }
        return tarefa;
    }
    public ArrayList<Tarefa> tarefaPorResponsavel(int id_utilizador) throws SQLException{
        ArrayList<Tarefa> listaTarefaResponsavel = new ArrayList<>();
        Connection conexao = null;
        PreparedStatement stmt = null;
        try {
            conexao = new ConexaoDB().getConexao();
            stmt = conexao.prepareStatement(
                    "SELECT * FROM tarefa WHERE id_utilizador = ? "

            );
            stmt.setInt(1, id_utilizador);
            ResultSet rs = stmt.executeQuery(); // para ler
            while (rs.next()){
                Tarefa t = construirTarefa(rs);
                listaTarefaResponsavel.add(t);
            }
        } finally {
            try { if (stmt != null) stmt.close(); } catch (SQLException e) {}
            try { if (conexao != null) conexao.close(); } catch (SQLException e) {}
        }
        return listaTarefaResponsavel;
    }
    public ArrayList<Tarefa> tarefaTodas() throws SQLException{
        ArrayList<Tarefa> listaTarefaTodas = new ArrayList<>();
        Connection conexao = null;
        PreparedStatement stmt = null;
        try {
            conexao = new ConexaoDB().getConexao();
            stmt = conexao.prepareStatement(
                    "SELECT * FROM tarefa "

            );
            ResultSet rs = stmt.executeQuery(); // para ler
            while (rs.next()){
                Tarefa t = construirTarefa(rs);
                listaTarefaTodas.add(t);
            }
        } finally {
            try { if (stmt != null) stmt.close(); } catch (SQLException e) {}
            try { if (conexao != null) conexao.close(); } catch (SQLException e) {}
        }
        return listaTarefaTodas;
    }
    public ArrayList<Tarefa> tarefaPorEstado(EstadoTarefa estado) throws  SQLException{
        ArrayList<Tarefa> listaTarefaEstado = new ArrayList<>();
        Connection conexao = null;
        PreparedStatement stmt = null;
        try {
            conexao = new ConexaoDB().getConexao();
            stmt = conexao.prepareStatement(
                    "SELECT * FROM tarefa WHERE estado = ? "

            );
            stmt.setString(1, estado.name());
            ResultSet rs = stmt.executeQuery(); // para ler
            while (rs.next()){
                Tarefa t = construirTarefa(rs);
                listaTarefaEstado.add(t);
            }
        } finally {
            try { if (stmt != null) stmt.close(); } catch (SQLException e) {}
            try { if (conexao != null) conexao.close(); } catch (SQLException e) {}
        }
        return listaTarefaEstado;
    }
    public ArrayList<Tarefa> tarefaPorProcesso(int id_processo) throws  SQLException{
        ArrayList<Tarefa> listaTarefaProcesso = new ArrayList<>();
        Connection conexao = null;
        PreparedStatement stmt = null;
        try {
            conexao = new ConexaoDB().getConexao();
            stmt = conexao.prepareStatement(
                    "SELECT * FROM tarefa WHERE id_processo = ? "

            );
            stmt.setInt(1, id_processo);
            ResultSet rs = stmt.executeQuery(); // para ler
            while (rs.next()){
                Tarefa t = construirTarefa(rs);
                listaTarefaProcesso.add(t);
            }
        } finally {
            try { if (stmt != null) stmt.close(); } catch (SQLException e) {}
            try { if (conexao != null) conexao.close(); } catch (SQLException e) {}
        }
        return listaTarefaProcesso;
    }
    public boolean atualizarTarefaPorEstado(Tarefa t) throws  SQLException
    {
        Connection conexao = null;
        PreparedStatement stmt = null;
        try {
            conexao = new ConexaoDB().getConexao();
            stmt = conexao.prepareStatement(
                    "UPDATE tarefa SET estado = ? WHERE id = ?");

            stmt.setString(1, t.getEstado().name());
            stmt.setInt(2, t.getId());
            return stmt.executeUpdate() > 0;

        } finally {
            try { if (stmt != null) stmt.close(); } catch (SQLException e) {}
            try { if (conexao != null) conexao.close(); } catch (SQLException e) {}
        }
    }

    private Tarefa construirTarefa(ResultSet rs) throws SQLException {
        int id = rs.getInt("id");
        String titulo = rs.getString("titulo");
        String descricao = rs.getString("descricao");
        Prioridade prioridade = Prioridade.valueOf(rs.getString("prioridade"));
        EstadoTarefa estadoTarefa  = EstadoTarefa.valueOf(rs.getString("estado"));
        LocalDate dataLimite = LocalDate.parse(rs.getString("dataLimite"));
        int processoId = rs.getInt("id_processo");
        Processo processo = new ProcessoDAO().processoPorId(processoId);
        int utilizadorId = rs.getInt("id_utilizador");
        Utilizador responsavel = new UtilizadorDAO().utilizadorPorId(utilizadorId);

        Tarefa t = new Tarefa (id, titulo, descricao, prioridade, estadoTarefa, dataLimite, processo, responsavel);
        return t;
    }
}
