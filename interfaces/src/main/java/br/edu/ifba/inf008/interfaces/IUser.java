package br.edu.ifba.inf008.interfaces;

import java.util.List;
import java.util.Optional;

import br.edu.ifba.inf008.interfaces.models.User;


public interface IUser {
    User save(User user);

    Optional<User> findById(Integer id);
    List<User> findAll();
    List<User> findByName(String name);
    List<User> findByEmailLike(String email);
    Optional<User> findByEmail(String email);

    User update(User user);

    boolean delete(Integer id);
}