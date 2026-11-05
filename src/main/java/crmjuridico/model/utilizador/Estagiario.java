package crmjuridico.model.utilizador;

import crmjuridico.model.enums.NivelAcesso;

import java.time.LocalDate;
public class Estagiario extends Utilizador
{

    public Estagiario(int id, String nome, String username, String passwordHash, Integer nif, LocalDate dataAdmissao, LocalDate dataDemissao, NivelAcesso nivelAcesso, boolean ativo) {
        super(id, nome, username, passwordHash, nif, dataAdmissao, dataDemissao, nivelAcesso, ativo);
    }

    @Override
    public boolean temPermissao(String acao) {
        switch (acao) {
            case "CRIAR_CLIENTE":
            case "CRIAR_PROCESSO":
            case "CRIAR_TAREFA":
            case "CRIAR_AGENDA":
            case "ALTERAR_CLIENTE":
            case "ALTERAR_PROCESSO":
            case "ALTERAR_TAREFA":
            case "ALTERAR_AGENDA":
                return false;
            default:
                return true;
        }
    }
}
