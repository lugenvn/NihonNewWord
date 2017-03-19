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
import self.lugen.nihonnewword.utils.Constants;
import self.lugen.nihonnewword.utils.Utils;

public class GroupAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private int groupNumber;
    private ArrayList<Character> currentList;

    private LayoutInflater mInflater;
    private Context mContext;
    private CompoundButton.OnCheckedChangeListener listenner;

    public GroupAdapter(Context context, int sessNum, ArrayList<Character> currentList) {
        mContext = context;
        this.mInflater = LayoutInflater.from(context);
        groupNumber = sessNum;
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
            ((ViewHolder) holder).checkBox.setChecked(currentList.contains(Utils.getChar(position)));
            ((ViewHolder) holder).checkBox.setText(String.format(mContext.getString(R.string.group_item), Utils.getChar(position)));
        }
    }

    @Override
    public int getItemCount() {
        return groupNumber;
    }

    public void setListenner(CompoundButton.OnCheckedChangeListener listenner) {
        this.listenner = listenner;
    }

    private class OnCheckChangeListener implements CompoundButton.OnCheckedChangeListener {
        ViewHolder viewHolder;

        OnCheckChangeListener(ViewHolder holder) {
            viewHolder = holder;
        }

        @Override
        public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
            if (b) {
                boolean check = false;
                for (Character i :
                        currentList) {
                    if (i == Utils.getChar(viewHolder.getAdapterPosition())) {
                        check = true;
                        break;
                    }
                }
                if (!check) {
                    currentList.add((char) (Constants.FIRST_CHARACTER_A + viewHolder.getAdapterPosition()));
                }
            } else {
                for (int i = 0; i < currentList.size(); i++) {
                    if (Utils.getChar(viewHolder.getAdapterPosition()) == currentList.get(i)) {
                        currentList.remove(i);
                        break;
                    }
                }
            }
            if (listenner != null) listenner.onCheckedChanged(compoundButton, b);
        }
    }

    private class ViewHolder extends RecyclerView.ViewHolder {
        CheckBox checkBox;

        ViewHolder(View itemView) {
            super(itemView);
            checkBox = (CheckBox) itemView.findViewById(R.id.item_check);
        }
    }
}
