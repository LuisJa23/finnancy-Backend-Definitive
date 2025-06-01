// TransactionRepository.java
package finnancy.back.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import finnancy.back.enums.PaymentMethod;
import finnancy.back.enums.TransactionType;
import finnancy.back.model.Transaction;

/**
 * Repositorio para operaciones CRUD de transacciones
 */

@Repository
public interface TransactionRepository extends MongoRepository<Transaction, String> {
    
    List<Transaction> findByDateBetween(LocalDate start, LocalDate end); // <-- Para reportes globales



    // Encontrar transacciones por usuario y tipo
    List<Transaction> findByUserIdAndType(String userId, TransactionType type);
    
    
    // Encontrar transacciones por usuario y rango de fechas
    List<Transaction> findByUserIdAndDateBetween(String userId, LocalDate start, LocalDate end);
    
    // Encontrar transacciones por usuario, categoría y tipo
    List<Transaction> findByUserIdAndCategoryAndType(String userId, String category, TransactionType type);
    
    // Encontrar transacciones por usuario y método de pago
    List<Transaction> findByUserIdAndPaymentMethod(String userId, PaymentMethod paymentMethod);

    List<Transaction> findByUserId(String userId);

}