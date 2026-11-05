package crmjuridico.viewGUI;

import crmjuridico.controller.GestaoEscritorio;
import crmjuridico.model.enums.EstadoAgenda;
import crmjuridico.model.enums.EstadoProcesso;
import crmjuridico.model.enums.EstadoTarefa;
import crmjuridico.model.prazo.Agenda;
import crmjuridico.model.processo.Cliente;
import crmjuridico.model.processo.Processo;
import crmjuridico.model.tarefa.Tarefa;
import crmjuridico.model.utilizador.Advogado;
import crmjuridico.model.utilizador.Utilizador;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;


public class AdminController implements Initializable {
    @FXML
    private Label nomeUtilizadorLabel;
    @FXML
    private Label tituloSecao;
    @FXML
    private Button btnAdicionar;
    @FXML
    private Button btnEditar;
    @FXML
    private Button btnAtivarDesativar;
    @FXML
    private FlowPane dashboardPane;
    @FXML
    private Label cardUtilizadores;
    @FXML
    private Label cardClientes;
    @FXML
    private Label cardProcessos;
    @FXML
    private Label cardTarefas;
    @FXML
    private Label cardAgenda;
    @FXML
    private HBox togglePane;
    @FXML
    private ToggleButton toggleAtivos;
    @FXML
    private ToggleButton toggleInativos;
    @FXML
    private HBox filtroEstadoPane;
    @FXML
    private ComboBox comboEstado;
    @FXML
    private TableView tabela;
    @FXML
    private HBox painelPesquisa;
    @FXML
    private ComboBox<String> comboPesquisa;
    @FXML
    private TextField campoPesquisa;
    private GestaoEscritorio controller = GestaoEscritorio.getInstance();
    private String secaoActiva = "dashboard";

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        nomeUtilizadorLabel.setText(controller.getUtilizadorLogado().getNome());
        mostrarDashboard();
        // executado automaticamente pelo JavaFX assim que a tela (FXML) é carregada.
        // Ele serve para preparar os dados e os componentes visuais antes da tela aparecer para o usuário.
    }

    @FXML
    public void mostrarDashboard() {
        secaoActiva = "dashboard";
        tituloSecao.setText("Dashboard");
        tabela.setVisible(false);
        togglePane.setVisible(false);
        filtroEstadoPane.setVisible(false);
        painelPesquisa.setVisible(false);
        btnAdicionar.setVisible(false);
        btnEditar.setVisible(false);
        btnAtivarDesativar.setVisible(false);
        dashboardPane.setVisible(true);
        try {
            cardUtilizadores.setText(String.valueOf(controller.listarUtilizadorAtivos().size()));
            cardClientes.setText(String.valueOf(controller.contarClientesAtivos()));
            cardProcessos.setText(String.valueOf(controller.contarProcessosAtivos()));
            cardTarefas.setText(String.valueOf(controller.contarTarefasPendentes()));
            cardAgenda.setText(String.valueOf(controller.contarAgendaHoje()));
        } catch (SQLException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erro");
            alert.setHeaderText(null);
            alert.setContentText("Erro ao carregar dashboard!");
            alert.showAndWait();
        }
    }

    @FXML
    public void mostrarClientes() { // ativa a tela de cliente com os campos em true
        secaoActiva = "clientes";
        tituloSecao.setText("Clientes");
        dashboardPane.setVisible(false);
        filtroEstadoPane.setVisible(false);
        togglePane.setVisible(true);
        tabela.setVisible(true);
        btnAdicionar.setVisible(true);
        btnEditar.setVisible(true);
        btnAtivarDesativar.setVisible(true);
        toggleAtivos.setSelected(true);
        toggleInativos.setSelected(false);
        tabela.getColumns().clear();
        painelPesquisa.setVisible(true);
        comboPesquisa.getItems().clear();
        comboPesquisa.getItems().addAll("Nif", "ID");
        comboPesquisa.setValue("Nif");

        // O JavaFX vai rodar um ciclo 'for' internamente para cada Cliente da sua lista,
        // aplicando esta fórmula (lambda) para preencher a coluna 'ID' de linha em linha.

        TableColumn<Cliente, String> colId = new TableColumn<>("ID");
        colId.setCellValueFactory(cadaCliente -> new SimpleStringProperty(String.valueOf(cadaCliente.getValue().getId())));

        TableColumn<Cliente, String> colNome = new TableColumn<>("NOME");
        colNome.setCellValueFactory(cadaCliente -> new SimpleStringProperty(cadaCliente.getValue().getNome()));

        TableColumn<Cliente, String> colNif = new TableColumn<>("NIF");
        colNif.setCellValueFactory(cadaCliente -> new SimpleStringProperty(String.valueOf(cadaCliente.getValue().getNif())));

        TableColumn<Cliente, String> colTelefone = new TableColumn<>("TELEFONE");
        colTelefone.setCellValueFactory(cadaCliente -> new SimpleStringProperty(String.valueOf(cadaCliente.getValue().getTelefone())));

        TableColumn<Cliente, String> colEmail = new TableColumn<>("E-MAIL");
        colEmail.setCellValueFactory(cadaCliente -> new SimpleStringProperty(cadaCliente.getValue().getEmail()));

        TableColumn<Cliente, String> colTipo = new TableColumn<>("TIPO CLIENTE");
        colTipo.setCellValueFactory(cadaCliente -> new SimpleStringProperty(String.valueOf(cadaCliente.getValue().getTipoCliente())));

        tabela.getColumns().addAll(colId, colNome, colNif, colTelefone, colEmail, colTipo);

        carregarClientesAtivos();

    }

    private void carregarClientesAtivos() { // atualiza a tabela automaticamente
        try {
            ObservableList<Cliente> lista = FXCollections.observableArrayList(); // atualiza automaticamente sempre que um cliente for adicionado, removido ou alterado.
            lista.addAll(controller.listarClientesAtivos());
            tabela.setItems(lista);
        } catch (SQLException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erro");
            alert.setHeaderText(null);
            alert.setContentText("Erro ao carregar clientes!");
            alert.showAndWait();
        }
    }

    private void carregarClientesInativos() {
        try {
            ObservableList<Cliente> lista = FXCollections.observableArrayList();
            lista.addAll(controller.listarClientesInativos());
            tabela.setItems(lista);
        } catch (SQLException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erro");
            alert.setHeaderText(null);
            alert.setContentText("Erro ao carregar clientes inativos!");
            alert.showAndWait();
        }
    }

    @FXML
    private void handleToggleAtivos() { // alterna entre ativo e inativo, carregando a lista correta
        toggleAtivos.setStyle("-fx-background-color: #2C3E50;");
        toggleInativos.setStyle("-fx-background-color: #BDC3C7;");
        if (secaoActiva.equals("clientes"))
            carregarClientesAtivos();
        else if (secaoActiva.equals("utilizadores"))
            carregarUtilizadoresAtivos();
    }

    @FXML
    private void handleToggleInativos() {
        toggleAtivos.setStyle("-fx-background-color: #BDC3C7;");
        toggleInativos.setStyle("-fx-background-color: #2C3E50;");
        if (secaoActiva.equals("clientes"))
            carregarClientesInativos();
        else if (secaoActiva.equals("utilizadores"))
            carregarUtilizadoresInativos();
    }

    @FXML
    public void mostrarUtilizadores() {
        secaoActiva = "utilizadores";
        tituloSecao.setText("Utilizadores");
        dashboardPane.setVisible(false);
        filtroEstadoPane.setVisible(false);
        togglePane.setVisible(true);
        tabela.setVisible(true);
        btnAdicionar.setVisible(true);
        btnEditar.setVisible(true);
        btnAtivarDesativar.setVisible(true);
        toggleAtivos.setSelected(true);
        toggleInativos.setSelected(false);
        tabela.getColumns().clear();
        painelPesquisa.setVisible(true);
        comboPesquisa.getItems().clear();
        comboPesquisa.getItems().addAll("Username");
        comboPesquisa.setValue("Username");
        TableColumn<Utilizador, String> colId = new TableColumn<>("ID");
        colId.setCellValueFactory(cadaUtilizador -> new SimpleStringProperty(String.valueOf(cadaUtilizador.getValue().getId())));

        TableColumn<Utilizador, String> colNome = new TableColumn<>("NOME");
        colNome.setCellValueFactory(cadaUtilizador -> new SimpleStringProperty(cadaUtilizador.getValue().getNome()));

        TableColumn<Utilizador, String> colUser = new TableColumn<>("USERNAME");
        colUser.setCellValueFactory(cadaUtilizador -> new SimpleStringProperty(cadaUtilizador.getValue().getUsername()));

        TableColumn<Utilizador, String> colNif = new TableColumn<>("NIF");
        colNif.setCellValueFactory(cadaUtilizador -> new SimpleStringProperty(String.valueOf(cadaUtilizador.getValue().getNif())));

        TableColumn<Utilizador, String> colDataAD = new TableColumn<>("DATA ADMISSÃO");
        colDataAD.setCellValueFactory(cadaUtilizador -> new SimpleStringProperty(String.valueOf(cadaUtilizador.getValue().getDataAdmissao())));

        TableColumn<Utilizador, String> colDataDM = new TableColumn<>("DATA DEMISSÃO");
        colDataDM.setCellValueFactory(cadaUtilizador -> new SimpleStringProperty(String.valueOf(cadaUtilizador.getValue().getDataDemissao() != null ? cadaUtilizador.getValue().getDataDemissao() : "---")));

        TableColumn<Utilizador, String> colTipo = new TableColumn<>("CARGO");
        colTipo.setCellValueFactory(cadaUtilizador -> new SimpleStringProperty(String.valueOf(cadaUtilizador.getValue().getNivelAcesso())));

        TableColumn<Utilizador, String> colDoc = new TableColumn<>("CÉDULA PROFISSIONAL");
        colDoc.setCellValueFactory(cadaUtilizador -> {
            Utilizador u = cadaUtilizador.getValue();
            if (u instanceof Advogado) {
                Advogado adv = (Advogado) u;
                return new SimpleStringProperty(adv.getCedulaProfissional());
            }
            return new SimpleStringProperty("---");
        });

        tabela.getColumns().addAll(colId, colNome, colUser, colNif, colDataAD, colDataDM, colTipo, colDoc);

        carregarUtilizadoresAtivos();

    }

    private void carregarUtilizadoresAtivos() {
        try {
            ObservableList<Utilizador> lista = FXCollections.observableArrayList();
            lista.addAll(controller.listarUtilizadorAtivos());
            tabela.setItems(lista);
        } catch (SQLException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erro");
            alert.setHeaderText(null);
            alert.setContentText("Erro ao carregar utilizadores!");
            alert.showAndWait();
        }
    }

    private void carregarUtilizadoresInativos() {
        try {
            ObservableList<Utilizador> lista = FXCollections.observableArrayList();
            lista.addAll(controller.listarUtilizadorInativos());
            tabela.setItems(lista);
        } catch (SQLException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erro");
            alert.setHeaderText(null);
            alert.setContentText("Erro ao carregar utilizadores inativos!");
            alert.showAndWait();
        }
    }

    @FXML
    public void mostrarProcessos() {
        secaoActiva = "processos";
        tituloSecao.setText("Processos");
        dashboardPane.setVisible(false);
        togglePane.setVisible(false);
        filtroEstadoPane.setVisible(true);
        tabela.setVisible(true);
        btnAdicionar.setVisible(true);
        btnEditar.setVisible(true);
        btnAtivarDesativar.setVisible(false);
        comboEstado.getItems().clear();
        comboEstado.getItems().addAll(EstadoProcesso.values());
        comboEstado.setValue(EstadoProcesso.EM_CURSO);
        tabela.getColumns().clear();
        painelPesquisa.setVisible(true);
        comboPesquisa.getItems().clear();
        comboPesquisa.getItems().addAll("Cliente", "Advogado");
        comboPesquisa.setValue("Cliente");

        TableColumn<Processo, String> colId = new TableColumn<>("ID");
        colId.setCellValueFactory(p -> new SimpleStringProperty(String.valueOf(p.getValue().getId())));

        TableColumn<Processo, String> colNum = new TableColumn<>("Nº PROCESSO");
        colNum.setCellValueFactory(p -> new SimpleStringProperty(p.getValue().getNumProcesso()));

        TableColumn<Processo, String> colAcao = new TableColumn<>("ACÇÃO");
        colAcao.setCellValueFactory(p -> new SimpleStringProperty(p.getValue().getNomeAcao()));

        TableColumn<Processo, String> colCliente = new TableColumn<>("CLIENTE");
        colCliente.setCellValueFactory(p -> new SimpleStringProperty(p.getValue().getCliente().getNome()));

        TableColumn<Processo, String> colAdvogado = new TableColumn<>("ADVOGADO");
        colAdvogado.setCellValueFactory(p -> new SimpleStringProperty(p.getValue().getAdvogado().getNome()));

        TableColumn<Processo, String> colEstado = new TableColumn<>("ESTADO");
        colEstado.setCellValueFactory(p -> new SimpleStringProperty(p.getValue().getEstado().name()));

        TableColumn<Processo, String> colData = new TableColumn<>("DATA DISTRIBUIÇÃO");
        colData.setCellValueFactory(p -> new SimpleStringProperty(p.getValue().getDataDistribuicao().toString()));

        tabela.getColumns().addAll(colId, colNum, colAcao, colCliente, colAdvogado, colEstado, colData);

        carregarProcessosPorEstado(EstadoProcesso.EM_CURSO);
    }

    private void carregarProcessosPorEstado(EstadoProcesso estado) {
        try {
            ObservableList<Processo> lista = FXCollections.observableArrayList();
            lista.addAll(controller.listarProcessoPorEstado(estado));
            tabela.setItems(lista);
        } catch (SQLException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erro");
            alert.setHeaderText(null);
            alert.setContentText("Erro ao carregar processos!");
            alert.showAndWait();
        }
    }

    @FXML
    private void handleFiltroEstado() { // campo (tipo select) para pesquisas por estado
        if (secaoActiva.equals("processos")) {
            EstadoProcesso estadoP = (EstadoProcesso) comboEstado.getValue();
            carregarProcessosPorEstado(estadoP);
        } else if (secaoActiva.equals("tarefas")) {
            EstadoTarefa estadoT = (EstadoTarefa) comboEstado.getValue();
            carregarTarefaPorEstado(estadoT);
        } else if (secaoActiva.equals("agenda")) {
            EstadoAgenda estadoA = (EstadoAgenda) comboEstado.getValue();
            carregarAgendaPorEstado(estadoA);
        }
    }

    @FXML
    public void mostrarTarefas() {
        secaoActiva = "tarefas";
        tituloSecao.setText("Tarefas");
        dashboardPane.setVisible(false);
        togglePane.setVisible(false);
        filtroEstadoPane.setVisible(true);
        tabela.setVisible(true);
        btnAdicionar.setVisible(true);
        btnEditar.setVisible(true);
        btnAtivarDesativar.setVisible(false);
        comboEstado.getItems().clear();
        comboEstado.getItems().addAll(EstadoTarefa.values());
        comboEstado.setValue(EstadoTarefa.EM_CURSO);
        tabela.getColumns().clear();
        painelPesquisa.setVisible(true);
        comboPesquisa.getItems().clear();
        comboPesquisa.getItems().addAll("Processo", "Utilizador");
        comboPesquisa.setValue("Processo");

        TableColumn<Tarefa, String> colId = new TableColumn<>("ID");
        colId.setCellValueFactory(t -> new SimpleStringProperty(String.valueOf(t.getValue().getId())));

        TableColumn<Tarefa, String> colTitulo = new TableColumn<>("TÍTULO");
        colTitulo.setCellValueFactory(t -> new SimpleStringProperty(t.getValue().getTitulo()));

        TableColumn<Tarefa, String> colDesc = new TableColumn<>("DESCRIÇÃO");
        colDesc.setCellValueFactory(t -> new SimpleStringProperty(t.getValue().getDescricao()));

        TableColumn<Tarefa, String> colPrioridade = new TableColumn<>("PRIORIDADE");
        colPrioridade.setCellValueFactory(t -> new SimpleStringProperty(t.getValue().getPrioridade().name()));

        TableColumn<Tarefa, String> colEstado = new TableColumn<>("ESTADO");
        colEstado.setCellValueFactory(t -> new SimpleStringProperty(t.getValue().getEstado().name()));

        TableColumn<Tarefa, String> colData = new TableColumn<>("DATA LIMITE");
        colData.setCellValueFactory(t -> new SimpleStringProperty(t.getValue().getDataLimite().toString()));

        TableColumn<Tarefa, String> colProcesso = new TableColumn<>("PROCESSO");
        colProcesso.setCellValueFactory(t -> new SimpleStringProperty(t.getValue().getProcesso().getNumProcesso()));

        TableColumn<Tarefa, String> colResp = new TableColumn<>("RESPONSÁVEL");
        colResp.setCellValueFactory(t -> new SimpleStringProperty(t.getValue().getResponsavel().getNome()));

        tabela.getColumns().addAll(colId, colTitulo, colDesc, colPrioridade, colEstado, colData, colProcesso, colResp);

        carregarTarefaPorEstado(EstadoTarefa.EM_CURSO);
    }

    private void carregarTarefaPorEstado(EstadoTarefa estado) {
        try {
            ObservableList<Tarefa> lista = FXCollections.observableArrayList();
            lista.addAll(controller.listarTarefaPorEstado(estado));
            tabela.setItems(lista);
        } catch (SQLException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erro");
            alert.setHeaderText(null);
            alert.setContentText("Erro ao carregar tarefas!");
            alert.showAndWait();
        }
    }

    @FXML
    public void mostrarAgenda() {
        secaoActiva = "agenda";
        tituloSecao.setText("Agenda");
        dashboardPane.setVisible(false);
        togglePane.setVisible(false);
        filtroEstadoPane.setVisible(true);
        tabela.setVisible(true);
        btnAdicionar.setVisible(true);
        btnEditar.setVisible(true);
        btnAtivarDesativar.setVisible(false);
        comboEstado.getItems().clear();
        comboEstado.getItems().addAll(EstadoAgenda.values());
        comboEstado.setValue(EstadoAgenda.AGENDADO);
        tabela.getColumns().clear();
        painelPesquisa.setVisible(true);
        comboPesquisa.getItems().clear();
        comboPesquisa.getItems().addAll("Processo", "Utilizador");
        comboPesquisa.setValue("Processo");

        TableColumn<Agenda, String> colId = new TableColumn<>("ID");
        colId.setCellValueFactory(a -> new SimpleStringProperty(String.valueOf(a.getValue().getId())));

        TableColumn<Agenda, String> colTitulo = new TableColumn<>("TÍTULO");
        colTitulo.setCellValueFactory(a -> new SimpleStringProperty(a.getValue().getTitulo()));

        TableColumn<Agenda, String> colUti = new TableColumn<>("RESPONSÁVEL");
        colUti.setCellValueFactory(a -> new SimpleStringProperty(a.getValue().getUtilizador().getNome()));

        TableColumn<Agenda, String> colData = new TableColumn<>("DATA e HORÁRIO");
        colData.setCellValueFactory(a -> new SimpleStringProperty(a.getValue().getDataHora().toString()));

        TableColumn<Agenda, String> colProcesso = new TableColumn<>("PROCESSO");
        colProcesso.setCellValueFactory(a -> new SimpleStringProperty(a.getValue().getProcesso().getNumProcesso()));

        TableColumn<Agenda, String> colLocal = new TableColumn<>("LOCAL");
        colLocal.setCellValueFactory(a -> new SimpleStringProperty(a.getValue().getLocal()));

        TableColumn<Agenda, String> colEstado = new TableColumn<>("ESTADO");
        colEstado.setCellValueFactory(a -> new SimpleStringProperty(a.getValue().getEstadoAgenda().name()));

        tabela.getColumns().addAll(colId, colTitulo, colUti, colEstado, colData, colLocal, colProcesso);

        carregarAgendaPorEstado(EstadoAgenda.AGENDADO);
    }

    private void carregarAgendaPorEstado(EstadoAgenda estado) {
        try {
            ObservableList<Agenda> lista = FXCollections.observableArrayList();
            lista.addAll(controller.listarAgendaPorEstado(estado));
            tabela.setItems(lista);
        } catch (SQLException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erro");
            alert.setHeaderText(null);
            alert.setContentText("Erro ao carregar agenda!");
            alert.showAndWait();
        }
    }

    @FXML
    private void handlePesquisar() { // campo de pesquisa diversas
        String tipo = (String) comboPesquisa.getValue();
        String valor = campoPesquisa.getText();

        if (valor.isEmpty()) {
            return;
        }
        switch (secaoActiva) {
            case "utilizadores":
                try {
                    Utilizador u = controller.consultaUtilizadorPorUser(valor);
                    ObservableList<Utilizador> lista = FXCollections.observableArrayList();
                    if (u != null)
                        lista.add(u);
                    tabela.setItems(lista);
                } catch (NumberFormatException e) {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Aviso");
                    alert.setHeaderText(null);
                    alert.setContentText("Introduza um número válido!");
                    alert.showAndWait();
                }
                catch (SQLException e) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Erro");
                    alert.setHeaderText(null);
                    alert.setContentText("Erro ao pesquisar utilizador!");
                    alert.showAndWait();
                }
                break;
            case "clientes":
                if (tipo.equals("Nif")) {
                    try {
                        Cliente c = controller.consultaNif(Integer.parseInt(valor));
                        ObservableList<Cliente> lista = FXCollections.observableArrayList();
                        if (c != null)
                            lista.add(c);
                        tabela.setItems(lista);
                    } catch (NumberFormatException e) {
                        Alert alert = new Alert(Alert.AlertType.WARNING);
                        alert.setTitle("Aviso");
                        alert.setHeaderText(null);
                        alert.setContentText("Introduza um número válido!");
                        alert.showAndWait();
                    }
                    catch (SQLException e) {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Erro");
                        alert.setHeaderText(null);
                        alert.setContentText("Erro ao pesquisar cliente!");
                        alert.showAndWait();
                    }
                } else if (tipo.equals("ID")) {
                    try {
                        Cliente c = controller.consultaIdCliente(Integer.parseInt(valor));
                        ObservableList<Cliente> lista = FXCollections.observableArrayList();
                        if (c != null)
                            lista.add(c);
                        tabela.setItems(lista);
                    } catch (NumberFormatException e) {
                        Alert alert = new Alert(Alert.AlertType.WARNING);
                        alert.setTitle("Aviso");
                        alert.setHeaderText(null);
                        alert.setContentText("Introduza um número válido!");
                        alert.showAndWait();
                    }
                    catch (SQLException e) {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Erro");
                        alert.setHeaderText(null);
                        alert.setContentText("Erro ao pesquisar cliente!");
                        alert.showAndWait();
                    }
                }
                break;
            case "processos":
                try {
                    ObservableList<Processo> lista = FXCollections.observableArrayList();
                    if (tipo.equals("Cliente")) {
                        Cliente c = controller.consultaNif(Integer.parseInt(valor));
                        if (c != null)
                            lista.addAll(controller.listarProcessoPorCliente(c));
                    } else if (tipo.equals("Advogado")) {
                        lista.addAll(controller.listarProcessoPorAdvogado(Integer.parseInt(valor)));
                    }
                    tabela.setItems(lista);
                } catch (NumberFormatException e) {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Aviso");
                    alert.setHeaderText(null);
                    alert.setContentText("Introduza um número válido!");
                    alert.showAndWait();
                }
                catch (SQLException e) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Erro");
                    alert.setHeaderText(null);
                    alert.setContentText("Erro ao pesquisar cliente!");
                    alert.showAndWait();
                }
                break;
            case "tarefas":
                try {
                    ObservableList<Tarefa> lista = FXCollections.observableArrayList();
                    if (tipo.equals("Processo"))
                        lista.addAll(controller.listarTarefaPorProcesso(Integer.parseInt(valor)));
                    else if (tipo.equals("Utilizador"))
                        lista.addAll(controller.listarTarefaPorUtilizador(Integer.parseInt(valor)));
                    tabela.setItems(lista);
                } catch (NumberFormatException e) {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Aviso");
                    alert.setHeaderText(null);
                    alert.setContentText("Introduza um número válido!");
                    alert.showAndWait();
                } catch (SQLException e) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Erro");
                    alert.setHeaderText(null);
                    alert.setContentText("Erro ao pesquisar tarefas!");
                    alert.showAndWait();
                }
                break;

            case "agenda":
                try {
                    ObservableList<Agenda> lista = FXCollections.observableArrayList();
                    if (tipo.equals("Processo"))
                        lista.addAll(controller.listarAgendaPorProcesso(Integer.parseInt(valor)));
                    else if (tipo.equals("Utilizador"))
                        lista.addAll(controller.listarAgendaPorUtilizador(Integer.parseInt(valor)));
                    tabela.setItems(lista);
                } catch (NumberFormatException e) {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Aviso");
                    alert.setHeaderText(null);
                    alert.setContentText("Introduza um número válido!");
                    alert.showAndWait();
                } catch (SQLException e) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Erro");
                    alert.setHeaderText(null);
                    alert.setContentText("Erro ao pesquisar agenda!");
                    alert.showAndWait();
                }
                break;
        }
    }

    @FXML
    public void handleAdicionar() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Em desenvolvimento");
        alert.setHeaderText(null);
        alert.setContentText("Funcionalidade de adição em desenvolvimento.");
        alert.showAndWait();
    }

    @FXML
    public void handleEditar() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Em desenvolvimento");
        alert.setHeaderText(null);
        alert.setContentText("Funcionalidade de edição em desenvolvimento.");
        alert.showAndWait();
    }

    @FXML
    public void handleAtivarDesativar() {
        Object seleccionado = tabela.getSelectionModel().getSelectedItem(); // seleciona a linha
        if (seleccionado == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Aviso");
            alert.setHeaderText(null);
            alert.setContentText("Seleccione um registo primeiro!");
            alert.showAndWait();
            return;
        }
        try {
            if (secaoActiva.equals("clientes")) {
                Cliente c = (Cliente) seleccionado;
                int resultado = controller.ativaDesativarCliente(c);
                if (resultado == 1)
                    new Alert(Alert.AlertType.INFORMATION, "Cliente activado!").showAndWait();
                else if (resultado == 0)
                    new Alert(Alert.AlertType.INFORMATION, "Cliente desactivado!").showAndWait();
                else
                    new Alert(Alert.AlertType.ERROR, "Sem permissão!").showAndWait();
                carregarClientesAtivos();

            } else if (secaoActiva.equals("utilizadores")) {
                Utilizador u = (Utilizador) seleccionado;
                int resultado = controller.ativaDesativarUtilizador(u);
                if (resultado == 1)
                    new Alert(Alert.AlertType.INFORMATION, "Utilizador activado!").showAndWait();
                else if (resultado == 0)
                    new Alert(Alert.AlertType.INFORMATION, "Utilizador desactivado!").showAndWait();
                else
                    new Alert(Alert.AlertType.ERROR, "Sem permissão!").showAndWait();
                carregarUtilizadoresAtivos();
            }
        } catch (SQLException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erro");
            alert.setHeaderText(null);
            alert.setContentText("Erro na base de dados!");
            alert.showAndWait();
        }
    }

    @FXML
    public void handleLogout() {
        try {
            controller.logout();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/crmjuridico/login-view.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) nomeUtilizadorLabel.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        // chama a função de logout, loarder carrega a pagina de login, stage.setScene susbstituiu a pagina atual pela a de login

    }
    @FXML
    private void handleLimparPesquisa() { // botão de limpeza e recarrega a listagem por defeito
        campoPesquisa.clear();
        switch (secaoActiva) {
            case "utilizadores":
                carregarUtilizadoresAtivos();
                break;
            case "clientes":
                carregarClientesAtivos();
                break;
            case "processos":
                carregarProcessosPorEstado((EstadoProcesso) comboEstado.getValue());
                break;
            case "tarefas":
                carregarTarefaPorEstado((EstadoTarefa) comboEstado.getValue());
                break;
            case "agenda":
                carregarAgendaPorEstado((EstadoAgenda) comboEstado.getValue());
                break;
        }
    }
}