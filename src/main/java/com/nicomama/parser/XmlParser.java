package com.nicomama.parser;

import com.nicomama.strategy.ShardStrategy;
import com.nicomama.strategy.ShardStrategyHolder;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class XmlParser {

    public static void parse(String xmlPath) {
        try {
            Document document = load(xmlPath);
            parse(document);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    private static void parse(Document document) throws Exception {
        Element root = document.getRootElement();
        List<?> strategies = root.elements("strategy");
        if (strategies == null || strategies.isEmpty()) {
            return;
        }
        for (Object o : strategies) {
            Element strategy = (Element) o;
            String table = strategy.attribute("table").getStringValue();
            String strategyClass = strategy.attribute("class").getStringValue();
            Class<?> clazz = Class.forName(strategyClass);
            ShardStrategy shardStrategy = (ShardStrategy) clazz.newInstance();
            ShardStrategyHolder.getInstance().add(table, shardStrategy);
        }
    }


    private static Document load(String xmlPath) throws Exception {
        InputStream configInputStream = null;
        try {
            ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
            configInputStream = classLoader.getResourceAsStream(xmlPath);

            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser parser = factory.newSAXParser();
            SAXReader reader = new SAXReader(parser.getXMLReader());
            return reader.read(configInputStream);
        } finally {
            try {
                if (configInputStream != null) {
                    configInputStream.close();
                }
            } catch (IOException e) {
            }
        }
    }

}
