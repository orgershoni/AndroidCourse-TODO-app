package exercise.android.reemh.todo_items;

import java.io.Serializable;

/**
 * Interface for updating TodoItemsData base when check is clicked
 */
public interface OnCheckClickCallback extends Serializable {

    void onClick(TodoItem item);
}
