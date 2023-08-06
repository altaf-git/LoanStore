package org.loan.store.logic.management.service;

import org.loan.store.logic.management.helper.LoanStore;
import org.loan.store.logic.management.model.Loan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LoanService {
    @Autowired
    private LoanStore loanStore;


    public Loan addLoan(Loan loan) {
        validateLoan(loan);
        return loanStore.addLoan(loan);
    }

    public List<Loan> getAllLoans() {
        return loanStore.getAllLoans();
    }

    public Loan getLoanById(Long id) {
        return loanStore.getLoanById(id);
    }

    public List<Loan> getOverdueLoans() {
        return loanStore.getOverdueLoans();
    }
    public Double getTotalRemainingAmountByLender(String lenderId) {
        return loanStore.getAllLoans().stream()
                .filter(loan -> loan.getLenderId().equals(lenderId))
                .mapToDouble(Loan::getRemainingAmount)
                .sum();
    }

    public Double getTotalInterestByLender(String lenderId) {
        return loanStore.getAllLoans().stream()
                .filter(loan -> loan.getLenderId().equals(lenderId))
                .mapToDouble(Loan::getInterestPerDay)
                .sum();
    }
    private void validateLoan(Loan loan) {
        if (loan.getPaymentDate().isEqual(loan.getDueDate()) || loan.getPaymentDate().isAfter(loan.getDueDate())) {
            throw new IllegalArgumentException("Payment date can't be greater or Equal to due date");
        }
    }
}
