
package finnancy.back;

import finnancy.back.enums.TransactionType;
import finnancy.back.model.Transaction;
import finnancy.back.dto.FinancialSummaryDTO;
import finnancy.back.repository.TransactionRepository;
import finnancy.back.service.TransactionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TransactionServiceTest {
    @Mock
    TransactionRepository transactionRepository;
    @InjectMocks
    TransactionService transactionService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateTransaction() {
        Transaction tx = new Transaction();
        tx.setAmount(100.0);
        when(transactionRepository.save(any(Transaction.class))).thenReturn(tx);
        Transaction result = transactionService.createTransaction(tx);
        assertEquals(100.0, result.getAmount());
        verify(transactionRepository).save(tx);
    }

    @Test
    void testUpdateTransaction() {
        Transaction tx = new Transaction();
        tx.setId("1");
        tx.setAmount(100.0);
        when(transactionRepository.findById("1")).thenReturn(Optional.of(tx));
        when(transactionRepository.save(any(Transaction.class))).thenReturn(tx);
        Transaction update = new Transaction();
        update.setAmount(200.0);
        Transaction result = transactionService.updateTransaction("1", update);
        assertEquals(200.0, result.getAmount());
    }

    @Test
    void testDeleteTransaction() {
        doNothing().when(transactionRepository).deleteById("1");
        transactionService.deleteTransaction("1");
        verify(transactionRepository).deleteById("1");
    }

    @Test
    void testGetTransactionsByUserId() {
        List<Transaction> txs = List.of(new Transaction());
        when(transactionRepository.findByUserId("user1")).thenReturn(txs);
        List<Transaction> result = transactionService.getTransactionsByFilters("user1", null, null, null, null, null);
        assertEquals(1, result.size());
    }

    @Test
    void testGetFinancialSummary() {
        Transaction income = new Transaction();
        income.setType(TransactionType.INCOME);
        income.setAmount(1000.0);
        Transaction expense = new Transaction();
        expense.setType(TransactionType.EXPENSE);
        expense.setAmount(200.0);
        when(transactionRepository.findByUserId("user1")).thenReturn(List.of(income, expense));
        FinancialSummaryDTO summary = transactionService.getFinancialSummary("user1");
        assertEquals(800.0, summary.getTotalBalance());
        assertEquals(1000.0, summary.getTotalIncome());
        assertEquals(200.0, summary.getTotalExpenses());
    }
}
