package approval;

import core.user.User;

public abstract class ApprovalHandler {
    protected ApprovalHandler next;

    public void setNext(ApprovalHandler next) {
        this.next = next;
    }

    public abstract boolean approve(ApprovalRequest request);

    protected boolean passToNext(ApprovalRequest request) {
        if (next == null) return true;
        return next.approve(request);
    }
}
