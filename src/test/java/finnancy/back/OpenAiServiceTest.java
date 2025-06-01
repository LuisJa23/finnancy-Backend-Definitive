
package finnancy.back;

import finnancy.back.model.Transaction;
import finnancy.back.model.SavingsGoal;
import finnancy.back.service.OpenAiService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.reactive.function.client.WebClient;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;

class OpenAiServiceTest {
    @Mock
    WebClient webClient;
    @InjectMocks
    OpenAiService openAiService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testBuildFinancialContext() {
        List<Transaction> txs = List.of();
        List<SavingsGoal> goals = List.of();
        String context = openAiService.buildFinancialContext(txs, goals);
        assertTrue(context.contains("Resumen financiero"));
    }
}
