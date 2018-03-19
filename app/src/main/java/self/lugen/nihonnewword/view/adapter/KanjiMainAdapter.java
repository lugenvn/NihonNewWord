package self.lugen.nihonnewword.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import self.lugen.nihonnewword.R;
import self.lugen.nihonnewword.utils.TrackingUtils;

public class KanjiMainAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private LayoutInflater mInflater;
    private Context mContext;
    private ArrayList<String> mData;
    private OnClickedListener listener;

    public KanjiMainAdapter(Context context, ArrayList<String> data) {
        mContext = context;
        mData = data;
        this.mInflater = LayoutInflater.from(context);

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_lesson, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        initListener(viewHolder);
        return viewHolder;
    }

    private void initListener(ViewHolder holder) {
        holder.viewMain.setOnClickListener(new OnItemClickListener(holder));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ViewHolder) {
            ((ViewHolder)holder).tvLessonName.setAllCaps(true);
            ((ViewHolder)holder).tvLessonName.setText(String.format(mContext.getString(R.string.kanji_lesson_name), mData.get(position)));
        }
    }

    private void onItemClick(int position) {
        listener.itemClicked(position);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public void setListener(OnClickedListener listener) {
        this.listener = listener;
    }

    private class OnItemClickListener implements View.OnClickListener {

        ViewHolder viewHolder;

        OnItemClickListener(ViewHolder holder) {
            viewHolder = holder;
        }

        @Override
        public void onClick(View view) {
            onItemClick(viewHolder.getAdapterPosition());
            TrackingUtils.trackingButton(mContext, TrackingUtils.ID_BUTTON, TrackingUtils.SCREEN_KANJI_LIST,
                    TrackingUtils.LESSON_KANJI + mData
                            .get(viewHolder.getAdapterPosition()));
        }
    }

    private class ViewHolder extends RecyclerView.ViewHolder {
        View viewMain;
        TextView tvLessonName;

        ViewHolder(View itemView) {
            super(itemView);
            viewMain = itemView;
            tvLessonName = (TextView) itemView.findViewById(R.id.tv_lesson_name);
        }
    }

    public interface OnClickedListener {
        void itemClicked(int pos);
    }
}
