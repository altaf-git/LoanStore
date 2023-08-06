package org.loan.store.logic.management.model;


import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.concurrent.atomic.AtomicLong;

@Data
@AllArgsConstructor
public class Loan {
    private static final AtomicLong ID_GENERATOR = new AtomicLong(1);

    private Long id;
    private String loanId;
    private String customerId;
    private String lenderId;
    private Double amount;
    private Double remainingAmount;
    private LocalDateTime paymentDate;
    private Double interestPerDay;
    private LocalDateTime dueDate;
    private Double penaltyPerDay;

    public Loan() {
        this.id = ID_GENERATOR.getAndIncrement();
    }

}

