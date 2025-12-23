package app;

import Ticket.*;
import Transaction.Facad.Facad;
import Transaction.*;
import Transaction.TransactionCommand.transfers;
import Transaction.TransactionCommand.withdrawals;
import Transaction.TransactionType;
import Transaction.dispatcher.TransactionDispatcher;
import Transaction.schedule.MonthlyInterestTransaction;
import Transaction.schedule.TransactionScheduler;
import account.Account;
import account.AccountGroup;
import account.AccountType;
import account.features.AccountFeature;
import account.features.AccountFeatureAdapter;
import account.features.decorator.CashbackFeature;
import account.features.decorator.InsuranceFeature;
import account.features.strategy.InterestApplier;
import account.features.strategy.SavingInterestStrategy;
import account.states.ClosedState;
import approval.*;
import core.user.AuthInfo;
import core.user.Role;
import core.user.User;
import core.user.UserProfile;
import infrastructure.bootstrap.SystemBootstrap;

import java.time.LocalDate;

public class BankingApplication {

    public static void main(String[] args) {

        System.out.println("Banking System Started...");

        SystemBootstrap systemBootstrap=new SystemBootstrap();
        TransactionDispatcher audit=systemBootstrap.auditOnly();
        TransactionDispatcher auditnotification=systemBootstrap.auditAndNotification();
        System.out.println("Observers registered.");

        UserProfile profile = new UserProfile(
                "Aya",
                "Monzer",
                "Almzayek",
                "Ghada",
                "0940759183",
                "123456789"
        );
        AuthInfo auth = new AuthInfo("password123");
        User user = new User(1, "aya@test.com", Role.CUSTOMER, auth, profile);
        System.out.println("User created: " + user.getEmail());

        Account account = new Account(1, "Aya Account", AccountType.SAVING, 1000, 0);
        System.out.println("Account created with balance: " + account.getBalance());

        AccountFeature accountFeature=new AccountFeatureAdapter(account);
        AccountFeature finalAccountFeature = accountFeature;
        auditnotification.dispatch(
                () -> finalAccountFeature.deposit(500),
                "DEPOSIT",
                "User " + user.getEmail() + " deposited 500"
        );
        System.out.println("New balance: " + account.getBalance());

        accountFeature = new CashbackFeature(accountFeature);
        accountFeature = new InsuranceFeature(accountFeature);
        System.out.println("Account Features: " + accountFeature.getDescription());

        AccountFeature finalAccountFeature1 = accountFeature;
        auditnotification.dispatch(
                () -> finalAccountFeature1.deposit(1000),
                "DEPOSIT_WITH_CASHBACK",
                "Deposit with cashback applied"
        );
        System.out.println("Balance after cashback deposit: " + account.getBalance());

        InterestApplier interestApplier = new InterestApplier(new SavingInterestStrategy());
        AccountFeature finalAccountFeature2 = accountFeature;
        audit.dispatch(
                () -> interestApplier.apply(finalAccountFeature2),
                "INTEREST_APPLIED",
                "Monthly interest applied to saving account"
        );
        System.out.println("Balance after interest: " + account.getBalance());


        MonthlyInterestTransaction monthlyInterest =
                new MonthlyInterestTransaction(accountFeature, new SavingInterestStrategy());
        TransactionScheduler scheduler = new TransactionScheduler();
        scheduler.add(monthlyInterest);
        scheduler.run(LocalDate.now());
        System.out.println("Balance after scheduler run: " + account.getBalance());

        Account receiver = new Account(2, "Receiver Account", AccountType.CHECKING, 500, 0);
        System.out.println("Receiver balance before transfer: " + receiver.getBalance());

        Transaction transferTx = new Transaction(300, 1, receiver, account, TransactionType.TRANSFER);
        transfers transferCommand = new transfers(transferTx);
        auditnotification.dispatch(
                transferCommand::execute,
                "TRANSFER",
                "Transfer 300 from Aya to Receiver"
        );
        System.out.println("Sender balance: " + account.getBalance());
        System.out.println("Receiver balance: " + receiver.getBalance());

        Transaction withdrawTx = new Transaction(
                200,
                1,
                null,
                account,
                TransactionType.withdrawals
        );

        withdrawals withdrawCommand = new withdrawals(withdrawTx);
        auditnotification.dispatch(
                withdrawCommand::execute,
                "WITHDRAWAL",
                "Withdraw 200 from account"
        );
        System.out.println("Balance after withdrawal: " + account.getBalance());

        account.setState(ClosedState.getInstance());
        System.out.println("Account state changed to CLOSED");
        try {
            AccountFeature finalAccountFeature3 = accountFeature;
            audit.dispatch(
                    () -> finalAccountFeature3.deposit(100),
                    "DEPOSIT",
                    "Attempt deposit on closed account"
            );
        } catch (Exception e) {
            System.out.println("Operation rejected: " + e.getMessage());
        }

        System.out.println("--- New Scenario: Second Account ---");

        Account secondAccount = new Account(3, "Business Account", AccountType.CHECKING, 20000, 0);
        AccountFeature secondFeature = new AccountFeatureAdapter(secondAccount);
        auditnotification.dispatch(
                () -> secondFeature.deposit(3000),
                "DEPOSIT",
                "Initial deposit for Business Account"
        );
        System.out.println("Business Account balance: " + secondAccount.getBalance());

        ApprovalHandler teller = new TellerHandler();
        ApprovalHandler manager = new ManagerHandler();
        ApprovalHandler admin = new AdminHandler();
        teller.setNext(manager);
        manager.setNext(admin);
        ApprovalHandler approvalChain = teller;
        Transaction bigTransfer = new Transaction(5000, 1, secondAccount, account, TransactionType.PAPAL);
        ApprovalRequest approvalRequest = new ApprovalRequest(bigTransfer, user, account.getPin_code());

        if (approvalChain.approve(approvalRequest)) {
            TransactionRepository tr = new TransactionRepository();
            Facad facade = new Facad(tr);
            auditnotification.dispatch(
                    () -> facade.processTransaction(bigTransfer),
                    "TRANSFER_APPROVED",
                    "Approved transfer executed via Facade"
            );
            System.out.println("Approved transfer executed.");
            System.out.println("Sender balance: " + account.getBalance());
            System.out.println("Receiver balance: " + secondAccount.getBalance());

        }
        else {
            System.out.println("Transfer rejected by approval chain");
            Ticket ticket =new Ticket(user.getId(), Tickettype.GEN, "Transfer Rejected", new GeneralInquiryStrategy());
            audit.dispatch(
                    () -> {},
                    "TICKET_CREATED",
                    "Support ticket created for rejected transfer"
            );
            System.out.println("Ticket created with ID: " + ticket.getId());
        }

        System.out.println("--- Composite Account Scenario ---");
        AccountGroup familyGroup = new AccountGroup(user.getId(), "Family Accounts", AccountType.SAVING);
        Account child1 = new Account(user.getId(), "Son Account", AccountType.SAVING, 500, 0);
        Account child2 = new Account(user.getId(), "Daughter Account", AccountType.CHECKING, 800, 0);
        familyGroup.add(child1);
        familyGroup.add(child2);
        System.out.println("Total balance before close: " + familyGroup.getTotalBalance());
        familyGroup.setState(ClosedState.getInstance());
        if (!child1.getState().isClosed()) {
            AccountFeature childFeature = new AccountFeatureAdapter(child1);
            audit.dispatch(
                    () -> childFeature.deposit(100),
                    "DEPOSIT",
                    "Deposit on child account"
            );
        } else {
            System.out.println("Account not active");
        }

    }
}
