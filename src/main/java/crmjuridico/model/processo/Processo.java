package crmjuridico.model.processo;

import crmjuridico.model.enums.EstadoProcesso;
import crmjuridico.model.utilizador.Advogado;

import java.time.LocalDate;

public class Processo
{
    private int id;
    private String numProcesso;
    private String nomeAcao;
    private EstadoProcesso estado;
    private Cliente cliente;
    private Advogado advogado;
    private LocalDate dataDistribuicao;
    private LocalDate dataFinalizacao;


    public Processo(int id, String numProcesso, String nomeAcao, EstadoProcesso estado, Cliente cliente, Advogado advogado, LocalDate dataDistribuicao, LocalDate dataFinalizacao) {
        this.id = id;
        this.numProcesso = numProcesso;
        this.nomeAcao = nomeAcao;
        this.estado = estado;
        this.cliente = cliente;
        this.advogado = advogado;
        this.dataDistribuicao = dataDistribuicao;
        this.dataFinalizacao = dataFinalizacao;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNumProcesso() {
        return numProcesso;
    }

    public void setNumProcesso(String numProcesso) {
        this.numProcesso = numProcesso;
    }

    public String getNomeAcao() {
        return nomeAcao;
    }

    public void setNomeAcao(String nomeAcao) {
        this.nomeAcao = nomeAcao;
    }

    public EstadoProcesso getEstado() {
        return estado;
    }

    public void setEstado(EstadoProcesso estado) {
        this.estado = estado;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public Advogado getAdvogado() {
        return advogado;
    }
    public void setAdvogado(Advogado advogado) {
        this.advogado = advogado;
    }

    public LocalDate getDataDistribuicao() {
        return dataDistribuicao;
    }

    public void setDataDistribuicao(LocalDate dataDistribuicao) {
        this.dataDistribuicao = dataDistribuicao;
    }

    public LocalDate getDataFinalizacao() {
        return dataFinalizacao;
    }

    public void setDataFinalizacao(LocalDate dataFinalizacao) {
        this.dataFinalizacao = dataFinalizacao;
    }

    public static String cabecalho() {
        String linha = "+-----+--------------------+---------------------------+--------------+----------------------+--------------------+--------------+";
        return linha + "\n"
                + String.format("| %-3s | %-18s | %-25s | %-12s | %-20s | %-18s | %-12s |",
                "ID", "Nº Processo", "Ação", "Estado", "Cliente", "Advogado", "Distribuição")
                + "\n" + linha;
    }

    @Override
    public String toString() {
        return String.format("| %-3d | %-18.18s | %-25.25s | %-12s | %-20.20s | %-18.18s | %-12s |",
                id, numProcesso, nomeAcao, estado,
                cliente.getNome(), advogado.getNome(), dataDistribuicao);
    }
}
