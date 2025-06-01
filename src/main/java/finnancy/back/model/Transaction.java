package finnancy.back.model;

import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.annotation.Id;
import finnancy.back.enums.ExpenseType;
import finnancy.back.enums.PaymentMethod;
import finnancy.back.enums.TransactionType;
import java.time.LocalDate;

// Entidad principal para transacciones
@Document(collection = "transactions")
public class Transaction {
    @Id
    private String id;
    private Double amount;            // Monto de la transacción
    private String category;          // Categoría (comida, transporte, etc.)
    private LocalDate date;           // Fecha de la transacción
    private String description;       // Descripción opcional
    private TransactionType type;      // INCOME/EXPENSE
    private ExpenseType expenseType;   // FIXED/VARIABLE (solo para gastos)
    private PaymentMethod paymentMethod; // Efectivo, tarjeta, transferencia
    private String userId;            // Usuario dueño de la transacción
    
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public Double getAmount() {
        return amount;
    }
    public void setAmount(Double amount) {
        this.amount = amount;
    }
    public String getCategory() {
        return category;
    }
    public void setCategory(String category) {
        this.category = category;
    }
    public LocalDate getDate() {
        return date;
    }
    public void setDate(LocalDate date) {
        this.date = date;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public TransactionType getType() {
        return type;
    }
    public void setType(TransactionType type) {
        this.type = type;
    }
    public ExpenseType getExpenseType() {
        return expenseType;
    }
    public void setExpenseType(ExpenseType expenseType) {
        this.expenseType = expenseType;
    }
    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }
    public void setPaymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }
    public String getUserId() {
        return userId;
    }
    public void setUserId(String userId) {
        this.userId = userId;
    }
    
   
}


