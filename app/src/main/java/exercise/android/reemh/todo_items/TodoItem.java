package exercise.android.reemh.todo_items;

import java.io.Serializable;

enum STATUS implements Serializable {
    DONE,
    IN_PROGRESS
}
public class TodoItem implements Serializable, Comparable<TodoItem> {

    private STATUS status;
    private String description;
    private final Integer timeAdded;
    private Integer doneTime;
    static int addedItemsCount = 0;
    static int doneItemsCount = 0;

    /**
     * Item is initialized with given description and immediately set to IN PROGRESS
     * @param desc - item's description
     */
    TodoItem(String desc){

       setDescription(desc);
       setInProgress();
       this.timeAdded = addedItemsCount++;
       this.doneTime = -1;
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
        doneTime = -1;  // of item is in progress - set it's doneTime to invalid value (-1)
    }

    void setDone(){
        status = STATUS.DONE;
        this.doneTime = doneItemsCount++;
    }

    /**
     * Compare two items by unique value of creation time
     * @param obj
     * @return
     */
    @Override
    public boolean equals(Object obj) {

        if (!(obj instanceof TodoItem))
        {
            return false;
        }
        TodoItem other = (TodoItem) obj;
        return timeAdded.equals(other.timeAdded);
    }

    /**
     * Compare method to implement order settings of todoitems
     * @param other
     * @return
     */
    @Override
    public int compareTo(TodoItem other){

        if (this.status != other.status)            // If 2 instances dont share the same status
                                                    // - in progress is "smaller"
        {
            if (this.status == STATUS.IN_PROGRESS)
            {
                return -1;
            }
            return 1;
        }
        else if (this.status == STATUS.IN_PROGRESS) {   // Else if both in progress compare by creation time
            return other.timeAdded - this.timeAdded;
        }
        else {          // Else, both items are done - compare by reverse "done" time
            return this.doneTime - other.doneTime;
        }
    }
}
