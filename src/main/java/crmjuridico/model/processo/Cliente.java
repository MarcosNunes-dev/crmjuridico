package crmjuridico.model.processo;

import crmjuridico.model.enums.TipoCliente;

import java.time.LocalDate;

public class Cliente
{
    private int id;
    private String nome;
    private String morada;
    private Integer telefone;
    private String email;
    private Integer nif;
    private TipoCliente tipoCliente;
    private LocalDate dataCadastro;
    private boolean ativo = true;

    public Cliente(int id, String nome, String morada, Integer telefone, String email, Integer nif, TipoCliente tipoCliente, LocalDate dataCadastro, boolean ativo) {
        this.id = id;
        this.nome = nome;
        this.morada = morada;
        this.telefone = telefone;
        this.email = email;
        this.nif = nif;
        this.tipoCliente = tipoCliente;
        this.dataCadastro = dataCadastro;
        this.ativo = ativo;
    }

    public int getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getMorada() {
        return morada;
    }

    public void setMorada(String morada) {
        this.morada = morada;
    }

    public Integer getTelefone() {
        return telefone;
    }

    public void setTelefone(Integer telefone) {
        this.telefone = telefone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getNif() {
        return nif;
    }

    public void setNif(Integer nif) {
        this.nif = nif;
    }

    public TipoCliente getTipoCliente() {
        return tipoCliente;
    }

    public void setTipoCliente(TipoCliente tipoCliente) {
        this.tipoCliente = tipoCliente;
    }

    public LocalDate getDataCadastro() {
        return dataCadastro;
    }

    public void setDataCadastro(LocalDate dataCadastro) {
        this.dataCadastro = dataCadastro;
    }

    public boolean getAtivo() {
        return ativo;
    }

    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
    }

    public static String cabecalho() {
        String linha = "+-----+------------------------+------------+---------------------------+-----------+------------+-------+";
        return linha + "\n"
                + String.format("| %-3s | %-22s | %-10s | %-25s | %-9s | %-10s | %-5s |",
                "ID", "Nome", "NIF", "Email", "Tipo", "Cadastro", "Ativo")
                + "\n" + linha;
    }

    @Override
    public String toString() {
        return String.format("| %-3d | %-22.22s | %-10s | %-25.25s | %-9s | %-10s | %-5s |",
                id, nome, nif, email, tipoCliente, dataCadastro, (ativo ? "Sim" : "Não"));
    }
}
