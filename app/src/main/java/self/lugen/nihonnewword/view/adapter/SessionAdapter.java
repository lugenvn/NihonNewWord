package self.lugen.nihonnewword.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import java.util.ArrayList;

import self.lugen.nihonnewword.R;

/**
 * Created by lugen on 3/15/17.
 */

public class SessionAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private int sessionNumber;
    private ArrayList<Integer> currentList;

    private LayoutInflater mInflater;
    private Context mContext;
    private CompoundButton.OnCheckedChangeListener listenner;

    public SessionAdapter(Context context, int sessNum, ArrayList<Integer> currentList) {
        mContext = context;
        this.mInflater = LayoutInflater.from(context);
        sessionNumber = sessNum;
        this.currentList = currentList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = mInflater.inflate(R.layout.item_session_dialog, parent, false);
        ViewHolder holder = new ViewHolder(v);
        holder.checkBox.setOnCheckedChangeListener(new OnCheckChangeListener(holder));
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ViewHolder) {
            ((ViewHolder) holder).checkBox.setChecked(currentList.contains(position));
            ((ViewHolder) holder).checkBox.setText(String.format(mContext.getString(R.string.session_item), position
                    + 1));
        }
    }

    @Override
    public int getItemCount() {
        return sessionNumber;
    }

    public ArrayList<Integer> retResult() {
        return currentList;
    }

    public void setListenner(CompoundButton.OnCheckedChangeListener listenner) {
        this.listenner = listenner;
    }

    class OnCheckChangeListener implements CompoundButton.OnCheckedChangeListener {
        ViewHolder viewHolder;

        public OnCheckChangeListener(ViewHolder holder) {
            viewHolder = holder;
        }

        @Override
        public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
            if (b) {
                boolean check = false;
                for (Integer i :
                        currentList) {
                    if (i == viewHolder.getAdapterPosition()) {
                        check = true;
                        break;
                    }
                }
                if (!check) {
                    currentList.add(viewHolder.getAdapterPosition());
                }
            } else {
                for (int i = 0; i < currentList.size(); i++) {
                    if (viewHolder.getAdapterPosition() == currentList.get(i)) {
                        currentList.remove(i);
                        break;
                    }
                }
            }
            if (listenner != null) listenner.onCheckedChanged(compoundButton, b);
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        CheckBox checkBox;

        public ViewHolder(View itemView) {
            super(itemView);
            checkBox = (CheckBox) itemView.findViewById(R.id.item_check);
        }
    }
}
