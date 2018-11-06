package com.developerdesk.xmppchat.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.developerdesk.xmppchat.Interface.UserCallback;
import com.developerdesk.xmppchat.R;
import com.developerdesk.xmppchat.datamodel.ChatDataModel;
import com.developerdesk.xmppchat.datamodel.UserModel;

import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {

    private List<UserModel> userModelList;
    private UserCallback userCallback;

    public UserAdapter(List<UserModel> userModelList,UserCallback userCallback)
    {
        this.userModelList = userModelList;
        this.userCallback = userCallback;
    }


    @NonNull
    @Override
    public UserAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from( parent.getContext()).inflate(R.layout.row_user,null);


        return new UserAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserAdapter.ViewHolder holder, final int position) {


        holder.userName.setText(userModelList.get(position).getUserName());
        holder.parentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userCallback.onUserSelect(position);
            }
        });


    }




    @Override
    public int getItemCount() {
        return userModelList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder
    {

        TextView userName;
        View parentView;

        public ViewHolder(View itemView) {
            super(itemView);


            userName = itemView.findViewById(R.id.userName);
            parentView = itemView;
        }
    }

}