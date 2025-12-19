package approval;

import core.user.User;

public abstract class ApprovalHandler {
    protected ApprovalHandler next;

    public void setNext(ApprovalHandler next){
        this.next=next;
    }
    public abstract boolean approve(User user,String operation,double amount)
    ;
}
