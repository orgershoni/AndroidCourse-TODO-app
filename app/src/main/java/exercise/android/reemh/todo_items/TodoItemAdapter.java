package exercise.android.reemh.todo_items;

import android.content.Context;
import android.graphics.Paint;
import android.text.style.StrikethroughSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class TodoItemAdapter extends RecyclerView.Adapter<TodoSingleItemHolder> {

    List<TodoItem> items;

    OnCheckClickCallback onCheckClickCallback;
    OnDeleteClickCallback onDeleteClickCallback;
    OnChangeCallback onChangeCallback;


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
        renderCheckBox(holder.checkBox, item.getStatus());

        holder.checkBox.setOnClickListener((View v)-> {

            checkTheBox((CheckBox) v);
            if (onCheckClickCallback != null) {
                onCheckClickCallback.onClick(item);
            }
            if (onChangeCallback != null) {
                onChangeCallback.onChange();
            }
        });

        holder.deleteButton.setOnClickListener((View v) -> {
            if (onDeleteClickCallback != null){
                onDeleteClickCallback.onClick(item);
            }
            if (onChangeCallback != null) {
                onChangeCallback.onChange();
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static private void checkTheBox(CheckBox checkBox){

        if (checkBox.isChecked()){

            checkBox.setPaintFlags(checkBox.getPaintFlags() &
                    ~Paint.STRIKE_THRU_TEXT_FLAG);
        }
        else{
            checkBox.setPaintFlags(checkBox.getPaintFlags() |
                    Paint.STRIKE_THRU_TEXT_FLAG);
        }

        checkBox.setChecked(!checkBox.isChecked());
    }

    static private void renderCheckBox(CheckBox checkBox, STATUS status){

        boolean isChecked = status == STATUS.DONE;
        if (isChecked && !checkBox.getPaint().isStrikeThruText()){
            checkBox.setPaintFlags(checkBox.getPaintFlags() |
                    Paint.STRIKE_THRU_TEXT_FLAG);
        }
        else if (!isChecked && checkBox.getPaint().isStrikeThruText()){
            checkBox.setPaintFlags(checkBox.getPaintFlags() &
                    ~Paint.STRIKE_THRU_TEXT_FLAG);
        }

        checkBox.setChecked(isChecked);
    }
}
