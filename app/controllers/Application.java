package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import models.ErrorReportModel;
import play.Logger;
import play.data.Form;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security;
import views.html.index;
import views.html.login;
import views.html.viewerror;

import java.util.UUID;

import static play.data.Form.form;

public class Application extends Controller {

    @Security.Authenticated(Secured.class)
    public static Result index() {
        try {
            Logger.info("Отображение списка для apiKey: " +session().get("apiKey"));
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
    public static Result log() {
        if (request().body() == null || request().body().asJson()==null){
            System.out.println(request().body());
            return badRequest("json value have to be provided:");
        }
        JsonNode json = request().body().asJson();
        ErrorReportModel model = Json.fromJson(json, ErrorReportModel.class);
        model.id = UUID.randomUUID();
        ErrorReportModel.save(model);
        return play.mvc.Results.ok();
    }

    public static Result login() {
        return ok(
                login.render(form(Login.class))
        );
    }

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

    public static Result register() {
        return play.mvc.Results.TODO;
    }

    public static class Login {

        public String email;
        public String password;

    }
}
