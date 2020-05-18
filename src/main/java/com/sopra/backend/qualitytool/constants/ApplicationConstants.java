package com.sopra.backend.qualitytool.constants;

public final class ApplicationConstants {
   /* Constants for Design Review File read operations */
	public static final String FILE_SHEET = "Review Record";
	public static final String RELEASE_REGEX="Release No.*";
	public static final String WORK_PRODUCT="Work Product";
	public static final String WORK_PRODUCT_REGEX=".*In Work Product.*";
	public static final String REVIEW_TYPE_REGEX="Defect (D) / Suggestion (S) / Query(Q) / Invalid(I)";
	public static final String CLASS_OF_BUGS="Class of Bug";
	public static final String PACK_REGEX="pack";
	public static final String PACK_NAME_REGEX="PACK_";
	public static final String DESIGN_REGEX="design";
	public static final String DESIGN_NAME_REGEX="Design";
	public static final String CODE_NAME_REGEX="Code";
	
	/* Constants for Quality Data Write Operations */
	public static final int QUALITY_SHEET_NUMBER = 0;
	public static final String PACK_REGEX_WRITER="Pack";
	public static final String MODULE_REGEX_WRITER="Module";
	public static final String GROUP_REGEX_WRITER="Group";
	public static final String CUF_REGEX_WRITER="CUF";
	public static final String PHASE_REGEX_WRITER="Phase";
	public static final String EFFORTS_REGEX_WRITER="Efforts";
	public static final String BLOCKER_REGEX_WRITER= "Blocker";
	public static final String MAJOR_REGEX_WRITER="Major";
	public static final String MINOR_REGEX_WRITER="Minor";
	public static final String DDBD_REGEX_WRITER="DDbd";
	public static final String RECORD_UPDATED_REGEX_WRITER="RecordUpdated";
}
