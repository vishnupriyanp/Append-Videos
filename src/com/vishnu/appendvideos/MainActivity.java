package com.vishnu.appendvideos;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.util.LinkedList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import com.coremedia.iso.boxes.Container;
import com.example.appendvideos.R;
import com.googlecode.mp4parser.authoring.Movie;
import com.googlecode.mp4parser.authoring.Track;
import com.googlecode.mp4parser.authoring.builder.DefaultMp4Builder;
import com.googlecode.mp4parser.authoring.container.mp4.MovieCreator;
import com.googlecode.mp4parser.authoring.tracks.AppendTrack;

public class MainActivity extends Activity {
	Button btnAppend;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		btnAppend = (Button) findViewById(R.id.buttonAppend);
		btnAppend.setOnClickListener(new OnClickListener() {
			@SuppressWarnings("resource")
			@Override
			public void onClick(View v) {
				try {
					String f1 = Environment.getExternalStorageDirectory()
							+ "/snap/Video1.mp4";
					String f2 = Environment.getExternalStorageDirectory()
							+ "/snap/Video2.mp4";
					String f3 = Environment.getExternalStorageDirectory()
							+ "/snap/Video3.mp4";
					String f4 = Environment.getExternalStorageDirectory()
							+ "/snap/Video4.mp4";
					Movie[] inMovies;

					inMovies = new Movie[] { MovieCreator.build(f1),
							MovieCreator.build(f2), MovieCreator.build(f3),
							MovieCreator.build(f4), };

					List<Track> videoTracks = new LinkedList<Track>();
					List<Track> audioTracks = new LinkedList<Track>();

					for (Movie m : inMovies) {
						for (Track t : m.getTracks()) {
							if (t.getHandler().equals("soun")) {
								audioTracks.add(t);
							}
							if (t.getHandler().equals("vide")) {
								videoTracks.add(t);
							}
						}
					}

					Movie result = new Movie();

					if (audioTracks.size() > 0) {
						result.addTrack(new AppendTrack(audioTracks
								.toArray(new Track[audioTracks.size()])));
					}
					if (videoTracks.size() > 0) {
						result.addTrack(new AppendTrack(videoTracks
								.toArray(new Track[videoTracks.size()])));
					}

					Container out = new DefaultMp4Builder().build(result);

					RandomAccessFile ram = new RandomAccessFile(String
							.format(Environment.getExternalStorageDirectory()
									+ "/Appendoutput.mp4"), "rw");
					FileChannel fc = ram.getChannel();
					out.writeContainer(fc);
					ram.close();
					fc.close();
					Toast.makeText(getApplicationContext(), "success",
							Toast.LENGTH_SHORT).show();
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		});

	}
}