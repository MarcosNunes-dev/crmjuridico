package crmjuridico.model.prazo;

import crmjuridico.model.enums.EstadoAgenda;
import crmjuridico.model.processo.Processo;
import crmjuridico.model.utilizador.Utilizador;

import java.time.LocalDateTime;

public class Agenda
{
    private int id;
    private String titulo;
    private Utilizador utilizador; // a pessoa para que vai aparecer o prazo
    private LocalDateTime dataHora;
    private Processo processo;
    private String local;
    private EstadoAgenda estadoAgenda = EstadoAgenda.AGENDADO;

    public Agenda(int id, String titulo, Utilizador utilizador, LocalDateTime dataHora, Processo processo, String local, EstadoAgenda estadoAgenda) {
        this.id = id;
        this.titulo = titulo;
        this.utilizador = utilizador;
        this.dataHora = dataHora;
        this.processo = processo;
        this.local = local;
        this.estadoAgenda = estadoAgenda;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public Utilizador getUtilizador() {
        return utilizador;
    }

    public void setUtilizador(Utilizador utilizador) {
        this.utilizador = utilizador;
    }

    public LocalDateTime getDataHora() {
        return dataHora;
    }

    public void setDataHora(LocalDateTime dataHora) {
        this.dataHora = dataHora;
    }

    public Processo getProcesso() {
        return processo;
    }

    public void setProcesso(Processo processo) {
        this.processo = processo;
    }

    public String getLocal() {
        return local;
    }

    public void setLocal(String local) {
        this.local = local;
    }

    public EstadoAgenda getEstadoAgenda() {
        return estadoAgenda;
    }

    public void setEstadoAgenda(EstadoAgenda estadoAgenda) {
        this.estadoAgenda = estadoAgenda;
    }

    public static String cabecalho() {
        String linha = "+-----+------------------------+------------------+---------------------------+------------+--------------------+--------------------+";
        return linha + "\n"
                + String.format("| %-3s | %-22s | %-16s | %-25s | %-10s | %-18s | %-18s |",
                "ID", "Título", "Data/Hora", "Local", "Estado", "Processo", "Utilizador")
                + "\n" + linha;
    }

    @Override
    public String toString() {
        return String.format("| %-3d | %-22.22s | %-16s | %-25.25s | %-10s | %-18.18s | %-18.18s |",
                id, titulo, dataHora, local, estadoAgenda,
                processo.getNumProcesso(), utilizador.getNome());
    }
}
