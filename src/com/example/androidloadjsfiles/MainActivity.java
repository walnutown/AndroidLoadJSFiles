package com.example.androidloadjsfiles;

import java.io.IOException;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.webkit.ConsoleMessage;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class MainActivity extends Activity {

	private WebView wv;
	private FileManager fileManager ;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		fileManager = FileManager.createInstance(getApplicationContext());
		wv = (WebView) findViewById(R.id.wv_webtab);
		wv.getSettings().setJavaScriptEnabled(true);
		wv.setWebViewClient(new ScriptLoginWebViewClient());
		wv.setWebChromeClient(new ScriptLoginWebChromeClient());
		wv.loadUrl("http://code.google.com/android");
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	public class ScriptLoginWebViewClient extends WebViewClient {
		@Override
		public void onPageFinished(WebView view, String url) {
			try {
				Log.d("SCRIPT", "onPageFinished()");
				// scriptLoginManager.log("test");
				loadJavaScriptAndCallInDiffLoadUrl();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

	public class ScriptLoginWebChromeClient extends WebChromeClient {
		/**
		 * This callback is used to communicate with the javascirpt in webview
		 * 
		 * @msg holds the result of last action and indicates the next action to
		 *      be taken, until the login process has completed
		 */
		@Override
		public boolean onConsoleMessage(ConsoleMessage consoleMessage) {
			String msg = consoleMessage.message();
			Log.d("Console", msg + " -- lineNumber: " + consoleMessage.lineNumber());
			return true;
		}

	}

	public void loadJavaScriptAndCallInSameLoadUrl() throws IOException {
		Log.d("SCRIPT", "DOM ready, load our own JS");
		StringBuilder js = new StringBuilder();
		js.append("window.__bitium = new function(){\n");
		js.append("     	console.log('JS, 1.1');\n");
		js.append(fileManager.readAssetFile("js/test2.js") + "\n");
		js.append("     	console.log('JS, 1.2');\n");
		js.append("changeColor('red');\n");
		js.append("     	console.log('JS, 1.3');\n");
		js.append("};\n");
		executeJavaScript(js.toString());
	}
	
	public void loadJavaScriptAndCallInDiffLoadUrl() throws IOException {
		Log.d("SCRIPT", "DOM ready, load our own JS");
		StringBuilder js = new StringBuilder();
		js.append(fileManager.readAssetFile("js/test2.js") + "\n");
		js.append("     	console.log('JS, 1.1');\n");
		executeJavaScript(js.toString());
		js.delete(0, js.length());
		js.append("window.__bitium = new function(){\n");
		js.append("     	console.log('JS, 1.2');\n");
		js.append("changeColor('blue');\n");
		js.append("     	console.log('JS, 1.3');\n");
		js.append("};\n");
		executeJavaScript(js.toString());
	}

	int lineCount = 1;

	private void logByLine(String s) {

		for (String line : s.split("\n")) {
			Log.d("JS", "line" + lineCount + " " + line);
			lineCount++;
		}
	}

	private void logAll(String s) {
		int maxSize = 1000;
		for (int i = 0; i <= s.length() / maxSize; i++) {
			int start = i * maxSize;
			int end = (i + 1) * maxSize;
			end = end > s.length() ? s.length() : end;
			Log.d("JS", s.substring(start, end));
		}
	}

	private void executeJavaScript(String js) {

		StringBuilder sb = new StringBuilder();
		sb.append("try{ \n");
		sb.append(js);
		sb.append("}catch(e){\n");
		// sb.append("if (console === undefined){\n");
		// sb.append("	  console.trace();\n");
		sb.append("	  console.log(e.stack);\n");
		// sb.append("	  document.write(e);\n");
		sb.append("}\n");
		logByLine(sb.toString());
		// logAll(sb.toString());
		wv.loadUrl("javascript:" + sb.toString());
	}

	private void executeJavaScript2(String js) {
		wv.loadUrl("javascript:(" + js + ")");
	}

}
