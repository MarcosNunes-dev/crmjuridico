package crmjuridico.model.enums;

public enum EstadoAgenda {
    AGENDADO,
    CONCLUIDO,
    CANCELADO;

    public boolean podeTransitar(EstadoAgenda novo) {
        switch (this) {
            case AGENDADO:  return novo == CONCLUIDO || novo == CANCELADO;
            case CONCLUIDO: return false;
            case CANCELADO: return false;
            default:        return false;
        }
    }
}