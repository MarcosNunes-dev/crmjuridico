package crmjuridico.database;

import crmjuridico.model.enums.NivelAcesso;
import crmjuridico.model.utilizador.Administrador;
import crmjuridico.model.utilizador.Advogado;
import crmjuridico.model.utilizador.Estagiario;
import crmjuridico.model.utilizador.Utilizador;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;


public class UtilizadorDAO
{
    public void inserirUtilizador(Utilizador u) throws SQLException{
        Connection conexao = null;
        PreparedStatement stmt = null;
        try {
            conexao = new ConexaoDB().getConexao();
            stmt = conexao.prepareStatement(
                    "INSERT INTO utilizador (nome, username, password, nif, dataAdmissao, dataDemissao, nivelAcesso, ativo, cedulaProfissional) " +
                            "VALUES (?,?,?,?,?,?,?,?,?)"
            );
            stmt.setString(1, u.getNome());
            stmt.setString(2, u.getUsername());
            stmt.setString(3, u.getPasswordHash());
            stmt.setInt(4, u.getNif());
            stmt.setString(5, String.valueOf(u.getDataAdmissao())); // ver outra forma de fazer isso
            if(u.getDataDemissao() != null)
                stmt.setString(6, String.valueOf(u.getDataDemissao())); // ver outra forma de fazer isso
            else
                stmt.setString(6, null); // ver outra forma de fazer isso
            stmt.setString(7, u.getNivelAcesso().name()); // transforma enum em string
            stmt.setInt(8, u.getAtivo() ? 1 : 0);
            if(u instanceof Advogado){
                Advogado advogado = (Advogado)u;
                stmt.setString(9, advogado.getCedulaProfissional());
            }
            else
                stmt.setString(9, null);
            stmt.executeUpdate(); // cria, altera e deleta
        } finally {
            try { if (stmt != null) stmt.close(); } catch (SQLException e) {}
            try { if (conexao != null) conexao.close(); } catch (SQLException e) {}
        }
    }

    public ArrayList<Utilizador> listarUtilizadoresAtivos() throws SQLException{
        ArrayList<Utilizador> listaUtilizadores = new ArrayList<>();
        Connection conexao = null;
        PreparedStatement stmt = null;
        try {
            conexao = new ConexaoDB().getConexao();
            stmt = conexao.prepareStatement(
                    "SELECT * FROM utilizador WHERE ativo = 1 ORDER BY nome ASC "
            );
            ResultSet rs = stmt.executeQuery(); // para ler
            while (rs.next()) {
                Utilizador u = construirUtilizador(rs);
                listaUtilizadores.add(u);
            }
        } finally {
            try { if (stmt != null) stmt.close(); } catch (SQLException e) {}
            try { if (conexao != null) conexao.close(); } catch (SQLException e) {}
        }
        return listaUtilizadores;
    }
    public ArrayList<Utilizador> listarUtilizadoresInativos() throws SQLException{
        ArrayList<Utilizador> listaUtilizadores = new ArrayList<>();
        Connection conexao = null;
        PreparedStatement stmt = null;
        try {
            conexao = new ConexaoDB().getConexao();
            stmt = conexao.prepareStatement(
                    "SELECT * FROM utilizador WHERE ativo = 0 ORDER BY nome ASC "
            );
            ResultSet rs = stmt.executeQuery(); // para ler
            while (rs.next()) {
                Utilizador u = construirUtilizador(rs);
                listaUtilizadores.add(u);
            }
        } finally {
            try { if (stmt != null) stmt.close(); } catch (SQLException e) {}
            try { if (conexao != null) conexao.close(); } catch (SQLException e) {}
        }
        return listaUtilizadores;
    }
    private Utilizador construirUtilizador(ResultSet rs) throws SQLException {
        int id = rs.getInt("id");
        String nome = rs.getString("nome");
        String username = rs.getString("username");
        String passwordHash = rs.getString("password");
        int nif = rs.getInt("nif");
        LocalDate dataAdmissao = LocalDate.parse(rs.getString("dataAdmissao"));
        LocalDate dataDemissao = null;
        if(rs.getString("dataDemissao") != null)
            dataDemissao = LocalDate.parse(rs.getString("dataDemissao"));
        NivelAcesso nivelAcesso = NivelAcesso.valueOf(rs.getString("nivelAcesso"));
        boolean ativo = rs.getInt("ativo") == 1;
        String cedulaProfissional = rs.getString("cedulaProfissional");

        Utilizador u = null;
        switch (nivelAcesso) {
            case ADVOGADO:
                Advogado adv = new Advogado(id, nome, username, passwordHash, nif, dataAdmissao, dataDemissao, nivelAcesso, ativo);
                adv.setCedulaProfissional(cedulaProfissional);
                u = adv;
                break;
            case ADMINISTRADOR:
                u = new Administrador(id, nome, username, passwordHash, nif, dataAdmissao, dataDemissao, nivelAcesso, ativo);
                break;
            case ESTAGIARIO:
                u = new Estagiario(id, nome, username, passwordHash, nif, dataAdmissao, dataDemissao, nivelAcesso, ativo);
                break;
        }
        return u;
    }
    public Utilizador utilizadorPorUser(String username) throws SQLException {
    Utilizador u = null;
    Connection conexao = null;
    PreparedStatement stmt = null;
    try {
        conexao = new ConexaoDB().getConexao();
        stmt = conexao.prepareStatement(
                "SELECT * FROM utilizador WHERE username = ? "
        );
        stmt.setString(1, username);
        ResultSet rs = stmt.executeQuery(); // para ler
        if (rs.next()){
            u = construirUtilizador(rs);
        }
    } finally {
        try { if (stmt != null) stmt.close(); } catch (SQLException e) {}
        try { if (conexao != null) conexao.close(); } catch (SQLException e) {}
    }
    return u;
}
    public Utilizador utilizadorPorId(int id) throws SQLException {
        Utilizador u = null;
        Connection conexao = null;
        PreparedStatement stmt = null;
        try {
            conexao = new ConexaoDB().getConexao();
            stmt = conexao.prepareStatement(
                    "SELECT * FROM utilizador WHERE id = ? "

            );
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery(); // para ler
            if (rs.next()){
                u = construirUtilizador(rs);
            }
        } finally {
            try { if (stmt != null) stmt.close(); } catch (SQLException e) {}
            try { if (conexao != null) conexao.close(); } catch (SQLException e) {}
        }
        return u;
    }
    public Utilizador utilizadorPorNif(int nif) throws SQLException {
        Utilizador u = null;
        Connection conexao = null;
        PreparedStatement stmt = null;
        try {
            conexao = new ConexaoDB().getConexao();
            stmt = conexao.prepareStatement(
                    "SELECT * FROM utilizador WHERE nif = ? "

            );
            stmt.setInt(1, nif);
            ResultSet rs = stmt.executeQuery(); // para ler
            if (rs.next()){
                u = construirUtilizador(rs);
            }
        } finally {
            try { if (stmt != null) stmt.close(); } catch (SQLException e) {}
            try { if (conexao != null) conexao.close(); } catch (SQLException e) {}
        }
        return u;
    }
    public void atualizarUtilizador(Utilizador u) throws SQLException{
        Connection conexao = null;
        PreparedStatement stmt = null;
        try {
            conexao = new ConexaoDB().getConexao();
            stmt = conexao.prepareStatement(
                    "UPDATE utilizador SET nome = ?, username  = ?, password  = ?, nif  = ?, dataAdmissao  = ?, dataDemissao  = ?, nivelAcesso  = ?, ativo  = ?, cedulaProfissional  = ? WHERE id = ?");
            stmt.setString(1, u.getNome());
            stmt.setString(2, u.getUsername());
            stmt.setString(3, u.getPasswordHash());
            stmt.setInt(4, u.getNif());
            stmt.setString(5, String.valueOf(u.getDataAdmissao()));
            if(u.getDataDemissao() != null)
                stmt.setString(6, String.valueOf(u.getDataDemissao()));
            else
                stmt.setString(6, null);
            stmt.setString(7, u.getNivelAcesso().name()); // transforma enum em string
            stmt.setInt(8, u.getAtivo() ? 1 : 0);
            if(u instanceof Advogado){
                Advogado advogado = (Advogado)u;
                stmt.setString(9, advogado.getCedulaProfissional());
            }
            else
                stmt.setString(9, null);
            stmt.setInt(10, u.getId());
            stmt.executeUpdate();
        } finally {
            try { if (stmt != null) stmt.close(); } catch (SQLException e) {}
            try { if (conexao != null) conexao.close(); } catch (SQLException e) {}
        }
    }

}

