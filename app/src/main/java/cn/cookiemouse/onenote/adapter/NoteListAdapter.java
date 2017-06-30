package cn.cookiemouse.onenote.adapter;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

import cn.cookiemouse.onenote.R;
import cn.cookiemouse.onenote.data.DataGrade;
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
    public View getView(final int position, View view, ViewGroup viewGroup) {

        ViewHolderOff viewHolderOff;
        final ViewHolderOn viewHolderOn;

        final NoteListData data = mNoteListDataList.get(position);

        if (TYPE_ON == getItemViewType(position)) {
            if (null == view) {
                viewHolderOn = new ViewHolderOn();
                view = LayoutInflater.from(context).inflate(R.layout.adapter_notelist_on
                        , viewGroup, false);

                viewHolderOn.textViewText = view.findViewById(R.id.tv_adapter_notelist_on);
                viewHolderOn.textViewTime = view.findViewById(R.id.tv_adapter_notelist_on_time);

                viewHolderOn.imageViewBottomDelete = view.findViewById(R.id.iv_adapter_layout_bottom_1);
                viewHolderOn.imageViewBottomEdit = view.findViewById(R.id.iv_adapter_layout_bottom_2);
                viewHolderOn.imageViewBottomCopy = view.findViewById(R.id.iv_adapter_layout_bottom_3);
                viewHolderOn.imageViewBottomPlay = view.findViewById(R.id.iv_adapter_layout_bottom_4);

                viewHolderOn.imageViewTopNormal = view.findViewById(R.id.iv_adapter_layout_top_1);
                viewHolderOn.imageViewTopImportant = view.findViewById(R.id.iv_adapter_layout_top_2);
                viewHolderOn.imageViewTopMajor = view.findViewById(R.id.iv_adapter_layout_top_3);
                viewHolderOn.imageViewTopMust = view.findViewById(R.id.iv_adapter_layout_top_4);

                viewHolderOn.linearLayoutBackground = view.findViewById(R.id.ll_adapter_notelist_on);
                view.setTag(viewHolderOn);
            } else {
                viewHolderOn = (ViewHolderOn) view.getTag();
            }

            viewHolderOn.imageViewBottomDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (null == mOnControlListener) {
                        throw new NullPointerException("OnControlListener is null");
                    }
                    mOnControlListener.onDelete(position);
                }
            });
            viewHolderOn.imageViewBottomEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (null == mOnControlListener) {
                        throw new NullPointerException("OnControlListener is null");
                    }
                    mOnControlListener.onEdit(position);
                }
            });
            viewHolderOn.imageViewBottomCopy.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (null == mOnControlListener) {
                        throw new NullPointerException("OnControlListener is null");
                    }
                    mOnControlListener.onCopyed(position);
                    ClipboardManager cm = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
                    // 将文本内容放到系统剪贴板里。
                    ClipData mClipData = ClipData.newPlainText("Label", data.getText());
                    cm.setPrimaryClip(mClipData);
                }
            });
            viewHolderOn.imageViewBottomPlay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (null == mOnControlListener) {
                        throw new NullPointerException("OnControlListener is null");
                    }
                    mOnControlListener.onPlay(position);
                }
            });

            viewHolderOn.imageViewTopNormal.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (null == mOnControlListener) {
                        throw new NullPointerException("OnControlListener is null");
                    }
                    viewHolderOn.linearLayoutBackground.setBackgroundResource(R.drawable.bg_item_green);
                    mOnControlListener.onGradeChanged(position, DataGrade.GRADE_NORMAL);
                }
            });

            viewHolderOn.imageViewTopImportant.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (null == mOnControlListener) {
                        throw new NullPointerException("OnControlListener is null");
                    }
                    viewHolderOn.linearLayoutBackground.setBackgroundResource(R.drawable.bg_item_blue);
                    mOnControlListener.onGradeChanged(position, DataGrade.GRADE_IMPORTANT);
                }
            });

            viewHolderOn.imageViewTopMajor.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (null == mOnControlListener) {
                        throw new NullPointerException("OnControlListener is null");
                    }
                    viewHolderOn.linearLayoutBackground.setBackgroundResource(R.drawable.bg_item_yellow);
                    mOnControlListener.onGradeChanged(position, DataGrade.GRADE_MAJOR);
                }
            });

            viewHolderOn.imageViewTopMust.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (null == mOnControlListener) {
                        throw new NullPointerException("OnControlListener is null");
                    }
                    viewHolderOn.linearLayoutBackground.setBackgroundResource(R.drawable.bg_item_orange);
                    mOnControlListener.onGradeChanged(position, DataGrade.GRADE_MUST);
                }
            });

            switch (data.getGrade()) {
                case DataGrade.GRADE_NORMAL: {
                    viewHolderOn.linearLayoutBackground.setBackgroundResource(R.drawable.bg_item_green);
                    break;
                }
                case DataGrade.GRADE_IMPORTANT: {
                    viewHolderOn.linearLayoutBackground.setBackgroundResource(R.drawable.bg_item_blue);
                    break;
                }
                case DataGrade.GRADE_MAJOR: {
                    viewHolderOn.linearLayoutBackground.setBackgroundResource(R.drawable.bg_item_yellow);
                    break;
                }
                case DataGrade.GRADE_MUST: {
                    viewHolderOn.linearLayoutBackground.setBackgroundResource(R.drawable.bg_item_orange);
                    break;
                }
                default: {
                    viewHolderOn.linearLayoutBackground.setBackgroundResource(R.drawable.bg_item_green);
                    break;
                }
            }
            viewHolderOn.textViewText.setText(data.getText());
            viewHolderOn.textViewTime.setText(data.getTime());

        } else {
            if (null == view) {
                viewHolderOff = new ViewHolderOff();
                view = LayoutInflater.from(context).inflate(R.layout.adapter_notelist_off
                        , viewGroup, false);

                viewHolderOff.textViewText = view.findViewById(R.id.tv_adapter_notelist_off);
                viewHolderOff.relativeLayoutBackground = view.findViewById(R.id.rl_adapter_notelist_off);

                view.setTag(viewHolderOff);
            } else {
                viewHolderOff = (ViewHolderOff) view.getTag();
            }
            viewHolderOff.textViewText.setText(data.getText());

            switch (data.getGrade()) {
                case DataGrade.GRADE_NORMAL: {
                    viewHolderOff.relativeLayoutBackground.setBackgroundResource(R.color.color_Green);
                    break;
                }
                case DataGrade.GRADE_IMPORTANT: {
                    viewHolderOff.relativeLayoutBackground.setBackgroundResource(R.color.color_LightBlue);
                    break;
                }
                case DataGrade.GRADE_MAJOR: {
                    viewHolderOff.relativeLayoutBackground.setBackgroundResource(R.color.color_LightYellow);
                    break;
                }
                case DataGrade.GRADE_MUST: {
                    viewHolderOff.relativeLayoutBackground.setBackgroundResource(R.color.color_LightOrange);
                    break;
                }
                default: {
                    viewHolderOff.relativeLayoutBackground.setBackgroundResource(R.color.color_Green);
                    break;
                }
            }
        }

        return view;
    }

    private class ViewHolderOff {
        TextView textViewText;
        //还有其他
        RelativeLayout relativeLayoutBackground;
    }

    private class ViewHolderOn {
        TextView textViewText;
        TextView textViewTime;
        //还有其他
        ImageView imageViewBottomDelete;
        ImageView imageViewBottomEdit;
        ImageView imageViewBottomCopy;
        ImageView imageViewBottomPlay;

        ImageView imageViewTopNormal;
        ImageView imageViewTopImportant;
        ImageView imageViewTopMajor;
        ImageView imageViewTopMust;

        LinearLayout linearLayoutBackground;
    }

    public interface OnControlListener {
        void onDelete(int position);

        void onEdit(int position);

        void onCopyed(int position);

        void onPlay(int position);

        void onGradeChanged(int position, int grade);
    }
}
