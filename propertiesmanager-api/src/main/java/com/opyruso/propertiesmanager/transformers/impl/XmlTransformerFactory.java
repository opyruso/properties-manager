package com.opyruso.propertiesmanager.transformers.impl;

import java.io.StringReader;
import java.io.StringWriter;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.inject.Named;
import javax.inject.Singleton;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import com.opyruso.propertiesmanager.api.entity.ApiLog;
import com.opyruso.propertiesmanager.constants.TransformerLogStatusEnum;
import com.opyruso.propertiesmanager.data.entity.PropertyValue;
import com.opyruso.propertiesmanager.transformers.BaseTransformerFactory;
import com.opyruso.propertiesmanager.transformers.ITransformerFactory;
import com.opyruso.propertiesmanager.utils.FileUtils;

import io.quarkus.logging.Log;

@Singleton
@Named(value = "xmlTransformerFactory")
public class XmlTransformerFactory extends BaseTransformerFactory implements ITransformerFactory {

	@Override
	public void preTransform() throws Exception {
		Log.info("XmlTransformerFactory transformation start");
		log.add(new ApiLog(TransformerLogStatusEnum.INFO, "########################"));
		log.add(new ApiLog(TransformerLogStatusEnum.INFO, "UPDATED by PropertiesManager (copyright oPyRuSo) on " + DateFormat.getInstance().format(new Date())));
		log.add(new ApiLog(TransformerLogStatusEnum.INFO, "########################"));
		log.add(new ApiLog(TransformerLogStatusEnum.INFO, "MODIFICATION DETAILS"));
		log.add(new ApiLog(TransformerLogStatusEnum.INFO, "########################"));
		log.add(new ApiLog(TransformerLogStatusEnum.INFO, "TYPE : XML"));
		log.add(new ApiLog(TransformerLogStatusEnum.INFO, "APPLICATION :" + getAppId()));
		log.add(new ApiLog(TransformerLogStatusEnum.INFO, "VERSION :" + getVersion()));
		log.add(new ApiLog(TransformerLogStatusEnum.INFO, "ENVIRONMENT :" + getEnvId()));
		log.add(new ApiLog(TransformerLogStatusEnum.INFO, "FILENAME :" + getFilename()));
		log.add(new ApiLog(TransformerLogStatusEnum.INFO, "########################"));
	}

	@Override
	public void postTransform() throws Exception {
	}

	@Override
	public List<String> transform() throws Exception {
		result = FileUtils.getFileContent(file);

		for (PropertyValue pv : getDatabaseValues().values()) {
			log.add(new ApiLog(TransformerLogStatusEnum.INFO, "# DATABASE DATA        # " + pv.getPk().getFilename() + "@" + pv.getPk().getPropertyKey()));
		}
		log.add(new ApiLog(TransformerLogStatusEnum.INFO, "########################"));
		List<String> usedCache = new ArrayList<String>();

		for (String key : getDatabaseValues().keySet()) {
			PropertyValue prop = getDatabaseValues().get(key);
			String k = prop.getPk().getPropertyKey();
			String v = prop.getNewValue();
			if (!prop.getPk().getFilename().equals(getFilename())) continue;

			DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = builderFactory.newDocumentBuilder();
			Document document = builder.parse(new InputSource(new StringReader(result)));
			XPath xpath = XPathFactory.newInstance().newXPath();
			try {
				Object node = xpath.evaluate(k, document, XPathConstants.NODESET);
				System.out.println("node : " + node.getClass());
				if (node != null && node instanceof NodeList) {
					NodeList nl = (NodeList)node;
					for (int i = 0; i < nl.getLength(); i++) {
						nl.item(i).setTextContent(v);
						if (!usedCache.contains(key)) usedCache.add(key);
						log.add(new ApiLog(TransformerLogStatusEnum.valueOf("UPDATED_" + prop.getStatus()), "" + prop.getPk().getPropertyKey() + ": " + prop.getNewValue()));
					}
				} else if (node != null && node instanceof Node) {
					((Node)node).setTextContent(v);
					if (!usedCache.contains(key)) usedCache.add(key);
					log.add(new ApiLog(TransformerLogStatusEnum.valueOf("UPDATED_" + prop.getStatus()), "" + prop.getPk().getPropertyKey() + ": " + prop.getNewValue()));
				}
			} catch (Exception e) {
				e.printStackTrace();
				log.add(new ApiLog(TransformerLogStatusEnum.IGNORED, "ERROR " + prop.getPk().getPropertyKey() + ": " + prop.getNewValue()));
			}
			StringWriter stringWriter = new StringWriter();
			Transformer xformer = TransformerFactory.newInstance().newTransformer();
			xformer.transform(new DOMSource(document), new StreamResult(stringWriter));
			result = stringWriter.toString();
			
		}

		return usedCache;
	}

}
