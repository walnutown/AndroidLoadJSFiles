(function () {
  var oldonload = window.onload;
  var bitium_bootstrap = function() {
    console.log('bb-strap');
    setTimeout(function() {
             console.log('bb-time: |' + document.location.href + '|');
             window.__bitium_objc.handle_call('js_complete', true);
             }, 100);
    }
    window.onload = function() {
    console.log('bb-onload');
    bitium_bootstrap();
    if (oldonload) { oldonload() };
  }
  if (document.readyState === 'complete') {
    console.log('bb-ready');
    bitium_bootstrap();
  }
}());