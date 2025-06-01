package finnancy.back;

import finnancy.back.model.SavingsGoal;
import finnancy.back.repository.SavingsGoalRepository;
import finnancy.back.service.SavingsGoalService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SavingsGoalServiceTest {
    @Mock
    private SavingsGoalRepository savingsGoalRepository;

    @InjectMocks
    private SavingsGoalService savingsGoalService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateSavingsGoal() {
        SavingsGoal goal = new SavingsGoal("user1", "Meta", 1000.0, LocalDate.now().plusMonths(1));
        when(savingsGoalRepository.save(any(SavingsGoal.class))).thenReturn(goal);

        SavingsGoal result = savingsGoalService.createSavingsGoal(goal);

        assertEquals("Meta", result.getName());
        assertEquals(1000.0, result.getTargetAmount());
    }

    @Test
    void testUpdateSavingsGoal() {
        // Preparar objeto existente con valores no nulos
        SavingsGoal existing = new SavingsGoal();
        existing.setId("1");
        existing.setName("Meta Original");
        existing.setTargetAmount(1000.0);
        existing.setCurrentAmount(200.0);
        when(savingsGoalRepository.findById("1")).thenReturn(Optional.of(existing));
        when(savingsGoalRepository.save(any(SavingsGoal.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Datos de actualización: solo nombre y target
        SavingsGoal update = new SavingsGoal();
        update.setName("Nueva Meta");
        update.setTargetAmount(1000.0);

        SavingsGoal result = savingsGoalService.updateSavingsGoal("1", update);

        assertEquals("Nueva Meta", result.getName());
        assertEquals(1000.0, result.getTargetAmount());
        // currentAmount should remain unchanged
        assertEquals(200.0, result.getCurrentAmount());
        verify(savingsGoalRepository).findById("1");
        verify(savingsGoalRepository).save(result);
    }

    @Test
    void testAddToSavingsGoal() {
        SavingsGoal goal = new SavingsGoal();
        goal.setId("1");
        goal.setCurrentAmount(100.0);
        goal.setTargetAmount(200.0);
        when(savingsGoalRepository.findById("1")).thenReturn(Optional.of(goal));
        when(savingsGoalRepository.save(any(SavingsGoal.class))).thenAnswer(invocation -> invocation.getArgument(0));

        SavingsGoal result = savingsGoalService.addToSavingsGoal("1", 50.0);

        assertEquals(150.0, result.getCurrentAmount());
    }

    @Test
    void testDeleteSavingsGoal() {
        doNothing().when(savingsGoalRepository).deleteById("1");
        savingsGoalService.deleteSavingsGoal("1");
        verify(savingsGoalRepository).deleteById("1");
    }
}
