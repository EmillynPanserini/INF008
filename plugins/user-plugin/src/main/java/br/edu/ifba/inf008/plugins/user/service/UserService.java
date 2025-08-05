package br.edu.ifba.inf008.plugins.user.service;

import br.edu.ifba.inf008.plugins.user.dao.UserDAO;
import br.edu.ifba.inf008.interfaces.models.User;

import java.util.List;
import java.util.Optional;

public class UserService {
    private final UserDAO userDAO;
    
    public UserService() {
        this.userDAO = new UserDAO();
    }
    
    public List<User> getAllUsers() {
        return userDAO.findAll();
    }
    
    public Optional<User> getUserById(int id) {
        return userDAO.findById(id);
    }
    
    public List<User> searchUsersByName(String name) {
        if (name == null || name.trim().isEmpty()) {
            return getAllUsers();
        }
        return userDAO.findByNameContaining(name);
    }
    
    public ValidationResult validateUser(User user, boolean isUpdate) {
        ValidationResult result = new ValidationResult();
        
        if (user.getName() == null || user.getName().trim().isEmpty()) {
            result.addError("Name is mandatory");
        } else if (user.getName().trim().length() < 2) {
            result.addError("Name must be at least 2 characters long");
        } else if (user.getName().trim().length() > 50) {
            result.addError("Name must have a maximum of 50 characters");
        }
        
        if (user.getEmail() == null || user.getEmail().trim().isEmpty()) {
            result.addError("Email is mandatory");
        } else if (!isValidEmail(user.getEmail().trim())) {
            result.addError("Email must be in a valid format");
        } else if (user.getEmail().trim().length() > 100) {
            result.addError("Email must have a maximum of 100 characters");
        } else {
            int excludeId = isUpdate ? user.getId() : -1;
            if (userDAO.emailExists(user.getEmail().trim(), excludeId)) {
                result.addError("Email is already in use by another user");
            }
        }
        
        return result;
    }
    
    public boolean createUser(User user) {
        ValidationResult validation = validateUser(user, false);
        if (!validation.isValid()) {
            throw new IllegalArgumentException("Invalid data: " + validation.getErrorsAsString());
        }
        
        return userDAO.insert(user);
    }
    
    public boolean updateUser(User user) {
        ValidationResult validation = validateUser(user, true);
        if (!validation.isValid()) {
            throw new IllegalArgumentException("Invalid data: " + validation.getErrorsAsString());
        }
        return userDAO.update(user);
    }
    
    public boolean deleteUser(int userId) {
        return userDAO.delete(userId);
    }
    
    private boolean isValidEmail(String email) {
        return email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");
    }
    public static class ValidationResult {
        private final List<String> errors;
        
        public ValidationResult() {
            this.errors = new java.util.ArrayList<>();
        }
        
        public void addError(String error) {
            errors.add(error);
        }
        
        public boolean isValid() {
            return errors.isEmpty();
        }
        
        public List<String> getErrors() {
            return new java.util.ArrayList<>(errors);
        }
        
        public String getErrorsAsString() {
            return String.join(", ", errors);
        }
    }
}
