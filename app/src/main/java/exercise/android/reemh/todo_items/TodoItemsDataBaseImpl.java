package exercise.android.reemh.todo_items;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TodoItemsDataBaseImpl implements TodoItemsDataBase {

  List<TodoItem> items;
  Integer additionCounter;
  Integer doneCounter;

  TodoItemsDataBaseImpl(){
    items = new ArrayList<>();
    additionCounter = 0;
    doneCounter = 0;
  }

  @Override
  public List<TodoItem> getCurrentItems() {
    Collections.sort(items);
    return items;
  }

  @Override
  public void addNewInProgressItem(String description) {
    items.add(new TodoItem(description, additionCounter));
    additionCounter++;
  }

  @Override
  public void markItemDone(TodoItem item) {
    item.setDone( doneCounter);
    doneCounter++;
  }

  @Override
  public void markItemInProgress(TodoItem item) {
    item.setInProgress();
  }

  @Override
  public void deleteItem(TodoItem item) {
    items.remove(item);
  }

  public void changeStatus(TodoItem item){
    if (item.getStatus() == STATUS.DONE){
      markItemInProgress(item);
    }
    else {
      markItemDone(item);
    }
  }

}
