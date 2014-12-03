package controllers;

import play.mvc.Http;
import play.mvc.Result;
import play.mvc.Security;

/**
 * Created by mirvoda_sg on 03.12.2014.
 */
public class Secured extends Security.Authenticator {
    @Override
    public String getUsername(Http.Context ctx) {
        return ctx.session().get("apiKey");
    }

    @Override
    public Result onUnauthorized(Http.Context ctx) {
        return redirect(routes.Application.login());
    }
}
