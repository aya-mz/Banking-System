package Transaction.TransactionCommand;

import Transaction.Adapter.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LegacyAdapterTest {

    @Mock
    legacyBankSystem mockBankSystem;

    LegacyAdapter legacyAdapter;

    @BeforeEach
    void setup() {
        legacyAdapter = new LegacyAdapter(mockBankSystem);
    }

    @Test
    void testPaymoney_callsMakePay() {
        double amount = 200.0;
        legacyAdapter.Paymoney(amount);
        verify(mockBankSystem, times(1)).MakePay(amount);
    }
}
