package board.bbs.model;

public class LikeDTO {
	private String no;
	private String username;
	private String likedate;
	
	public LikeDTO() {}
	
	public LikeDTO(String no, String username, String likedate) {
		super();
		this.no = no;
		this.username = username;
		this.likedate = likedate;
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
	public String getLikedate() {
		return likedate;
	}
	public void setLikedate(String likedate) {
		this.likedate = likedate;
	}
	
	
	
}
