package crmjuridico.database;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexaoDB
{
    private String db = "database/crm.db";

    public Connection getConexao() throws SQLException {
        File dbFolder = new File("database");
        if (!dbFolder.exists())
            dbFolder.mkdirs();
        return DriverManager.getConnection("jdbc:sqlite:" + db);
    }
}

