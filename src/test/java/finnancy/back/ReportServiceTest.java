
package finnancy.back;

import finnancy.back.repository.TransactionRepository;
import finnancy.back.repository.SavingsGoalRepository;
import finnancy.back.service.ReportService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.time.LocalDate;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ReportServiceTest {
    @Mock
    TransactionRepository transactionRepository;
    @Mock
    SavingsGoalRepository savingsGoalRepository;
    @InjectMocks
    ReportService reportService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetReport() {
        when(transactionRepository.findByUserIdAndDateBetween(anyString(), any(), any())).thenReturn(List.of());
        when(savingsGoalRepository.findByUserId(anyString())).thenReturn(List.of());
        Map<String, Object> report = reportService.getReport("user1", LocalDate.now().minusDays(7), LocalDate.now());
        assertTrue(report.containsKey("totalIncome"));
        assertTrue(report.containsKey("totalExpense"));
    }
}
