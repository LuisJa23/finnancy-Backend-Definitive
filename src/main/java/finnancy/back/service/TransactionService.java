// TransactionService.java
package finnancy.back.service;

import finnancy.back.model.Transaction;
import finnancy.back.dto.FinancialSummaryDTO;
import finnancy.back.enums.PaymentMethod;
import finnancy.back.enums.TransactionType;
import finnancy.back.repository.TransactionRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

/**
 * Servicio para manejar la lógica de negocio relacionada con transacciones
 */
@Service
public class TransactionService {

    private final TransactionRepository transactionRepository;

    public TransactionService(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    /**
     * Crea una nueva transacción
     */
    public Transaction createTransaction(Transaction transaction) {
        return transactionRepository.save(transaction);
    }

    /**
     * Actualiza una transacción existente
     */
    public Transaction updateTransaction(String id, Transaction transactionDetails) {
        Transaction transaction = transactionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Transaction not found"));
        
        transaction.setAmount(transactionDetails.getAmount());
        transaction.setCategory(transactionDetails.getCategory());
        transaction.setDate(transactionDetails.getDate());
        transaction.setDescription(transactionDetails.getDescription());
        transaction.setType(transactionDetails.getType());
        transaction.setExpenseType(transactionDetails.getExpenseType());
        transaction.setPaymentMethod(transactionDetails.getPaymentMethod());
        
        return transactionRepository.save(transaction);
    }

    /**
     * Elimina una transacción
     */
    public void deleteTransaction(String id) {
        transactionRepository.deleteById(id);
    }

    /**
     * Obtiene transacciones por usuario y filtros
     */
    public List<Transaction> getTransactionsByFilters(String userId, LocalDate startDate, LocalDate endDate, 
                                                     String category, TransactionType type, 
                                                     PaymentMethod paymentMethod) {
        if (startDate != null && endDate != null) {
            return transactionRepository.findByUserIdAndDateBetween(userId, startDate, endDate);
        } else if (category != null && type != null) {
            return transactionRepository.findByUserIdAndCategoryAndType(userId, category, type);
        } else if (paymentMethod != null) {
            return transactionRepository.findByUserIdAndPaymentMethod(userId, paymentMethod);
        } else if (type != null) {
            return transactionRepository.findByUserIdAndType(userId, type);
        } else {
            return transactionRepository.findByUserId(userId);
        }
    }

    public FinancialSummaryDTO getFinancialSummary(String userId) {
    List<Transaction> transactions = transactionRepository.findByUserId(userId);
    
    double totalIncome = 0;
    double totalExpenses = 0;
    int incomeCount = 0;
    int expenseCount = 0;
    
    for (Transaction transaction : transactions) {
        if (transaction.getType() == TransactionType.INCOME) {
            totalIncome += transaction.getAmount();
            incomeCount++;
        } else {
            totalExpenses += transaction.getAmount();
            expenseCount++;
        }
    }
    
    double totalBalance = totalIncome - totalExpenses;
    
    return new FinancialSummaryDTO(
        totalBalance,
        totalIncome,
        totalExpenses,
        incomeCount,
        expenseCount
    );
}
}