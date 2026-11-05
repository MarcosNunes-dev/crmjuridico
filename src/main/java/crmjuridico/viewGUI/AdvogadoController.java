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
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

public class AdvogadoController implements Initializable {

    @FXML private Label nomeUtilizadorLabel;
    @FXML private Label tituloSecao;
    @FXML private Button btnAdicionar;
    @FXML private Button btnEditar;
    @FXML private FlowPane dashboardPane;
    @FXML private Label cardProcessos;
    @FXML private Label cardTarefas;
    @FXML private Label cardAgenda;
    @FXML private HBox filtroEstadoPane;
    @FXML private ComboBox comboEstado;
    @FXML private HBox painelPesquisa;
    @FXML private ComboBox<String> comboPesquisa;
    @FXML private TextField campoPesquisa;
    @FXML private TableView tabela;
    private GestaoEscritorio controller = GestaoEscritorio.getInstance();
    private String secaoActiva = "dashboard";

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        nomeUtilizadorLabel.setText(controller.getUtilizadorLogado().getNome());
        mostrarDashboard();
    }

    @FXML
    public void mostrarDashboard() {
        secaoActiva = "dashboard";
        tituloSecao.setText("Dashboard");

        tabela.setVisible(false);
        filtroEstadoPane.setVisible(false);
        painelPesquisa.setVisible(false);
        btnAdicionar.setVisible(false);
        btnEditar.setVisible(false);
        dashboardPane.setVisible(true);

        try {
            cardProcessos.setText(String.valueOf(controller.contarProcessosAtivos()));
            cardTarefas.setText(String.valueOf(controller.contarTarefasPendentesPorResp()));
            cardAgenda.setText(String.valueOf(controller.contarAgendaHojePorResp()));
        } catch (SQLException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erro");
            alert.setHeaderText(null);
            alert.setContentText("Erro ao carregar dashboard!");
            alert.showAndWait();
        }
    }

    @FXML
    public void mostrarClientes() {
        secaoActiva = "clientes";
        tituloSecao.setText("Clientes");

        dashboardPane.setVisible(false);
        filtroEstadoPane.setVisible(false);
        tabela.setVisible(true);
        btnAdicionar.setVisible(true);
        btnEditar.setVisible(true);
        painelPesquisa.setVisible(true);
        comboPesquisa.getItems().clear();
        comboPesquisa.getItems().add("Nif");
        comboPesquisa.getItems().add("ID");
        comboPesquisa.setValue("Nif");
        tabela.getColumns().clear();

        // O JavaFX vai rodar um ciclo 'for' internamente para cada Cliente da sua lista,
        // aplicando esta fórmula (lambda) para preencher a coluna 'ID' de linha em linha.

        TableColumn<Cliente, String> colId = new TableColumn<>("ID");
        colId.setCellValueFactory(c -> new SimpleStringProperty(String.valueOf(c.getValue().getId())));

        TableColumn<Cliente, String> colNome = new TableColumn<>("NOME");
        colNome.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getNome()));

        TableColumn<Cliente, String> colNif = new TableColumn<>("NIF");
        colNif.setCellValueFactory(c -> new SimpleStringProperty(String.valueOf(c.getValue().getNif())));

        TableColumn<Cliente, String> colTelefone = new TableColumn<>("TELEFONE");
        colTelefone.setCellValueFactory(c -> new SimpleStringProperty(String.valueOf(c.getValue().getTelefone())));

        TableColumn<Cliente, String> colEmail = new TableColumn<>("E-MAIL");
        colEmail.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getEmail()));

        TableColumn<Cliente, String> colTipo = new TableColumn<>("TIPO");
        colTipo.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getTipoCliente().name()));

        tabela.getColumns().addAll(colId, colNome, colNif, colTelefone, colEmail, colTipo);

        carregarClientesAtivos();
    }

    private void carregarClientesAtivos() {
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

    @FXML
    public void mostrarProcessos() {
        secaoActiva = "processos";
        tituloSecao.setText("Processos");

        dashboardPane.setVisible(false);
        filtroEstadoPane.setVisible(true);
        tabela.setVisible(true);
        btnAdicionar.setVisible(true);
        btnEditar.setVisible(true);
        painelPesquisa.setVisible(true);

        comboEstado.getItems().clear();
        comboEstado.getItems().addAll(EstadoProcesso.values());
        comboEstado.setValue(EstadoProcesso.EM_CURSO);

        comboPesquisa.getItems().clear();
        comboPesquisa.getItems().add("Cliente");
        comboPesquisa.getItems().add("Advogado");
        comboPesquisa.setValue("Cliente");

        tabela.getColumns().clear();

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
    public void mostrarTarefas() {
        secaoActiva = "tarefas";
        tituloSecao.setText("Tarefas");

        dashboardPane.setVisible(false);
        filtroEstadoPane.setVisible(false);
        tabela.setVisible(true);
        btnAdicionar.setVisible(true);
        btnEditar.setVisible(true);
        painelPesquisa.setVisible(false);

        comboEstado.getItems().clear();
        comboEstado.getItems().addAll(EstadoTarefa.values());
        comboEstado.setValue(EstadoTarefa.EM_CURSO);

        comboPesquisa.getItems().clear();
        comboPesquisa.getItems().add("Processo");
        comboPesquisa.getItems().add("Utilizador");
        comboPesquisa.setValue("Processo");

        tabela.getColumns().clear();

        TableColumn<Tarefa, String> colId = new TableColumn<>("ID");
        colId.setCellValueFactory(t -> new SimpleStringProperty(String.valueOf(t.getValue().getId())));

        TableColumn<Tarefa, String> colTitulo = new TableColumn<>("TÍTULO");
        colTitulo.setCellValueFactory(t -> new SimpleStringProperty(t.getValue().getTitulo()));

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

        tabela.getColumns().addAll(colId, colTitulo, colPrioridade, colEstado, colData, colProcesso, colResp);

        carregarTarefasPorResp();
    }

    private void carregarTarefasPorResp() {
        try {
            ObservableList<Tarefa> lista = FXCollections.observableArrayList();
            lista.addAll(controller.listarTarefaPorResp());
            tabela.setItems(lista);
        } catch (SQLException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erro");
            alert.setHeaderText(null);
            alert.setContentText("Erro ao carregar tarefas!");
            alert.showAndWait();
        }
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
        filtroEstadoPane.setVisible(false);
        tabela.setVisible(true);
        btnAdicionar.setVisible(true);
        btnEditar.setVisible(true);
        painelPesquisa.setVisible(false);

        comboEstado.getItems().clear();
        comboEstado.getItems().addAll(EstadoAgenda.values());
        comboEstado.setValue(EstadoAgenda.AGENDADO);

        comboPesquisa.getItems().clear();
        comboPesquisa.getItems().add("Processo");
        comboPesquisa.getItems().add("Utilizador");
        comboPesquisa.setValue("Processo");

        tabela.getColumns().clear();

        TableColumn<Agenda, String> colId = new TableColumn<>("ID");
        colId.setCellValueFactory(a -> new SimpleStringProperty(String.valueOf(a.getValue().getId())));

        TableColumn<Agenda, String> colTitulo = new TableColumn<>("TÍTULO");
        colTitulo.setCellValueFactory(a -> new SimpleStringProperty(a.getValue().getTitulo()));

        TableColumn<Agenda, String> colResp = new TableColumn<>("RESPONSÁVEL");
        colResp.setCellValueFactory(a -> new SimpleStringProperty(a.getValue().getUtilizador().getNome()));

        TableColumn<Agenda, String> colData = new TableColumn<>("DATA e HORÁRIO");
        colData.setCellValueFactory(a -> new SimpleStringProperty(
                a.getValue().getDataHora().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"))));

        TableColumn<Agenda, String> colProcesso = new TableColumn<>("PROCESSO");
        colProcesso.setCellValueFactory(a -> new SimpleStringProperty(a.getValue().getProcesso().getNumProcesso()));

        TableColumn<Agenda, String> colLocal = new TableColumn<>("LOCAL");
        colLocal.setCellValueFactory(a -> new SimpleStringProperty(a.getValue().getLocal()));

        TableColumn<Agenda, String> colEstado = new TableColumn<>("ESTADO");
        colEstado.setCellValueFactory(a -> new SimpleStringProperty(a.getValue().getEstadoAgenda().name()));

        tabela.getColumns().addAll(colId, colTitulo, colResp, colData, colProcesso, colLocal, colEstado);

        carregarAgendaPorResp();
    }

    private void carregarAgendaPorResp() {
        try {
            ObservableList<Agenda> lista = FXCollections.observableArrayList();
            lista.addAll(controller.listarAgendaPorResp());
            tabela.setItems(lista);
        } catch (SQLException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erro");
            alert.setHeaderText(null);
            alert.setContentText("Erro ao carregar agenda!");
            alert.showAndWait();
        }
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
    private void handleFiltroEstado() {
        // recarrega a tabela com o estado seleccionado no combo
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
    private void handlePesquisar() {
        String tipo = (String) comboPesquisa.getValue();
        String valor = campoPesquisa.getText();

        if (valor.isEmpty()) return;

        switch (secaoActiva) {
            case "clientes":
                try {
                    Cliente c = tipo.equals("Nif")
                            ? controller.consultaNif(Integer.parseInt(valor))
                            : controller.consultaIdCliente(Integer.parseInt(valor));
                    ObservableList<Cliente> lista = FXCollections.observableArrayList();
                    if (c != null) lista.add(c);
                    tabela.setItems(lista);
                } catch (NumberFormatException e) {
                    new Alert(Alert.AlertType.WARNING, "Introduza um número válido!").showAndWait();
                } catch (SQLException e) {
                    new Alert(Alert.AlertType.ERROR, "Erro ao pesquisar cliente!").showAndWait();
                }
                break;

            case "processos":
                try {
                    ObservableList<Processo> lista = FXCollections.observableArrayList();
                    if (tipo.equals("Cliente")) {
                        Cliente c = controller.consultaNif(Integer.parseInt(valor));
                        if (c != null) lista.addAll(controller.listarProcessoPorCliente(c));
                    } else if (tipo.equals("Advogado")) {
                        lista.addAll(controller.listarProcessoPorAdvogado(Integer.parseInt(valor)));
                    }
                    tabela.setItems(lista);
                } catch (NumberFormatException e) {
                    new Alert(Alert.AlertType.WARNING, "Introduza um número válido!").showAndWait();
                } catch (SQLException e) {
                    new Alert(Alert.AlertType.ERROR, "Erro ao pesquisar processos!").showAndWait();
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
                    new Alert(Alert.AlertType.WARNING, "Introduza um número válido!").showAndWait();
                } catch (SQLException e) {
                    new Alert(Alert.AlertType.ERROR, "Erro ao pesquisar tarefas!").showAndWait();
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
                    new Alert(Alert.AlertType.WARNING, "Introduza um número válido!").showAndWait();
                } catch (SQLException e) {
                    new Alert(Alert.AlertType.ERROR, "Erro ao pesquisar agenda!").showAndWait();
                }
                break;
        }
    }

    @FXML
    private void handleLimparPesquisa() {
        campoPesquisa.clear();
        switch (secaoActiva) {
            case "clientes": carregarClientesAtivos(); break;
            case "processos": carregarProcessosPorEstado((EstadoProcesso) comboEstado.getValue()); break;
            case "tarefas": carregarTarefasPorResp(); break;
            case "agenda": carregarAgendaPorResp(); break;
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
    }
}
