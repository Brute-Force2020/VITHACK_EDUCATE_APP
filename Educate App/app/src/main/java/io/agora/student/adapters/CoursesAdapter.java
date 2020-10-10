package io.agora.student.adapters;

import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import io.agora.models.Course;
import io.agora.openvcall.R;
import io.agora.student.interfaces.ItemClickInterface;

import java.util.List;

public class CoursesAdapter extends RecyclerView.Adapter<CoursesAdapter.CoursesViewHolder> {

    private List<Course> coursesList;
    private ItemClickInterface itemClickInterface;

    public CoursesAdapter(List<Course> coursesList, ItemClickInterface itemClickInterface) {
        this.coursesList = coursesList;
        this.itemClickInterface = itemClickInterface;
    }

    @NonNull
    @Override
    public CoursesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CoursesViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.topic_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final CoursesViewHolder holder, int position) {
        final Course course = coursesList.get(position);
        holder.tvTopic.setText(course.getCourseName());

        Glide.with(holder.itemView.getContext()).load(course.getCoursePic()).addListener(new RequestListener<Drawable>() {
            @Override
            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                holder.progressBar.setVisibility(View.INVISIBLE);
                return false;
            }

            @Override
            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                holder.progressBar.setVisibility(View.INVISIBLE);
                return false;
            }
        }).into(holder.image);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                itemClickInterface.clickCourse(course.getCourseId());
            }
        });
    }

    @Override
    public int getItemCount() {
        return coursesList.size();
    }

    public static class CoursesViewHolder extends RecyclerView.ViewHolder {
        private TextView tvTopic;
        private ImageView image;
        private ProgressBar progressBar;

        public CoursesViewHolder(@NonNull View itemView) {
            super(itemView);

            tvTopic = itemView.findViewById(R.id.tvTitle);
            image = itemView.findViewById(R.id.image);
            progressBar=itemView.findViewById(R.id.progress_bar);

        }
    }
}
