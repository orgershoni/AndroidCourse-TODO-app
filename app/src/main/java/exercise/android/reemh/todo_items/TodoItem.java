package exercise.android.reemh.todo_items;

import java.io.Serializable;

enum STATUS implements Serializable{
    DONE,
    IN_PROGRESS
}
public class TodoItem implements Serializable, Comparable<TodoItem> {

    private STATUS status = STATUS.IN_PROGRESS;
    private String description;
    private final Integer time_added;
    private Integer done_time;


    TodoItem(String desc, int time_added){
       setDescription(desc);
       setInProgress();
       this.time_added = time_added;
       this.done_time = -1;
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
        done_time = -1;
    }

    void setDone(Integer done_time){
        status = STATUS.DONE;
        this.done_time = done_time;
    }


    @Override
    public boolean equals(Object obj) {

        if (!(obj instanceof TodoItem))
        {
            return false;
        }
        TodoItem other = (TodoItem) obj;
        return time_added.equals(other.time_added);
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
            return other.time_added - this.time_added;
        }
        else {          // Both items are done
            return this.done_time - other.done_time;
        }
    }
}
