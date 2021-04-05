package com.mdp.fypapp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.view.menu.MenuView;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.mdp.fypapp.Model.Quiz;
import com.mdp.fypapp.R;

import java.util.ArrayList;
import java.util.List;

public class QuizAdapter extends RecyclerView.Adapter<QuizAdapter.MyViewHolder> {

    List<Quiz> quizzes = new ArrayList<>();

    public QuizAdapter(List<Quiz> quizzes) {
        this.quizzes = quizzes;
    }

    @NonNull
    @Override
    public QuizAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.quiz_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull QuizAdapter.MyViewHolder holder, int position) {
        holder.title.setText(quizzes.get(position).getTitle());
    }

    @Override
    public int getItemCount() {
        return quizzes.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        TextView title;
        ImageView iconView;
        CardView cardContainer;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.quizTitle);
            iconView = itemView.findViewById(R.id.quizIcon);
            cardContainer = itemView.findViewById(R.id.cardContainer);
        }
    }
}
