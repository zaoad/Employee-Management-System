package com.example.employee_management.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.employee_management.R;
import com.example.employee_management.activity.ShowEmployeeDetails;
import com.example.employee_management.domain.EmployeeInfo;
import com.example.employee_management.utils.Constants;

import java.io.Serializable;
import java.util.List;

public class EmployeeAdapter extends RecyclerView.Adapter<EmployeeAdapter.ItemViewHolder> {
    private Context context;
    private List<EmployeeInfo> employeeInfoList;

    public EmployeeAdapter(Context context , List<EmployeeInfo> employeeInfoList) {
        this.context=context;
        this.employeeInfoList=employeeInfoList;
    }
    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(parent.getContext());
        View view= inflater.from(parent.getContext()).inflate(R.layout.employee_card_view,parent,false);
        ItemViewHolder itemViewHolder=new ItemViewHolder(view);
        return itemViewHolder;
    }

    @Override
    public void onBindViewHolder(final ItemViewHolder holder, int position) {
        final EmployeeInfo employeeInfo = employeeInfoList.get(position);

        holder.id.setText("ID : " + employeeInfo.get_id());
        holder.name.setText("Name :"+employeeInfo.getName());
        holder.card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(view.getContext(), ShowEmployeeDetails.class);
                intent.putExtra(Constants.ID, ""+employeeInfo.get_id());
                intent.putExtra(Constants.NAME, employeeInfo.getName());
                intent.putExtra(Constants.AGE, employeeInfo.getAge());
                intent.putExtra(Constants.GENDER, employeeInfo.getGender());
                intent.putExtra(Constants.PICTURE,employeeInfo.getPicture());
//                    Bundle bundle=new Bundle();
//                    bundle.putSerializable(Constants.EMPLOYEE_OBJECT, employeeInfo);
//                    intent.putExtras(bundle);
                    //Log.e("Delivery Addres:" , "gece");
                    view.getContext().startActivity(intent);

            }
        });
    }

    @Override
    public int getItemCount() {
        if(employeeInfoList!=null)
            return employeeInfoList.size();
        else
            return 0;
    }

    public  static  class ItemViewHolder extends RecyclerView.ViewHolder {
        private CardView card;
        private TextView id, name;
        private RelativeLayout relativeLayout;
        public ItemViewHolder(View itemView) {
            super(itemView);
            card = itemView.findViewById(R.id.cvItem);;
            relativeLayout=itemView.findViewById(R.id.cardlayout);
            id=itemView.findViewById(R.id.id);
            name=itemView.findViewById(R.id.name);
        }
    }
    public void clear() {

        employeeInfoList.clear();

        notifyDataSetChanged();

    }



// Add a list of items -- change to type used

    public void addAll(List<EmployeeInfo> list) {

        employeeInfoList.addAll(list);

        notifyDataSetChanged();

    }
}
