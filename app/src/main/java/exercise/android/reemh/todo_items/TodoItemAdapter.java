package exercise.android.reemh.todo_items;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.nfc.Tag;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;

public class TodoItemAdapter extends RecyclerView.Adapter<TodoItemHolder> {

    List<TodoItem> items;

    // Callback functions to support changes done inside a ViewHolder
    OnCheckClickCallback onCheckClickCallback;
    OnDeleteClickCallback onDeleteClickCallback;
    OnTodoClickCallback onTodoClickCallback;

    /**
     * Set items in adapter and notify data has changed
     * @param items
     */
    void setItems(List<TodoItem> items){
        if (this.items == null)
        {
            this.items = new ArrayList<>();
        }
        this.items.clear();
        this.items.addAll(items);

        notifyDataSetChanged();
    }


    /**
     * Create a new view holder
     * @param parent
     * @param viewType
     * @return
     */
    @Override
    public TodoItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        Context context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.row_todo_item, parent, false);
        return new TodoItemHolder(view);
    }

    /**
     * Bind a TodoItem and a view holder is position "position"
     * @param holder - ViewHolderInstance
     * @param position - position of item
     */
    @Override
    public void onBindViewHolder(TodoItemHolder holder, int position) {

        // extract item
        TodoItem item = items.get(position);

        // Set text inside the text box
        holder.checkBox.setText(item.getDescription());

        // Set information inside the checkbox
        renderCheckBox(holder.checkBox, item.getStatus());

        // On click lister of check box
        holder.checkBox.setOnClickListener((View v)-> {

            checkTheBox(holder.checkBox);
            if (onCheckClickCallback != null)
            {
                onCheckClickCallback.onClick(item);
            }
        });

        holder.todoItem.setOnClickListener(v -> {

            Intent editTodoIntent = new Intent(v.getContext(), EditItemActivity.class);
            editTodoIntent.putExtra("to_edit", item);
            onTodoClickCallback.onClick(editTodoIntent);
        });

        // On click lister of delete button
        holder.deleteButton.setOnClickListener((View v) -> {

            // callback and update data accordingly (no need for UI changes)
            if (onDeleteClickCallback != null){
                onDeleteClickCallback.onClick(item);
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    /**
     * Change checkBox UI to the negative state after a check.
     * Flip the checkbox value, and strike-through (or delete strike-through) according to previous
     * state
     * @param checkBox
     */
    static private void checkTheBox(CheckBox checkBox){

        checkBox.setChecked(!checkBox.isChecked());
        checkBox.getPaint().setStrikeThruText(!checkBox.isChecked());
    }

    /**
     * Render checkbox by the given status
     * @param checkBox
     * @param status
     */
    static private void renderCheckBox(CheckBox checkBox, STATUS status){

        boolean isChecked = status == STATUS.DONE;
        checkBox.getPaint().setStrikeThruText(isChecked);
        checkBox.setChecked(isChecked);
    }
}
