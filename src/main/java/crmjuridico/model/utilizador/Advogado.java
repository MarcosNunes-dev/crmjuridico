package crmjuridico.model.utilizador;

import crmjuridico.model.enums.NivelAcesso;

import java.time.LocalDate;



public class Advogado extends Utilizador{

    private String cedulaProfissional;

    public Advogado(int id, String nome, String username, String passwordHash, Integer nif, LocalDate dataAdmissao, LocalDate dataDemissao, NivelAcesso nivelAcesso, boolean ativo) {
        super(id, nome, username, passwordHash, nif, dataAdmissao, dataDemissao, nivelAcesso, ativo);
    }

    public String getCedulaProfissional() {
        return cedulaProfissional;
    }

    public void setCedulaProfissional(String cedulaProfissional) {
        this.cedulaProfissional = cedulaProfissional;
    }


    @Override
    public String toString() {
        return String.format("| %-3d | %-22.22s | %-12.12s | %-10s | %-13s | %-5s | %-10s |",
                getId(), getNome(), getUsername(), getNif(), getNivelAcesso(),
                (getAtivo() ? "Sim" : "Não"), cedulaProfissional);
    }
}
