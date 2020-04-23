package com.infojaalprime.hrms.leave_model;

public class LeaveBalanceModel {

    private String leaveId;
    private String leaveType;
    private String shortDesc;
    private String currentYearLeaves;
    private String totalLeavesEarned;
    private String totalLeave;
    private String leavesAvailed;
    private String balanceLeave;

    public String getLeaveId() {
        return leaveId;
    }

    public void setLeaveId(String leaveId) {
        this.leaveId = leaveId;
    }

    public String getLeaveType() {
        return leaveType;
    }

    public void setLeaveType(String leaveType) {
        this.leaveType = leaveType;
    }

    public String getShortDesc() {
        return shortDesc;
    }

    public void setShortDesc(String shortDesc) {
        this.shortDesc = shortDesc;
    }

    public String getCurrentYearLeaves() {
        return currentYearLeaves;
    }

    public void setCurrentYearLeaves(String currentYearLeaves) {
        this.currentYearLeaves = currentYearLeaves;
    }

    public String getTotalLeavesEarned() {
        return totalLeavesEarned;
    }

    public void setTotalLeavesEarned(String totalLeavesEarned) {
        this.totalLeavesEarned = totalLeavesEarned;
    }

    public String getTotalLeave() {
        return totalLeave;
    }

    public void setTotalLeave(String totalLeave) {
        this.totalLeave = totalLeave;
    }

    public String getLeavesAvailed() {
        return leavesAvailed;
    }

    public void setLeavesAvailed(String leavesAvailed) {
        this.leavesAvailed = leavesAvailed;
    }

    public String getBalanceLeave() {
        return balanceLeave;
    }

    public void setBalanceLeave(String balanceLeave) {
        this.balanceLeave = balanceLeave;
    }
}
