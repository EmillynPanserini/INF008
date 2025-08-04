package br.edu.ifba.inf008.interfaces.usecase;

public interface UseCase<I, O>{
    O execute(I input);
}