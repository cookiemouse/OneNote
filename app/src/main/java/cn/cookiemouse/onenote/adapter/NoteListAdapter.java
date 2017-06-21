package cn.cookiemouse.onenote.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.iflytek.cloud.thirdparty.V;

import java.util.List;

import cn.cookiemouse.onenote.R;
import cn.cookiemouse.onenote.data.NoteListData;

/**
 * Created by cookiemouse on 17-6-21.
 */

public class NoteListAdapter extends BaseAdapter {

    private static final int TYPE_ON = 0;
    private static final int TYPE_OFF = 1;

    private Context context;
    private List<NoteListData> mNoteListDataList;

    public NoteListAdapter(Context context, List<NoteListData> mNoteListDataList) {
        this.context = context;
        this.mNoteListDataList = mNoteListDataList;
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
    public View getView(int i, View view, ViewGroup viewGroup) {

        ViewHolderOff viewHolderOff;
        ViewHolderOn viewHolderOn;

        NoteListData data = mNoteListDataList.get(i);

        if (TYPE_ON == getItemViewType(i)) {
            if (null == view) {
                viewHolderOn = new ViewHolderOn();
                view = LayoutInflater.from(context).inflate(R.layout.adapter_notelist_on, viewGroup, false);

                viewHolderOn.textViewText = view.findViewById(R.id.tv_adapter_notelist_on);
                view.setTag(viewHolderOn);
            } else {
                viewHolderOn = (ViewHolderOn) view.getTag();
            }
//            viewHolderOn.textViewText.setText("");
        } else {
            if (null == view) {
                viewHolderOff = new ViewHolderOff();
                view = LayoutInflater.from(context).inflate(R.layout.adapter_notelist_off, viewGroup, false);

                viewHolderOff.textViewText = view.findViewById(R.id.tv_adapter_notelist_off);
                view.setTag(viewHolderOff);
            } else {
                viewHolderOff = (ViewHolderOff) view.getTag();
            }
//            viewHolderOff.textViewText.setText("");
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
    }
}
