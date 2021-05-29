package exercise.android.reemh.todo_items;

import android.app.Application;

/**
 * Native singleton class to hold the TodoItems DB
 */
public class TodoItemsApp extends Application {

    TodoItemsDataBase dataBase;

    @Override
    public void onCreate() {
        super.onCreate();
        dataBase = new TodoItemsDataBaseImpl(this);
        instance = this;
    }

    private static TodoItemsApp instance = null;
    public static TodoItemsApp getInstance() {
        return instance;
    }

    public TodoItemsDataBase getDataBase() {
        return dataBase;
    }
}
