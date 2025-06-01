package finnancy.back.service;

import finnancy.back.model.SavingsGoal;
import finnancy.back.repository.SavingsGoalRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

/**
 * Servicio para manejar la lógica de metas de ahorro
 */
@Service
public class SavingsGoalService {

    private final SavingsGoalRepository savingsGoalRepository;

    public SavingsGoalService(SavingsGoalRepository savingsGoalRepository) {
        this.savingsGoalRepository = savingsGoalRepository;
    }

    /**
     * Crea una nueva meta de ahorro
     */
    public SavingsGoal createSavingsGoal(SavingsGoal savingsGoal) {
        // Validar que los campos requeridos estén presentes
        if (savingsGoal.getName() == null || savingsGoal.getTargetAmount() == null || 
            savingsGoal.getTargetDate() == null || savingsGoal.getUserId() == null) {
            throw new IllegalArgumentException("Missing required fields for savings goal");
        }

        // Asegurar que el monto actual empiece en 0
        savingsGoal.setCurrentAmount(0.0);
        savingsGoal.setCompleted(false);
        
        return savingsGoalRepository.save(savingsGoal);
    }

    /**
     * Actualiza una meta de ahorro existente
     */
    public SavingsGoal updateSavingsGoal(String id, SavingsGoal savingsGoalDetails) {
        SavingsGoal savingsGoal = savingsGoalRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Savings goal not found with id: " + id));
        
        // Actualizar campos editables
        savingsGoal.setName(savingsGoalDetails.getName());
        savingsGoal.setTargetAmount(savingsGoalDetails.getTargetAmount());
        savingsGoal.setTargetDate(savingsGoalDetails.getTargetDate());
        
        // Verificar si se ha completado la meta
        checkAndUpdateGoalCompletion(savingsGoal);
        
        return savingsGoalRepository.save(savingsGoal);
    }

    /**
     * Agrega fondos a una meta de ahorro
     */
    public SavingsGoal addToSavingsGoal(String id, Double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Amount must be positive");
        }

        SavingsGoal savingsGoal = savingsGoalRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Savings goal not found with id: " + id));
        
        savingsGoal.setCurrentAmount(savingsGoal.getCurrentAmount() + amount);
        
        // Verificar si se ha completado la meta
        checkAndUpdateGoalCompletion(savingsGoal);
        
        return savingsGoalRepository.save(savingsGoal);
    }

    /**
     * Verifica y actualiza el estado de completado de una meta
     */
    private void checkAndUpdateGoalCompletion(SavingsGoal savingsGoal) {
        boolean wasCompleted = savingsGoal.isCompleted();
        boolean isNowCompleted = savingsGoal.getCurrentAmount() >= savingsGoal.getTargetAmount();
        
        savingsGoal.setCompleted(isNowCompleted);
        
        // Aquí podríamos agregar lógica para notificaciones cuando se complete
        // (se implementará cuando hagamos el RF-05)
    }

    /**
     * Obtiene todas las metas de ahorro de un usuario
     */
    public List<SavingsGoal> getAllUserSavingsGoals(String userId) {
        return savingsGoalRepository.findByUserId(userId);
    }

    /**
     * Obtiene las metas de ahorro activas de un usuario (no completadas)
     */
    public List<SavingsGoal> getActiveUserSavingsGoals(String userId) {
        return savingsGoalRepository.findByUserIdAndCompletedFalse(userId);
    }

    /**
     * Obtiene las metas de ahorro completadas de un usuario
     */
    public List<SavingsGoal> getCompletedUserSavingsGoals(String userId) {
        return savingsGoalRepository.findByUserIdAndCompletedTrue(userId);
    }

    /**
     * Elimina una meta de ahorro
     */
    public void deleteSavingsGoal(String id) {
        savingsGoalRepository.deleteById(id);
    }
}