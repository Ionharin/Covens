package zabi.minecraft.covens.common.patreon;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.IOUtils;

import zabi.minecraft.covens.common.lib.Log;
import zabi.minecraft.covens.common.lib.Reference;

public class ContributorDownloader implements Runnable {

	public static ArrayList<String> contributors = new ArrayList<String>();
	
	@Override
	public void run() {
		InputStream in = null;
		try {
			in = new URL(Reference.CONTRIBUTORS_URL).openStream();
			List<String> contributorData = IOUtils.readLines(in);
			contributorData.parallelStream()
			.filter(s -> !s.startsWith("#"))
			.forEach(s -> {
				contributors.add(s);
			});
			Log.i("Contributors updated! Found "+contributors.size()+" contributors");
		} catch (IOException e) {
			Log.d("error during contributors poll: ");
			Log.d(e);
		} finally {
			try {
				in.close();
			} catch (IOException e) {
			}
		}
	}

}
