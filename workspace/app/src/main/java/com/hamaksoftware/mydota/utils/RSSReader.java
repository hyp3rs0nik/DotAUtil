package com.hamaksoftware.mydota.utils;

import android.content.Context;
import android.util.Log;

import com.hamaksoftware.mydota.model.BlogFeed;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

/**
 * Created by JV on 6/17/2014.
 */
public class RSSReader {
    private static RSSReader obj = null;
    private static Context ctx;
    private static XmlHandler xmlHandler = null;

    private RSSReader() {
        xmlHandler = new XmlHandler();
    }

    public static RSSReader getInstance(Context context) {
        if (obj == null) {
            obj = new RSSReader();
            ctx = context;

        }
        ctx = context;

        return obj;
    }

    public static XmlHandler getXmlHandler() {
        return xmlHandler;
    }

    public class XmlHandler extends DefaultHandler {
        private BlogFeed feedStr = new BlogFeed();
        private ArrayList<BlogFeed> rssList = new ArrayList<BlogFeed>();

        private int articlesAdded = 0;

        // Number of articles to download
        private static final int ARTICLES_LIMIT = 25;

        StringBuffer chars = new StringBuffer();

        public void startElement(String uri, String localName, String qName, Attributes atts) {
            chars = new StringBuffer();

            if (qName.equalsIgnoreCase("media:content")) {
                if (!atts.getValue("url").toString().equalsIgnoreCase("null")) {
                    feedStr.setImgLink(atts.getValue("url").toString());
                } else {
                    feedStr.setImgLink("");
                }
            }

        }

        public void endElement(String uri, String localName, String qName) throws SAXException {
            if (localName.equalsIgnoreCase("title")) {
                feedStr.setTitle(chars.toString());
            } else if (localName.equalsIgnoreCase("description")) {
                feedStr.setDescription(chars.toString());
            } else if (localName.equalsIgnoreCase("pubDate")) {
                feedStr.setPubDate(chars.toString());
            } else if (localName.equalsIgnoreCase("encoded")) {

                feedStr.setEncodedContent(chars.toString());
            } else if (qName.equalsIgnoreCase("media:content")) {

            } else if (localName.equalsIgnoreCase("link")) {
                feedStr.setLink(chars.toString());
            }
            if (localName.equalsIgnoreCase("item")) {
                rssList.add(feedStr);

                feedStr = new BlogFeed();
                articlesAdded++;
                if (articlesAdded >= ARTICLES_LIMIT) {
                    throw new SAXException();
                }
            }
        }

        public void characters(char ch[], int start, int length) {
            chars.append(new String(ch, start, length));
        }

        public ArrayList<BlogFeed> getLatestFeeds(String feedUrl) {
            URL url;
            try {
                SAXParserFactory spf = SAXParserFactory.newInstance();
                SAXParser sp = spf.newSAXParser();
                XMLReader xr = sp.getXMLReader();
                url = new URL(feedUrl);
                xr.setContentHandler(this);
                xr.parse(new InputSource(url.openStream()));
            } catch (IOException e) {
                Log.e("getLatestFeeds", "Feed URL Error: " + e.getMessage());
            } catch (SAXException e) {
                Log.e("getLatestFeeds", "XML Parsing Error" + e.getMessage());
            } catch (ParserConfigurationException e) {
                Log.e("getLatestFeeds", "Parser Config Error" + e.getMessage());
            }

            return rssList;
        }

    }


}
