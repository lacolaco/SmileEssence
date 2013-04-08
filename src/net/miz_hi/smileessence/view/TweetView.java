package net.miz_hi.smileessence.view;

import java.io.File;

import net.miz_hi.smileessence.Client;
import net.miz_hi.smileessence.R;
import net.miz_hi.smileessence.core.EnumRequestCode;
import net.miz_hi.smileessence.data.StatusModel;
import net.miz_hi.smileessence.dialog.YesNoDialogHelper;
import net.miz_hi.smileessence.event.ToastManager;
import net.miz_hi.smileessence.listener.TweetViewTouchListener;
import net.miz_hi.smileessence.menu.TweetMenu;
import net.miz_hi.smileessence.preference.EnumPreferenceKey;
import net.miz_hi.smileessence.status.StatusViewFactory;
import net.miz_hi.smileessence.system.MainSystem;
import net.miz_hi.smileessence.system.TweetSystem;
import net.miz_hi.smileessence.util.UiHandler;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.provider.MediaStore;
import android.provider.MediaStore.MediaColumns;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.slidingmenu.lib.SlidingMenu;
import com.slidingmenu.lib.SlidingMenu.OnCloseListener;
import com.slidingmenu.lib.SlidingMenu.OnClosedListener;
import com.slidingmenu.lib.SlidingMenu.OnOpenListener;
import com.slidingmenu.lib.SlidingMenu.OnOpenedListener;

public class TweetView
{

	private static TweetView instance;
	private SlidingMenu menu;
	private Activity activity;
	private EditText editTextTweet;
	private ListView listViewReply;
	private LinearLayout linearLayoutReply;
	private LinearLayout linearLayoutPict;

	private TweetView(Activity activity)
	{
		this.activity = activity;
		init();
	}
	
	public static void init(Activity activity)
	{
		instance = new TweetView(activity);
	}
	
	public static TweetView getInstance()
	{
		return instance;
	}
	
	public String getText()
	{
		return editTextTweet.getText().toString();
	}
	
	public int getCursor()
	{
		return editTextTweet.getSelectionEnd();
	}
	
	public void removeReply()
	{
		linearLayoutReply.removeAllViews();
	}
	
	public void setInReplyToStatus(StatusModel status)
	{
		linearLayoutReply.removeAllViews();
		View viewStatus = StatusViewFactory.getView(activity.getLayoutInflater(), status);
		linearLayoutReply.addView(viewStatus);
	}
	
	public void setPictureImage(File path)
	{
		linearLayoutPict.removeAllViews();
		if(path == null)
		{
			return;
		}
		ImageView viewImage = new ImageView(activity);
		Options opt = new Options();
		opt.inPurgeable = true; // GC可能にする
		opt.inSampleSize = 2;
		Bitmap bm = BitmapFactory.decodeFile(path.getPath(), opt);
		viewImage.setImageBitmap(bm);
		LayoutParams lp = new LayoutParams(200, 200);
		viewImage.setLayoutParams(lp);
		viewImage.setOnLongClickListener(new OnLongClickListener()
		{
			
			@Override
			public boolean onLongClick(View v)
			{
				YesNoDialogHelper.show(activity, "取り消し", "画像の投稿を取り消しますか？", new Runnable()
				{
					
					@Override
					public void run()
					{
						TweetSystem.setPicturePath(null);
						linearLayoutPict.removeAllViews();
						ToastManager.toast("取り消しました");
					}
				});
				return true;
			}
		});
		linearLayoutPict.addView(viewImage);
	}

	public static void toggle()
	{
		new UiHandler()
		{
			
			@Override
			public void run()
			{
				getInstance().menu.toggle();
			}
		}.post();
	}

	public static void open()
	{
		new UiHandler()
		{
			
			@Override
			public void run()
			{
				getInstance().menu.showMenu();
			}
		}.post();
	}
	
	public static void close()
	{
		new UiHandler()
		{
			
			@Override
			public void run()
			{
				getInstance().menu.showContent();
			}
		}.post();
	}

	public static boolean isOpening()
	{
		return getInstance().menu.isMenuShowing();
	}

	private void init()
	{
		TweetSystem.start(activity);
		createSlidingMenu();
		editTextTweet = (EditText) menu.findViewById(R.id.editText_tweet);
		final TextView textViewCount = (TextView) menu.findViewById(R.id.textView_count);
		ImageButton imageButtonSubmit = (ImageButton) menu.findViewById(R.id.imageButton_submit);
		ImageButton imageButtonClear = (ImageButton) menu.findViewById(R.id.imageButton_clean);
		ImageButton imageButtonMenu = (ImageButton)menu.findViewById(R.id.imageButton_menu);
		ImageButton imageButtonPict = (ImageButton)menu.findViewById(R.id.imageButton_pict);
		ImageButton imageButtonCamera = (ImageButton)menu.findViewById(R.id.imageButton_camera);
		linearLayoutReply = (LinearLayout)menu.findViewById(R.id.linearLayout_tweet_reply);
		linearLayoutPict = (LinearLayout)menu.findViewById(R.id.linearLayout_tweet_pict);
		
		textViewCount.setText("140");
		editTextTweet.setFocusable(true);
		editTextTweet.setTextSize(Client.getTextSize());
		editTextTweet.addTextChangedListener(new TextWatcher()
		{

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count)
			{
				textViewCount.setText(String.valueOf(140 - s.length()));
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after){}
			
			@Override
			public void afterTextChanged(Editable s){}
		});

		imageButtonSubmit.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				TweetSystem.setText(editTextTweet.getText().toString());
				TweetSystem.submit();
				editTextTweet.setText("");
				if(Client.<Boolean>getPreferenceValue(EnumPreferenceKey.AFTER_SUBMIT))
				{
					close();
				}
			}
		});

		imageButtonClear.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				YesNoDialogHelper.show(activity, "注意", "全消去しますか？", new Runnable()
				{
					@Override
					public void run()
					{
						editTextTweet.setText("");
						TweetSystem.clear();
					}
				});
			}
		});

		imageButtonMenu.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				InputMethodManager imm = (InputMethodManager) Client.getApplication().getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(editTextTweet.getWindowToken(), 0);	
				TweetMenu.show();
			}
		});
		
		imageButtonPict.setOnClickListener(new OnClickListener()
		{
			
			@Override
			public void onClick(View v)
			{
				Intent intent = new Intent(Intent.ACTION_PICK);
				intent.setType("image/*");
				activity.startActivityForResult(intent, EnumRequestCode.PICTURE.ordinal());
			}
		});
		
		imageButtonCamera.setOnClickListener(new OnClickListener()
		{
			
			@Override
			public void onClick(View v)
			{
				ContentValues values = new ContentValues();

				//ファイル名を決めて
				String filename = System.currentTimeMillis() + ".jpg";
				//必要な情報を詰める
				values.put(MediaColumns.TITLE, filename);
				values.put(MediaColumns.MIME_TYPE, "image/jpeg");
				//Uriを取得して覚えておく、Intentにも保存先として渡す
				MainSystem.getInstance().tempFilePath= activity.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

				//インテントの設定
				Intent intent = new Intent();
				intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
				intent.putExtra(MediaStore.EXTRA_OUTPUT, MainSystem.getInstance().tempFilePath);
				activity.startActivityForResult(intent, EnumRequestCode.CAMERA.ordinal());
			}
		});
	}
	
	private void onOpeningMenu()
	{
		String text = TweetSystem.getText();
		editTextTweet.setText(text);
		
		if(text.startsWith(" RT"))
		{
			editTextTweet.setSelection(0);
		}
		else
		{
			editTextTweet.setSelection(text.length());
		}
	}

	private void onOpenedMenu()
	{
		editTextTweet.setTextSize(Client.getTextSize());
		editTextTweet.requestFocus();
				
		if(Client.<Boolean>getPreferenceValue(EnumPreferenceKey.OPEN_IME))
		{
			InputMethodManager imm = (InputMethodManager) Client.getApplication().getSystemService(Context.INPUT_METHOD_SERVICE);
			imm.showSoftInput(editTextTweet, 0);
		}
	}
	
	private void onClosingMenu()
	{
		TweetSystem.setText(editTextTweet.getText().toString());
	}

	private void onClosedMenu()
	{
		InputMethodManager imm = (InputMethodManager) Client.getApplication().getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(editTextTweet.getWindowToken(), 0);
	}

	private void createSlidingMenu()
	{
		menu = new SlidingMenu(activity);
		menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
		menu.setShadowWidthRes(R.dimen.shadow_width);
		menu.setBehindOffsetRes(R.dimen.slidingmenu_offset);
		menu.setFadeDegree(0.35f);
		menu.setShadowDrawable(R.drawable.shadow);
		menu.attachToActivity(activity, SlidingMenu.SLIDING_WINDOW);
		View rootView = LayoutInflater.from(activity).inflate(R.layout.tweet_layout, null);
		menu.setMenu(rootView);
		rootView.setOnTouchListener(new TweetViewTouchListener());

		menu.setOnOpenListener(new OnOpenListener()
		{
			
			@Override
			public void onOpen()
			{
				onOpeningMenu();
			}
		});
		menu.setOnOpenedListener(new OnOpenedListener()
		{
			
			@Override
			public void onOpened()
			{
				onOpenedMenu();
			}
		});
		menu.setOnCloseListener(new OnCloseListener()
		{
			
			@Override
			public void onClose()
			{
				onClosingMenu();				
			}
		});
		menu.setOnClosedListener(new OnClosedListener()
		{
			
			@Override
			public void onClosed()
			{
				onClosedMenu();
			}
		});
	}

	public SlidingMenu getSlidingMenu()
	{
		return menu;
	}

	public EditText getEditTextTweet()
	{
		return editTextTweet;
	}
}
