package crmjuridico.database;

import crmjuridico.model.enums.EstadoAgenda;
import crmjuridico.model.enums.EstadoTarefa;
import crmjuridico.model.prazo.Agenda;
import crmjuridico.model.processo.Processo;
import crmjuridico.model.tarefa.Tarefa;
import crmjuridico.model.utilizador.Utilizador;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class AgendaDAO
{
    public void inserirAgenda(Agenda agenda) throws SQLException
    {
        Connection conexao = null;
        PreparedStatement stmt = null;
        try {
            conexao = new ConexaoDB().getConexao();
            stmt = conexao.prepareStatement(
                    "INSERT INTO agenda (titulo, local, dataProtocolo, id_processo, id_utilizador, estado)"+
                            "VALUES (?,?,?,?,?,?)"
            );
            stmt.setString(1, agenda.getTitulo());
            stmt.setString(2, agenda.getLocal());
            stmt.setString(3, agenda.getDataHora().toString());
            stmt.setInt(4, agenda.getProcesso().getId());
            stmt.setInt(5, agenda.getUtilizador().getId());
            stmt.setString(6, agenda.getEstadoAgenda().name());
            stmt.executeUpdate();
        } finally {
            try { if (stmt != null) stmt.close(); } catch (SQLException e) {}
            try { if (conexao != null) conexao.close(); } catch (SQLException e) {}
        }
    }
    public Agenda agendaPorId(int id) throws SQLException{
        Agenda agenda = null;
        Connection conexao = null;
        PreparedStatement stmt = null;
        try {
            conexao = new ConexaoDB().getConexao();
            stmt = conexao.prepareStatement(
                    "SELECT * FROM agenda WHERE id = ? "

            );
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery(); // para ler
            if (rs.next()){
                agenda = construirAgenda(rs);

            }
        } finally {
            try { if (stmt != null) stmt.close(); } catch (SQLException e) {}
            try { if (conexao != null) conexao.close(); } catch (SQLException e) {}
        }
        return agenda;
    }
    public ArrayList<Agenda> agendaPorUtilizador(int id_utilizador) throws SQLException{
        ArrayList<Agenda> listaAgendaUtilizador = new ArrayList<>();
        Connection conexao = null;
        PreparedStatement stmt = null;
        try {
            conexao = new ConexaoDB().getConexao();
            stmt = conexao.prepareStatement(
                    "SELECT * FROM agenda WHERE id_utilizador = ? AND estado NOT IN ('CANCELADO', 'CONCLUIDO')"

            );
            stmt.setInt(1, id_utilizador);
            ResultSet rs = stmt.executeQuery(); // para ler
            while (rs.next()){
                Agenda a = construirAgenda(rs);
                listaAgendaUtilizador.add(a);
            }
        } finally {
            try { if (stmt != null) stmt.close(); } catch (SQLException e) {}
            try { if (conexao != null) conexao.close(); } catch (SQLException e) {}
        }
        return listaAgendaUtilizador;
    }
    public ArrayList<Agenda> agendaToda() throws SQLException{
        ArrayList<Agenda> listaAgendaToda = new ArrayList<>();
        Connection conexao = null;
        PreparedStatement stmt = null;
        try {
            conexao = new ConexaoDB().getConexao();
            stmt = conexao.prepareStatement(
                    "SELECT * FROM agenda WHERE estado NOT IN ('CANCELADO', 'CONCLUIDO')"

            );
            ResultSet rs = stmt.executeQuery(); // para ler
            while (rs.next()){
                Agenda a = construirAgenda(rs);
                listaAgendaToda.add(a);
            }
        } finally {
            try { if (stmt != null) stmt.close(); } catch (SQLException e) {}
            try { if (conexao != null) conexao.close(); } catch (SQLException e) {}
        }
        return listaAgendaToda;
    }
    public ArrayList<Agenda> agendaPorProcesso(int id_processo) throws  SQLException{
        ArrayList<Agenda> listaAgendaProcesso = new ArrayList<>();
        Connection conexao = null;
        PreparedStatement stmt = null;
        try {
            conexao = new ConexaoDB().getConexao();
            stmt = conexao.prepareStatement(
                    "SELECT * FROM agenda WHERE id_processo = ? AND estado NOT IN ('CANCELADO', 'CONCLUIDO') "

            );
            stmt.setInt(1, id_processo);
            ResultSet rs = stmt.executeQuery(); // para ler
            while (rs.next()){
                Agenda a = construirAgenda(rs);
                listaAgendaProcesso.add(a);
            }
        } finally {
            try { if (stmt != null) stmt.close(); } catch (SQLException e) {}
            try { if (conexao != null) conexao.close(); } catch (SQLException e) {}
        }
        return listaAgendaProcesso;
    }
    public ArrayList<Agenda> agendaPorEstado(EstadoAgenda estado) throws  SQLException{
        ArrayList<Agenda> listaAgendaEstado = new ArrayList<>();
        Connection conexao = null;
        PreparedStatement stmt = null;
        try {
            conexao = new ConexaoDB().getConexao();
            stmt = conexao.prepareStatement(
                    "SELECT * FROM agenda WHERE estado = ? "

            );
            stmt.setString(1, estado.name());
            ResultSet rs = stmt.executeQuery(); // para ler
            while (rs.next()){
                Agenda a = construirAgenda(rs);
                listaAgendaEstado.add(a);
            }
        } finally {
            try { if (stmt != null) stmt.close(); } catch (SQLException e) {}
            try { if (conexao != null) conexao.close(); } catch (SQLException e) {}
        }
        return listaAgendaEstado;
    }
    public boolean atualizarAgendaPorEstado(Agenda a) throws  SQLException
    {
        Connection conexao = null;
        PreparedStatement stmt = null;
        try {
            conexao = new ConexaoDB().getConexao();
            stmt = conexao.prepareStatement(
                    "UPDATE agenda SET estado = ? WHERE id = ? ");

            stmt.setString(1, a.getEstadoAgenda().name());
            stmt.setInt(2, a.getId());
            return stmt.executeUpdate() > 0;

        } finally {
            try { if (stmt != null) stmt.close(); } catch (SQLException e) {}
            try { if (conexao != null) conexao.close(); } catch (SQLException e) {}
        }
    }
    private Agenda construirAgenda(ResultSet rs) throws SQLException {
        int id = rs.getInt("id");
        String titulo = rs.getString("titulo");
        String local = rs.getString("local");
        LocalDateTime dataProtocolo = LocalDateTime.parse(rs.getString("dataProtocolo"));
        int processoId = rs.getInt("id_processo");
        Processo processo = new ProcessoDAO().processoPorId(processoId);
        int utilizadorId = rs.getInt("id_utilizador");
        Utilizador utilizador = new UtilizadorDAO().utilizadorPorId(utilizadorId);
        EstadoAgenda estadoAgenda = EstadoAgenda.valueOf(rs.getString("estado"));

        Agenda a = new Agenda (id, titulo, utilizador, dataProtocolo, processo, local, estadoAgenda);
        return a;
    }
}
