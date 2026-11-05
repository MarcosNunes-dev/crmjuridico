package crmjuridico.model.utilizador;


import crmjuridico.model.enums.NivelAcesso;

import java.time.LocalDate;

public class Administrador extends Utilizador{
    public Administrador(int id, String nome, String username, String passwordHash, Integer nif, LocalDate dataAdmissao, LocalDate dataDemissao, NivelAcesso nivelAcesso, boolean ativo) {
        super(id, nome, username, passwordHash, nif, dataAdmissao, dataDemissao, nivelAcesso, ativo);
    }

}
