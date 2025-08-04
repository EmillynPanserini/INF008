package br.edu.ifba.inf008.application.usecase;

import br.edu.ifba.inf008.interfaces.models.LoginDto;
import br.edu.ifba.inf008.interfaces.models.User;
import br.edu.ifba.inf008.interfaces.usecase.UseCase;


public interface LoginUseCase extends UseCase<LoginDto, User>{
    boolean authenticate(String email);
}
