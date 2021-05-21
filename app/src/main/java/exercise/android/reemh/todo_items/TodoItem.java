package exercise.android.reemh.todo_items;

import java.io.Serializable;

enum STATUS implements Serializable{
    DONE,
    IN_PROGRESS
}
public class TodoItem implements Serializable, Comparable<TodoItem> {

    private STATUS status = STATUS.IN_PROGRESS;
    private String description;
    private final Integer timeAdded;
    private Integer doneItem;
    static int addedItemsCount = 0;
    static int doneItemsCount = 0;


    TodoItem(String desc){
       setDescription(desc);
       setInProgress();
       this.timeAdded = addedItemsCount++;
       this.doneItem = -1;
    }

    void setDescription(String desc){
        description = desc;
    }

    String getDescription(){
        return description;
    }

    STATUS getStatus(){
        return status;
    }

    void setInProgress(){
        status = STATUS.IN_PROGRESS;
        doneItem = -1;
    }

    void setDone(){
        status = STATUS.DONE;
        this.doneItem = doneItemsCount++;
    }


    @Override
    public boolean equals(Object obj) {

        if (!(obj instanceof TodoItem))
        {
            return false;
        }
        TodoItem other = (TodoItem) obj;
        return timeAdded.equals(other.timeAdded);
    }

    @Override
    public int compareTo(TodoItem other){

        if (this.status != other.status)
        {
            if (this.status == STATUS.IN_PROGRESS)
            {
                return -1;
            }
            return 1;
        }
        else if (this.status == STATUS.IN_PROGRESS) {
            return other.timeAdded - this.timeAdded;
        }
        else {          // Both items are done
            return this.doneItem - other.doneItem;
        }
    }
}
