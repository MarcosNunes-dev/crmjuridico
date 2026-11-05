package crmjuridico.viewGUI;

import crmjuridico.controller.GestaoEscritorio;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class LoginController {

    @FXML private TextField usernameField;
    @FXML private PasswordField passField;
    @FXML private Button loginButton;

    private GestaoEscritorio controller = GestaoEscritorio.getInstance();

    @FXML
    private void handleLogin() {
        String username = usernameField.getText();
        String pass = passField.getText();

        if (controller.login(username, pass)) {
            Stage stage = (Stage) loginButton.getScene().getWindow(); // pega a janela atual(login) atraves do obj botão e fecha a janela
            stage.close();

            String ecranFXML;
            // com base no nivel do usuário escolhe qual view abrir
            switch (controller.getUtilizadorLogado().getNivelAcesso()) {
                case ADMINISTRADOR:
                    ecranFXML = "/crmjuridico/admin-view.fxml";
                    break;
                case ADVOGADO:
                    ecranFXML = "/crmjuridico/advogado-view.fxml";
                    break;
                case ESTAGIARIO:
                    ecranFXML = "/crmjuridico/estagiario-view.fxml";
                    break;
                default:
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Erro de Login");
                    alert.setHeaderText(null);
                    alert.setContentText("Nivel de acesso desconhecido!");
                    alert.showAndWait();
                    return;
            }
            try {
                FXMLLoader carregar = new FXMLLoader(getClass().getResource(ecranFXML)); // carrega o xml escolhido no case
                Stage novoStage = new Stage();                      // cria a nova janela
                novoStage.setScene(new Scene(carregar.load()));     // exibe o conteudo nessa nova janela
                novoStage.setMaximized(true); // maximiza a janela por defeito quando abre
                novoStage.show();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erro de Login");
            alert.setHeaderText(null);
            alert.setContentText("Username ou password incorrectos!");
            alert.showAndWait();
        }
    }
}