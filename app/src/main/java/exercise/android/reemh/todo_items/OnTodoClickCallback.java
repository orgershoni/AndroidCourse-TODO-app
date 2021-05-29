package exercise.android.reemh.todo_items;

import android.content.Intent;

/**
 * Interface to allow EditItemActivity launch
 */
public interface OnTodoClickCallback {

    void onClick(Intent intent);
}
