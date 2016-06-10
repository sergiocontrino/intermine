package org.intermine.bio.dataloader.job;

/**
 * Enumeration representing the status of a an Execution.
 *
 */
public enum BatchStatus {

	/**
	 * The order of the status values is significant because it can be used to
	 * aggregate a set of status values - the result should be the maximum
	 * value. Since COMPLETED is first in the order, only if all elements of an
	 * execution are COMPLETED will the aggregate status be COMPLETED. A running
	 * execution is expected to move from STARTING to STARTED to COMPLETED
	 * (through the order defined by {@link #upgradeTo(BatchStatus)}). Higher
	 * values than STARTED signify more serious failure. ABANDONED is used for
	 * steps that have finished processing, but were not successful, and where
	 * they should be skipped on a restart (so FAILED is the wrong status).
	 */
	COMPLETED, STARTING, STARTED, STOPPING, STOPPED, FAILED, ABANDONED, UNKNOWN;

	public static BatchStatus max(BatchStatus status1, BatchStatus status2) {
		return status1.isGreaterThan(status2) ? status1 : status2;
	}

	/**
	 * Convenience method to decide if a status indicates work is in progress.
	 * 
	 * @return true if the status is STARTING, STARTED
	 */
	public boolean isRunning() {
		return this == STARTING || this == STARTED;
	}

	/**
	 * Convenience method to decide if a status indicates execution was
	 * unsuccessful.
	 * 
	 * @return true if the status is FAILED or greater
	 */
	public boolean isUnsuccessful() {
		return this == FAILED || this.isGreaterThan(FAILED);
	}

	/**
	 * Method used to move status values through their logical progression, and
	 * override less severe failures with more severe ones. This value is
	 * compared with the parameter and the one that has higher priority is
	 * returned. If both are STARTED or less than the value returned is the
	 * largest in the sequence STARTING, STARTED, COMPLETED. Otherwise the value
	 * returned is the maximum of the two.
	 * 
	 * @param other another status to compare to
	 * @return either this or the other status depending on their priority
	 */
	public BatchStatus upgradeTo(BatchStatus other) {
		if (isGreaterThan(STARTED) || other.isGreaterThan(STARTED)) {
			return max(this, other);
		}
		// Both less than or equal to STARTED
		if (this == COMPLETED || other == COMPLETED) {
			return COMPLETED;
		}
		return max(this, other);
	}

	/**
	 * @param other a status value to compare
	 * @return true if this is greater than other
	 */
	public boolean isGreaterThan(BatchStatus other) {
		return this.compareTo(other) > 0;
	}

	/**
	 * @param other a status value to compare
	 * @return true if this is less than other
	 */
	public boolean isLessThan(BatchStatus other) {
		return this.compareTo(other) < 0;
	}

	/**
	 * @param other a status value to compare
	 * @return true if this is less than other
	 */
	public boolean isLessThanOrEqualTo(BatchStatus other) {
		return this.compareTo(other) <= 0;
	}

	/**
	 * Converts the current status to the JSR-352 equivalent
	 * 
	 * @return JSR-352 equivalent to the current status
	 */
	public BatchStatus getBatchStatus() {
		if(this == ABANDONED) {
			return BatchStatus.ABANDONED;
		} else if(this == COMPLETED) {
			return BatchStatus.COMPLETED;
		} else if(this == STARTED) {
			return BatchStatus.STARTED;
		} else if(this == STARTING) {
			return BatchStatus.STARTING;
		} else if(this == STOPPED) {
			return BatchStatus.STOPPED;
		} else if(this == STOPPING) {
			return BatchStatus.STOPPING;
		} else {
			return BatchStatus.FAILED;
		}
	}

	/**
	 * Find a BatchStatus that matches the beginning of the given value. If no
	 * match is found, return COMPLETED as the default because has is low
	 * precedence.
	 * 
	 * @param value a string representing a status
	 * @return a BatchStatus
	 */
	public static BatchStatus match(String value) {
		for (BatchStatus status : values()) {
			if (value.startsWith(status.toString())) {
				return status;
			}
		}
		// Default match should be the lowest priority
		return COMPLETED;
	}
}
