package crmjuridico.controller;

import crmjuridico.auxiliares.Hash;
import crmjuridico.database.*;
import crmjuridico.model.enums.*;
import crmjuridico.model.prazo.Agenda;
import crmjuridico.model.processo.Cliente;
import crmjuridico.model.processo.Processo;
import crmjuridico.model.tarefa.Tarefa;
import crmjuridico.model.utilizador.Administrador;
import crmjuridico.model.utilizador.Advogado;
import crmjuridico.model.utilizador.Estagiario;
import crmjuridico.model.utilizador.Utilizador;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class GestaoEscritorio
{
    private Utilizador utilizadorLogado;
    private UtilizadorDAO utilizadorDAO;
    private ClienteDAO clienteDAO;
    private ProcessoDAO processoDAO;
    private TarefaDAO tarefaDAO;
    private AgendaDAO agendaDAO;
    private static GestaoEscritorio instancia;

    private GestaoEscritorio() {
        this.utilizadorDAO = new UtilizadorDAO();
        this.clienteDAO = new ClienteDAO();
        this.processoDAO = new ProcessoDAO();
        this.tarefaDAO = new TarefaDAO();
        this.agendaDAO = new AgendaDAO();
    }
    public static GestaoEscritorio getInstance(){ // cria o objeto gestão escritório uma unica vez durante a execuçaõ do programa.
        if(instancia == null)
            instancia = new GestaoEscritorio();
        return instancia;
    }
    // login
    public boolean login(String user, String pass) {
        try {
            Utilizador u = utilizadorDAO.utilizadorPorUser(user);
            if (u == null)
                return false; // utilizador não existe

            String passRecebida = Hash.sha256(pass);
            if (passRecebida.equals(u.getPasswordHash())) {
                if (!u.getAtivo())
                    return false; // utilizador desativado não entra
                utilizadorLogado = u;
                return true;
            }
            return false;

        } catch (SQLException e) {
            return false;
        }
    }
    public void logout() {
        utilizadorLogado = null;
    }
    public Utilizador getUtilizadorLogado() {
        return utilizadorLogado;
    }
    // utilizadores
    // verificar se o NIF existe
    public boolean nifUtilizadorExiste(Integer nif) throws SQLException {
        return utilizadorDAO.utilizadorPorNif(nif) != null;
    }
    public boolean criarAdminInicial(Utilizador u) throws SQLException {
        if (utilizadorDAO.listarUtilizadoresAtivos().isEmpty())  {
            utilizadorDAO.inserirUtilizador(u);
            return true;
        }
        return false;
    }
    public boolean criarAdvogado(String nome, String username, String pass, Integer nif, LocalDate dataAdmissao, String cedula) throws SQLException {
        if (!utilizadorLogado.isAdmin())
            return false;
        if (nifUtilizadorExiste(nif))
            return false;
        Advogado adv = new Advogado(0, nome, username, Hash.sha256(pass), nif, dataAdmissao, null, NivelAcesso.ADVOGADO, true);
        adv.setCedulaProfissional(cedula);
        utilizadorDAO.inserirUtilizador(adv);
        return true;
    }
    public boolean criarAdministrador(String nome, String username, String pass, Integer nif, LocalDate dataAdmissao) throws SQLException {
        if (!utilizadorLogado.isAdmin())
            return false;
        if (nifUtilizadorExiste(nif))
            return false;
        Administrador admin =  new Administrador(0, nome, username, Hash.sha256(pass), nif, dataAdmissao, null, NivelAcesso.ADMINISTRADOR, true);
        utilizadorDAO.inserirUtilizador(admin);
        return true;
    }
    public boolean criarEstagiario(String nome, String username, String pass, Integer nif, LocalDate dataAdmissao) throws SQLException {
        if (!utilizadorLogado.isAdmin())
            return false;
        if (nifUtilizadorExiste(nif))
            return false;
        Estagiario estagiario =  new Estagiario(0, nome, username, Hash.sha256(pass), nif, dataAdmissao, null, NivelAcesso.ESTAGIARIO, true);
        utilizadorDAO.inserirUtilizador(estagiario);
        return true;
    }
    public ArrayList<Utilizador> listarUtilizadorAtivos() throws SQLException {
        if (!utilizadorLogado.isAdmin())
            return null;
        else
            return utilizadorDAO.listarUtilizadoresAtivos();
    }
    public ArrayList<Utilizador> listarUtilizadorInativos() throws SQLException {
        if (!utilizadorLogado.isAdmin())
            return null;
        else
            return utilizadorDAO.listarUtilizadoresInativos();
    }
    public int ativaDesativarUtilizador(Utilizador u) throws SQLException { // atualizada todos os dados do utilizador
        if (!utilizadorLogado.isAdmin())
            return -1;
        if(!u.getAtivo()) {
            u.setAtivo(true);
            utilizadorDAO.atualizarUtilizador(u);
            return 1;
        }
        else{
            u.setAtivo(false);
            utilizadorDAO.atualizarUtilizador(u);
            return 0;
        }
    }
    public boolean alterarUtilizador(Utilizador u, String nome, Integer nif, LocalDate dataAdmissao, LocalDate dataDemissao, NivelAcesso nivel) throws SQLException {
        if (!utilizadorLogado.isAdmin())
            return false;
        else {
            u.setNome(nome);
            u.setNif(nif);
            u.setDataAdmissao(dataAdmissao);
            u.setDataDemissao(dataDemissao);
            u.setNivelAcesso(nivel);
            utilizadorDAO.atualizarUtilizador(u);
            return true;
        }
    }
    //alterar dados um por um utiliador
    public int alteraDadosUtilizador (Utilizador u, String campo, Object valor) throws SQLException {
        if (!utilizadorLogado.isAdmin())
            return -1;
        if (u == null)
            return 0;
        campo = campo.toUpperCase();
        switch (campo) {
            case "NOME":
                u.setNome((String) valor);
                utilizadorDAO.atualizarUtilizador(u);
                break;
            case "NIF":
                u.setNif((Integer) valor);
                utilizadorDAO.atualizarUtilizador(u);
                break;
            case "DATAAD":
                u.setDataAdmissao((LocalDate) valor);
                utilizadorDAO.atualizarUtilizador(u);
                break;
            case "DATADEM":
                u.setDataDemissao((LocalDate) valor);
                utilizadorDAO.atualizarUtilizador(u);
                break;
            case "NIVEL":
                u.setNivelAcesso((NivelAcesso) valor);
                utilizadorDAO.atualizarUtilizador(u);
                break;
        }
        return 1;
    }
    public boolean alteraSenha(String passAntiga, String novaPass) throws SQLException {
        String antigaHash = Hash.sha256(passAntiga);
        if (antigaHash.equals(utilizadorLogado.getPasswordHash())) {
            utilizadorLogado.setPasswordHash(Hash.sha256(novaPass));
            utilizadorDAO.atualizarUtilizador(utilizadorLogado);
            return true;
        }
        return false;
    }
    public Utilizador consultaUtilizadorPorUser(String username) throws SQLException {
        return utilizadorDAO.utilizadorPorUser(username);
    }
// clientes
//consulta cliente por nif
    public Cliente consultaNif(Integer nif) throws SQLException {
        Cliente x = clienteDAO.clientePorNif(nif);
        return x;
    }
    public Cliente consultaIdCliente(Integer id) throws SQLException {
        Cliente x = clienteDAO.clientePorId(id);
        return x;
    }
    public boolean criarCliente(String nome, String morada, Integer telefone, String email, Integer nif, TipoCliente tipo) throws SQLException {
        if (!utilizadorLogado.temPermissao("CRIAR_CLIENTE"))
            return false;
        Cliente c = new Cliente(0, nome, morada, telefone, email, nif, tipo, LocalDate.now(), true);
        clienteDAO.inserirCliente(c);
        return true;
    }
    public ArrayList<Cliente> listarClientesAtivos() throws SQLException {
        return clienteDAO.listarClienteAtivo();
    }
    public ArrayList<Cliente> listarClientesInativos() throws SQLException {
        if (!utilizadorLogado.isAdmin())
            return null;
        else
            return clienteDAO.listarClienteInativo();
    }
    public int ativaDesativarCliente(Cliente c) throws SQLException { // atualizada todos os dados do utilizador
        if (!utilizadorLogado.isAdmin())
            return -1;
        if(!c.getAtivo()) {
            c.setAtivo(true);
            clienteDAO.atualizarCliente(c);
            return 1;
        }
        else{
            c.setAtivo(false);
            clienteDAO.atualizarCliente(c);
            return 0;
        }
    }
    public int alterarCliente(Cliente c, String nome, String morada, Integer telefone, String email, Integer nif, TipoCliente tipo) throws SQLException {
        if (!utilizadorLogado.temPermissao("ALTERAR_CLIENTE"))
            return -1;
        if (c == null )
            return 0;
        else
        {
            c.setNome(nome);
            c.setMorada(morada);
            c.setTelefone(telefone);
            c.setEmail(email);
            c.setNif(nif);
            c.setTipoCliente(tipo);
            clienteDAO.atualizarCliente(c);
        }
        return 1;
    }
    //alterar dados um por um Cliente
    public int alteraDadosCliente (Cliente c, String campo, Object valor) throws SQLException {
        if (!utilizadorLogado.temPermissao("ALTERAR_CLIENTE"))
            return -1;
        if( c != null ) {
            campo = campo.toUpperCase();
            switch (campo) {
                case "NOME":
                    c.setNome((String) valor);
                    clienteDAO.atualizarCliente(c);
                    break;
                case "NIF":
                    c.setNif((Integer) valor);
                    clienteDAO.atualizarCliente(c);
                    break;
                case "MORADA":
                    c.setMorada((String) valor);
                    clienteDAO.atualizarCliente(c);
                    break;
                case "TELEFONE":
                    c.setTelefone((Integer) valor);
                    clienteDAO.atualizarCliente(c);
                    break;
                case "EMAIL":
                    c.setEmail((String) valor);
                    clienteDAO.atualizarCliente(c);
                    break;
                case "TIPO":
                    c.setTipoCliente((TipoCliente) valor);
                    clienteDAO.atualizarCliente(c);
                    break;
            }
            return 1;
        }
        else
            return 0;
    }
    // processo
    public int criarProcesso(String numProcesso, String nomeAcao, EstadoProcesso estado, Integer nifCliente, String usernameAdv, LocalDate dataDistribuicao) throws SQLException {
        if (!utilizadorLogado.temPermissao("CRIAR_PROCESSO"))
            return -1;
        Cliente cliente = clienteDAO.clientePorNif(nifCliente);
        if (cliente == null)
            return 0;
        Utilizador u = utilizadorDAO.utilizadorPorUser(usernameAdv);
        if (u == null || !(u instanceof Advogado))
            return 2;
        Advogado adv = (Advogado) u;
        Processo p = new Processo(0, numProcesso, nomeAcao, estado, cliente, adv, dataDistribuicao, null);
        processoDAO.inserirProcesso(p);
        return 1;
    }
    public Processo consultaIdProcesso(Integer id) throws SQLException {
        Processo x = processoDAO.processoPorId(id);
        return x;
    }
    public ArrayList<Processo> listarProcessoPorEstado(EstadoProcesso estado) throws SQLException {
        return processoDAO.processoPorEstado(estado);
    }
    public ArrayList<Processo> listarProcessoPorCliente(Cliente cliente) throws SQLException {
        return processoDAO.processoPorCliente(cliente);
    }
    public ArrayList<Processo> listarProcessoPorAdvogado(int id) throws SQLException {
        Utilizador u = utilizadorDAO.utilizadorPorId(id);
        if (u == null)
            return null;
        return processoDAO.processoPorAdvogado(u);
    }
    public boolean alterarEstadoProcesso(Processo p, EstadoProcesso novoEstado) throws SQLException {
        if (!utilizadorLogado.temPermissao("ALTERAR_PROCESSO"))
            return false;
        if (!p.getEstado().podeTransitar(novoEstado))
            return false;
        p.setEstado(novoEstado);
        return processoDAO.atualizarProcessoPorEstado(p);
    }
    // tarefas
    public int criarTarefa(String titulo, String descricao, Prioridade prioridade, EstadoTarefa estado, LocalDate dataLimite, Integer idProcesso, String usernameResp) throws SQLException {
        if (!utilizadorLogado.temPermissao("CRIAR_TAREFA"))
            return -1;
        Processo processo = processoDAO.processoPorId(idProcesso);
        if (processo == null)
            return 0;
        Utilizador responsavel = utilizadorDAO.utilizadorPorUser(usernameResp);
        if (responsavel == null)
            return 2;
        Tarefa t = new Tarefa(0, titulo, descricao, prioridade, estado, dataLimite, processo, responsavel);
        tarefaDAO.inserirTarefa(t);
        return 1;
    }
    public Tarefa consultaIdTarefa(Integer id) throws SQLException {
        Tarefa x = tarefaDAO.tarefaPorId(id);
        return x;
    }
    public ArrayList<Tarefa> listarTarefaPorEstado(EstadoTarefa estado) throws SQLException {
        ArrayList<Tarefa> lista = tarefaDAO.tarefaPorEstado(estado);
        if (utilizadorLogado.isAdmin())
            return lista;
        ArrayList<Tarefa> minhas = new ArrayList<>();
        for (Tarefa t : lista)
            if (t.getResponsavel().getId() == utilizadorLogado.getId())
                minhas.add(t);
        return minhas;
    }
    public ArrayList<Tarefa> listarTarefaPorProcesso(Integer idProcesso) throws SQLException {
        ArrayList<Tarefa> lista = tarefaDAO.tarefaPorProcesso(idProcesso);
        if (utilizadorLogado.isAdmin())
            return lista;
        ArrayList<Tarefa> minhas = new ArrayList<>();
        for (Tarefa t : lista)
            if (t.getResponsavel().getId() == utilizadorLogado.getId())
                minhas.add(t);
        return minhas;
    }
    public ArrayList<Tarefa> listarTarefaPorResp() throws SQLException {
        if (!utilizadorLogado.isAdmin())
            return tarefaDAO.tarefaPorResponsavel(utilizadorLogado.getId());
        else
            return tarefaDAO.tarefaTodas();
    }
    public ArrayList<Tarefa> listarTarefaPorUtilizador(int id) throws SQLException {
        if (!utilizadorLogado.isAdmin())
            return null;
        Utilizador u = utilizadorDAO.utilizadorPorId(id);
        if (u == null)
            return null;
        return tarefaDAO.tarefaPorResponsavel(u.getId());
    }
    public boolean alterarEstadoTarefa(Tarefa t, EstadoTarefa novoEstado) throws SQLException {
        if (!utilizadorLogado.temPermissao("ALTERAR_TAREFA"))
            return false;
        if (!t.getEstado().podeTransitar(novoEstado))
            return false;
        t.setEstado(novoEstado);
        return tarefaDAO.atualizarTarefaPorEstado(t);
    }
    // agenda
    public int criarAgenda(String titulo, String local, LocalDateTime dataHora, Integer idProcesso, String usernameUtil) throws SQLException {
        if (!utilizadorLogado.temPermissao("CRIAR_AGENDA"))
            return -1;
        Processo processo = processoDAO.processoPorId(idProcesso);
        if (processo == null)
            return 0;
        Utilizador utilizador = utilizadorDAO.utilizadorPorUser(usernameUtil);
        if (utilizador == null)
            return 2;
        Agenda a = new Agenda(0, titulo, utilizador, dataHora, processo, local, EstadoAgenda.AGENDADO);
        agendaDAO.inserirAgenda(a);
        return 1;
    }
    public Agenda consultaIdAgenda(Integer id) throws SQLException {
        Agenda x = agendaDAO.agendaPorId(id);
        return x;
    }
    public ArrayList<Agenda> listarAgendaPorEstado(EstadoAgenda estado) throws SQLException {
        ArrayList<Agenda> lista = agendaDAO.agendaPorEstado(estado);
        if (utilizadorLogado.isAdmin())
            return lista;
        ArrayList<Agenda> minhas = new ArrayList<>();
        for (Agenda a : lista)
            if (a.getUtilizador().getId() == utilizadorLogado.getId())
                minhas.add(a);
        return minhas;
    }
    public ArrayList<Agenda> listarAgendaPorProcesso(Integer idProcesso) throws SQLException {
        ArrayList<Agenda> lista = agendaDAO.agendaPorProcesso(idProcesso);
        if (utilizadorLogado.isAdmin())
            return lista;
        ArrayList<Agenda> minhas = new ArrayList<>();
        for (Agenda a : lista)
            if (a.getUtilizador().getId() == utilizadorLogado.getId())
                minhas.add(a);
        return minhas;
    }
    public ArrayList<Agenda> listarAgendaPorResp() throws SQLException {
        if (!utilizadorLogado.isAdmin())
            return agendaDAO.agendaPorUtilizador(utilizadorLogado.getId());
        else
            return agendaDAO.agendaToda();
    }
    public ArrayList<Agenda> listarAgendaPorUtilizador(int id) throws SQLException {
        if (!utilizadorLogado.isAdmin())
            return null;
        Utilizador u = utilizadorDAO.utilizadorPorId(id);
        if (u == null)
            return null;
        return agendaDAO.agendaPorUtilizador(u.getId());
    }
    public boolean alterarEstadoAgenda(Agenda a, EstadoAgenda novoEstado) throws SQLException {
        if (!utilizadorLogado.temPermissao("ALTERAR_AGENDA")) return false;
        if (!a.getEstadoAgenda().podeTransitar(novoEstado))
            return false;
        a.setEstadoAgenda(novoEstado);
        return agendaDAO.atualizarAgendaPorEstado(a);
    }
    //dashboard
    public int contarClientesAtivos() throws SQLException {
        return clienteDAO.listarClienteAtivo().size();
    }
    public int contarProcessosAtivos() throws SQLException {
        int distribuidos = processoDAO.processoPorEstado(EstadoProcesso.DISTRIBUIDO).size();
        int emCurso = processoDAO.processoPorEstado(EstadoProcesso.EM_CURSO).size();
        return distribuidos + emCurso;
    }
    public int contarTarefasPendentes() throws SQLException {
        int pendentes = tarefaDAO.tarefaPorEstado(EstadoTarefa.PENDENTE).size();
        int emCurso = tarefaDAO.tarefaPorEstado(EstadoTarefa.EM_CURSO).size();
        return pendentes + emCurso;
    }
    public int contarAgendaHoje() throws SQLException {

        int count = 0;
        for (Agenda a : agendaDAO.agendaToda()) {
            if (a.getEstadoAgenda() == EstadoAgenda.AGENDADO)
                count++;
        }
        return count;
    }
    public int contarTarefasPendentesPorResp() throws SQLException {
        int count = 0;
        for (Tarefa t : tarefaDAO.tarefaPorResponsavel(utilizadorLogado.getId())) {
            if (t.getEstado() == EstadoTarefa.PENDENTE || t.getEstado() == EstadoTarefa.EM_CURSO)
                count++;
        }
        return count;
    }
    public int contarAgendaHojePorResp() throws SQLException {
        int count = 0;
        for (Agenda a : agendaDAO.agendaPorUtilizador(utilizadorLogado.getId())) {
            if (a.getEstadoAgenda() == EstadoAgenda.AGENDADO)
                count++;
        }
        return count;
    }
}
