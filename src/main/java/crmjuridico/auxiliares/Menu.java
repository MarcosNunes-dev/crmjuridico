package crmjuridico.auxiliares;

public class Menu
{
    public static void menuLogin() {
        System.out.println("+-------------------------------+");
        System.out.println("|             LOGIN             |");
        System.out.println("+-------------------------------+");
        System.out.print("| Username: ");
    }

    public static void menuPrincipalAdmin() {
        System.out.println("+-------------------------------+");
        System.out.println("|        MENU PRINCIPAL         |");
        System.out.println("+-------------------------------+");
        System.out.println("| 1 - Utilizadores              |");
        System.out.println("| 2 - Clientes                  |");
        System.out.println("| 3 - Processos                 |");
        System.out.println("| 4 - Tarefas                   |");
        System.out.println("| 5 - Agenda                    |");
        System.out.println("| 6 - Alterar senha             |");
        System.out.println("| 0 - Logout                    |");
        System.out.println("+-------------------------------+");
        System.out.print("Escolha uma opção: ");
    }

    public static void menuPrincipalUtilizador() {
        System.out.println("+-------------------------------+");
        System.out.println("|        MENU PRINCIPAL         |");
        System.out.println("+-------------------------------+");
        System.out.println("| 1 - Clientes                  |");
        System.out.println("| 2 - Processos                 |");
        System.out.println("| 3 - Tarefas                   |");
        System.out.println("| 4 - Agenda                    |");
        System.out.println("| 5 - Alterar senha             |");
        System.out.println("| 0 - Logout                    |");
        System.out.println("+-------------------------------+");
        System.out.print("Escolha uma opção: ");
    }


    public static void menuUtilizadores() {
        System.out.println("+-------------------------------+");
        System.out.println("|        UTILIZADORES           |");
        System.out.println("+-------------------------------+");
        System.out.println("| 1 - Listar ativos             |");
        System.out.println("| 2 - Listar inativos           |");
        System.out.println("| 3 - Criar utilizador          |");
        System.out.println("| 4 - Alterar utilizador        |");
        System.out.println("| 5 - Ativar/Desativar          |");
        System.out.println("| 0 - Voltar                    |");
        System.out.println("+-------------------------------+");
        System.out.print("Escolha uma opção: ");
    }

    public static void menuAlterarUtilizador() {
        System.out.println("+-------------------------------+");
        System.out.println("|      ALTERAR UTILIZADOR       |");
        System.out.println("+-------------------------------+");
        System.out.println("| 1 - Alterar todos os dados    |");
        System.out.println("| 2 - Alterar nome              |");
        System.out.println("| 3 - Alterar NIF               |");
        System.out.println("| 4 - Alterar data admissão     |");
        System.out.println("| 5 - Alterar data demissão     |");
        System.out.println("| 6 - Alterar nível acesso      |");
        System.out.println("| 0 - Voltar                    |");
        System.out.println("+-------------------------------+");
        System.out.print("Escolha uma opção: ");
    }

    public static void menuClientes() {
        System.out.println("+-------------------------------+");
        System.out.println("|           CLIENTES            |");
        System.out.println("+-------------------------------+");
        System.out.println("| 1 - Listar ativos             |");
        System.out.println("| 2 - Listar inativos           |");
        System.out.println("| 3 - Buscar por NIF            |");
        System.out.println("| 4 - Buscar por ID             |");
        System.out.println("| 5 - Criar cliente             |");
        System.out.println("| 6 - Alterar cliente           |");
        System.out.println("| 7 - Ativar/Desativar          |");
        System.out.println("| 0 - Voltar                    |");
        System.out.println("+-------------------------------+");
        System.out.print("Escolha uma opção: ");
    }

    public static void menuAlterarCliente() {
        System.out.println("+-------------------------------+");
        System.out.println("|       ALTERAR CLIENTE         |");
        System.out.println("+-------------------------------+");
        System.out.println("| 1 - Alterar todos os dados    |");
        System.out.println("| 2 - Alterar nome              |");
        System.out.println("| 3 - Alterar NIF               |");
        System.out.println("| 4 - Alterar morada            |");
        System.out.println("| 5 - Alterar telefone          |");
        System.out.println("| 6 - Alterar email             |");
        System.out.println("| 7 - Alterar tipo cliente      |");
        System.out.println("| 0 - Voltar                    |");
        System.out.println("+-------------------------------+");
        System.out.print("Escolha uma opção: ");
    }

    public static void menuProcessos() {
        System.out.println("+-------------------------------+");
        System.out.println("|           PROCESSOS           |");
        System.out.println("+-------------------------------+");
        System.out.println("| 1 - Listar por estado         |");
        System.out.println("| 2 - Listar por cliente        |");
        System.out.println("| 3 - Listar por advogado       |");
        System.out.println("| 4 - Buscar por ID             |");
        System.out.println("| 5 - Criar processo            |");
        System.out.println("| 6 - Alterar estado            |");
        System.out.println("| 0 - Voltar                    |");
        System.out.println("+-------------------------------+");
        System.out.print("Escolha uma opção: ");
    }

    public static void menuTarefasAdmin() {
        System.out.println("+-------------------------------+");
        System.out.println("|            TAREFAS            |");
        System.out.println("+-------------------------------+");
        System.out.println("| 1 - Listar todas              |");
        System.out.println("| 2 - Listar por utilizador     |");
        System.out.println("| 3 - Listar por estado         |");
        System.out.println("| 4 - Listar por processo       |");
        System.out.println("| 5 - Criar tarefa              |");
        System.out.println("| 6 - Alterar estado            |");
        System.out.println("| 0 - Voltar                    |");
        System.out.println("+-------------------------------+");
        System.out.print("Escolha uma opção: ");
    }

    public static void menuTarefasUtilizador() {
        System.out.println("+-------------------------------+");
        System.out.println("|            TAREFAS            |");
        System.out.println("+-------------------------------+");
        System.out.println("| 1 - Listar minhas tarefas     |");
        System.out.println("| 2 - Listar por estado         |");
        System.out.println("| 3 - Listar por processo       |");
        System.out.println("| 4 - Criar tarefa              |");
        System.out.println("| 5 - Alterar estado            |");
        System.out.println("| 0 - Voltar                    |");
        System.out.println("+-------------------------------+");
        System.out.print("Escolha uma opção: ");
    }

    public static void menuAgendaAdmin() {
        System.out.println("+-------------------------------+");
        System.out.println("|            AGENDA             |");
        System.out.println("+-------------------------------+");
        System.out.println("| 1 - Listar toda agenda        |");
        System.out.println("| 2 - Listar por utilizador     |");
        System.out.println("| 3 - Listar por processo       |");
        System.out.println("| 4 - Listar por estado         |");
        System.out.println("| 5 - Criar evento              |");
        System.out.println("| 6 - Alterar estado            |");
        System.out.println("| 0 - Voltar                    |");
        System.out.println("+-------------------------------+");
        System.out.print("Escolha uma opção: ");
    }

    public static void menuAgendaUtilizador() {
        System.out.println("+-------------------------------+");
        System.out.println("|            AGENDA             |");
        System.out.println("+-------------------------------+");
        System.out.println("| 1 - Listar minha agenda       |");
        System.out.println("| 2 - Listar por processo       |");
        System.out.println("| 3 - Listar por estado         |");
        System.out.println("| 4 - Criar evento              |");
        System.out.println("| 5 - Alterar estado            |");
        System.out.println("| 0 - Voltar                    |");
        System.out.println("+-------------------------------+");
        System.out.print("Escolha uma opção: ");
    }
    public static void menuEstadoProcesso() {
        System.out.println("+-------------------------------+");
        System.out.println("|       ESTADO DO PROCESSO      |");
        System.out.println("+-------------------------------+");
        System.out.println("| 1 - Distribuído               |");
        System.out.println("| 2 - Em curso                  |");
        System.out.println("| 3 - Suspenso                  |");
        System.out.println("| 4 - Finalizado                |");
        System.out.println("| 5 - Arquivado                 |");
        System.out.println("| 0 - Voltar                    |");
        System.out.println("+-------------------------------+");
        System.out.print("Escolha uma opção: ");
    }

    public static void menuEstadoTarefa() {
        System.out.println("+-------------------------------+");
        System.out.println("|       ESTADO DA TAREFA        |");
        System.out.println("+-------------------------------+");
        System.out.println("| 1 - Pendente                  |");
        System.out.println("| 2 - Em curso                  |");
        System.out.println("| 3 - Concluída                 |");
        System.out.println("| 4 - Cancelada                 |");
        System.out.println("| 0 - Voltar                    |");
        System.out.println("+-------------------------------+");
        System.out.print("Escolha uma opção: ");
    }

    public static void menuEstadoAgenda() {
        System.out.println("+-------------------------------+");
        System.out.println("|       ESTADO DA AGENDA        |");
        System.out.println("+-------------------------------+");
        System.out.println("| 1 - Agendado                  |");
        System.out.println("| 2 - Concluído                 |");
        System.out.println("| 3 - Cancelado                 |");
        System.out.println("| 0 - Voltar                    |");
        System.out.println("+-------------------------------+");
        System.out.print("Escolha uma opção: ");
    }
    public static void menuPrioridade() {
        System.out.println("+-------------------------------+");
        System.out.println("|           PRIORIDADE          |");
        System.out.println("+-------------------------------+");
        System.out.println("| 1 - Alta                      |");
        System.out.println("| 2 - Média                     |");
        System.out.println("| 3 - Baixa                     |");
        System.out.println("+-------------------------------+");
        System.out.print("Escolha uma opção: ");
    }
}