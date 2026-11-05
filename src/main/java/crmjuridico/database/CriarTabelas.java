package crmjuridico.database;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class CriarTabelas {
    public void criarTabelas() {
        Connection conexao = null;
        Statement stmt = null;
        try {
            conexao = new ConexaoDB().getConexao();
            stmt = conexao.createStatement();
            stmt.execute("""
                    CREATE TABLE IF NOT EXISTS utilizador(
                        id INTEGER PRIMARY KEY AUTOINCREMENT,
                        nome TEXT NOT NULL,
                        username TEXT UNIQUE NOT NULL,
                        password TEXT NOT NULL,
                        nif INTEGER UNIQUE,
                        dataAdmissao TEXT,
                        dataDemissao TEXT,
                        nivelAcesso TEXT,
                        ativo INTEGER DEFAULT 1,
                        cedulaProfissional TEXT
                    );
                    """);
            stmt.execute("""
                    CREATE TABLE IF NOT EXISTS cliente (
                        id INTEGER PRIMARY KEY AUTOINCREMENT,
                        nome TEXT NOT NULL,
                        morada TEXT,
                        telefone INTEGER,
                        email TEXT,
                        nif INTEGER UNIQUE,
                        tipoCliente TEXT,
                        dataCadastro TEXT,
                        ativo INTEGER DEFAULT 1                               
                    );
                    """);
            stmt.execute(""" 
                    CREATE TABLE IF NOT EXISTS processo (
                        id INTEGER PRIMARY KEY AUTOINCREMENT,
                        numProcesso TEXT UNIQUE,
                        nomeAcao TEXT,
                        estado TEXT,
                        dataDistribuicao TEXT,
                        dataFinalizacao TEXT,
                        id_advogado INTEGER,
                        id_cliente INTEGER,
                        FOREIGN KEY (id_advogado) REFERENCES utilizador(id),
                        FOREIGN KEY (id_cliente) REFERENCES cliente(id)
                    );
                    """);
            stmt.execute("""
                    CREATE TABLE IF NOT EXISTS tarefa (
                        id INTEGER PRIMARY KEY AUTOINCREMENT,
                        titulo TEXT NOT NULL,
                        descricao TEXT,
                        prioridade TEXT,
                        estado TEXT,
                        dataLimite TEXT,       
                        id_processo INTEGER,
                        id_utilizador INTEGER,
                        FOREIGN KEY (id_processo) REFERENCES processo(id),
                        FOREIGN KEY (id_utilizador) REFERENCES utilizador(id)
                    );
                    """);
            stmt.execute("""
                    CREATE TABLE IF NOT EXISTS agenda (
                        id INTEGER PRIMARY KEY AUTOINCREMENT,
                        titulo TEXT NOT NULL,
                        local TEXT,
                        dataProtocolo TEXT,
                        id_processo INTEGER,
                        id_utilizador INTEGER,
                        estado TEXT DEFAULT'AGENDADO',
                        FOREIGN KEY (id_processo) REFERENCES processo(id),
                        FOREIGN KEY (id_utilizador) REFERENCES utilizador(id)
                    );
                    """);
        } catch (SQLException e) {
            System.out.println("ERRO ao criar tabelas: " + e.getMessage());
        }
        finally {
            try { if (stmt != null) stmt.close(); } catch (SQLException e) {}
            try { if (conexao != null) conexao.close(); } catch (SQLException e) {}
        }
    }
}
