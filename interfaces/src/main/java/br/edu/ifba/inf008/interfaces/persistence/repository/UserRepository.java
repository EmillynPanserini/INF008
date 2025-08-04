package br.edu.ifba.inf008.interfaces.persistence.repository;

import br.edu.ifba.inf008.interfaces.persistence.entity.UserEntity; // Importar sua entidade JPA
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Integer> { // UserEntity e tipo de ID

    UserEntity findByEmail(String email);
    void deleteByEmail(String email);

    List<UserEntity> findByNameContainingIgnoreCase(String name);
    List<UserEntity> findByEmailContainingIgnoreCase(String email);

}