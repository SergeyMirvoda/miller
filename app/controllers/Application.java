package controllers;

import com.avaje.ebean.Ebean;
import com.fasterxml.jackson.databind.JsonNode;
import models.*;
import play.Logger;
import play.data.Form;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security;
import views.html.*;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;
import static play.data.Form.form;

public class Application extends Controller {

    @Security.Authenticated(Secured.class)
    public static Result index() {
        try {
            Logger.info("list for ApiKey || Отображение списка для apiKey: " +session().get("apiKey"));
            flash("success");
            return ok(index.render(ErrorReportModel.find
                                    .where()
                                    .eq("apiKey", session().get("apiKey"))
                                    .findList()
                    )
            );
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }

    }

    @Security.Authenticated(Secured.class)
    public static Result errors(UUID id) {
        return ok(viewerror.render(ErrorReportModel.find.byId(id)));
    }

        public static Result log()  {
            if (request().body() == null || request().body().asJson()==null){
                System.out.println(request().body());
                return badRequest("json value have to be provided:");
            }
            JsonNode json = request().body().asJson();
            ErrorReportModel model = Json.fromJson(json, ErrorReportModel.class);
            model.id = UUID.randomUUID();
            model.happens = 1;
            String x = model.getMessage();

            List<ErrorReportModel> list = ErrorReportModel.find.where().eq("message", x).findList();// упорядочили по сообщению
            int counter = list.size();

            for (ErrorReportModel reportModel: list){
                if (list.size() != 0){
                    if (reportModel.happens == 1){
                        int temp = counter;
                        System.out.println("if message: " + reportModel.happens);
                        model.setHappens(model.happens + 1);
                        Ebean.delete(reportModel);
                        Ebean.save(reportModel);
                        Ebean.save(model);

                    } else if (reportModel.happens > 1){
                        int temp = counter;
                        temp = temp -1;
                        temp = temp + reportModel.happens;
                        Ebean.delete(reportModel);
                        Ebean.save(reportModel);
                        model.setHappens(temp + 1);
                        Ebean.save(model);
                        System.out.println("else if message: " + reportModel.happens + " " + temp);
                        System.out.println("temp in  " + temp);

                    } else if (reportModel.happens == 0){
                        Ebean.delete(reportModel);
                        System.out.println("Something go wrong");
                    }
                } else if (list.size() == 0 ){
                    Ebean.save(model);
                }

            }

            flash("new report been added");
            ErrorReportModel.save(model);
            return play.mvc.Results.ok();
        }


    //Удалить отчет
    public static Result remove(UUID id){
       ErrorReportModel.find.ref(id).delete();
       return redirect(routes.Application.index());
    }
    //Авторизация
    public static Result login() {
        return ok(
                login.render(form(Login.class))
        );
    }
    //Аутентификация
    public static Result authenticate() {
        Form<Login> loginForm = form(Login.class).bindFromRequest();
        if (loginForm.hasErrors()) {
            return badRequest(login.render(loginForm));
        } else {
            session().clear();
            //Получить из БД apiKey и установить его в сессию
            session("apiKey", "DEV-KEY_1");//Пока установим тестовый
            return redirect(
                    routes.Application.index()
            );
        }
    }
    //Валидация введенных данных
    public static class Login {
        public String email;
        public String password;

        public String validate(){
            if (User.authenticate(email, password) == null){
                return "Неверный адрес или пароль"; // i18
            }
            return null;
        }
    }
    //переадресация на регистрация
    public static Result register(){
        return ok(register.render(form(User.class)));
    }
    //Регистрация нового пользователя
    public static Result newUser(){
        Form<User> userForm = form(User.class).bindFromRequest();
        if (userForm.hasErrors()){
            return badRequest(register.render(userForm));
        }else{
            userForm.get().save();
            return redirect(routes.Application.index());
        }
    }

    //Выход из системы
    public static Result logout(){
        session().clear();
        return redirect(routes.Application.login());
    }


//    О программе
//    public static Result about() {
//        return ok(about.render());
//    }

}
