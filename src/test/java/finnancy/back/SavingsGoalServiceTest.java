
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
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SavingsGoalServiceTest {
    @Mock
    SavingsGoalRepository savingsGoalRepository;
    @InjectMocks
    SavingsGoalService savingsGoalService;

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
    }

    @Test
    void testUpdateSavingsGoal() {
        SavingsGoal goal = new SavingsGoal();
        goal.setId("1");
        when(savingsGoalRepository.findById("1")).thenReturn(Optional.of(goal));
        when(savingsGoalRepository.save(any(SavingsGoal.class))).thenReturn(goal);
        SavingsGoal update = new SavingsGoal();
        update.setName("Nueva Meta");
        SavingsGoal result = savingsGoalService.updateSavingsGoal("1", update);
        assertEquals("Nueva Meta", result.getName());
    }

    @Test
    void testAddToSavingsGoal() {
        SavingsGoal goal = new SavingsGoal();
        goal.setId("1");
        goal.setCurrentAmount(100.0);
        goal.setTargetAmount(200.0);
        when(savingsGoalRepository.findById("1")).thenReturn(Optional.of(goal));
        when(savingsGoalRepository.save(any(SavingsGoal.class))).thenReturn(goal);
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
