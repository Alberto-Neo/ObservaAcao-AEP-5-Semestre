package org.aep.observacao.service;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public final class DatabaseManager {
    private static final String DB_DIRECTORY = "persistence";
    private static final String JDBC_URL_FILE = "jdbc:h2:./" + DB_DIRECTORY + "/observacao-db;DATABASE_TO_UPPER=false";
    private static final String JDBC_URL_MEMORY = "jdbc:h2:mem:observacao-test;DB_CLOSE_DELAY=-1;DATABASE_TO_UPPER=false";
    private static final String USER = "sa";
    private static final String PASSWORD = "";
    private static boolean initialized = false;

    private DatabaseManager() {
    }

    private static boolean isTestMode() {
        for (StackTraceElement element : Thread.currentThread().getStackTrace()) {
            if (element.getClassName().contains("junit") || element.getClassName().contains("Test")) {
                return true;
            }
        }
        return false;
    }

    private static String getJdbcUrl() {
        return isTestMode() ? JDBC_URL_MEMORY : JDBC_URL_FILE;
    }

    public static synchronized void initializeDatabase() {
        if (initialized) {
            return;
        }
        if (!isTestMode()) {
            createDataDirectory();
        }
        try (Connection connection = getConnection()) {
            createTables(connection);
            initialized = true;
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao inicializar o banco de dados H2.", e);
        }
    }

    private static void createDataDirectory() {
        File dataFolder = new File(DB_DIRECTORY);
        if (!dataFolder.exists() && !dataFolder.mkdirs()) {
            throw new RuntimeException("Não foi possível criar a pasta de persistência.");
        }
    }

    private static void createTables(Connection connection) throws SQLException {
        try (Statement statement = connection.createStatement()) {
            statement.execute("""
                    CREATE TABLE IF NOT EXISTS solicitacao (
                        id INT PRIMARY KEY,
                        protocolo VARCHAR(32) NOT NULL,
                        categoria VARCHAR(100) NOT NULL,
                        categoria_sla INT NOT NULL,
                        descricao VARCHAR(1000) NOT NULL,
                        localizacao VARCHAR(255) NOT NULL,
                        prioridade VARCHAR(32) NOT NULL,
                        status VARCHAR(32) NOT NULL,
                        data_criacao VARCHAR(64) NOT NULL,
                        usuario_id INT,
                        usuario_nome VARCHAR(255),
                        usuario_email VARCHAR(255),
                        usuario_telefone VARCHAR(64),
                        anonimo BOOLEAN NOT NULL
                    )
                    """);

            statement.execute("""
                    CREATE TABLE IF NOT EXISTS historico_status (
                        id INT PRIMARY KEY,
                        solicitacao_id INT NOT NULL,
                        status VARCHAR(32) NOT NULL,
                        data VARCHAR(64) NOT NULL,
                        responsavel VARCHAR(255) NOT NULL,
                        comentario VARCHAR(1000) NOT NULL
                    )
                    """);
        }
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(getJdbcUrl(), USER, PASSWORD);
    }
}
