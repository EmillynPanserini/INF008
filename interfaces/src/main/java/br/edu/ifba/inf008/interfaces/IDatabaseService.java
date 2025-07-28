package br.edu.ifba.inf008.interfaces;

import java.sql.Connection;
import java.sql.SQLException;

public interface IDatabaseService {
    Connection getConnection() throws SQLException;

}
