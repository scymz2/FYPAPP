package com.mdp.fypapp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mdp.fypapp.Model.Question;
import com.mdp.fypapp.R;

import java.util.ArrayList;
import java.util.List;

public class OptionAdapter extends RecyclerView.Adapter<OptionAdapter.MyViewHolder>{

    private List<String> options = new ArrayList<>();
    private Question question;
    private Context context;

    public OptionAdapter(Question question){
        this.question = question;
        options.add(question.getOption1());
        options.add(question.getOption2());
        options.add(question.getOption3());
        options.add(question.getOption4());
    }

    @NonNull
    @Override
    public OptionAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.option_item, parent, false);
        return new OptionAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final OptionAdapter.MyViewHolder holder, final int position) {
        holder.option.setText(options.get(position));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, options.get(position),Toast.LENGTH_SHORT).show();
                question.setAnswer(options.get(position));
                notifyDataSetChanged();
            }
        });
        if(question.getAnswer() == options.get(position)){
            holder.itemView.setBackgroundResource(R.drawable.option_item_selected_bg);
        }else{
            holder.itemView.setBackgroundResource(R.drawable.option_item_bg);
        }
    }

    @Override
    public int getItemCount() {
        return options.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        TextView option;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            option = itemView.findViewById(R.id.quiz_option);
        }
    }
}
