package controllers;

import com.avaje.ebean.Ebean;
import com.fasterxml.jackson.databind.JsonNode;
import models.*;
import play.Logger;
import play.data.Form;
import play.data.validation.Constraints;
import play.i18n.Messages;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security;
import views.html.*;

import java.util.List;
import java.util.UUID;
import static play.data.Form.form;

public class Application extends Controller {

    public static final Result GO_HOME = redirect(
            routes.Application.index());

    @Security.Authenticated(Secured.class)
    public static Result index() {
        try {
            String username = session().get("email");
            User user = User.findByEmail(username);
            UUID key = user.appKey;
            Logger.info("list for ApiKey || Отображение списка для email: " +username + key);
            flash("success");
            return ok(index.render(ErrorReportModel.find
                                    .where()
                                    .eq("apiKey", key)
                                    .orderBy("message")
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
        String mess = model.getMessage().toLowerCase();
        String key = model.apiKey;
        System.out.println(key + " " + mess); //TODO message goes to lower case, case work
        model.setMessage(mess);
        UUID suuid = UUID.fromString(key);
        String x = model.getMessage();
        UUID appKey = reportChecker(suuid);
        if (appKey != null){
            User user = User.findByKey(appKey);

            List<ErrorReportModel> list = ErrorReportModel.find.where().eq("apiKey", user.appKey).eq("message", x).findList();// упорядочили по сообщению
            int counter = list.size();

            for (ErrorReportModel reportModel : list) {
                if (list.size() != 0) {
                    /**
                     * входящему отчету присваевается id, а так же в message меняется регистр на прописной ->
                     * Сверяемся на принадлежность ключа(apiKey) из отчета какому-либо пользователю->
                     * ..если совпадений нет, то отчет удаляется
                     * ..если совпадения есть то формируется спсок по пунктам->
                     *   ..пользователя(которому пренадлежит ключ) записи с таким же полем message ->
                     *     ..если записей не существует, то добавляется новая
                     *     ..если запись есть, счётчик = 1, старая запись удаляется, а новая сохраняется +1 к счётчику
                     *     ..если запись есть, счётчик > 1, старая запись удаляется, новая сохраняется добавляя к счётчику значение старой записи
                     *     ..если запись есть, счётчик = 0 или <0, запись удаляется, новая сохраняется, значение счётчика = 1
                     *
                     */
                    if (reportModel.happens == 1) {
                        int temp = counter;
                            model.setHappens(model.happens + 1);
                            Ebean.delete(reportModel);
                            Ebean.save(reportModel);
                            Ebean.save(model);

                    } else if (reportModel.happens > 1) {
                        int temp = counter;
                        temp = temp - 1;
                        temp = temp + reportModel.happens;
                        Ebean.delete(reportModel);
                        Ebean.save(reportModel);
                        model.setHappens(temp + 1);
                        Ebean.save(model);
                    } else if (reportModel.happens == 0) { //TODO добавить или меньше ноля
                            Ebean.delete(reportModel);
                            System.out.println("Something go wrong");
                    }
                } else if (list.size() == 0) {
                    Ebean.save(model);
                    }
            }

            flash("new report been added");
            ErrorReportModel.save(model);
            return play.mvc.Results.ok();
        } else {

        }return ok();
    }

    public static UUID reportChecker(UUID newId){
        User user = User.find.where().eq("appKey", newId).findUnique();
        if (user == null){
            return null;
        }else{
        return newId;
    }
    }




    //Удалить отчет
    public static Result remove(UUID id){
       ErrorReportModel.find.ref(id).delete();
       return GO_HOME;
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
            String x = loginForm.get().email; //для первоначальных настроек безопасности и проверки работоспособности

            //TODO Получить из БД apiKey и установить его в сессию
            session("email", x);     //loginForm.get().email
             return GO_HOME;
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
            String newUserEmail = userForm.get().email;
            session().put("email", newUserEmail);
            userForm.get().save();
            return redirect(routes.Application.secondRegistrationStep());
        }
    }

    //можно сразу заполнить информацию о приложении, а можно пропустить и заполнить потом
    @Security.Authenticated(Secured.class)
    public static Result secondRegistrationStep(){
        return ok(views.html.Account.secondstep.render(form(User.class)));
    }
    @Security.Authenticated(Secured.class)
    public static Result addNewApplication(){
        String username = session().get("email");
        UUID key = User.uuidGeneration();
        String appname = form().bindFromRequest().get("applicationName");
        String description = form().bindFromRequest().get("description");

        User user = User.findByEmail(username);

        user.setApplicationName(appname);
        user.setDescription(description);
        user.setAppKey(key);

        Ebean.update(user);
        session().put("email", username);
        return GO_HOME;
    }

    //Выход из системы
    public static Result logout(){
        session().clear();
        return redirect(routes.Application.login());
    }


//    О программе
    public static Result about() {
        return ok(about.render());
    }

    @Security.Authenticated(Secured.class)
    public static Result settings() {
        return  play.mvc.Results.ok(views.html.Settings.options.render());
    }


    @Security.Authenticated(Secured.class)
    public static Result userProfilePage() {
        User user = User.findByEmail(session().get("email"));
        return play.mvc.Results.ok(views.html.Settings.userProfile.render(user));
    }
    @Security.Authenticated(Secured.class)
    public static Result userProfileEdit() {
        final User user = User.findByEmail(session().get("email"));
        String passOrigin = user.password;
        String oldPass = form().bindFromRequest().get("oldPass");
        String newPass = form().bindFromRequest().get("newPass");
        String rePass = form().bindFromRequest().get("rePass");

        String passValidation = passwordIsValid(session().get("email"), oldPass);
        if (passValidation == null){
            return ok("не совпали1");
        }else {
            if ((newPass == rePass) || newPass != null){

                user.setPassword(newPass);      //TODO изменить возвращение при ошибках
                user.setPassword(newPass);
                user.update();
                Ebean.update(user);
                return GO_HOME;

            }else{
                return ok("не совпали2");
            }
        }
    }
    public static String passwordIsValid(String email, String oldPassword){
        User user = User.findByEmail(email);
        if (!user.password.equals(oldPassword))
            return null;
        return oldPassword;
    }

    @Security.Authenticated(Secured.class)
    public static Result applicationProfilePage() {
        User user = User.findByEmail(session().get("email"));
        return play.mvc.Results.ok(views.html.Settings.applicationProfile.render(user));
    }
    @Security.Authenticated(Secured.class)
    public static Result applicationProfileEdit(){
        User user = User.findByEmail(session().get("email"));
        String appName = form().bindFromRequest().get("applicationName");
        String descript = form().bindFromRequest().get("description");
        user.setApplicationName(appName);
        user.setDescription(descript);
        user.save();
        Ebean.save(user);
        return play.mvc.Results.ok(views.html.Settings.applicationProfile.render(user));
    }
    @Security.Authenticated(Secured.class)
    public static Result applicationKeyPage() {
        User user = User.findByEmail(session().get("email"));
        return play.mvc.Results.ok(views.html.Settings.applicationKey.render(user));
    }
    @Security.Authenticated(Secured.class)
    public static Result apiKeyGenerator(){
        UUID x = UUID.randomUUID();
        User user = User.findByEmail(session().get("email"));
        user.setAppKey(x);
        user.save();
        Ebean.save(user);

        return play.mvc.Results.ok(views.html.Settings.applicationKey.render(user));

    }



    //тестовое оповещение об ошибке
    public static Result testOnError() {
        throw new IllegalArgumentException();
    }
}
