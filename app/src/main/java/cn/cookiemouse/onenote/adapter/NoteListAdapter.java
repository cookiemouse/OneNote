package cn.cookiemouse.onenote.adapter;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import cn.cookiemouse.onenote.DatabaseOperator;
import cn.cookiemouse.onenote.R;
import cn.cookiemouse.onenote.data.NoteListData;

/**
 * Created by cookiemouse on 17-6-21.
 */

public class NoteListAdapter extends BaseAdapter {

    private static final String TAG = "NoteListAdapter";

    private static final int TYPE_ON = 0;
    private static final int TYPE_OFF = 1;

    private Context context;
    private List<NoteListData> mNoteListDataList;

    private OnControlListener mOnControlListener;
    private int position;

    public NoteListAdapter(Context context, List<NoteListData> mNoteListDataList) {
        this.context = context;
        this.mNoteListDataList = mNoteListDataList;
        this.mOnControlListener = (OnControlListener) context;
    }

    @Override
    public int getCount() {
        return mNoteListDataList.size();
    }

    @Override
    public Object getItem(int i) {
        return mNoteListDataList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public int getItemViewType(int position) {
//        return super.getItemViewType(position);
        if (mNoteListDataList.get(position).isState()) {
            return TYPE_ON;
        }
        return TYPE_OFF;
    }

    @Override
    public int getViewTypeCount() {
//        return super.getViewTypeCount();
        return 2;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {

        position = i;

        ViewHolderOff viewHolderOff;
        final ViewHolderOn viewHolderOn;

        final NoteListData data = mNoteListDataList.get(i);

        if (TYPE_ON == getItemViewType(i)) {
            if (null == view) {
                viewHolderOn = new ViewHolderOn();
                view = LayoutInflater.from(context).inflate(R.layout.adapter_notelist_on
                        , viewGroup, false);

                viewHolderOn.textViewText = view.findViewById(R.id.tv_adapter_notelist_on);

                viewHolderOn.imageViewBottomDelete = view.findViewById(R.id.iv_adapter_layout_bottom_1);
                viewHolderOn.imageViewBottomEdit = view.findViewById(R.id.iv_adapter_layout_bottom_2);
                viewHolderOn.imageViewBottomCopy = view.findViewById(R.id.iv_adapter_layout_bottom_3);
                viewHolderOn.imageViewBottomPlay = view.findViewById(R.id.iv_adapter_layout_bottom_4);
                view.setTag(viewHolderOn);
            } else {
                viewHolderOn = (ViewHolderOn) view.getTag();
            }
            viewHolderOn.textViewText.setText(data.getText());
            viewHolderOn.imageViewBottomDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (null == mOnControlListener){
                        throw new NullPointerException("OnControlListener is null");
                    }
                    mOnControlListener.onDelete(position);
                }
            });
            viewHolderOn.imageViewBottomEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (null == mOnControlListener){
                        throw new NullPointerException("OnControlListener is null");
                    }
                    mOnControlListener.onEdit(position);
                }
            });
            viewHolderOn.imageViewBottomCopy.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (null == mOnControlListener){
                        throw new NullPointerException("OnControlListener is null");
                    }
                    ClipboardManager cm = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
                    // 将文本内容放到系统剪贴板里。
                    ClipData mClipData = ClipData.newPlainText("Label", data.getText());
                    cm.setPrimaryClip(mClipData);
                }
            });
            viewHolderOn.imageViewBottomPlay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (null == mOnControlListener){
                        throw new NullPointerException("OnControlListener is null");
                    }
                    mOnControlListener.onPlay(position);
                }
            });

        } else {
            if (null == view) {
                viewHolderOff = new ViewHolderOff();
                view = LayoutInflater.from(context).inflate(R.layout.adapter_notelist_off
                        , viewGroup, false);

                viewHolderOff.textViewText = view.findViewById(R.id.tv_adapter_notelist_off);
                view.setTag(viewHolderOff);
            } else {
                viewHolderOff = (ViewHolderOff) view.getTag();
            }
            viewHolderOff.textViewText.setText(data.getText());
        }

        return view;
    }

    private class ViewHolderOff {
        TextView textViewText;
        //还有其他
    }

    private class ViewHolderOn {
        TextView textViewText;
        //还有其他
        private ImageView imageViewBottomDelete;
        private ImageView imageViewBottomEdit;
        private ImageView imageViewBottomCopy;
        private ImageView imageViewBottomPlay;
    }

    public interface OnControlListener{
        void onDelete(int position);
        void onEdit(int position);
        void onPlay(int position);
    }
}
