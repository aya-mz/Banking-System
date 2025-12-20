package approval;

import core.user.Role;


public class AdminHandler  extends ApprovalHandler{
    @Override
    public boolean approve(ApprovalRequest request) {
        if (request.getUser().getRole() != Role.ADMIN &&
                request.getTransaction().getAmount() > 10000) {

            System.out.println("❌ Admin approval required");
            return false;
        }
        System.out.println("✅ Admin approval passed");
        return true;
    }
    
}
