package br.edu.ifba.inf008.application.service;

import br.edu.ifba.inf008.interfaces.IDatabaseService;
import org.springframework.stereotype.Service; // Importar @Service (ou @Component)

import java.sql.Connection;
import java.sql.SQLException;

@Service
public class DatabaseService implements IDatabaseService {
    @Override
    public Connection getConnection() throws SQLException {
        return null;
    }
}