/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.android.media;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
<<<<<<< HEAD
import android.os.AsyncTask;
=======
import android.net.Uri;
>>>>>>> 7d7cd75a3d40cab4fd0fb8ca751b75c7640b0401
import android.os.Bundle;
import android.util.Log;
import android.webkit.URLUtil;
import android.widget.Toast;

public class VideoViewDemo extends Activity {
	private static final String TAG = "VideoViewDemo";

	private MyVideoView mVideoView;
	String url;
	
	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		setContentView(R.layout.main);

//		Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:hnOPu0_YWhw")); startActivityForResult(i,0);
		Intent lVideoIntent = new Intent(null, Uri.parse("ytv://KCUFxFoaloE"), this, IntroVideoActivity.class);
		startActivity(lVideoIntent);		
		mVideoView = (MyVideoView) findViewById(R.id.surface_view);
		mVideoView.mDisplay = getWindowManager().getDefaultDisplay();
		
		mVideoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
			
			@Override
			public void onCompletion(MediaPlayer mp) {
				setResult(RESULT_OK);
				finish();
			}
		});
		
		mVideoView.setOnErrorListener(new MediaPlayer.OnErrorListener() {
			
			@Override
			public boolean onError(MediaPlayer mp, int what, int extra) {
				setResult(RESULT_CANCELED);
				finish();
				return false;
			}
		});
		
		url = null;
		
		Bundle extras = getIntent().getExtras();
		if ((extras!=null)&&(!extras.getString("PARAM").equals(""))) {
			url = extras.getString("PARAM");
			//playVideo(url);
		}else{
			url = "http://daily3gp.com/vids/747.3gp";
			//playVideo(url);
		}

	//	new LoadVideoAsyncTask().execute(url);
	}

	
	@Override
	protected void onResume() {
		playVideo(url);
		super.onResume();
	}


	class LoadVideoAsyncTask extends AsyncTask<String,Void,String> {

		@Override
		protected String doInBackground(String... params) {
			playVideo(params[0]);

			return null;
		}
		
	}
	
	private void playVideo(String url) {
		try {
			if (url == null || url.length() == 0) {
				Toast.makeText(VideoViewDemo.this, "File URL/path is empty",
						Toast.LENGTH_LONG).show();
 
			} else {
				mVideoView.setVideoPath(getDataSource(url));
				mVideoView.start();
				mVideoView.requestFocus();

			}
		} catch (Exception e) {
			Log.e(TAG, "error: " + e.getMessage(), e);
			if (mVideoView != null) {
				mVideoView.stopPlayback();
			}
		}
	}

	private String getDataSource(String path) throws IOException {
		if (!URLUtil.isNetworkUrl(path)) {
			return path;
		} else {
			URL url = new URL(path);
			URLConnection cn = url.openConnection();
			cn.connect();
			InputStream stream = cn.getInputStream();
			if (stream == null)
				throw new RuntimeException("stream is null");
			File temp = File.createTempFile("mediaplayertmp", "dat");
			temp.deleteOnExit();
			String tempPath = temp.getAbsolutePath();
			FileOutputStream out = new FileOutputStream(temp);
			byte buf[] = new byte[128];
			do {
				int numread = stream.read(buf);
				if (numread <= 0)
					break;
				out.write(buf, 0, numread);
			} while (true);
			try {
				stream.close();
			} catch (IOException ex) {
				Log.e(TAG, "error: " + ex.getMessage(), ex);
			}
			return tempPath;
		}
	}
	
}
