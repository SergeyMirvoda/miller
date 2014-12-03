/**
 * Шаблон "Модуль"
 * http://www.adequatelygood.com/JavaScript-Module-Pattern-In-Depth.html
 * Created by mirvoda_sg on 03.12.2014.
 */
var ET = (function () {
    var my = {},
        apiKey = null;

    function privateMethod() {
        // ...
    }

    my.init = function (key) {
        apiKey = key;
    };
    my.send = function(success){
        //todo: Здесь нужно научиться собирать StackTrace и другие параметры браузера
        if (!apiKey){
            throw new Error("Требуется вызвать метод init и передать ему ключ API")
        }

        var data = JSON.stringify({
            message: "test message",
            url: window.location.toString(),
            agent: navigator.userAgent,
            browser: navigator.appName,
            apiKey:apiKey
        });
        $.ajax ({
            url: "/api/log",
            type: "POST",
            contentType: "application/json; charset=utf-8",
            dataType:"json",
            data: data
        } ).always( success());
    }
    return my;
}());