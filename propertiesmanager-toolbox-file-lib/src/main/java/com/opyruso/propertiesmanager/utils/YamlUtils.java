package com.opyruso.propertiesmanager.utils;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.opyruso.propertiesmanager.utils.entity.YamlLine;

import io.quarkus.logging.Log;

public class YamlUtils {
	
	public static void main(String[] args) throws Exception {
		yamlReader(new File("E:/developpement/workspace/tmp/test/test.yaml"));
	}

	public static List<YamlLine> yamlReader(File file) throws Exception {
		List<YamlLine> result = new ArrayList<YamlLine>();
		try {
			Log.debug("fichier a transformer : " + file.getPath());
			String original = FileUtils.getFileContent(file);
			String[] lines = original.replaceAll("\r\n", "\n").split("\n");
			Log.debug("yaml : " + lines.length + " line(s)");
			String content = "";
			Pattern pLine = Pattern.compile(
					"^(?<SPACES>(\\s\\s)+)?"
					+ "(?<ISTAB>\\-\\s)?"
					+ "("
						+ "(?<KEY>.*?)"
						+ "(?<OPTIONNAL>\\:)"
						+ "(?<MANDSPACE>\\s)?"
					+ ")?"
					+ "(?<VALUE>.*)$");
			Pattern pMultiline = Pattern.compile("^(?<SPACES>\\s*)(?<VALUE>.*)$");
			String p = "";
			boolean isMultiline = false;
			int multilineIndentRef = 0;
			Map<String, Integer> tabIndexMap = new HashMap<String, Integer>();
			Map<Integer, String> lastIndentMap = new HashMap<Integer, String>();
			int lineNumber = 0;
			for (String l : lines) {
				lineNumber++;
				YamlLine yamlLine = null;
				if ("".equals(l.trim()) || l.trim().startsWith("#")) {
					content = content + (isMultiline ? "\\\n" : "\n");
					content = content + l.trim();
					result.add(null);
					continue;
				}
				if (isMultiline) {
					Matcher mMultiline = pMultiline.matcher(l);
					if (mMultiline.find()) {
						if (mMultiline.group("SPACES").length() < multilineIndentRef) {
							isMultiline = false;
							multilineIndentRef = 0;
							Log.info("fin de multiline : " + l);
						} else {
							content = content + (isMultiline ? "\\\n" : "\n");
							content = content + l;
							result.add(null);
							continue;
						}
					}
				}
				content = content + (content.isEmpty() ? "" : "\n");

				String lTemp = l;
				Matcher mLine = pLine.matcher(lTemp);
				if (mLine.find()) {
					yamlLine = new YamlLine();
					if (mLine.group("SPACES") != null) {
						yamlLine.spaces = mLine.group("SPACES");
					} else {
						yamlLine.spaces = "";
					}
					yamlLine.isArrayItem = (mLine.group("ISTAB") != null && mLine.group("ISTAB").length() > 0);
					if (mLine.group("KEY") != null) {
						yamlLine.key = mLine.group("KEY").trim().replace(".", "%").replace(" ", "\\u0020");
					} else {
						yamlLine.key = "";
					}
					if (mLine.group("OPTIONNAL") != null) {
						yamlLine.optionnal = mLine.group("OPTIONNAL").trim();
					} else {
						yamlLine.optionnal = "";
					}
					if (mLine.group("MANDSPACE") != null) {
						yamlLine.mandatorySpace = " ";
					} else {
						yamlLine.mandatorySpace = "";
					}
					yamlLine.value = mLine.group("VALUE").trim();
					
//					Log.debug("New Line : [" + mLine.group(0) + ","
//						+ yamlLine.spaces + ","
//						+ yamlLine.isArrayItem + ","
//						+ yamlLine.key + ","
//						+ yamlLine.optionnal + ","
//						+ yamlLine.mandatorySpace + ","
//						+ yamlLine.value + "]");
					
					
					p = yamlLine.key;
					
					
					


					
					
					
					
					if (yamlLine.spaces.length() > 0) {
						int delta = 0;
						while ((p = lastIndentMap.get(yamlLine.spaces.length() / 2 - 1 - delta)) == null) {
							delta++;
						}
						if (!yamlLine.isArrayItem) {
							p = p + "." + yamlLine.key;
						} else {
							if (tabIndexMap.containsKey(p)) {
								tabIndexMap.merge(p, 1, Integer::sum);
							} else {
								tabIndexMap.put(p, 0);
							}
							p = p + "[" + tabIndexMap.get(p) + "]";
						}
					}
					
					

					
					

					lastIndentMap.put(yamlLine.spaces.length() / 2, p);
					List<Integer> toRemove = new ArrayList<Integer>();
					for (Entry<Integer, String> e : lastIndentMap.entrySet()) {
						if (e.getKey() > yamlLine.spaces.length() / 2) {
							toRemove.add(e.getKey());
						}
					}
					for (Integer i : toRemove) {
						lastIndentMap.remove(i);
					}

					if (yamlLine.value.startsWith("|")) {
						isMultiline = true;
						multilineIndentRef = yamlLine.spaces.length() + 2;
						content = content + p + " = " + yamlLine.value.replace("|-", "").replace("|", "");
						result.add(yamlLine);
						continue;
					}
				} else {
					Log.debug(lineNumber + ": Nothing to do here, not regex compliant");
				}
//				Log.debug(lineNumber + ": before : " + l);
				if (yamlLine.key.trim().equals("")) {
					Log.debug(lineNumber + ": --> NOTHING : " + p + " = " + yamlLine.value);
				} else {
					Log.debug(lineNumber + ": --> KEY : " + yamlLine.key + ", value : " + yamlLine.value + " (" + p + ")");
				}
				content = content + p + " = " + yamlLine.value;
				yamlLine.path = p;
				result.add(yamlLine);
			}
		} catch (Exception e) {
			throw e;
		}
		return result;
	}

}
