package com.developerdesk.xmppchat.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.developerdesk.xmppchat.R;
import com.developerdesk.xmppchat.datamodel.ChatDataModel;

import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ViewHolder> {

    private List<ChatDataModel> dataModelList;

    public ChatAdapter(List<ChatDataModel> dataModelList)
    {
        this.dataModelList = dataModelList;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from( parent.getContext()).inflate(R.layout.row_chat_field,null);


        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

if (dataModelList.get(position).isOwnMessage())
{
    holder.leftView.setVisibility(View.GONE);
    holder.righView.setVisibility(View.INVISIBLE);
}
else
{
    holder.righView.setVisibility(View.GONE);
    holder.leftView.setVisibility(View.INVISIBLE);
}

        holder.textView.setText(dataModelList.get(position).getMessage());

    }

    @Override
    public int getItemCount() {
        return dataModelList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder
    {
        View leftView;
        View righView;
        TextView textView;

        public ViewHolder(View itemView) {
            super(itemView);

            leftView = itemView.findViewById(R.id.leftView);
            righView = itemView.findViewById(R.id.rightView);
            textView = itemView.findViewById(R.id.chatMessage);
        }
    }

}
