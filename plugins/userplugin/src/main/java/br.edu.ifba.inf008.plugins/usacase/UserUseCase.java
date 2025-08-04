package br.edu.ifba.inf008.plugins.usacase;

import br.edu.ifba.inf008.interfaces.models.LoginDto;
import br.edu.ifba.inf008.interfaces.models.User;
import br.edu.ifba.inf008.interfaces.usecase.UseCase;

public interface UserUseCase extends UseCase<User, LoginDto> {
}
