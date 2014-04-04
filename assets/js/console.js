 consol = new Object();
 consol.log = function(log) {
 var iframe = document.createElement("IFRAME");
 iframe.setAttribute("src", "android-log:" + log);
 document.documentElement.appendChild(iframe);
 iframe.parentNode.removeChild(iframe);
 iframe = null;
 }

 
/*console = new Object();
console.log = function(log) {
  var iframe = document.createElement("IFRAME");
  iframe.setAttribute("src", "ios-log:#iOS#" + log);
  document.documentElement.appendChild(iframe);
  iframe.parentNode.removeChild(iframe);
  iframe = null;
}
console.debug = console.log;
console.info = console.log;
console.warn = console.log;
console.error = console.log;*/


window.__bitium_android = new Object();
window.__bitium_android.handle_call = function(method, result) {
  var iframe = document.createElement("IFRAME");
  iframe.setAttribute("src", "android-js:" + method + ';' + result);
  document.documentElement.appendChild(iframe);
  iframe.parentNode.removeChild(iframe);
  iframe = null;
}

window.bitiumDateNow = function() {
  d = new Date();
  return d.getTime()
}