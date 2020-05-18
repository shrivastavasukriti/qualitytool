
package com.sopra.backend.qualitytool.constants;

public final class ApplicationConstants {
	/* Constants for Design Review File read operations */
	public static final String FILE_SHEET = "Review Record";
	public static final String RELEASE_REGEX = "Release No.*";
	public static final String WORK_PRODUCT = "Work Product";
	public static final String WORK_PRODUCT_REGEX = ".*In Work Product.*";
	public static final String REVIEW_TYPE_REGEX = "Defect (D) / Suggestion (S) / Query(Q) / Invalid(I)";
	public static final String CLASS_OF_BUGS = "Class of Bug";
	public static final String PACK_REGEX = "pack";
	public static final String DESIGN_REGEX = "design";

	/* Constants for Quality Data Write Operations */
	public static final int QUALITY_SHEET_NUMBER = 0;
	public static final String PACK_REGEX_WRITER = "Pack";
	public static final String PACK_NAME_REGEX = "PACK_";
	public static final String MODULE_REGEX_WRITER = "Module";
	public static final String GROUP_REGEX_WRITER = "Group";
	public static final String CUF_REGEX_WRITER = "CUF";
	public static final String PHASE_REGEX_WRITER = "Phase";
	public static final String EFFORTS_REGEX_WRITER = "Efforts";
	public static final String BLOCKER_REGEX_WRITER = "Blocker";
	public static final String MAJOR_REGEX_WRITER = "Major";
	public static final String MINOR_REGEX_WRITER = "Minor";
	public static final String DDBD_REGEX_WRITER = "DDbd";
	public static final String RECORD_UPDATED_REGEX_WRITER = "RecordUpdated";

	/* Constants for Test Execution File read operations */
	public static final Integer TEST_EXECUTION_FILE_SHEET_INDEX = 0;
	public static final String TEST_EXECUTION = "Test Execution";
	public static final String GCU_NAME_REGEX = "GCU";
	public static final String PRIORITY_NAME_REGEX = "Priority";

	/* Phase Names */
	public static final String TEST_EXECUTION_PHASE = "Test Execution";
	public static final String TEST_PLAN_PHASE = "Test Plan";
	public static final String DESIGN_PHASE = "Design";
	public static final String CODE_PHASE = "Code";
	public static final String SFD_PHASE = "SFD";

	/* Constants for Test Plan File read operations */
	public static final Integer TEST_PLAN_FILE_SHEET_INDEX = 0;
	public static final String PACK_COLUMN_REGEX = "Release No.";
	public static final String DEFECT_COLUMN_REGEX = "Defect (D) / Suggestion (S) / Query(Q) / Invalid(I)";
	public static final String CLASS_OF_BUG_COLUMN_REGEX = "Class of Bug";
	public static final String TEST_PLAN_GCU_COLUMN_REGEX = "GCU";

	/* Constants for SuiviCp File read operations */
	public static final String ATTERRISSAGE_SFD_COLUMN_REGEX = "Atterrissage SFD";
	public static final String CONSO_COLUMN_REGEX = "Conso";
	public static final String SFD_ROW_REGEX = "SFD";
	public static final String DESIGN_ROW_REGEX = "Design";
	public static final String DEVELOPMENT_ROW_REGEX = "Developments";
	public static final String SELECT_DATA_ROW_REGEX = "Select data";
	public static final String WRITE_TEST_PLAN_ROW_REGEX = "Write Test Plan";
	public static final String EXECUTE_TEST_PLAN_ROW_REGEX = "Execute Test Plan";
	public static final String FIX_BUGS_ROW_REGEX = "Fix Bugs";

}
