package controllers;

import models.Task;
import models.SQLconnection;
import play.data.*;
import play.data.Form;
import play.db.*;
import play.mvc.*;

import java.sql.*;
import java.util.*;
import views.html.*;

public class Application extends Controller {

    static Form<Task> taskForm = Form.form(Task.class);

    public static Result index() {
        return redirect(controllers.routes.Application.tasks());
    }


    public static Result tasks(){
        return ok(views.html.index.render(Task.all(), taskForm));
    }

    public static Result newTask(){ //для in-memory h2 database
        Form<Task> filledForm = taskForm.bindFromRequest();
        if (filledForm.hasErrors()){
            return badRequest(views.html.index.render(Task.all(), filledForm));
        } else {
            Task.create(filledForm.get());
            return redirect(routes.Application.tasks());
        }
    }


    public static Result deleteTask(Long id){
        Task.delete(id);
        return redirect(routes.Application.tasks());
    }

    public static Result errorMonitor(){
        return ok(views.html.errormonitor.render(Task.all(),taskForm));
    }

    Connection connection = DB.getConnection();


    /*public static void sqlQuery(){
       return ok();
    }*/


 }
