package br.edu.ifba.inf008.service;

import br.edu.ifba.inf008.interfaces.IDatabaseService;

import java.sql.Connection;
import java.sql.SQLException;

public class DatabaseService implements IDatabaseService {
    @Override
    public Connection getConnection() throws SQLException {
        return null;
    }
}
