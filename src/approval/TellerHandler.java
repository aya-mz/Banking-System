package approval;

import core.user.Role;
import core.user.User;

public class TellerHandler extends ApprovalHandler {

    @Override
    public boolean approve(User user, String operation, double amount) {
        if (amount <= 0) return false;
        if(user.getRole()== Role.TELLER && amount <1000){
            return true;
        }
    if (next != null) {
        return next.approve(user, operation, amount);
        
    }
    return false;
    }
    
}
