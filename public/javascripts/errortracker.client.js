/**
 * Шаблон "Модуль"
 * http://www.adequatelygood.com/JavaScript-Module-Pattern-In-Depth.html
 * Created by mirvoda_sg on 03.12.2014.
 *
 *
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
        var navAppVer = navigator.appVersion;
        var navUsrAgt = navigator.userAgent;
        var unknown = "unknown"; //Можно сделать проверку - если переменная "null" то забить ее Unknown

        //Получение времени в удобном формате
        function getTime(){
        var d = new Date();
        var hours = d.getHours();        if(hours<10){hours='0'+hours}
        var minutes = d.getMinutes();    if(minutes<10){minutes='0'+minutes}
        var seconds = d.getSeconds();    if(seconds<10){seconds='0'+seconds}
        var day = d.getDate();        if(day<10){ day='0'+day}
        var month = d.getMonth()+1;        if(month<10){month='0'+month}
        var year = d.getFullYear();
        var x = hours+':'+minutes+':'+seconds+' '+day+"/"+month+'/'+year
        var toUtc = d.toISOString();
        return {
            Date: x,
            UTC: toUtc
        };
        }

        function getScreenResolution(){

            var screenResolution = window.screen.width + "X" + window.screen.height;
            var
                width = window.innerWidth ,
                height = window.innerHeight,
                colorD = window.screen.colorDepth;

            return {

                browserResolution: width + 'X' + height,
                nativeScreenResolution: screenResolution,
                colorDepth: colorD
            };
        }

        //Получение информации о битности приложения
        function getPlatformInfo(){
            var verOfSet,
                platform = window.navigator.platform;

            if (verOfSet = navUsrAgt.indexOf("WOW64") != -1){
                platform = "32-битное приложение работающее на 64-битном процессоре";

            } else if (verOfSet = navUsrAgt.indexOf("Win64") != -1) {
                platform = "64-битное приложение";

            } else if (verOfSet = navUsrAgt.indexOf("amd64") != -1) {
                platform = "64-битное приложение работающее под процессоре AMD";

            } else if (verOfSet = navUsrAgt.indexOf("x86_64") != -1) {
                platform = "64-битное приложение работающее на процессоре Intel"

            } else if (verOfSet = navUsrAgt.indexOf("armv7") != -1) {
                platform = "Операционная система работающая на процессоре ARM v7"
            }

            return {
                platformString: platform
            };
        }


        //получение характеристик браузера
        function getBrowserInfo(){
            var
            browser = navigator.appName,
            vendor = unknown,
            version = '' + parseFloat(navAppVer),
            majorVersion = parseInt(navAppVer, 10),
            nameOffset, verOffset, ix;

            // Opera
            if ((verOffset = navUsrAgt.indexOf('Opera')) != -1) {
                browser = 'Opera';
                vendor = "OPERA SOFTWARE"
                version = navUsrAgt.substring(verOffset + 6);
                if ((verOffset = navUsrAgt.indexOf('Version')) != -1) {
                    version = navUsrAgt.substring(verOffset + 8);
                }
            }
            // Chrome
            else if ((verOffset = navUsrAgt.indexOf('Chrome')) != -1) {
                browser = 'Chrome';
                version = navUsrAgt.substring(verOffset + 7);
                vendor = "Google Inc"
            }
            // Safari
            else if ((verOffset = navUsrAgt.indexOf('Safari')) != -1) {
                browser = 'Safari';
                vendor = "Apple Inc"
                version = navUsrAgt.substring(verOffset + 7);
                if ((verOffset = navUsrAgt.indexOf('Version')) != -1) {
                    version = navUsrAgt.substring(verOffset + 8);
                }
            }
            // Firefox
            else if ((verOffset = navUsrAgt.indexOf('Firefox')) != -1) {
                browser = 'Firefox';
                vendor = "The Mozilla Foundation"
                version = navUsrAgt.substring(verOffset + 8);
            }
            // IE ниже 11 версии
            else if ((verOffset = navUsrAgt.indexOf('MSIE')) != -1) {
                browser = 'Microsoft Internet Explorer';
                version = navUsrAgt.substring(verOffset + 5);
                vendor = "Microsoft Inc"
            }
            // MSIE 11+
            else if (navUsrAgt.indexOf('Trident/') != -1) {
                browser = 'Microsoft Internet Explorer';
                vendor = "Microsoft Inc"
                version = navUsrAgt.substring(navUsrAgt.indexOf('rv:') + 3);
            }
            // Другие браузеры
            else if ((nameOffset = navUsrAgt.lastIndexOf(' ') + 1) < (verOffset = navUsrAgt.lastIndexOf('/'))) {
                browser = navUsrAgt.substring(nameOffset, verOffset);
                version = navUsrAgt.substring(verOffset + 1);
                if (browser.toLowerCase() == browser.toUpperCase()) {
                    browser = navigator.appName;
                }
            }
            // trim the version string
            if ((ix = version.indexOf(';')) != -1) version = version.substring(0, ix);
            if ((ix = version.indexOf(' ')) != -1) version = version.substring(0, ix);
            if ((ix = version.indexOf(')')) != -1) version = version.substring(0, ix);

            majorVersion = parseInt('' + version, 10);
            if (isNaN(majorVersion)) {
                version = '' + parseFloat(navAppVer);
                majorVersion = parseInt(navAppVer, 10);
            }

            return {
                name: browser,
                version: majorVersion,
                versionString: version,
                productVendor: vendor
            };
        }

        // Получение информации о операционной системе
        //надо еще покопаться
        function getOS() {

            var osVersion = unknown;
            var os = unknown;

            var clientStrings = [
                { s: 'Windows 8.1', r: /(Windows 8.1|Windows NT 6.3)/ },
                { s: 'Windows 8', r: /(Windows 8|Windows NT 6.2)/ },
                { s: 'Windows 7', r: /(Windows 7|Windows NT 6.1)/ },
                { s: 'Windows Vista', r: /Windows NT 6.0/ },
                { s: 'Windows Server 2003', r: /(Windows NT 5.2|Windows Server 2003)/ },
                { s: 'Windows XP', r: /(Windows NT 5.1|Windows XP)/ },
                { s: 'Windows 2000 (SP1)', r: /Windows NT 5.01/ },
                { s: 'Windows 2000', r: /(Windows NT 5.0|Windows 2000)/ },
                { s: 'Windows ME', r: /(Win 9x 4.90|Windows ME)/ },
                { s: 'Windows 98', r: /(Windows 98|Win98)/ },
                { s: 'Windows 95', r: /(Windows 95|Win95|Windows_95)/ },
                { s: 'Windows 3.11', r: /Win16/ },
                { s: 'Windows CE', r: /Windows CE/ },
                { s: 'Windows NT 4.0', r: /(Windows NT 4.0|WinNT4.0|WinNT|Windows NT)/ },
                { s: 'Windows ME', r: /Windows ME/ },
                { s: 'Android', r: /Android/ },
                { s: 'Open BSD', r: /OpenBSD/ },
                { s: 'Sun OS', r: /SunOS/ },
                { s: 'Linux', r: /(Linux|X11)/ },
                { s: 'iOS', r: /(iPhone|iPad|iPod)/ },
                { s: 'Mac OS X', r: /Mac OS X/ },
                { s: 'Mac OS', r: /(MacPPC|MacIntel|Mac_PowerPC|Macintosh)/ },
                { s: 'QNX', r: /QNX/ },
                { s: 'UNIX', r: /UNIX/ },
                { s: 'BeOS', r: /BeOS/ },
                { s: 'OS/2', r: /OS\/2/ },
                { s: 'Search Bot', r: /(nuhk|Googlebot|Yammybot|Openbot|Slurp|MSNBot|Ask Jeeves\/Teoma|ia_archiver)/ }
            ];
            for (var id in clientStrings) {
                var cs = clientStrings[id];
                if (cs.r.test(navUsrAgt)) {
                    os = cs.s;
                    break;
                }
            }

            if (/Windows/.test(os)) {
                osVersion = /Windows (.*)/.exec(os)[1];
                os = 'Windows';
            }

            switch (os) {
                case 'Mac OS X':
                    osVersion = /Mac OS X (10[\.\_\d]+)/.exec(navUsrAgt)[1];
                    break;

                case 'Android':
                    osVersion = /Android ([\.\_\d]+)/.exec(navUsrAgt)[1];
                    break;

                case 'iOS':
                    osVersion = /OS (\d+)_(\d+)_?(\d+)?/.exec(navAppVer);
                    osVersion = osVersion[1] + '.' + osVersion[2] + '.' + (osVersion[3] | 0);
                    break;

            }

            return {
                name: os,
                versionString: osVersion
            };
        }

        //stackTrace еще не готов
        function stackTrace() {
            try {
                var err = new Error();
                throw err;
            }catch(e){
                return e.stack;
            }

        }

        //apiKey еще не готов
        if (!apiKey){
            throw new Error("need to call init method and give ApiKey to him ||" +
            " Требуется вызвать метод init и передать ему ключ API")

        }


        var data = JSON.stringify({
        //Сводка
            message: "Page not Found",
            date: getTime().UTC,

        //Подробно
            stack: stackTrace(),

        //Среда
            browser: getBrowserInfo().name + " " + getBrowserInfo().version,
            browserVendor: getBrowserInfo().productVendor,
            browserVersion: getBrowserInfo().versionString,
            agent: navUsrAgt,
            browserResolution: getScreenResolution().browserResolution,
            browserLanguage: navigator.language,
            browserCookie: navigator.cookieEnabled,
            browserJava: navigator.javaEnabled(),
            platform: getPlatformInfo().platformString,
            platformVersion: getOS().name + " " + getOS().versionString ,
            screenResolution: getScreenResolution().nativeScreenResolution,

        //Служебная информация
            client: "RTF-Error-Tracker 0.1",
            apiKey: apiKey,

        //Запрос
            url: location.toString(),
            host: document.domain


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