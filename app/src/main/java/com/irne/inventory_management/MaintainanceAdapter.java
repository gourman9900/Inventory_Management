package com.irne.inventory_management;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MaintainanceAdapter extends RecyclerView.Adapter<MaintainanceAdapter.ViewHolder> {
    private List<Maintainance> listData;

    public MaintainanceAdapter(List<Maintainance> listData) {
        this.listData = listData;
    }

    @NonNull
    @Override
    public MaintainanceAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.maintainance_data,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MaintainanceAdapter.ViewHolder holder, int position) {
        Maintainance maitainancedata = listData.get(position);
        holder.maintainance_list_id.setText(maitainancedata.getMaintainance_id());
        holder.maintainance_list_part.setText(maitainancedata.getParts());
        holder.maitainance_list_date.setText(maitainancedata.getMaintainance_date());
        holder.maintainance_list_remark.setText(maitainancedata.getRemarks());
    }

    @Override
    public int getItemCount() {
        return listData.size();
    }

    public  class ViewHolder extends RecyclerView.ViewHolder{
        private TextView maintainance_list_part,maitainance_list_date,maintainance_list_remark,maintainance_list_id;
        private ImageView image_maintainance;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            maintainance_list_id = (TextView) itemView.findViewById(R.id.maintainance_list_id);
            maintainance_list_part = (TextView) itemView.findViewById(R.id.maintainance_list_part);
            maitainance_list_date = (TextView) itemView.findViewById(R.id.maintainance_list_date);
            maintainance_list_remark = (TextView) itemView.findViewById(R.id.maintainance_list_remarks);
            image_maintainance = (ImageView) itemView.findViewById(R.id.image_maintainance);
        }
    }
}
