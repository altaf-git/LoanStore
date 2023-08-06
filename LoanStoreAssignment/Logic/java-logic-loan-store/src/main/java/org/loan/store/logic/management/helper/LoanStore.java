package org.loan.store.logic.management.helper;

import org.loan.store.logic.management.model.Loan;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class LoanStore {
    private final Map<Long, Loan> loanMap = new HashMap<>();
    private Long nextId = 1L;

    public Loan addLoan(Loan loan) {
        loan.setId(nextId++);
        loanMap.put(loan.getId(), loan);
        return loan;
    }

    public Loan getLoanById(Long id) {
        return loanMap.get(id);
    }

    public List<Loan> getAllLoans() {
        return new ArrayList<>(loanMap.values());
    }

    public List<Loan> getOverdueLoans() {
        LocalDateTime now = LocalDateTime.now();
        return loanMap.values().stream()
                .filter(loan -> loan.getDueDate().isBefore(now))
                .collect(Collectors.toList());
    }

}
