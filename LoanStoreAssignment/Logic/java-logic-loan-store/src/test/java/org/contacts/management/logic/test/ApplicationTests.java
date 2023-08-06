package org.contacts.management.logic.test;

import lombok.extern.java.Log;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.logging.Logger;
import org.loan.store.logic.management.helper.LoanStore;
import org.loan.store.logic.management.model.Loan;
import org.loan.store.logic.management.service.LoanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@EnableAutoConfiguration
@SpringBootTest(classes = Application.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ComponentScan(basePackages = "org.loan")
@ConfigurationPropertiesScan(basePackages = {"org.loan"})
@ActiveProfiles("test")
@Log
class ApplicationTests {

    static {
        System.setProperty("io.netty.native.workdir", "/opt/Applications/netty");
    }


    @Autowired
    private LoanService loanService;

    @Test
    public void testAddLoan() {
        Loan loan = createSampleLoan();

        Loan savedLoan = loanService.addLoan(loan);
        assertNotNull(savedLoan.getId());
    }

    @Test
    public void testGetAllLoans() {
        Loan loan1 = createSampleLoan();
        Loan loan2 = createSampleLoan();

        loanService.addLoan(loan1);
        loanService.addLoan(loan2);

        List<Loan> allLoans = loanService.getAllLoans();
        assertEquals(9, allLoans.size());
    }

    @Test
    public void testGetLoanById() {
        Loan loan = createSampleLoan();
        loanService.addLoan(loan);

        Loan retrievedLoan = loanService.getLoanById(1L);
        assertNotNull(retrievedLoan);
        log.info("The Id is Loan Id is "+retrievedLoan.getId()+", "+retrievedLoan.getLoanId());
        assertEquals("L1", retrievedLoan.getLoanId());
    }

    @Test
    public void testGetOverdueLoans() {
        Loan loan1 = createSampleLoan();
        Loan loan2 = createSampleLoan();
        loan2.setDueDate(LocalDateTime.now().minusDays(2));

        loanService.addLoan(loan1);
        loanService.addLoan(loan2);

        List<Loan> overdueLoans = loanService.getOverdueLoans();
        assertEquals(12, overdueLoans.size());
    }

    @Test
    public void testInvalidPaymentDate() {
        Loan loan = createSampleLoan();
        loan.setPaymentDate(LocalDateTime.now().plusDays(1));

        assertThrows(IllegalArgumentException.class, () -> loanService.addLoan(loan));
    }
    @Test
    public void testGetTotalRemainingAmountByLender() {
        Loan loan1 = createSampleLoan("L1", "C1", "LEN1", 10000.0, 10000.0);
        Loan loan2 = createSampleLoan("L2", "C2", "LEN1", 20000.0, 5000.0);
        Loan loan3 = createSampleLoan("L3", "C1", "LEN2", 15000.0, 5000.0);

        loanService.addLoan(loan1);
        loanService.addLoan(loan2);
        loanService.addLoan(loan3);

        String lenderId = "LEN1";
        Double totalRemainingAmount = loanService.getTotalRemainingAmountByLender(lenderId);
        assertEquals(15000.0, totalRemainingAmount, 0.001);
    }

    @Test
    public void testGetTotalInterestByLender() {
        Loan loan1 = createSampleLoan("L1", "C1", "LEN1", 10000.0, 10000.0);
        Loan loan2 = createSampleLoan("L2", "C2", "LEN1", 20000.0, 5000.0);
        Loan loan3 = createSampleLoan("L3", "C1", "LEN2", 15000.0, 5000.0);

        loanService.addLoan(loan1);
        loanService.addLoan(loan2);
        loanService.addLoan(loan3);

        String lenderId = "LEN1";
        Double totalInterest = loanService.getTotalInterestByLender(lenderId);
        assertEquals(4.0, totalInterest);
    }

    private Loan createSampleLoan() {
        return new Loan(null, "L1", "C1", "LEN1", 10000.0, 10000.0,
                LocalDateTime.of(2023, 5, 6, 0, 0),
                1.0, LocalDateTime.of(2023, 5, 7, 0, 0), 0.01);
    }
    private Loan createSampleLoan(String loanId, String customerId, String lenderId,
                                  Double amount, Double remainingAmount) {
        return new Loan(null, loanId, customerId, lenderId, amount, remainingAmount,
                LocalDateTime.of(2023, 5, 6, 0, 0),
                1.0, LocalDateTime.of(2023, 5, 7, 0, 0), 0.01);
    }
}