package board.util;

public enum PathKeys {
	INDEX("INDEX"),LOGIN("LOGIN"),INDEX_VIEW("INDEX-VIEW"),LOGIN_VIEW("LOGIN-VIEW"),
	JOIN_VIEW("JOIN-VIEW"),EDIT("EDIT"),EDIT_VIEW("EDIT-VIEW"),IMG_PROFILE_BOARD("IMG-PROFILE-BOARD");
	
	String key;
	
	PathKeys(String key) {
		this.key=key;
	}
	
	public String getValue() {
		return key;
	}

}
