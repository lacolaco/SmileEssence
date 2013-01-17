package net.miz_hi.warotter.model;

import net.miz_hi.warotter.model.Consumers.Consumer;
import twitter4j.auth.AccessToken;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "accounts")
public class Account
{

	@DatabaseField(generatedId = true)
	private Integer id;
	@DatabaseField
	private String consumerKey;
	@DatabaseField
	private String consumerSecret;
	@DatabaseField
	private String accessToken;
	@DatabaseField
	private String accessTokenSecret;
	@DatabaseField
	private String screenName;
	@DatabaseField
	private Long userId;

	public Account()
	{
	}

	public Account(AccessToken token, Consumer consumer)
	{
		this.accessToken = token.getToken();
		this.accessTokenSecret = token.getTokenSecret();
		this.screenName = token.getScreenName();
		this.userId = token.getUserId();
		this.setConsumerKey(consumer.key);
		this.setConsumerSecret(consumer.secret);
	}

	public Integer getId()
	{
		return id;
	}

	public void setId(Integer id)
	{
		this.id = id;
	}

	public String getAccessToken()
	{
		return accessToken;
	}

	public void setAccessToken(String accessToken)
	{
		this.accessToken = accessToken;
	}

	public String getAccessTokenSecret()
	{
		return accessTokenSecret;
	}

	public void setAccessTokenSecret(String accessTokenSecret)
	{
		this.accessTokenSecret = accessTokenSecret;
	}

	public String getScreenName()
	{
		return screenName;
	}

	public void setScreenName(String screenName)
	{
		this.screenName = screenName;
	}

	public Long getUserId()
	{
		return userId;
	}

	public void setUserId(Long userId)
	{
		this.userId = userId;
	}

	public String getConsumerKey()
	{
		return consumerKey;
	}

	public void setConsumerKey(String consumerKey)
	{
		this.consumerKey = consumerKey;
	}

	public String getConsumerSecret()
	{
		return consumerSecret;
	}

	public void setConsumerSecret(String consumerSecret)
	{
		this.consumerSecret = consumerSecret;
	}
}
