package board.member.model;

import java.util.List;

import board.bbs.model.BBSDTO;

public class MemberDTO {
	private String username;
	private String password;
	private String gender;
	private String inters;
	private String education;
	private String self_intro;
	private String self_image;
	private String joinDate;
	private String content_type;
	private List<BBSDTO> bbs;
	
	public MemberDTO() {}

	
	
	public MemberDTO(String username, String password, String gender, String inters, String education,
			String self_intro, String self_image, String joinDate, String content_type, List<BBSDTO> bbs) {
		this.username = username;
		this.password = password;
		this.gender = gender;
		this.inters = inters;
		this.education = education;
		this.self_intro = self_intro;
		this.self_image = self_image;
		this.joinDate = joinDate;
		this.content_type = content_type;
		this.bbs = bbs;
	}

	

	public List<BBSDTO> getBbs() {
		return bbs;
	}

	public void setBbs(List<BBSDTO> bbs) {
		this.bbs = bbs;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getInters() {
		return inters;
	}

	public void setInters(String inters) {
		this.inters = inters;
	}

	public String getEducation() {
		return education;
	}

	public void setEducation(String education) {
		this.education = education;
	}

	public String getSelf_intro() {
		return self_intro;
	}

	public void setSelf_intro(String self_intro) {
		this.self_intro = self_intro;
	}

	public String getSelf_image() {
		return self_image;
	}

	public void setSelf_image(String self_image) {
		this.self_image = self_image;
	}

	public String getJoinDate() {
		return joinDate;
	}

	public void setJoinDate(String joinDate) {
		this.joinDate = joinDate;
	}

	public String getContent_type() {
		return content_type;
	}

	public void setContent_type(String content_type) {
		this.content_type = content_type;
	}


	
	
	
	
}
