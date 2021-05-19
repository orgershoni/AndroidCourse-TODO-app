package exercise.android.reemh.todo_items;

import android.content.Context;
import android.graphics.Paint;
import android.text.style.StrikethroughSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class TodoItemAdapter extends RecyclerView.Adapter<TodoSingleItemHolder> {

    List<TodoItem> items;

    OnCheckClickCallback onCheckClickCallback;
    OnDeleteClickCallback onDeleteClickCallback;


    void setItems(List<TodoItem> items){
        if (this.items == null)
        {
            this.items = new ArrayList<>();
        }
        this.items.clear();
        this.items.addAll(items);
        notifyDataSetChanged();
    }

    @Override
    public TodoSingleItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        Context context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.row_todo_item, parent, false);
        return new TodoSingleItemHolder(view);
    }

    @Override
    public void onBindViewHolder(TodoSingleItemHolder holder, int position) {

        TodoItem item = items.get(position);
        holder.checkBox.setText(item.getDescription());
        holder.checkBox.setChecked(item.getStatus() == STATUS.DONE);

        holder.checkBox.setOnClickListener((View v)-> {
            if (onCheckClickCallback != null)
            {
                if (holder.checkBox.getPaint().isStrikeThruText())
                {
                    holder.checkBox.setPaintFlags(holder.checkBox.getPaintFlags() &
                            ~Paint.STRIKE_THRU_TEXT_FLAG);
                }
                else
                {
                    holder.checkBox.setPaintFlags(holder.checkBox.getPaintFlags() |
                            Paint.STRIKE_THRU_TEXT_FLAG);
                }
                onCheckClickCallback.onClick(item);
                STATUS bool = item.getStatus();
                holder.checkBox.setChecked(item.getStatus() == STATUS.IN_PROGRESS);
            }
        });

        holder.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onDeleteClickCallback != null)
                {
                    onDeleteClickCallback.onClick(item);
                }
            }
        });


    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}
