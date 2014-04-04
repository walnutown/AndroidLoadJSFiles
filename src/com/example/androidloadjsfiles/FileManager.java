package com.example.androidloadjsfiles;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

/*
 * FileManager is a singleton class
 * All files are stored in app internal storage
 */
public final class FileManager {
	private static FileManager sRef;
	private Context context;

	private FileManager(Context context) {
		this.context = context;
	}

	public static synchronized FileManager createInstance(Context context) {
		if (sRef == null)
			sRef = new FileManager(context);
		return sRef;
	}

	public static synchronized FileManager getInstance() {
		if (sRef == null)
			throw new IllegalStateException("FileManager::createInstance() should" + "be called before FileManager::getInstance()");
		return sRef;
	}

	/**
	 * Check whether the internal storage has the 'file'
	 */
	public boolean has(String file) {
		String[] files = context.fileList();
		for (String f : files) {
			if (f.equals(file))
				return true;
		}
		return false;
	}

	public void write(String content, String file) throws IOException {
		FileOutputStream fout = context.openFileOutput(file, Context.MODE_PRIVATE);
		fout.write(content.getBytes());
		fout.close();
	}

	public void write(Bitmap image, String file) throws IOException {
		FileOutputStream fout = context.openFileOutput(file, Context.MODE_PRIVATE);
		image.compress(Bitmap.CompressFormat.PNG, 100, fout);
		fout.close();
	}

	public String read(String file) throws IOException {
		StringBuilder sb = new StringBuilder();
		FileInputStream fin = context.openFileInput(file);
		int c = 0;
		while ((c = fin.read()) != -1)
			sb.append((char) c);
		fin.close();
		return sb.toString();
	}

	public Bitmap getBitMap(String file) throws FileNotFoundException {
		FileInputStream fin = context.openFileInput(file);
		return BitmapFactory.decodeStream(fin);
	}

	public void delete(String file) {
		if (has(file))
			context.deleteFile(file);
	}

	public String readAssetFile(String fileName) throws IOException {
		StringBuilder sb = new StringBuilder();
		AssetManager assetmanager = context.getAssets();
		InputStream in = assetmanager.open(fileName);
		BufferedReader br = new BufferedReader(new InputStreamReader(in));
		String line = "";
		while ((line = br.readLine()) != null) {
			sb.append(line);
		}
		in.close();
		return sb.toString();
	}
}
