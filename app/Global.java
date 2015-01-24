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
        return Promise.<SimpleResult> pure(notFound(views.html.pageNotFound.render(request.uri())));
    }
}
