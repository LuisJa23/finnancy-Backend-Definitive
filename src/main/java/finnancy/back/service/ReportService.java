package finnancy.back.service;

import finnancy.back.model.Transaction;
import finnancy.back.model.SavingsGoal;
import finnancy.back.repository.TransactionRepository;
import finnancy.back.repository.SavingsGoalRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

@Service
public class ReportService {
    private final TransactionRepository transactionRepository;
    private final SavingsGoalRepository savingsGoalRepository;

    public ReportService(TransactionRepository transactionRepository, SavingsGoalRepository savingsGoalRepository) {
        this.transactionRepository = transactionRepository;
        this.savingsGoalRepository = savingsGoalRepository;
    }

    public Map<String, Object> getReport(String userId, LocalDate start, LocalDate end) {
        List<Transaction> transactions = transactionRepository.findByUserIdAndDateBetween(userId, start, end);
        List<SavingsGoal> goals = savingsGoalRepository.findByUserId(userId);

        double totalIncome = transactions.stream()
            .filter(t -> t.getType().toString().equalsIgnoreCase("income"))
            .mapToDouble(Transaction::getAmount)
            .sum();

        double totalExpense = transactions.stream()
            .filter(t -> t.getType().toString().equalsIgnoreCase("expense"))
            .mapToDouble(Transaction::getAmount)
            .sum();

        Map<String, Object> result = new HashMap<>();
        result.put("totalIncome", totalIncome);
        result.put("totalExpense", totalExpense);
        result.put("goals", goals);

        return result;
    }

    public Map<String, Object> getReport(String userId, String interval, LocalDate now, boolean global, String type) {
        LocalDate start;
        switch (interval.toLowerCase()) {
            case "monthly":
                start = now.withDayOfMonth(1);
                break;
            case "semiannual":
                start = now.minusMonths(6);
                break;
            case "annual":
                start = now.withDayOfYear(1);
                break;
            default:
                start = now.minusDays(7);
        }
    
        List<Transaction> transactions;
        List<SavingsGoal> goals;
    
        if (global) {
            transactions = transactionRepository.findByDateBetween(start, now);
            goals = savingsGoalRepository.findByTargetDateBetween(start, now);
        } else if (userId != null) {
            transactions = transactionRepository.findByUserIdAndDateBetween(userId, start, now);
            goals = savingsGoalRepository.findByUserId(userId);
        } else {
            transactions = List.of();
            goals = List.of();
        }
    
        if (type != null) {
            transactions = transactions.stream()
                .filter(t -> t.getType().toString().equalsIgnoreCase(type))
                .toList();
        }
    
        long incomeCount = transactions.stream().filter(t -> t.getType().toString().equalsIgnoreCase("income")).count();
        long expenseCount = transactions.stream().filter(t -> t.getType().toString().equalsIgnoreCase("expense")).count();
        long goalsCount = goals.stream().filter(g -> !g.getTargetDate().isBefore(start) && !g.getTargetDate().isAfter(now)).count();
    
        Map<String, Object> result = new HashMap<>();
        result.put("interval", interval);
        result.put("startDate", start.toString());
        result.put("endDate", now.toString());
        result.put("incomeCount", incomeCount);
        result.put("expenseCount", expenseCount);
        result.put("savingsGoalsCount", goalsCount);
        result.put("transactions", transactions); // Opcional: para mostrar detalles
    
        return result;
    }
    // ...existing code...
}
