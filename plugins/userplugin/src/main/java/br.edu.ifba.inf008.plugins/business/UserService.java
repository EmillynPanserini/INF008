package br.edu.ifba.inf008.plugins.business;

import br.edu.ifba.inf008.interfaces.persistence.entity.UserEntity; // A entidade JPA (agora em interfaces/persistence/entity)
import br.edu.ifba.inf008.exception.UserNotFoundException; // Sua exceção customizada
import br.edu.ifba.inf008.interfaces.models.User;
import br.edu.ifba.inf008.interfaces.persistence.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class UserService {

    private final UserRepository userRepository;
    // Construtor para injeção de dependência pelo Spring
    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // --- Métodos CRUD ---

    // Cria um novo usuário
    public User createUser(User user) {
        UserEntity userEntity = mapUserToUserEntity(user);
        userEntity.setRegisteredAt(LocalDateTime.now());

        userEntity = userRepository.save(userEntity);
        return mapEntityToUser(userEntity);
    }

    public User updateUser(User user) {
        UserEntity existingUser = userRepository.findById(Integer.parseInt(user.getId()))
                .orElseThrow(() -> new UserNotFoundException("User  not found: " + user.getId()));

        existingUser.setName(user.getName());
        existingUser.setEmail(user.getEmail());

        UserEntity updatedEntity = userRepository.save(existingUser);
        return mapEntityToUser(updatedEntity);
    }

    // Deleta um usuário
    public boolean deleteUser(Integer userId) { // userId é Integer, conforme UserRepository
        try {

            // Chamada dos  métodos do serviço de empréstimos/livros.
            // Para isso, LoanService e BookService precisariam ser injetados aqui.
            // Ex: @Autowired private LoanService loanService;
            //     @Autowired private BookService bookService;

            // Lógica para finalizar empréstimos e atualizar cópias (adaptada do seu código original):
            // Isso assumiria que LoanService e BookService são injetados aqui.
            /*
            List<Loan> activeLoans = loanService.findByUserIdWithDetails(userId);
            List<Loan> unreturnedLoans = activeLoans.stream()
                .filter(loan -> loan.getReturnDate() == null)
                .toList();
            if (!unreturnedLoans.isEmpty()) {
                for (Loan loan : unreturnedLoans) {
                    Book book = loan.getBook();
                    if (book != null) {
                        book.setCopiesAvailable(book.getCopiesAvailable() + 1);
                        bookService.updateBook(book); // Supondo que BookService tem um update
                    }
                    loanService.deleteLoan(loan.getLoanId()); // Supondo que LoanService tem um delete
                }
            }
            */

            userRepository.deleteById(userId);
            return true;
        } catch (Exception e) {
            System.err.println("Error user not removed " + userId + ": " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    // --- Métodos de Busca ---

    public List<User> getAllUsers() {
        return userRepository.findAll().stream()
                .map(this::mapEntityToUser)
                .collect(Collectors.toList());
    }

    public User getUserById(String id) { // ID é String na UI
        return userRepository.findById(Integer.parseInt(id)) // Converte String para Integer para o repositório
                .map(this::mapEntityToUser) // Mapeia se encontrar
                .orElse(null); // Retorna null se não encontrar
    }

    public List<User> findUsersByName(String name) {
        return userRepository.findByNameContainingIgnoreCase(name).stream()
                .map(this::mapEntityToUser)
                .collect(Collectors.toList());
    }

    public List<User> findUsersByEmail(String email) {
        return userRepository.findByEmailContainingIgnoreCase(email).stream()
                .map(this::mapEntityToUser)
                .collect(Collectors.toList());
    }



    // --- Lógica de Empréstimos Ativos (Adaptação do seu código original) ---
    // NOTA: Para que este metodo funcione, o UserService precisaria injetar LoanRepository e BookRepository.
    // Ou esta lógica de negócio deveria estar em um LoanService.
    /*
    public String getActiveLoansWarning(Integer userId) {
        // Isso assume que LoanService e BookService são injetados aqui ou acessíveis.
        // List<Loan> activeLoans = loanService.findByUserIdWithDetails(userId);
        // List<Loan> unreturnedLoans = activeLoans.stream()
        //     .filter(loan -> loan.getReturnDate() == null)
        //     .toList();

        // StringBuilder warning = new StringBuilder();
        // warning.append("ATENÇÃO: O usuário possui ").append(unreturnedLoans.size()).append(" empréstimo(s) ativo(s):\n\n");
        // ... (restante da lógica de formatação da mensagem)
        // return warning.toString();
        return null; // Retorno temporário
    }
    */
    public String getActiveLoansWarning(Integer userId) {
        // Este metodo dependeria da injeção de LoanRepository e BookRepository e LoanService/BookService.
        // Estou retornando null para evitar erros de compilação sem essas injeções.
        return null;
    }


    // --- Métodos de Mapeamento (Conversão entre Entidade JPA e Modelo JavaFX) ---

    private User mapEntityToUser(UserEntity entity) {
        if (entity == null) return null;
        return new User.Builder(
                entity.getUserId() != null ? entity.getUserId().toString() : null,
                entity.getName(),
                entity.getEmail(),
                entity.getRegisteredAt() != null ? entity.getRegisteredAt().toLocalDate() : null // LocalDateTime para LocalDate
        ).build();
    }

    // Converte User (modelo JavaFX DTO) para UserEntity (entidade JPA)
    private UserEntity mapUserToUserEntity(User user) {
        if (user == null) return null;
        return UserEntity.builder()
                .userId(user.getId() != null ? Integer.parseInt(user.getId()) : null)
                .name(user.getName())
                .email(user.getEmail())
                .registeredAt(user.getRegistrationDate() != null ? user.getRegistrationDate().atStartOfDay() : null)
                .build();
    }
}