package approval;

import core.user.Role;
import core.user.User;

public class AdminHandler  extends ApprovalHandler{

    @Override
    public boolean approve(User user, String operation, double amount) {
         if (amount <= 0) return false;
        if(user.getRole()== Role.ADMIN){
            return true;
        }

    return false;
    }
    
}
