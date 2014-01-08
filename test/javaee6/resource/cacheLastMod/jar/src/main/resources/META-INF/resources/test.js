function doSomething(id)  {
    var elem = document.getElementById(id);
    // change default value to some other text to reproduce the caching issue
    elem.defaultValue = "bla-bla text...";
}