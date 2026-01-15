package com.JobPortalJobService.DTOS;

public interface  UserApplicationStats {
	
    Long getTotalApplied();
    Long getShortlistedCount();
    Long getRejectedCount();
    Long getSelectedCount();

}
