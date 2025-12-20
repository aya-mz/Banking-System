package approval;

public class ManagerHandler extends ApprovalHandler{

    private static final double LIMIT = 5000;

    @Override
    public boolean approve(ApprovalRequest request) {
        if (request.getTransaction().getAmount() > LIMIT) {
            System.out.println("ℹ Needs manager approval");
            return passToNext(request);
        }
        return true;
    }
    
}
