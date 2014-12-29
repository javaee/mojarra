jsf.ajax.addOnEvent(function (data) {
    // the status is checked in the unittest
    window.status = data.status;
});

jsfAjaxRequest = jsf.ajax.request;

jsf.ajax.request = function (source, event, options) {
    // always make synchronous ajax calls to make live easier for HtmlUnit
    options.async = false;

    jsfAjaxRequest(source, event, options);
};
