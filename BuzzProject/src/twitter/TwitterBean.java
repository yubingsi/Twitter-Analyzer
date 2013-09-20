package twitter;

public class TwitterBean {
	private String tweetId;
	private String text;
	private String fromUser;
	private String fromUserName;
	private String createdAt;
	private String profileImageUrl;
	
	public TwitterBean(String tweetId,String text, String fromUser, String fromUserName,
			String createdAt, String profileImageUrl) {
		super();
		this.tweetId = tweetId;
		this.text = text;
		this.fromUser = fromUser;
		this.fromUserName = fromUserName;
		this.createdAt = createdAt;
		this.profileImageUrl = profileImageUrl;
	}

	public String getTweetId() {
		return tweetId;
	}

	public void setTweetId(String tweetId) {
		this.tweetId = tweetId;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getFromUser() {
		return fromUser;
	}

	public void setFromUser(String fromUser) {
		this.fromUser = fromUser;
	}

	public String getFromUserName() {
		return fromUserName;
	}

	public void setFromUserName(String fromUserName) {
		this.fromUserName = fromUserName;
	}

	public String getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(String createdAt) {
		this.createdAt = createdAt;
	}

	public String getProfileImageUrl() {
		return profileImageUrl;
	}

	public void setProfileImageUrl(String profileImageUrl) {
		this.profileImageUrl = profileImageUrl;
	}
	

	
	
	

	

}
