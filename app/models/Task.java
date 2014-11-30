package models;

import controllers.routes;
import org.joda.time.DateTime;
import play.data.format.Formats;
import play.data.validation.Constraints.*;
import play.db.ebean.*;

import javax.persistence.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Vladimir on 20.11.2014.
 */
@Entity
public class Task extends Model{
/*
*   Этот класс сделан для h2 database
*
* */
    @Id
    public Long id;
    public String categoryError;
    public String typeError;
    public String fileError;
    public String commentError;

    @Required
    public String label;
    @Formats.DateTime(pattern="dd.MM.yy")
    public DateTime newDate = new DateTime();
    public String dateNew = (newDate.getHourOfDay() + ":" + newDate.getMinuteOfHour()
            + " " + newDate.getDayOfMonth() + "/" + newDate.getMonthOfYear() + "/" + newDate.getYear());
    //try SimpleDataFormat

    public static Finder<Long,Task> find = new Finder(Long.class, Task.class);
    public static List<Task> all(){
            return find.all();
    }
    public static void create(Task task){
        task.save();
    }
    public static void delete(Long id){
        find.ref(id).delete();
    }
}
