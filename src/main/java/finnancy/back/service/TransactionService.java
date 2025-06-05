// TransactionService.java
package finnancy.back.service;

import finnancy.back.model.Transaction;
import finnancy.back.dto.FinancialSummaryDTO;
import finnancy.back.enums.PaymentMethod;
import finnancy.back.enums.TransactionType;
import finnancy.back.repository.TransactionRepository;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.util.List;

/**
 * Servicio para manejar la lógica de negocio relacionada con transacciones
 */
@Service
public class TransactionService {

    private static final Logger logger = LoggerFactory.getLogger(TransactionService.class);
    private final TransactionRepository transactionRepository;

    public TransactionService(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    /**
     * Crea una nueva transacción
     */
    public Transaction createTransaction(Transaction transaction) {
        logger.debug("Creando transacción para usuario: {}", transaction.getUserId());
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
        logger.debug("Buscando transacciones para usuario: {}", userId);
        
        List<Transaction> transactions;
        
        // Si no se proporciona userId, devolver todas las transacciones
        if (userId == null) {
            transactions = transactionRepository.findAll();
        } else if (startDate != null && endDate != null) {
            transactions = transactionRepository.findByUserIdAndDateBetween(userId, startDate, endDate);
        } else if (category != null && type != null) {
            transactions = transactionRepository.findByUserIdAndCategoryAndType(userId, category, type);
        } else if (paymentMethod != null) {
            transactions = transactionRepository.findByUserIdAndPaymentMethod(userId, paymentMethod);
        } else if (type != null) {
            transactions = transactionRepository.findByUserIdAndType(userId, type);
        } else {
            transactions = transactionRepository.findByUserId(userId);
        }
        
        logger.debug("Encontradas {} transacciones para usuario: {}", transactions.size(), userId);
        return transactions;
    }

    /**
     * Método de debug para obtener información detallada de las transacciones
     */
    public List<Transaction> getAllTransactionsForDebug(String userId) {
        logger.info("DEBUG: Buscando TODAS las transacciones para usuario: {}", userId);
        
        // Primero intentar con findByUserId
        List<Transaction> transactions = transactionRepository.findByUserId(userId);
        logger.info("DEBUG: findByUserId devolvió {} transacciones", transactions.size());
        
        // Si no hay resultados, intentar obtener todas las transacciones de la base de datos
        if (transactions.isEmpty()) {
            logger.warn("DEBUG: No se encontraron transacciones con findByUserId, obteniendo todas las transacciones");
            List<Transaction> allTransactions = transactionRepository.findAll();
            logger.info("DEBUG: Total de transacciones en la base de datos: {}", allTransactions.size());
            
            // Filtrar manualmente por userId
            transactions = allTransactions.stream()
                .filter(t -> userId.equals(t.getUserId()))
                .toList();
            logger.info("DEBUG: Transacciones filtradas manualmente para usuario {}: {}", userId, transactions.size());
            
            // Log de las primeras transacciones para debug
            allTransactions.stream().limit(5).forEach(t -> 
                logger.info("DEBUG: Transacción muestra - ID: {}, UserID: {}, Amount: {}", 
                    t.getId(), t.getUserId(), t.getAmount()));
        }
        
        return transactions;
    }

    public FinancialSummaryDTO getFinancialSummary(String userId) {
        List<Transaction> transactions = transactionRepository.findByUserId(userId);
        logger.debug("Calculando resumen financiero para usuario: {} con {} transacciones", userId, transactions.size());
        
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