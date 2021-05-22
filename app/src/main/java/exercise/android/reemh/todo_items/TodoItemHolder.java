package exercise.android.reemh.todo_items;

import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageButton;
import androidx.recyclerview.widget.RecyclerView;


public class TodoItemHolder extends RecyclerView.ViewHolder {

    ImageButton deleteButton;
    CheckBox    checkBox;

    public TodoItemHolder(View itemView) {
        super(itemView);

        deleteButton = itemView.findViewById(R.id.deleteButton);
        checkBox = itemView.findViewById(R.id.checkbox);

    }
}
