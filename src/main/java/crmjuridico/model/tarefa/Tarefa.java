package crmjuridico.model.tarefa;

import crmjuridico.model.enums.EstadoTarefa;
import crmjuridico.model.enums.Prioridade;
import crmjuridico.model.processo.Processo;
import crmjuridico.model.utilizador.Utilizador;

import java.time.LocalDate;

public class Tarefa
{
    private int id;
    private String titulo;
    private String descricao;
    private Prioridade prioridade;
    private EstadoTarefa estado;
    private LocalDate dataLimite;
    private Processo processo;
    private Utilizador responsavel;

    public Tarefa(int id, String titulo, String descricao, Prioridade prioridade, EstadoTarefa estado, LocalDate dataLimite, Processo processo, Utilizador responsavel) {
        this.id = id;
        this.titulo = titulo;
        this.descricao = descricao;
        this.prioridade = prioridade;
        this.estado = estado;
        this.dataLimite = dataLimite;
        this.processo = processo;
        this.responsavel = responsavel;
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

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Prioridade getPrioridade() {
        return prioridade;
    }

    public void setPrioridade(Prioridade prioridade) {
        this.prioridade = prioridade;
    }

    public EstadoTarefa getEstado() {
        return estado;
    }

    public void setEstado(EstadoTarefa estado) {
        this.estado = estado;
    }

    public LocalDate getDataLimite() {
        return dataLimite;
    }

    public void setDataLimite(LocalDate dataLimite) {
        this.dataLimite = dataLimite;
    }

    public Processo getProcesso() {
        return processo;
    }

    public void setProcesso(Processo processo) {
        this.processo = processo;
    }

    public Utilizador getResponsavel() {
        return responsavel;
    }

    public void setResponsavel(Utilizador responsavel) {
        this.responsavel = responsavel;
    }

    public static String cabecalho() {
        String linha = "+-----+------------------------+-----------+------------+--------------+--------------------+--------------------+";
        return linha + "\n"
                + String.format("| %-3s | %-22s | %-9s | %-10s | %-12s | %-18s | %-18s |",
                "ID", "Título", "Prioridade", "Estado", "Prazo", "Processo", "Responsável")
                + "\n" + linha;
    }

    @Override
    public String toString() {
        return String.format("| %-3d | %-22.22s | %-9s | %-10s | %-12s | %-18.18s | %-18.18s |",
                id, titulo, prioridade, estado, dataLimite,
                processo.getNumProcesso(), responsavel.getNome());
    }
}
