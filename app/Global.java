/**
 * добавить обработку TODO и BAD Request
 */
import play.GlobalSettings;
import play.libs.F.Promise;
import play.mvc.Http.RequestHeader;
import play.mvc.SimpleResult;
import static play.mvc.Results.*;

public class Global extends GlobalSettings {


    @Override
    public Promise<SimpleResult> onHandlerNotFound(RequestHeader request) {
        //Обработка 404 - Page not found
        return Promise.<SimpleResult> pure(notFound(views.html.ServicePage.pageNotFound.render(request.uri())));
    }

    @Override
    public Promise<SimpleResult> onBadRequest(RequestHeader request, String error){
        return Promise.<SimpleResult>pure(badRequest(views.html.ServicePage.badRequest.render(error)));
    }
    @Override
    public Promise<SimpleResult> onError(RequestHeader request, Throwable t){
        String error = t.toString();
        return Promise.<SimpleResult> pure(internalServerError(views.html.ServicePage.errorPage.render(error)));
    }
}
