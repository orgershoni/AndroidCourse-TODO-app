package exercise.android.reemh.todo_items;

import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import exercise.android.reemh.todo_items.R;

public class TodoSingleItemHolder extends RecyclerView.ViewHolder {

    ImageButton deleteButton;
    CheckBox    checkBox;

    public TodoSingleItemHolder (View itemView) {
        super(itemView);

        deleteButton = itemView.findViewById(R.id.deleteButton);
        checkBox = itemView.findViewById(R.id.checkbox);

    }
}
