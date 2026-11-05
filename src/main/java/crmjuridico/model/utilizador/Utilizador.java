package crmjuridico.model.utilizador;

import crmjuridico.model.enums.NivelAcesso;

import java.time.LocalDate;

public abstract class Utilizador
{
    private int id;
    private String nome;
    private String username;
    private String passwordHash;
    private Integer nif;
    private LocalDate dataAdmissao;
    private LocalDate dataDemissao;
    private NivelAcesso nivelAcesso;
    private Boolean ativo = true;


    public Utilizador(int id, String nome, String username, String passwordHash, Integer nif, LocalDate dataAdmissao, LocalDate dataDemissao, NivelAcesso nivelAcesso, boolean ativo) {
        this.id = id;
        this.nome = nome;
        this.username = username;
        this.passwordHash = passwordHash;
        this.nif = nif;
        this.dataAdmissao = dataAdmissao;
        this.dataDemissao = dataDemissao;
        this.nivelAcesso = nivelAcesso;
        this.ativo = ativo;

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public Integer getNif() {
        return nif;
    }

    public void setNif(Integer nif) {
        this.nif = nif;
    }

    public LocalDate getDataAdmissao() {
        return dataAdmissao;
    }

    public void setDataAdmissao(LocalDate dataAdmissao) {
        this.dataAdmissao = dataAdmissao;
    }

    public LocalDate getDataDemissao() {
        return dataDemissao;
    }

    public void setDataDemissao(LocalDate dataDemissao) {
        this.dataDemissao = dataDemissao;
    }

    public NivelAcesso getNivelAcesso() {
        return nivelAcesso;
    }

    public void setNivelAcesso(NivelAcesso nivelAcesso) {
        this.nivelAcesso = nivelAcesso;
    }


    public Boolean getAtivo() {
        return ativo;
    }
    public void setAtivo(Boolean ativo) {
        this.ativo = ativo;
    }
    
    public boolean isAdmin() {
        return nivelAcesso == NivelAcesso.ADMINISTRADOR;
    }

    public boolean temPermissao(String acao) {
        return true; // por defeito tem permissão
    }

    public static String cabecalho() {
        String linha = "+-----+------------------------+--------------+------------+---------------+-------+------------+";
        return linha + "\n"
                + String.format("| %-3s | %-22s | %-12s | %-10s | %-13s | %-5s | %-10s |",
                "ID", "Nome", "Username", "NIF", "Nível", "Ativo", "Cédula")
                + "\n" + linha;
    }

    @Override
    public String toString() {
        return String.format("| %-3d | %-22.22s | %-12.12s | %-10s | %-13s | %-5s | %-10s |",
                id, nome, username, nif, nivelAcesso,
                (ativo ? "Sim" : "Não"), "---");
    }
}
