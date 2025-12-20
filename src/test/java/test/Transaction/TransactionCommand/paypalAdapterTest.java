package Transaction.TransactionCommand;

import Transaction.Adapter.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.verify;
import static org.mockito.internal.verification.VerificationModeFactory.times;


@ExtendWith(MockitoExtension.class)
class paypalAdapterTest {

    @Mock
    paypal paypalMock;

    paypalAdapter paypalAdapter;

    @BeforeEach
    void setup() {
        paypalAdapter = new paypalAdapter(paypalMock);
    }

    @Test
    void testPaymoney_callsPaypalMakepayment() {
        double amount = 200.0;
        paypalAdapter.Paymoney(amount);
        verify(paypalMock, times(1)).makepayment(amount);
    }
}
