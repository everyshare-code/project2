package board.bbs.model;

import java.util.List;

public class BBSDTO {
	private String no;
	private String username;
	private String content;
	private String view_count;
	private String postdate;
	private String content_type;
	private String img;
	private String profile;
	private String profile_type;
	private List<LikeDTO> likes;
	private String tags;
	public BBSDTO() {}

	public BBSDTO(String no, String username, String content, String view_count, String postdate, String content_type,
			String img, String profile, String profile_type, List<LikeDTO> likes) {
		this.no = no;
		this.username = username;
		this.content = content;
		this.view_count = view_count;
		this.postdate = postdate;
		this.content_type = content_type;
		this.img = img;
		this.profile = profile;
		this.profile_type = profile_type;
		this.likes = likes;
	}
	
	
	

	public String getTags() {
		return tags;
	}

	public void setTags(String tags) {
		this.tags = tags;
	}

	public List<LikeDTO> getLikes() {
		return likes;
	}

	public void setLikes(List<LikeDTO> likes) {
		this.likes = likes;
	}

	public String getNo() {
		return no;
	}
	public void setNo(String no) {
		this.no = no;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getView_count() {
		return view_count;
	}
	public void setView_count(String view_count) {
		this.view_count = view_count;
	}
	public String getPostdate() {
		return postdate;
	}
	public void setPostdate(String postdate) {
		this.postdate = postdate;
	}
	public String getContent_type() {
		return content_type;
	}
	public void setContent_type(String content_type) {
		this.content_type = content_type;
	}
	public String getImg() {
		return img;
	}
	public void setImg(String img) {
		this.img = img;
	}

	public String getProfile() {
		return profile;
	}

	public void setProfile(String profile) {
		this.profile = profile;
	}

	public String getProfile_type() {
		return profile_type;
	}

	public void setProfile_type(String profile_type) {
		this.profile_type = profile_type;
	}

	
	
	
	
}
