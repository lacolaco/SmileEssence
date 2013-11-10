package net.miz_hi.smileessence.view.fragment.impl;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.MediaStore.MediaColumns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.*;
import net.miz_hi.smileessence.Client;
import net.miz_hi.smileessence.R;
import net.miz_hi.smileessence.command.ICommand;
import net.miz_hi.smileessence.command.MenuCommand;
import net.miz_hi.smileessence.core.EnumRequestCode;
import net.miz_hi.smileessence.core.MyExecutor;
import net.miz_hi.smileessence.dialog.ConfirmDialog;
import net.miz_hi.smileessence.dialog.SimpleMenuDialog;
import net.miz_hi.smileessence.listener.PostEditTextListener;
import net.miz_hi.smileessence.menu.PostingMenu;
import net.miz_hi.smileessence.model.status.tweet.TweetModel;
import net.miz_hi.smileessence.notification.Notificator;
import net.miz_hi.smileessence.preference.EnumPreferenceKey;
import net.miz_hi.smileessence.status.StatusViewFactory;
import net.miz_hi.smileessence.status.TweetUtils;
import net.miz_hi.smileessence.system.MainActivitySystem;
import net.miz_hi.smileessence.system.PostSystem;
import net.miz_hi.smileessence.system.PostSystem.PostPageState;
import net.miz_hi.smileessence.util.UiHandler;
import net.miz_hi.smileessence.view.activity.MainActivity;
import net.miz_hi.smileessence.view.fragment.NamedFragment;

import java.util.ArrayList;
import java.util.List;

@SuppressLint("ValidFragment")
public class PostFragment extends NamedFragment implements OnClickListener
{

    TextView textCount;
    EditText editText;
    FrameLayout frameInReplyTo;
    ImageView imagePict;
    private static PostFragment singleton;

    public static PostFragment singleton()
    {
        return singleton;
    }

    public PostFragment()
    {
        singleton = this;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    @Override
    public String getTitle()
    {
        return "Post";
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View page = inflater.inflate(R.layout.post_layout, container, false);
        editText = (EditText) page.findViewById(R.id.editText_tweet);
        frameInReplyTo = (FrameLayout) page.findViewById(R.id.frame_inreplyto);
        imagePict = (ImageView) page.findViewById(R.id.image_pict);
        textCount = (TextView) page.findViewById(R.id.textView_count);
        Button imageButtonSubmit = (Button) page.findViewById(R.id.imBtn_tweet);
        ImageButton imageButtonDelete = (ImageButton) page.findViewById(R.id.imBtn_delete);
        ImageButton imageButtonMenu = (ImageButton) page.findViewById(R.id.imBtn_tweetmenu);
        ImageButton imageButtonPict = (ImageButton) page.findViewById(R.id.imBtn_pickpict);

        PostEditTextListener listener = new PostEditTextListener(textCount);
        editText.setTextSize(Client.getTextSize() + 3);
        editText.addTextChangedListener(listener);
        editText.setOnFocusChangeListener(listener);
        imageButtonSubmit.setOnClickListener(this);
        imageButtonDelete.setOnClickListener(this);
        imageButtonMenu.setOnClickListener(this);
        imageButtonPict.setOnClickListener(this);
        imagePict.setOnClickListener(this);

        return page;
    }

    public void update()
    {
        new UiHandler()
        {

            @Override
            public void run()
            {
                PostPageState state = PostSystem.getState();
                String text = state.getText();
                setText(text);
                int cursor = state.getCursor();
                setCursor(cursor);
                long inReplyTo = state.getInReplyToStatusId();
                setInReplyTo(inReplyTo);
                String picturePath = state.getPicturePath();
                setPicture(picturePath);
            }
        }.post();
    }

    public void load()
    {
        new UiHandler()
        {

            @Override
            public void run()
            {
                PostPageState state = PostSystem.getState();
                String text = state.getText();
                setText(text);
                int cursor = state.getCursor();
                setCursor(cursor);
                long inReplyTo = state.getInReplyToStatusId();
                setInReplyTo(inReplyTo);
                String picturePath = state.getPicturePath();
                setPicture(picturePath);
                openIme();
            }
        }.post();
    }

    /**
     * save to state: text, cursor
     */
    public void save()
    {
        if (editText != null)
        {
            PostPageState state = PostSystem.getState();
            String text = editText.getText().toString();
            state.setText(text);
            int cursor = editText.getSelectionEnd();
            state.setCursor(cursor);
        }
        hideIme();
    }

    public void setText(final String s)
    {
        if (editText == null)
        {
            return;
        }

        new UiHandler()
        {

            @Override
            public void run()
            {
                editText.setText(s);
            }
        }.post();
    }

    public void setCursor(final int i)
    {
        if (editText == null)
        {
            return;
        }

        new UiHandler()
        {

            @Override
            public void run()
            {
                if (i < 0)
                {
                    editText.setSelection(0);
                }
                else if (i > editText.getText().length())
                {
                    editText.setSelection(editText.getText().length());
                }
                else
                {
                    editText.setSelection(i);
                }

            }
        }.post();
    }

    public void setInReplyTo(final long l)
    {
        if (frameInReplyTo == null)
        {
            return;
        }

        if (l == PostSystem.NONE_ID)
        {
            new UiHandler()
            {

                @Override
                public void run()
                {
                    frameInReplyTo.setVisibility(View.GONE);
                }
            }.post();
        }
        else
        {
            MyExecutor.execute(new Runnable()
            {

                @Override
                public void run()
                {
                    try
                    {
                        TweetModel status = TweetUtils.getOrCreateStatusModel(l);
                        final View v = StatusViewFactory.newInstance(MainActivity.getInstance().getLayoutInflater(), null).getStatusView(status);
                        new UiHandler()
                        {

                            @Override
                            public void run()
                            {
                                frameInReplyTo.removeAllViews();
                                frameInReplyTo.addView(v);
                                frameInReplyTo.setVisibility(View.VISIBLE);
                            }
                        }.post();
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                }
            });
        }
    }


    public void setPicture(final String path)
    {
        if (imagePict == null)
        {
            return;
        }
        if (path == null)
        {
            imagePict.setVisibility(View.GONE);
            return;
        }
        MyExecutor.execute(new Runnable()
        {
            public void run()
            {
                new UiHandler()
                {

                    @Override
                    public void run()
                    {
                        Options opt = new Options();
                        opt.inPurgeable = true; // GC可能にする
                        opt.inSampleSize = 2;
                        Bitmap bm = BitmapFactory.decodeFile(path, opt);
                        imagePict.setImageBitmap(bm);
                        imagePict.setVisibility(View.VISIBLE);
                    }
                }.post();
            }
        });
    }

    public void clear()
    {
        editText.setText("");
        setInReplyTo(PostSystem.NONE_ID);
        PostSystem.clear(true);
        removePicture();
    }

    public void clearBySubmit()
    {
        editText.setText("");
        setInReplyTo(PostSystem.NONE_ID);
        PostSystem.clear(false);
        imagePict.setVisibility(View.GONE);
    }

    public void removePicture()
    {
        if (imagePict.isShown())
        {
            ConfirmDialog.show(MainActivity.getInstance(), "画像の投稿を取り消しますか？", new Runnable()
            {

                @Override
                public void run()
                {
                    PostSystem.getState().clearPicturePath();
                    imagePict.setVisibility(View.GONE);
                    Notificator.info("取り消しました");
                }
            });
        }
    }

    public void openIme()
    {
        if (editText == null)
        {
            return;
        }

        new UiHandler()
        {

            @Override
            public void run()
            {
                if (Client.<Boolean>getPreferenceValue(EnumPreferenceKey.OPEN_IME))
                {
                    InputMethodManager imm = (InputMethodManager) Client.getApplication().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.showSoftInput(editText, 0);
                }
            }
        }.post();
    }

    public void hideIme()
    {
        if (editText == null)
        {
            return;
        }

        new UiHandler()
        {

            @Override
            public void run()
            {
                InputMethodManager imm = (InputMethodManager) Client.getApplication().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
            }
        }.post();
    }

    private void startGallery()
    {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        getActivity().startActivityForResult(intent, EnumRequestCode.PICTURE.ordinal());
    }

    private void startCamera()
    {
        MainActivitySystem system = MainActivity.getInstance().system;
        ContentValues values = new ContentValues();
        String filename = System.currentTimeMillis() + ".jpg";
        // 必要な情報を詰める
        values.put(MediaColumns.TITLE, filename);
        values.put(MediaColumns.MIME_TYPE, "image/jpeg");

        // Uriを取得して覚えておく、Intentにも保存先として渡す
        system.tempFilePath = getActivity().getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        // インテントの設定
        Intent intent = new Intent();
        intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, system.tempFilePath);
        getActivity().startActivityForResult(intent, EnumRequestCode.CAMERA.ordinal());
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.imBtn_tweet:
            {
                if (PostSystem.submit(editText.getText().toString()))
                {
                    if (Client.<Boolean>getPreferenceValue(EnumPreferenceKey.AFTER_SUBMIT))
                    {
                        MainActivity.getInstance().getViewPager().setCurrentItem(1);
                    }
                    clearBySubmit();
                }
                break;
            }
            case R.id.imBtn_tweetmenu:
            {
                save();
                new PostingMenu(getActivity()).create().show();
                break;
            }
            case R.id.imBtn_pickpict:
            {
                save();
                SimpleMenuDialog selectImageDialog = new SimpleMenuDialog(getActivity())
                {
                    @Override
                    public List<ICommand> getMenuList()
                    {
                        List<ICommand> list = new ArrayList<ICommand>();
                        list.add(new MenuCommand()
                        {
                            @Override
                            public void workOnUiThread()
                            {
                                startGallery();
                            }

                            @Override
                            public String getName()
                            {
                                return "画像を選択";
                            }
                        });

                        list.add(new MenuCommand()
                        {
                            @Override
                            public void workOnUiThread()
                            {
                                startCamera();
                            }

                            @Override
                            public String getName()
                            {
                                return "カメラを起動";
                            }
                        });
                        return list;
                    }
                };
                selectImageDialog.create().show();
                break;
            }
            case R.id.imBtn_delete:
            {
                ConfirmDialog.show(getActivity(), "全消去しますか？", new Runnable()
                {
                    @Override
                    public void run()
                    {
                        clear();
                    }
                });
                break;
            }
            case R.id.image_pict:
            {
                removePicture();
                break;
            }
            default:
                break;
        }
    }
}