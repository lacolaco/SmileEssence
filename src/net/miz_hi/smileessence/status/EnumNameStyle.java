package net.miz_hi.smileessence.status;

import net.miz_hi.smileessence.Client;
import net.miz_hi.smileessence.R;

public enum EnumNameStyle
{
	
	S_N(Client.getString(R.string.namestyle_s_n)),
	N_S(Client.getString(R.string.namestyle_n_s)),
	S(Client.getString(R.string.namestyle_s)),
	N(Client.getString(R.string.namestyle_n));
	
	private final String str;
	
	private EnumNameStyle(String s)
	{
		this.str = s; 
	}
	
	public String get()
	{
		return str;
	}
}
