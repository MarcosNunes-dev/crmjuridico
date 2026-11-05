package crmjuridico.model.enums;

public enum EstadoProcesso
{
    DISTRIBUIDO,
    FINALIZADO,
    SUSPENSO,
    ARQUIVADO,
    EM_CURSO;

    public boolean podeTransitar(EstadoProcesso novo) {
        switch (this) {
            case DISTRIBUIDO: return novo == EM_CURSO || novo == SUSPENSO || novo == FINALIZADO || novo == ARQUIVADO;
            case EM_CURSO:    return novo == SUSPENSO || novo == FINALIZADO || novo == ARQUIVADO;
            case SUSPENSO:    return novo == EM_CURSO || novo == FINALIZADO || novo == ARQUIVADO;
            case FINALIZADO:  return novo == ARQUIVADO;
            case ARQUIVADO:   return false;
            default:          return false;
        }
    }
}
