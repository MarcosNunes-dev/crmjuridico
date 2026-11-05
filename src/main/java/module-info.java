module crmjuridico.crmjuridico {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires org.xerial.sqlitejdbc;

    opens crmjuridico to javafx.fxml;
    opens crmjuridico.controller to javafx.fxml;
    opens crmjuridico.model.enums to javafx.fxml;
    opens crmjuridico.model.prazo to javafx.fxml;
    opens crmjuridico.model.processo to javafx.fxml;
    opens crmjuridico.model.tarefa to javafx.fxml;
    opens crmjuridico.model.utilizador to javafx.fxml;
    opens crmjuridico.viewGUI to javafx.fxml;

    exports crmjuridico.viewGUI;
    exports crmjuridico.controller;
    exports crmjuridico.viewConsola;
    opens crmjuridico.viewConsola to javafx.fxml;
}