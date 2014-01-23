$(document).ready(function () {
    $('input').change(function (event) {
        var targetElement = $(event.target);
        var required = $(targetElement).data("required");
        if (required && !targetElement.val()) {
            targetElement.css("background-color", "#FFEEEE");
        } else {
            targetElement.css("background-color", "");
        }
    });
});
