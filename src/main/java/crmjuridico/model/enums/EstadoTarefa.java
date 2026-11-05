package crmjuridico.model.enums;

public enum EstadoTarefa {
    PENDENTE,
    EM_CURSO,
    CONCLUIDA,
    CANCELADA;

    public boolean podeTransitar(EstadoTarefa novo) {
        switch (this) {
            case PENDENTE:  return novo == EM_CURSO || novo == CANCELADA;
            case EM_CURSO:  return novo == CONCLUIDA || novo == CANCELADA;
            case CONCLUIDA: return false;
            case CANCELADA: return false;
            default:        return false;
        }
    }
}