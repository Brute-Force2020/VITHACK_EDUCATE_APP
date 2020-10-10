package io.agora.student.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import io.agora.models.Course;
import io.agora.openvcall.R;
import io.agora.openvcall.ui.SplashScreenActivity;
import io.agora.openvcall.ui.homePage;
import io.agora.student.interfaces.ItemClickInterface;

import java.util.List;

public class TopicAdapter extends RecyclerView.Adapter<TopicAdapter.TopicViewHolder> {

    List<String> topicList;
    ItemClickInterface itemClickInterface;
    Context context;

    public TopicAdapter(List<String> topicList, ItemClickInterface itemClickInterface, Context context) {
        this.topicList = topicList;
        this.itemClickInterface = itemClickInterface;
        this.context = context;
    }

    @NonNull
    @Override
    public TopicViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new TopicViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.topic_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final TopicViewHolder holder, int position) {
        final String topic = topicList.get(position);
        holder.progressBar.setVisibility(View.GONE);
        holder.tvTopic.setText(topic);

        switch (topic) {
            case "coding":
                holder.image.setImageResource(R.drawable.coding);
                break;
            case "high_school":
                holder.image.setImageResource(R.drawable.high_school);
                break;
            case "college":
                holder.image.setImageResource(R.drawable.college);
                break;
            case "music":
                holder.image.setImageResource(R.drawable.music);
                break;
            case "dance":
                holder.image.setImageResource(R.drawable.dance);
                break;
        }

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //itemClickInterface.clickTopic(topic);
                Intent intent = new Intent(context, homePage.class);
                intent.putExtra("dept","Student");
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return topicList.size();
    }

    public static class TopicViewHolder extends RecyclerView.ViewHolder {
        private TextView tvTopic;
        private ImageView image;
        private ProgressBar progressBar;
        private CardView cardView;

        public TopicViewHolder(@NonNull final View itemView) {
            super(itemView);
            tvTopic = itemView.findViewById(R.id.tvTitle);
            image = itemView.findViewById(R.id.image);
            progressBar = itemView.findViewById(R.id.progress_bar);
            cardView = itemView.findViewById(R.id.cardView);
        }
    }
}
