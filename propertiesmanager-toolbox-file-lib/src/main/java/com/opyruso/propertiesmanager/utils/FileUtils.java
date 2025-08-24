package com.opyruso.propertiesmanager.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Map.Entry;

import io.quarkus.logging.Log;

public class FileUtils {

	public static File createTempFile(String content) throws Exception {
		if (content == null) throw new Exception();
		File result = Files.createTempFile("propertiesmanager_", ".tmp").toFile();
		FileWriter fw = new FileWriter(result, Charset.forName("UTF-8"), true);
		fw.append(content);
		fw.flush();
		fw.close();
		return result;
	}

	public static void appendToFile(File lf, String message) throws Exception {
		if (lf.isFile() && lf.canRead() && lf.canWrite()) {
			FileWriter fw = new FileWriter(lf, true);
			fw.append(message);
			fw.flush();
			fw.close();
		}
	}

	public static String getFileContent(File file) throws Exception {
		if (file == null) throw new Exception();
		String result = null;
		Log.debug("getFileContent : file = " + file);
		String encoding = detectEncodingForFile(file);
		try (FileInputStream fis = new FileInputStream(file);
				InputStreamReader isr = new InputStreamReader(fis, encoding);
				BufferedReader br = new BufferedReader(isr);) {
			StringBuffer sb = new StringBuffer("");
			String line = br.readLine();
			while (line != null) {
				sb.append(line);
				sb.append("\n");
				line = br.readLine();
			}
			result = sb.toString().trim();	
		} catch (Exception e) {
			Log.error(e.getMessage(), e);
			throw e;
		}
		if (result.startsWith("\uFEFF")) {
			result = result.substring(1);
        }
		return result;
	}

	public static String convert(String text, String fromEncoding, String toEncoding) throws Exception {
		if (text == null || fromEncoding == null || toEncoding == null) throw new Exception();
		try {
			ByteBuffer inputBuffer = ByteBuffer.wrap(text.getBytes(Charset.forName(fromEncoding)));
			CharBuffer data = Charset.forName(fromEncoding).decode(inputBuffer);
			ByteBuffer outputBuffer = Charset.forName(toEncoding).encode(data);
			byte[] outputData = outputBuffer.array();
			return new String(outputData, Charset.forName(toEncoding)).trim();
		} catch (Exception e) {
			throw new IllegalStateException(e);
		}
	}

	public static String detectEncodingForFile(File file) throws Exception {
		if (file == null) throw new Exception();
		final String[] encodings = { "UTF-8", "ISO-8859-1", "ASCII", StandardCharsets.UTF_16.name() };
		String result = null;
		for (String charset : encodings) {
			try {
				Files.readAllLines(file.toPath(), Charset.forName(charset));
				result = charset;
				break;
			} catch (Exception e) {
				Log.error(e.getMessage() + " / " + file + " / " + charset);
			}
		}
		if (result == null) {
			for (Entry<String, Charset> charset : Charset.availableCharsets().entrySet()) {
				try {
					Files.readAllLines(file.toPath(), charset.getValue());
					result = charset.getValue().name();
					break;
				} catch (Exception e) {
					Log.error(e.getMessage() + " / " + file + " / " + charset.getValue());
				}
			}
		}
		if (result != null) {
			Log.debug("Encoding : " + result);
		} else {
			Log.debug("Encoding not found !");
		}
		return result;
	}
	
}
