package com.nicomama.parser;

import com.nicomama.strategy.ShardStrategy;
import com.nicomama.strategy.ShardStrategyHolder;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.dom4j.io.SAXValidator;

import javax.xml.XMLConstants;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
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
        InputStream schemaInputStream = null;
        try {
            ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
            configInputStream = classLoader.getResourceAsStream(xmlPath);
            schemaInputStream = classLoader.getResourceAsStream("mybatis-sharding-config.xsd");

            SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            Schema schema = schemaFactory.newSchema(new StreamSource(schemaInputStream));
            SAXParserFactory factory = SAXParserFactory.newInstance();
            factory.setValidating(true);
            factory.setNamespaceAware(true);
            factory.setSchema(schema);

            SAXParser parser = factory.newSAXParser();
            SAXReader reader = new SAXReader(parser.getXMLReader());
            Document document = reader.read(configInputStream);

            SAXValidator validator = new SAXValidator(parser.getXMLReader());
            validator.validate(document);
            return document;
        } finally {
            try {
                if (configInputStream != null) {
                    configInputStream.close();
                }
            } catch (IOException e) {
            }
            if (schemaInputStream != null) {
                try {
                    schemaInputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
