package net.lacolaco.smileessence.entity;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

@Table(name = "Accounts")
public class Account extends Model
{

    @Column(name = "Token", notNull = true)
    public String accessToken;
    @Column(name = "Secret", notNull = true)
    public String accessSecret;

    public Account()
    {
        super();
    }

    public Account(String token, String tokenSecret)
    {
        super();
        this.accessToken = token;
        this.accessSecret = tokenSecret;
    }
}
