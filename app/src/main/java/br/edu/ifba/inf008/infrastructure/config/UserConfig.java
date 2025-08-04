package br.edu.ifba.inf008.infrastructure.config;

import br.edu.ifba.inf008.application.usecase.LoginUseCase;
import br.edu.ifba.inf008.application.service.LoginUseCaseImpl;
import br.edu.ifba.inf008.interfaces.persistence.repository.UserRepository; // ADICIONADO: Importar UserRepository
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.beans.factory.annotation.Autowired; // ADICIONADO: Importar @Autowired

@Configuration
public class UserConfig {


}