/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.hadoop.test;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.junit.Test;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.net.URL;
import java.util.UUID;

public class TestUtils {

  public static String getResourceName( Class clazz, String name ) {
    name = clazz.getName().replaceAll( "\\.", "/" ) + "/" + name;
    return name;
  }

  public static URL getResourceUrl( Class clazz, String name ) throws FileNotFoundException {
    name = getResourceName( clazz, name );
    URL url = ClassLoader.getSystemResource( name );
    if( url == null ) {
      throw new FileNotFoundException( name );
    }
    return url;
  }

  public static URL getResourceUrl( String name ) throws FileNotFoundException {
    URL url = ClassLoader.getSystemResource( name );
    if( url == null ) {
      throw new FileNotFoundException( name );
    }
    return url;
  }

  public static InputStream getResourceStream( String name ) throws IOException {
    URL url = ClassLoader.getSystemResource( name );
    InputStream stream = url.openStream();
    return stream;
  }

  public static InputStream getResourceStream( Class clazz, String name ) throws IOException {
    URL url = getResourceUrl( clazz, name );
    InputStream stream = url.openStream();
    return stream;
  }

  public static Reader getResourceReader( String name, String charset ) throws IOException {
    return new InputStreamReader( getResourceStream( name ), charset );
  }

  public static Reader getResourceReader( Class clazz, String name, String charset ) throws IOException {
    return new InputStreamReader( getResourceStream( clazz, name ), charset );
  }

  public static String getResourceString( Class clazz, String name, String charset ) throws IOException {
    return IOUtils.toString( getResourceReader( clazz, name, charset ) );
  }

  public static File createTempDir( String prefix ) throws IOException {
    File targetDir = new File( System.getProperty( "user.dir" ), "target" );
    File tempDir = new File( targetDir, prefix + UUID.randomUUID() );
    FileUtils.forceMkdir( tempDir );
    return tempDir;
  }

  public static Document parseXml( InputStream stream ) throws ParserConfigurationException, IOException, SAXException {
    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
    DocumentBuilder builder = factory.newDocumentBuilder();
    Document document = builder.parse( new InputSource( stream ) );
    return document;
  }

  public static void dumpXml( Document document ) throws TransformerException {
    Transformer transformer = TransformerFactory.newInstance().newTransformer();
    transformer.setOutputProperty( OutputKeys.INDENT, "yes" );
    StreamResult result = new StreamResult( new StringWriter() );
    DOMSource source = new DOMSource( document );
    transformer.transform( source, result );
    String xmlString = result.getWriter().toString();
    System.out.println( xmlString );
  }

  public static void LOG_ENTER() {
    StackTraceElement caller = Thread.currentThread().getStackTrace()[2];
    System.out.println( String.format( "Running %s#%s", caller.getClassName(), caller.getMethodName() ) );
  }

  public static void LOG_EXIT() {
    StackTraceElement caller = Thread.currentThread().getStackTrace()[2];
    System.out.println( String.format( "Exiting %s#%s", caller.getClassName(), caller.getMethodName() ) );
  }

}
