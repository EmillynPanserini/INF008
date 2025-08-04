package br.edu.ifba.inf008.application.service;

import br.edu.ifba.inf008.application.usecase.LoginUseCase;
import br.edu.ifba.inf008.interfaces.models.LoginDto;
import br.edu.ifba.inf008.interfaces.models.User;
import br.edu.ifba.inf008.interfaces.persistence.entity.UserEntity;
import br.edu.ifba.inf008.interfaces.persistence.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LoginUseCaseImpl implements LoginUseCase {

    private final UserRepository userRepository;

    @Autowired
    public LoginUseCaseImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public boolean authenticate(String email) {
        UserEntity userEntity = userRepository.findByEmail(email);

        if (userEntity != null) {
            return true;
        }
        return false;
    }

    @Override
    public User execute(LoginDto input) {
        return null;
    }
}