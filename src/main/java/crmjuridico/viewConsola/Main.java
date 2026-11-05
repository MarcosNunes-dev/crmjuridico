package crmjuridico.viewConsola;

import crmjuridico.auxiliares.Hash;
import crmjuridico.auxiliares.IN;
import crmjuridico.auxiliares.Menu;
import crmjuridico.controller.GestaoEscritorio;
import crmjuridico.database.CriarTabelas;
import crmjuridico.model.enums.*;
import crmjuridico.model.prazo.Agenda;
import crmjuridico.model.processo.Cliente;
import crmjuridico.model.processo.Processo;
import crmjuridico.model.tarefa.Tarefa;
import crmjuridico.model.utilizador.Administrador;
import crmjuridico.model.utilizador.Utilizador;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.time.LocalDateTime;

public class Main {
    private static GestaoEscritorio controller = GestaoEscritorio.getInstance(); // get.instance é o metodo que verifica se já está instanciado o controller e guarda o utuilizador logado

    private static final String RESET  = "\u001B[0m";
    private static final String VERDE  = "\u001B[32m";
    private static final String VERMELHO = "\u001B[31m";

    public static void main(String[] args) throws Exception {

        new CriarTabelas().criarTabelas();

        // cria admin inicial se BD estiver vazia
        Administrador admin = new Administrador(0, "Admin", "admin", Hash.sha256("1234"), 123456789, LocalDate.now(), null, NivelAcesso.ADMINISTRADOR, true);
        if (controller.criarAdminInicial(admin))
            System.out.println("Admin inicial criado!");
        // login
        boolean logado = false;
        while (!logado) {
            Menu.menuLogin();
            String user = IN.getString();
            System.out.print("| Password: ");
            String pass = IN.getString();
            logado = controller.login(user, pass);
            if (!logado)
                System.out.println("Username ou password incorrectos. Tente novamente.");
        }
        System.out.println("Bem-vindo, " + controller.getUtilizadorLogado().getNome() + "!");
        pausa();

        boolean isAdmin = controller.getUtilizadorLogado().isAdmin();

        int opcao = -1;
        while (opcao != 0) {
            limparEcran();
            if (isAdmin)
                Menu.menuPrincipalAdmin();
            else
                Menu.menuPrincipalUtilizador();
            opcao = IN.getInt();
            // administrador
            if (isAdmin) {
                switch (opcao) {
                    case 1: // menu utilizadores
                        int opTemp = -1;
                        while (opTemp!= 0) {
                            limparEcran();
                            Menu.menuUtilizadores();
                            opTemp = IN.getInt();
                            switch (opTemp) {
                                case 1: //  listar utilizadores ativos
                                    try {
                                        ArrayList<Utilizador> lista = controller.listarUtilizadorAtivos();
                                        if(lista == null || lista.isEmpty())
                                            System.out.println("Sem Utilizadores!");
                                        else
                                            imprimirUtilizadores(lista);
                                    }catch (Exception e) {
                                        System.out.println(VERMELHO + "Erro na base de dados!" + RESET);
                                    }
                                    pausa();
                                    break;
                                case 2: // listar inativos
                                    try {
                                        ArrayList<Utilizador> lista = controller.listarUtilizadorInativos();
                                        if(lista == null || lista.isEmpty())
                                            System.out.println("Não existem utilizadores inativos!");
                                        else
                                            imprimirUtilizadores(lista);
                                    }catch (Exception e) {
                                        System.out.println(VERMELHO + "Erro na base de dados!" + RESET);
                                    }
                                    pausa();
                                    break;
                                case 3: // criar utilizador
                                    adicionarUtilizador();
                                    pausa();
                                    break;
                                case 4: // alterar dados utilizador
                                    System.out.print("Digite o username do utilizador que deseja atualizar: ");
                                    String username = IN.getString();
                                    try {
                                        Utilizador x = controller.consultaUtilizadorPorUser(username);
                                        if (x != null) {
                                            opTemp = -1;
                                            while (opTemp != 0) {
                                                limparEcran();
                                                Menu.menuAlterarUtilizador();
                                                opTemp = IN.getInt();
                                                switch (opTemp) {
                                                    case 1: // alterar todos os dados
                                                        System.out.print("Nome: ");
                                                        String nome = IN.getString();
                                                        System.out.print("NIF: ");
                                                        int nif = IN.getNif();
                                                        System.out.print("Data admissão (AAAA/MM/DD): ");
                                                        LocalDate dataAdmissao = IN.getLocalDate();
                                                        System.out.print("Data demissão (AAAA/MM/DD) ou ENTER para ignorar: ");
                                                        String dataDem = IN.getEnter();
                                                        LocalDate dataDemissao = dataDem.isBlank() ? null : LocalDate.parse(dataDem);
                                                        NivelAcesso nivel;
                                                        while (true) {
                                                            System.out.println("Nível: 1-ADMINISTRADOR | 2-ADVOGADO | 3-ESTAGIARIO");
                                                            switch (IN.getInt()) {
                                                                case 1: nivel = NivelAcesso.ADMINISTRADOR; break;
                                                                case 2: nivel = NivelAcesso.ADVOGADO; break;
                                                                case 3: nivel = NivelAcesso.ESTAGIARIO; break;
                                                                default:
                                                                    System.out.println(VERMELHO + "Opção inválida!" + RESET);
                                                                    pausa();
                                                                    continue;
                                                            }
                                                            break;
                                                        }
                                                        if (controller.alterarUtilizador(x, nome, nif, dataAdmissao, dataDemissao, nivel))
                                                            System.out.println(VERDE + "Dados alterados com sucesso!" + RESET);
                                                        else
                                                            System.out.println(VERMELHO + "Sem permissão!" + RESET);
                                                        pausa();
                                                        break;
                                                    case 2: // alterar nome
                                                        System.out.print("Nome: ");
                                                        int r = controller.alteraDadosUtilizador(x, "NOME", IN.getString());
                                                        if (r == 1)
                                                            System.out.println(VERDE + "Nome atualizado com sucesso!" + RESET);
                                                        else if (r == -1)
                                                            System.out.println(VERMELHO + "Sem permissão!" + RESET);
                                                        else
                                                            System.out.println(VERMELHO + "Utilizador não encontrado!!" + RESET);
                                                        pausa();
                                                        break;
                                                    case 3: // alterar NIF
                                                        System.out.print("NIF: ");
                                                        int r2 = controller.alteraDadosUtilizador(x, "NIF", IN.getNif());
                                                        if (r2 == 1)
                                                            System.out.println(VERDE + "NIF atualizado com sucesso!" + RESET);
                                                        else if (r2 == -1)
                                                            System.out.println(VERMELHO + "Sem permissão!" + RESET);
                                                        else
                                                            System.out.println(VERMELHO + "Utilizador não encontrado!!" + RESET);
                                                        pausa();
                                                        break;
                                                    case 4: // alterar data admissão
                                                        System.out.print("Data admissão (AAAA/MM/DD): ");
                                                        int r3 = controller.alteraDadosUtilizador(x, "DATAAD", IN.getLocalDate());
                                                        if (r3 == 1)
                                                            System.out.println(VERDE + "Data atualizada com sucesso!" + RESET);
                                                        else if (r3 == -1)
                                                            System.out.println(VERMELHO + "Sem permissão!" + RESET);
                                                        else
                                                            System.out.println(VERMELHO + "Utilizador não encontrado!!" + RESET);
                                                        pausa();
                                                        break;
                                                    case 5: // alterar data demissão
                                                        System.out.print("Data demissão (AAAA/MM/DD): ");
                                                        int r4 = controller.alteraDadosUtilizador(x, "DATADEM", IN.getLocalDate());
                                                        if (r4 == 1)
                                                            System.out.println(VERDE + "Data atualizada com sucesso!" + RESET);
                                                        else if (r4 == -1)
                                                            System.out.println(VERMELHO + "Sem permissão!" + RESET);
                                                        else
                                                            System.out.println(VERMELHO + "Utilizador não encontrado!!" + RESET);
                                                        pausa();
                                                        break;
                                                    case 6: // alterar nível acesso
                                                        NivelAcesso novoNivel;
                                                        while (true){
                                                            System.out.println("Nível: 1-ADMINISTRADOR | 2-ADVOGADO | 3-ESTAGIARIO");
                                                            switch (IN.getInt()) {
                                                                case 1:
                                                                    novoNivel = NivelAcesso.ADMINISTRADOR;
                                                                    break;
                                                                case 2:
                                                                    novoNivel = NivelAcesso.ADVOGADO;
                                                                    break;
                                                                case 3:
                                                                    novoNivel = NivelAcesso.ESTAGIARIO;
                                                                    break;
                                                                default:
                                                                    System.out.println(VERMELHO + "Opção inválida!" + RESET);
                                                                    pausa();
                                                                    continue;
                                                            }
                                                            break;
                                                        }
                                                        int r5 = controller.alteraDadosUtilizador(x, "NIVEL", novoNivel);
                                                        if (r5 == 1)
                                                            System.out.println(VERDE + "Nível atualizado com sucesso!" + RESET);
                                                        else if (r5 == -1)
                                                            System.out.println(VERMELHO + "Sem permissão!" + RESET);
                                                        else
                                                            System.out.println(VERMELHO + "Erro ao atualizar!" + RESET);
                                                        pausa();
                                                        break;
                                                    case 0:
                                                        break;
                                                    default:
                                                        System.out.println(VERMELHO + "Opção inválida!" + RESET);
                                                        pausa();
                                                }
                                            }
                                        } else {
                                            System.out.println(VERMELHO + "Utilizador não encontrado." + RESET);
                                        }
                                    } catch (SQLException e) {
                                        System.out.println(VERMELHO + "Erro na base de dados!" + RESET);
                                    }
                                    pausa();
                                    break;
                                case 5: // ativar/desativar
                                    System.out.print("Digite o username do utilizador: ");
                                    String user = IN.getString();
                                    try {
                                        Utilizador u = controller.consultaUtilizadorPorUser(user);
                                        if (u != null) {
                                            int resultado = controller.ativaDesativarUtilizador(u);
                                            if (resultado == 1)
                                                System.out.println(VERDE + "Utilizador ativado com sucesso!" + RESET);
                                            else if (resultado == 0)
                                                System.out.println(VERDE + "Utilizador desativado com sucesso!" + RESET);
                                            else
                                                System.out.println(VERMELHO + "Sem permissão!" + RESET);
                                        } else
                                            System.out.println(VERMELHO + "Utilizador não encontrado!" + RESET);
                                    } catch (SQLException e) {
                                        System.out.println(VERMELHO + "Erro na base de dados!" + RESET);
                                    }
                                    pausa();
                                    break;
                                case 0:
                                    break;
                            }
                        }
                        break;
                    case 2:
                        subMenuCliente();
                        break;
                    case 3:
                        subMenuProcesso();
                        break;
                    case 4:
                        subMenuTarefa();
                        break;
                    case 5:
                        subMenuAgenda();
                        break;
                    case 6:
                        alterarSenha();
                        break;
                    case 0:
                        controller.logout();
                        System.out.println("Sessão terminada.");
                        break;
                    default:
                        System.out.println(VERMELHO + "\nOpção inválida!" + RESET);
                        pausa();
                }
            }
            else // utilizadores
            {
                switch (opcao) {
                    case 1:
                        subMenuCliente();
                        break;
                    case 2:
                        subMenuProcesso();
                        break;
                    case 3:
                        subMenuTarefa();
                        break;
                    case 4:
                        subMenuAgenda();
                        break;
                    case 5:
                        alterarSenha();
                        break;
                    case 0:
                        controller.logout();
                        System.out.println("Sessão terminada.");
                        break;
                    default:
                        System.out.println(VERMELHO + "\nOpção inválida!" + RESET);
                        pausa();
                }
            }
        }
    }
    // lógica de add novos utilizadores.
    private static void adicionarUtilizador() {
        System.out.print("NIF: ");
        int nif = IN.getNif();
        try {
            if(controller.nifUtilizadorExiste(nif)){
                System.out.println(VERMELHO + "NIF já cadastrado!" + RESET);
                return;
            }
        } catch (SQLException e) {
            System.out.println(VERMELHO + "Erro na base de dados!" + RESET);
            return;
        }
        System.out.print("Nome: ");
        String nome = IN.getString();
        System.out.print("Username: ");
        String username = IN.getString();
        System.out.print("Password: ");
        String pass = IN.getString();
        System.out.print("Data admissão (AAAA/MM/DD): ");
        LocalDate dataAdmissao = IN.getLocalDate();
        System.out.println("Nível: 1-ADMINISTRADOR | 2-ADVOGADO | 3-ESTAGIARIO");
        int nivel = IN.getInt();
        try {
            boolean criado = false;
            switch (nivel) {
                case 1:
                    criado = controller.criarAdministrador(nome, username, pass, nif, dataAdmissao);
                    break;
                case 2:
                    System.out.print("Cédula profissional: ");
                    String cedula = IN.getString();
                    criado = controller.criarAdvogado(nome, username, pass, nif, dataAdmissao, cedula);
                    break;
                case 3:
                    criado = controller.criarEstagiario(nome, username, pass, nif, dataAdmissao);
                    break;
                default:
                    System.out.println(VERMELHO + "Nível inválido!" + RESET);
                    pausa();
                    return;
            }
            if (criado)
                System.out.println(VERDE + "Utilizador criado com sucesso!" + RESET);
            else
                System.out.println(VERMELHO + "Sem permissão!" + RESET);
        } catch (SQLException e) {
            System.out.println(VERMELHO + "Erro na base de dados!" + "ERRO: " + e.getMessage()+ RESET);
        }
    }
    private static void alterarSenha(){
        String nova, antiga;
        System.out.println(controller.getUtilizadorLogado().getNome() + "!");
        System.out.println("");
        System.out.print("Digite sua password atual: ");
        antiga = IN.getString();
        System.out.print("Digite sua nova password: ");
        nova = IN.getString();
        try {
            if (controller.alteraSenha(antiga, nova))
                System.out.println("Password alterada com sucesso!");
            else
                System.out.println("Password atual está incorreta!");
        } catch (SQLException e) {
            System.out.println(VERMELHO + "Erro na base de dados!" + RESET);
        }
    }
    // logica do cliente (comum entre admin e utilizadores)
    private static void subMenuCliente(){
        Cliente cliente;
        int nif = 0;
        int opcao = -1;
        while (opcao != 0) {
            limparEcran();
            Menu.menuClientes();
            opcao = IN.getInt();
            switch (opcao) {
                case 1: // listar clientes ativos
                    try {
                        ArrayList<Cliente> lista = controller.listarClientesAtivos();
                        if(lista == null || lista.isEmpty())
                            System.out.println("Sem Clientes cadastrados!");
                        else
                            imprimirClientes(lista);
                    }catch (Exception e) {
                        System.out.println(VERMELHO + "Erro na base de dados!" + RESET);
                    }
                    pausa();
                    break;
                case 2: // listar clientes inativos
                    if (semPermissaoAdmin()){
                        pausa();
                        break;
                    }
                    try {
                        ArrayList<Cliente> lista = controller.listarClientesInativos();
                        if(lista == null || lista.isEmpty())
                            System.out.println("Sem Clientes cadastrados!");
                        else
                            imprimirClientes(lista);
                    }catch (Exception e) {
                        System.out.println(VERMELHO + "Erro na base de dados!" + RESET);
                    }
                    pausa();
                    break;
                case 3: //buscar por nif
                    System.out.print("Digite o NIF do cliente: ");
                    nif = IN.getNif();
                    try {
                        cliente = controller.consultaNif(nif);
                        if (cliente != null)
                            System.out.println(cliente);
                        else
                            System.out.println(VERMELHO + "Nenhum Cliente foi encontrado!" + RESET);
                    } catch (Exception e) {
                        System.out.println(VERMELHO + "Erro na base de dados!" + RESET);
                    }
                    pausa();
                    break;
                case 4: // buscar por ID
                    System.out.print("Digite o ID do cliente: ");
                    int id = IN.getInt();
                    try {
                        cliente = controller.consultaIdCliente(id);
                        if (cliente != null)
                            System.out.println(cliente);
                        else
                            System.out.println(VERMELHO + "Nenhum Cliente foi encontrado!" + RESET);
                    }
                    catch (Exception e) {
                        System.out.println(VERMELHO + "Erro na base de dados!" + RESET);
                    }
                    pausa();
                    break;
                case 5: // criar cliente
                    adicionarCliente();
                    pausa();
                    break;
                case 6: //alterar cliente
                    alterarCLiente();
                    pausa();
                    break;
                case 7: // remover cliente
                    if (semPermissaoAdmin()){
                        pausa();
                        break;
                    }
                    System.out.print("Digite o NIF do cliente: ");
                    nif = IN.getNif();
                    try {
                        Cliente c = controller.consultaNif(nif);
                        if (c != null) {
                            int resultado = controller.ativaDesativarCliente(c);
                            if (resultado == 1)
                                System.out.println(VERDE + "Cliente reativado com sucesso!" + RESET);
                            else if (resultado == 0)
                                System.out.println(VERDE + "Cliente excluído com sucesso!" + RESET);
                            else
                                System.out.println(VERMELHO + "Sem permissão!" + RESET);
                        } else
                            System.out.println(VERMELHO + "Cliente não encontrado!" + RESET);
                    } catch (SQLException e) {
                        System.out.println(VERMELHO + "Erro na base de dados!" + RESET);
                    }
                    pausa();
                    break;
                case 0:
                    break;
                default:
                    System.out.println(VERMELHO + "\nOpção inválida!" + RESET);
                    pausa();
            }
        }
    }
    private static void adicionarCliente() {
        if (semPermissao("CRIAR_CLIENTE"))
            return;
        System.out.print("Nome: ");
        String nome = IN.getString();
        System.out.print("Morada: ");
        String morada = IN.getString();
        System.out.print("Telefone: ");
        int telefone = IN.getTelefone();
        System.out.print("Email: ");
        String email = IN.getString();
        System.out.print("NIF: ");
        int nif = IN.getNif();
        TipoCliente tipoCliente;
        while (true) {
            System.out.println("Tipo: 1-SINGULAR | 2-COLECTIVO");
            switch (IN.getInt()) {
                case 1: tipoCliente = TipoCliente.SINGULAR; break;
                case 2: tipoCliente = TipoCliente.EMPRESA; break;
                default:
                    System.out.println(VERMELHO + "Opção inválida!" + RESET);
                    pausa();
                    continue;
            }
            break;
        }
        try {
            if (controller.criarCliente(nome, morada, telefone, email, nif, tipoCliente))
                System.out.println(VERDE + "Cliente criado com sucesso!" + RESET);
            else
                System.out.println(VERMELHO + "Erro ao criar cliente!" + RESET);
        } catch (SQLException e) {
            System.out.println(VERMELHO + "Erro na base de dados!" + RESET);
        }
    }
    private static void alterarCLiente(){
        if (semPermissao("ALTERAR_CLIENTE"))
            return;
        System.out.print("Digite o NIF do cliente que deseja atualizar: ");
        int nif = IN.getNif();
        try {
            Cliente x = controller.consultaNif(nif);
            if (x != null) {
                int opTemp = -1;
                while (opTemp != 0) {
                    limparEcran();
                    Menu.menuAlterarCliente();
                    opTemp = IN.getInt();
                    switch (opTemp) {
                        case 1: // alterar todos os dados
                            System.out.print("Nome: ");
                            String nome = IN.getString();
                            System.out.print("Morada: ");
                            String morada = IN.getString();
                            System.out.print("Telefone: ");
                            int telefone = IN.getTelefone();
                            System.out.print("Email: ");
                            String email = IN.getString();
                            System.out.print("NIF: ");
                            int novoNif = IN.getNif();
                            TipoCliente tipo;
                            while (true) {
                                System.out.println("Tipo: 1-SINGULAR | 2-COLECTIVO");
                                switch (IN.getInt()) {
                                    case 1: tipo = TipoCliente.SINGULAR; break;
                                    case 2: tipo = TipoCliente.EMPRESA; break;
                                    default:
                                        System.out.println(VERMELHO + "Opção inválida!" + RESET);
                                        pausa();
                                        continue;
                                }
                                break;
                            }
                            try {
                                int r = controller.alterarCliente(x, nome, morada, telefone, email, novoNif, tipo);
                                if (r == 1)
                                    System.out.println(VERDE + "Cliente alterado com sucesso!" + RESET);
                                else if
                                    (r == 0) System.out.println(VERMELHO + "Cliente não encontrado!" + RESET);
                                else
                                    System.out.println(VERMELHO + "Sem permissão!" + RESET);
                            } catch (SQLException e) {
                                System.out.println(VERMELHO + "Erro na base de dados!" + RESET);
                            }
                            pausa();
                            break;
                        case 2: // alterar nome
                            System.out.print("Nome: ");
                            int r = controller.alteraDadosCliente(x, "NOME", IN.getString());
                            if (r == 1)
                                System.out.println(VERDE + "Nome atualizado com sucesso!" + RESET);
                            else
                                System.out.println(VERMELHO + "Erro ao atualizar!" + RESET);
                            pausa();
                            break;
                        case 3: // alterar NIF
                            System.out.print("NIF: ");
                            int novoN = IN.getNif();
                            Cliente cNif = controller.consultaNif(novoN);
                            if (cNif != null)
                                System.out.println(VERMELHO + "NIF já cadastrado!" + RESET);
                            else {
                                int res = controller.alteraDadosCliente(x, "NIF", novoN);
                                if (res == 1)
                                    System.out.println(VERDE + "NIF atualizado com sucesso!" + RESET);
                                else
                                    System.out.println(VERMELHO + "Erro ao atualizar!" + RESET);
                            }
                            pausa();
                            break;
                        case 4: // alterar morada
                            System.out.print("Morada: ");
                            if (controller.alteraDadosCliente(x, "MORADA", IN.getString()) == 1)
                                System.out.println(VERDE + "Morada atualizada com sucesso!" + RESET);
                            else
                                System.out.println(VERMELHO + "Erro ao atualizar!" + RESET);
                            pausa();
                            break;
                        case 5: // alterar telefone
                            System.out.print("Telefone: ");
                            if (controller.alteraDadosCliente(x, "TELEFONE", IN.getTelefone()) == 1)
                                System.out.println(VERDE + "Telefone atualizado com sucesso!" + RESET);
                            else
                                System.out.println(VERMELHO + "Erro ao atualizar!" + RESET);
                            pausa();
                            break;
                        case 6: // alterar email
                            System.out.print("Email: ");
                            if (controller.alteraDadosCliente(x, "EMAIL", IN.getString()) == 1)
                                System.out.println(VERDE + "Email atualizado com sucesso!" + RESET);
                            else
                                System.out.println(VERMELHO + "Erro ao atualizar!" + RESET);
                            pausa();
                            break;
                        case 7: // alterar tipo cliente
                            TipoCliente tipoC;
                            while (true) {
                                System.out.println("Tipo: 1-SINGULAR | 2-COLECTIVO");
                                switch (IN.getInt()) {
                                    case 1: tipoC = TipoCliente.SINGULAR; break;
                                    case 2: tipoC = TipoCliente.EMPRESA; break;
                                    default:
                                        System.out.println(VERMELHO + "Opção inválida!" + RESET);
                                        pausa();
                                        continue;
                                }
                                break;
                            }
                            if (controller.alteraDadosCliente(x, "TIPO", tipoC) == 1)
                                System.out.println(VERDE + "Tipo atualizado com sucesso!" + RESET);
                            else
                                System.out.println(VERMELHO + "Erro ao atualizar!" + RESET);
                            pausa();
                            break;
                        case 0:
                            break;
                        default:
                            System.out.println(VERMELHO + "Opção inválida!" + RESET);
                            pausa();
                    }
                }
            } else {
                System.out.println(VERMELHO + "Nenhum cliente encontrado." + RESET);
            }
        } catch (SQLException e) {
            System.out.println(VERMELHO + "Erro na base de dados!" + RESET);
        }
    }
    // logica do processo (comum entre admin e utilizadores)
    private static void subMenuProcesso() {
        Processo processo;
        ArrayList<Processo> lista;
        EstadoProcesso status = null;
        int nif = 0;
        int opcao = -1;
        while (opcao != 0) {
            limparEcran();
            Menu.menuProcessos();
            opcao = IN.getInt();
            switch (opcao) {
                case 1: // listar por estado
                    boolean voltar = false;
                    while (true) {
                        limparEcran();
                        Menu.menuEstadoProcesso();
                        switch (IN.getInt()) {
                            case 1:
                                status = EstadoProcesso.DISTRIBUIDO;
                                break;
                            case 2:
                                status = EstadoProcesso.EM_CURSO;
                                break;
                            case 3:
                                status = EstadoProcesso.SUSPENSO;
                                break;
                            case 4:
                                status = EstadoProcesso.FINALIZADO;
                                break;
                            case 5:
                                status = EstadoProcesso.ARQUIVADO;
                                break;
                            case 0:
                                voltar = true;
                                break;
                            default:
                                System.out.println(VERMELHO + "Opção inválida!" + RESET);
                                pausa();
                                continue;
                        }
                        break;
                    }
                    if (voltar)
                        break;
                    try {
                        lista = controller.listarProcessoPorEstado(status);
                        if (lista == null || lista.isEmpty())
                            System.out.println("Sem processo cadastrado para esse estado !");
                        else
                            imprimirProcessos(lista);
                    } catch (Exception e) {
                        System.out.println(VERMELHO + "Erro na base de dados!" + RESET);
                    }
                    pausa();
                    break;
                case 2: // listar processo por clientes
                    System.out.print("Digite o NIF do cliente: ");
                    nif = IN.getNif();
                    try {
                        Cliente c = controller.consultaNif(nif);
                        if (c != null) {
                            lista = controller.listarProcessoPorCliente(c);
                            if (lista == null || lista.isEmpty())
                                System.out.println("Sem processo cadastrado para esse cliente !");
                            else
                                imprimirProcessos(lista);
                        }
                    }catch(Exception e){
                            System.out.println(VERMELHO + "Erro na base de dados!" + RESET);
                    }
                    pausa();
                    break;
                case 3: //listar por adv
                    System.out.print("ID do advogado: ");
                    int idAdv = IN.getInt();
                    try {
                        lista = controller.listarProcessoPorAdvogado(idAdv);
                        if (lista == null || lista.isEmpty())
                            System.out.println(VERMELHO + "Sem processo atribuídos a esse advogado" + RESET);
                        else
                            imprimirProcessos(lista);
                    } catch (Exception e) {
                        System.out.println(VERMELHO + "Erro na base de dados!" + RESET);
                    }
                    pausa();
                    break;
                case 4: // buscar por ID do processo
                    System.out.print("Digite o ID do processo: ");
                    int id = IN.getInt();
                    try {
                        processo = controller.consultaIdProcesso(id);
                        if (processo != null)
                            System.out.println(processo);
                        else
                            System.out.println(VERMELHO + "Nenhum processo foi encontrado!" + RESET);
                    } catch (Exception e) {
                        System.out.println(VERMELHO + "Erro na base de dados!" + RESET);
                    }
                    pausa();
                    break;
                case 5: // criar processp
                    adicionarProcesso();
                    pausa();
                    break;
                case 6: //alterar estado do processo
                    if (semPermissao("ALTERAR_PROCESSO")) {
                        pausa();
                        break;
                    }
                    System.out.print("ID do processo: ");
                    int idProc = IN.getInt();
                    try {
                        Processo p = controller.consultaIdProcesso(idProc);
                        if (p != null) {
                            boolean voltar1 = false;
                            while (true) {
                                limparEcran();
                                Menu.menuEstadoProcesso();
                                switch (IN.getInt()) {
                                    case 1:
                                        status = EstadoProcesso.DISTRIBUIDO;
                                        break;
                                    case 2:
                                        status = EstadoProcesso.EM_CURSO;
                                        break;
                                    case 3:
                                        status = EstadoProcesso.SUSPENSO;
                                        break;
                                    case 4:
                                        status = EstadoProcesso.FINALIZADO;
                                        break;
                                    case 5:
                                        status = EstadoProcesso.ARQUIVADO;
                                        break;
                                    case 0:
                                        voltar1 = true;
                                        break;
                                    default:
                                        System.out.println(VERMELHO + "Opção inválida!" + RESET);
                                        pausa();
                                        continue;
                                }
                                break;
                            }
                            if (voltar1)
                                break;
                            if (controller.alterarEstadoProcesso(p, status))
                                System.out.println(VERDE + "Estado alterado com sucesso!" + RESET);
                            else
                                System.out.println(VERMELHO + "Transição de estado inválida!" + RESET);
                        } else
                            System.out.println(VERMELHO + "Processo não encontrado!" + RESET);
                    } catch (SQLException e) {
                        System.out.println(VERMELHO + "Erro na base de dados!" + RESET);
                    }
                    pausa();
                    break;
                case 0:
                    break;
                default:
                    System.out.println(VERMELHO + "\nOpção inválida!" + RESET);
                    pausa();
            }
        }
    }
    private static void adicionarProcesso() {
        if (semPermissao("CRIAR_PROCESSO"))
            return;
        System.out.print("Número do processo: ");
        String numProcesso = IN.getString();
        System.out.print("Nome da acção: ");
        String nomeAcao = IN.getString();
        EstadoProcesso estado;
        while (true) {
            limparEcran();
            Menu.menuEstadoProcesso();
            switch (IN.getInt()) {
                case 1: estado = EstadoProcesso.DISTRIBUIDO; break;
                case 2: estado = EstadoProcesso.EM_CURSO; break;
                case 3: estado = EstadoProcesso.SUSPENSO; break;
                case 4: estado = EstadoProcesso.FINALIZADO; break;
                case 5: estado = EstadoProcesso.ARQUIVADO; break;
                case 0: return;
                default:
                    System.out.println(VERMELHO + "Opção inválida!" + RESET);
                    pausa();
                    continue;
            }
            break;
        }
        System.out.print("Data distribuição (AAAA/MM/DD): ");
        LocalDate dataDistribuicao = IN.getLocalDate();
        System.out.print("NIF do cliente: ");
        int nif = IN.getNif();
        System.out.print("Username do advogado: ");
        String usernameAdv = IN.getString();
        try {
            switch (controller.criarProcesso(numProcesso, nomeAcao, estado, nif, usernameAdv, dataDistribuicao)) {
                case 1: System.out.println(VERDE + "Processo criado com sucesso!" + RESET); break;
                case 0: System.out.println(VERMELHO + "Cliente não encontrado!" + RESET); break;
                case 2: System.out.println(VERMELHO + "Advogado não encontrado!" + RESET); break;
                case -1: System.out.println(VERMELHO + "Sem permissão!" + RESET); break;
            }
        } catch (SQLException e) {
            System.out.println(VERMELHO + "Erro na base de dados!" + RESET);
        }
    }
    // tarefas (comum entre utilizadores e admim)
    private static void subMenuTarefa() {
        boolean isAdmin = controller.getUtilizadorLogado().isAdmin();
        ArrayList<Tarefa> lista;
        int opcao = -1;
        while (opcao != 0) {
            limparEcran();
            if (isAdmin)
                Menu.menuTarefasAdmin();
            else
                Menu.menuTarefasUtilizador();
            opcao = IN.getInt();
            if (isAdmin) {
                switch (opcao) {
                    case 1: //listar todas
                        listarTarefas();
                        pausa();
                        break;
                    case 2: //listar por utilizador
                        System.out.print("ID do advogado: ");
                        int idAdv = IN.getInt();
                        try {
                            lista = controller.listarTarefaPorUtilizador(idAdv);
                            if (lista == null || lista.isEmpty())
                                System.out.println(VERMELHO + "Sem processo atribuídos a esse advogado" + RESET);
                            else
                                imprimirTarefas(lista);
                        } catch (Exception e) {
                            System.out.println(VERMELHO + "Erro na base de dados!" + RESET);
                        }
                        pausa();
                        break;
                    case 3: //listar por estado
                        listarEstadoTarefa();
                        pausa();
                        break;
                    case 4: //listar por processo
                        listarTarefaPorProcesso();
                        pausa();
                        break;
                    case 5: //criar tarefa
                        adicionarTarefa();
                        pausa();
                        break;
                    case 6: //alterar estado
                        alterarEstadoTarefa();
                        pausa();
                        break;
                    case 0:
                        break;
                    default:
                        System.out.println(VERMELHO + "Opção inválida!" + RESET);
                        pausa();
                }

            } else {
                switch (opcao) {
                    case 1: //listar minhas tarefas
                        listarTarefas();
                        pausa();
                        break;
                    case 2: //listar por estado
                        listarEstadoTarefa();
                        pausa();
                        break;
                    case 3: //listar por processo
                        listarTarefaPorProcesso();
                        pausa();
                        break;
                    case 4: //criar tarefa
                        adicionarTarefa();
                        pausa();
                        break;
                    case 5: //alterar estado
                        alterarEstadoTarefa();
                        pausa();
                        break;
                    case 0:
                        break;
                    default:
                        System.out.println(VERMELHO + "Opção inválida!" + RESET);
                        pausa();
                }
            }
        }
    }
    private static void listarTarefas() {
        ArrayList<Tarefa> lista;
        try {
            lista = controller.listarTarefaPorResp();
            if (lista == null || lista.isEmpty())
                System.out.println(VERMELHO + "Sem tarefas cadastradas" + RESET);
            else
                imprimirTarefas(lista);
        } catch (Exception e) {
            System.out.println(VERMELHO + "Erro na base de dados!" + RESET);
        }
    }
    private static void listarEstadoTarefa() {
        EstadoTarefa estado;
        ArrayList<Tarefa> lista;
        while (true) {
            limparEcran();
            Menu.menuEstadoTarefa();
            switch (IN.getInt()) {
                case 1:
                    estado = EstadoTarefa.PENDENTE;
                    break;
                case 2:
                    estado = EstadoTarefa.EM_CURSO;
                    break;
                case 3:
                    estado = EstadoTarefa.CONCLUIDA;
                    break;
                case 4:
                    estado = EstadoTarefa.CANCELADA;
                    break;
                case 0: return;
                default:
                    System.out.println(VERMELHO + "Opção inválida!" + RESET);
                    pausa();
                    continue;
            }
            break;
        }
        try {
            lista = controller.listarTarefaPorEstado(estado);
            if (lista == null || lista.isEmpty())
                System.out.println("Sem tarefas com esse estado!");
            else
                imprimirTarefas(lista);
        } catch (Exception e) {
            System.out.println(VERMELHO + "Erro na base de dados!" + RESET);
        }
    }
    private static void listarTarefaPorProcesso(){
        System.out.print("ID do processo: ");
        int idProcesso = IN.getInt();
        ArrayList<Tarefa> lista;
        try {
            lista = controller.listarTarefaPorProcesso(idProcesso);
            if (lista == null || lista.isEmpty())
                System.out.println(VERMELHO + "Nenhum processo com tarefas" + RESET);
            else
                imprimirTarefas(lista);
        } catch (Exception e) {
            System.out.println(VERMELHO + "Erro na base de dados!" + RESET);
        }
    }
    private static void alterarEstadoTarefa(){
        if (semPermissao("ALTERAR_TAREFA"))
            return;
        System.out.print("ID da tarefa: ");
        int idTarefa = IN.getInt();
        EstadoTarefa estado;
        try {
            Tarefa t = controller.consultaIdTarefa(idTarefa);
            if (t != null) {
                while (true) {
                    limparEcran();
                    Menu.menuEstadoTarefa();
                    switch (IN.getInt()) {
                        case 1:
                            estado = EstadoTarefa.PENDENTE;
                            break;
                        case 2:
                            estado = EstadoTarefa.EM_CURSO;
                            break;
                        case 3:
                            estado = EstadoTarefa.CONCLUIDA;
                            break;
                        case 4:
                            estado = EstadoTarefa.CANCELADA;
                            break;
                        case 0: return;
                        default:
                            System.out.println(VERMELHO + "Opção inválida!" + RESET);
                            pausa();
                            continue;
                    }
                    break;
                }
                if (controller.alterarEstadoTarefa(t, estado))
                    System.out.println(VERDE + "Estado alterado com sucesso!" + RESET);
                else
                    System.out.println(VERMELHO + "Transição de estado inválida!" + RESET);
            }
            else
                System.out.println(VERMELHO + "Tarefa não encontrado!" + RESET);
        } catch (SQLException e) {
            System.out.println(VERMELHO + "Erro na base de dados!" + RESET);
        }
    }
    private static void adicionarTarefa() {
        if (semPermissao("CRIAR_TAREFA"))
            return;
        System.out.print("Título: ");
        String titulo = IN.getString();
        System.out.print("Descrição: ");
        String descricao = IN.getString();
        Prioridade prioridade;
        while (true) {
            Menu.menuPrioridade();
            switch (IN.getInt()) {
                case 1: prioridade = Prioridade.ALTA; break;
                case 2: prioridade = Prioridade.MEDIA; break;
                case 3: prioridade = Prioridade.BAIXA; break;
                default:
                    System.out.println(VERMELHO + "Opção inválida!" + RESET);
                    pausa();
                    continue;
            }
            break;
        }
        EstadoTarefa estado;
        while (true) {
            Menu.menuEstadoTarefa();
            switch (IN.getInt()) {
                case 1: estado = EstadoTarefa.PENDENTE; break;
                case 2: estado = EstadoTarefa.EM_CURSO; break;
                case 3: estado = EstadoTarefa.CONCLUIDA; break;
                case 4: estado = EstadoTarefa.CANCELADA; break;
                default:
                    System.out.println(VERMELHO + "Opção inválida!" + RESET);
                    pausa();
                    continue;
            }
            break;
        }
        System.out.print("Data limite (AAAA/MM/DD): ");
        LocalDate dataLimite = IN.getLocalDate();
        System.out.print("ID do processo: ");
        int idProcesso = IN.getInt();
        System.out.print("Username do responsável: ");
        String username = IN.getString();
        try {
            switch (controller.criarTarefa(titulo, descricao, prioridade, estado, dataLimite, idProcesso, username)) {
                case 1: System.out.println(VERDE + "Tarefa criada com sucesso!" + RESET); break;
                case 0: System.out.println(VERMELHO + "Processo não encontrado!" + RESET); break;
                case 2: System.out.println(VERMELHO + "Responsável não encontrado!" + RESET); break;
                case -1: System.out.println(VERMELHO + "Sem permissão!" + RESET); break;
            }
        } catch (SQLException e) {
            System.out.println(VERMELHO + "Erro na base de dados!" + RESET);
        }
    }
        // agenda admin e utilizadores
    private static void subMenuAgenda() {
        boolean isAdmin = controller.getUtilizadorLogado().isAdmin();
        ArrayList<Agenda> lista;
        int opcao = -1;
        while (opcao != 0) {
            limparEcran();
            if (isAdmin)
                Menu.menuAgendaAdmin();
            else
                Menu.menuAgendaUtilizador();
            opcao = IN.getInt();
            if (isAdmin) {
                switch (opcao) {
                    case 1:
                        listarAgenda();
                        pausa();
                        break;
                    case 2:
                        System.out.print("ID do advogado: ");
                        int idAdv = IN.getInt();
                        try {
                            lista = controller.listarAgendaPorUtilizador(idAdv);

                            if (lista == null || lista.isEmpty())
                                System.out.println(VERMELHO + "Sem prazos atribuídos a esse advogado" + RESET);
                            else
                                imprimirAgenda(lista);
                        } catch (Exception e) {
                            System.out.println(VERMELHO + "Erro na base de dados!" + RESET);
                        }
                        pausa();
                        break;
                    case 3:
                        listarAgendaPorProcesso();
                        pausa();
                        break;
                    case 4:
                        listarEstadoAgenda();
                        pausa();
                        break;
                    case 5:
                        adicionarAgenda();
                        pausa();
                        break;
                    case 6:
                        alterarEstadoAgenda();
                        pausa();
                        break;
                    case 0:
                        break;
                    default:
                        System.out.println(VERMELHO + "Opção inválida!" + RESET);
                        pausa();
                }
            } else {
                switch (opcao) {
                    case 1:
                        listarAgenda();
                        pausa();
                        break;
                    case 2:
                        listarAgendaPorProcesso();
                        pausa();
                        break;
                    case 3:
                        listarEstadoAgenda();
                        pausa();
                        break;
                    case 4:
                        adicionarAgenda();
                        pausa();
                        break;
                    case 5:
                        alterarEstadoAgenda();
                        pausa();
                        break;
                    case 0:
                        break;
                    default:
                        System.out.println(VERMELHO + "Opção inválida!" + RESET);
                        pausa();
                }
            }
        }
    }
    private static void listarAgenda() {
        ArrayList<Agenda> lista;
        try {
            lista = controller.listarAgendaPorResp();
            if (lista == null || lista.isEmpty())
                System.out.println(VERMELHO + "Sem prazos cadastradas" + RESET);
            else
                imprimirAgenda(lista);
        } catch (Exception e) {
            System.out.println(VERMELHO + "Erro na base de dados!" + RESET);
        }
    }
    private static void adicionarAgenda() {
        if (semPermissao("CRIAR_AGENDA"))
            return;
        System.out.print("Título: ");
        String titulo = IN.getString();
        System.out.print("Local: ");
        String local = IN.getString();
        System.out.print("Data (AAAA/MM/DD): ");
        LocalDateTime dataHora = IN.getLocalDate().atStartOfDay();
        System.out.print("ID do processo: ");
        int idProcesso = IN.getInt();
        System.out.print("Username do utilizador: ");
        String username = IN.getString();
        try {
            switch (controller.criarAgenda(titulo, local, dataHora, idProcesso, username)) {
                case 1: System.out.println(VERDE + "Evento criado com sucesso!" + RESET); break;
                case 0: System.out.println(VERMELHO + "Processo não encontrado!" + RESET); break;
                case 2: System.out.println(VERMELHO + "Utilizador não encontrado!" + RESET); break;
                case -1: System.out.println(VERMELHO + "Sem permissão!" + RESET); break;
            }
        } catch (SQLException e) {
            System.out.println(VERMELHO + "Erro na base de dados!" + RESET);
        }
    }
    private static void listarAgendaPorProcesso(){
        System.out.print("ID do processo: ");
        int idProcesso = IN.getInt();
        ArrayList<Agenda> lista;
        try {
            lista = controller.listarAgendaPorProcesso(idProcesso);
            if (lista == null || lista.isEmpty())
                System.out.println(VERMELHO + "Nenhum evento" + RESET);
            else
                imprimirAgenda(lista);
        } catch (Exception e) {
            System.out.println(VERMELHO + "Erro na base de dados!" + RESET);
        }
    }
    private static void alterarEstadoAgenda(){
        if (semPermissao("ALTERAR_AGENDA"))
            return;
        System.out.print("ID do prazo: ");
        int idAgenda = IN.getInt();
        EstadoAgenda estado;
        try {
            Agenda a = controller.consultaIdAgenda(idAgenda);
            if (a != null) {
                while (true) {
                    limparEcran();
                    Menu.menuEstadoAgenda();
                    switch (IN.getInt()) {
                        case 1:
                            estado = EstadoAgenda.AGENDADO;
                            break;
                        case 2:
                            estado = EstadoAgenda.CONCLUIDO;
                            break;
                        case 3:
                            estado = EstadoAgenda.CANCELADO;
                            break;
                        case 0: return;
                        default:
                            System.out.println(VERMELHO + "Opção inválida!" + RESET);
                            pausa();
                            continue;
                    }
                    break;
                }
                if (controller.alterarEstadoAgenda(a, estado))
                    System.out.println(VERDE + "Estado alterado com sucesso!" + RESET);
                else
                    System.out.println(VERMELHO + "Transição de estado inválida!" + RESET);
            }
            else
                System.out.println(VERMELHO + "prazo não encontrado!" + RESET);
        } catch (SQLException e) {
            System.out.println(VERMELHO + "Erro na base de dados!" + RESET);
        }
    }
    private static void listarEstadoAgenda() {
        EstadoAgenda estado;
        ArrayList<Agenda> lista;
        while (true) {
            limparEcran();
            Menu.menuEstadoAgenda();
            switch (IN.getInt()) {
                case 1:
                    estado = EstadoAgenda.AGENDADO;
                    break;
                case 2:
                    estado = EstadoAgenda.CONCLUIDO;
                    break;
                case 3:
                    estado = EstadoAgenda.CANCELADO;
                    break;
                case 0: return;
                default:
                    System.out.println(VERMELHO + "Opção inválida!" + RESET);
                    pausa();
                    continue;
            }
            break;
        }
        try {
            lista = controller.listarAgendaPorEstado(estado);
            if (lista == null || lista.isEmpty())
                System.out.println("Sem agenda com esse estado!");
            else
                imprimirAgenda(lista);
        } catch (Exception e) {
            System.out.println(VERMELHO + "Erro na base de dados!" + RESET);
        }
    }
    private static void limparEcran(){
        for(int i=0; i < 20; i++)
            System.out.println('\n');
    }
    private static void pausa() {
        System.out.println("\nPressione ENTER para continuar...");
        IN.getEnter();
    }

    private static boolean semPermissao(String acao) {
        if (!controller.getUtilizadorLogado().temPermissao(acao)) {
            System.out.println(VERMELHO + "Sem permissão!" + RESET);
            return true;
        }
        return false;
    }
    private static boolean semPermissaoAdmin() {
        if (!controller.getUtilizadorLogado().isAdmin()) {
            System.out.println(VERMELHO + "Sem permissão!" + RESET);
            return true;
        }
        return false;
    }
    private static void imprimirUtilizadores(ArrayList<Utilizador> lista) {
        if (lista == null || lista.isEmpty()) {
            System.out.println(VERMELHO + "Sem utilizadores para mostrar." + RESET);
            return;
        }
        System.out.println(Utilizador.cabecalho());
        for (Utilizador u : lista)
            System.out.println(u);
        System.out.println("+-----+------------------------+--------------+------------+---------------+-------+------------+");
    }

    private static void imprimirClientes(ArrayList<Cliente> lista) {
        if (lista == null || lista.isEmpty()) {
            System.out.println(VERMELHO + "Sem clientes para mostrar." + RESET);
            return;
        }
        System.out.println(Cliente.cabecalho());
        for (Cliente c : lista)
            System.out.println(c);
        System.out.println("+-----+------------------------+------------+---------------------------+-----------+------------+-------+");
    }

    private static void imprimirProcessos(ArrayList<Processo> lista) {
        if (lista == null || lista.isEmpty()) {
            System.out.println(VERMELHO + "Sem processos para mostrar." + RESET);
            return;
        }
        System.out.println(Processo.cabecalho());
        for (Processo p : lista)
            System.out.println(p);
        System.out.println("+-----+--------------------+---------------------------+--------------+----------------------+--------------------+--------------+");
    }

    private static void imprimirTarefas(ArrayList<Tarefa> lista) {
        if (lista == null || lista.isEmpty()) {
            System.out.println(VERMELHO + "Sem tarefas para mostrar." + RESET);
            return;
        }
        System.out.println(Tarefa.cabecalho());
        for (Tarefa t : lista)
            System.out.println(t);
        System.out.println("+-----+------------------------+-----------+------------+--------------+--------------------+--------------------+");
    }

    private static void imprimirAgenda(ArrayList<Agenda> lista) {
        if (lista == null || lista.isEmpty()) {
            System.out.println(VERMELHO + "Sem eventos para mostrar." + RESET);
            return;
        }
        System.out.println(Agenda.cabecalho());
        for (Agenda a : lista)
            System.out.println(a);
        System.out.println("+-----+------------------------+------------------+---------------------------+------------+--------------------+--------------------+");
    }

}