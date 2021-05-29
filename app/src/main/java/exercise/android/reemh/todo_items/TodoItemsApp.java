package exercise.android.reemh.todo_items;

import android.app.Application;

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
