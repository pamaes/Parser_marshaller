import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class Parser {

	private Document dom = null;
	private ArrayList<Libro> libros = null;

	public Parser() {
		libros = new ArrayList<Libro>();
	}

	public void parseFicheroXml(String fichero) {
		// creamos una factory
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

		try {
			// creamos un documentbuilder
			DocumentBuilder db = dbf.newDocumentBuilder();

			// parseamos el XML y obtenemos una representaci�n DOM
			dom = db.parse(fichero);
		} catch (ParserConfigurationException pce) {
			pce.printStackTrace();
		} catch (SAXException se) {
			se.printStackTrace();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}

	}

	public void parseDocument() {
		// obtenemos el elemento ra�z
		Element docEle = dom.getDocumentElement();

		// obtenemos el nodelist de elementos
		NodeList nl = docEle.getElementsByTagName("libro");
		if (nl != null && nl.getLength() > 0) {
			for (int i = 0; i < nl.getLength(); i++) {

				// obtenemos un elemento de la lista (persona)
				Element el = (Element) nl.item(i);

				// obtenemos una persona
				Libro p = getLibro(el);

				// lo a�adimos al array
				libros.add(p);
			}
		}
	}

	private Libro getLibro(Element libroEle) {

		String titulo = getTextValue(libroEle, "titulo");
		String editor = getTextValue(libroEle, "editor");
		int paginas = getIntValue(libroEle, "paginas");
		ArrayList<String> lista=getNombres(libroEle,"autor");
		int anyo=Integer.parseInt(getAttribute(libroEle,"titulo","anyo"));
		
		Libro l = new Libro(titulo, lista, editor, paginas,anyo);

		return l;

	}
	
	private String getAttribute(Element ele, String tag, String attr) {
	
		String textVal="";
		NodeList nl = ele.getElementsByTagName(tag);
		if (nl != null && nl.getLength() > 0) {
			Element el = (Element) nl.item(0);
			textVal = el.getAttribute(attr);
		}
		return textVal;

		
	}

	private ArrayList<String> getNombres(Element ele, String tag) {
		ArrayList<String> lista_nombres = new ArrayList<String>();
		
		NodeList nl = ele.getElementsByTagName(tag);

		if (nl != null && nl.getLength() > 0) {
			Element el_autor = (Element) nl.item(0);

			NodeList nodelist_nombres = el_autor.getElementsByTagName("nombre");

			if (nodelist_nombres != null && nodelist_nombres.getLength() > 0) {
				for (int i = 0; i < nodelist_nombres.getLength(); i++) {
					Element el_nombre = (Element) nodelist_nombres.item(i);
					String texto = el_nombre.getFirstChild().getNodeValue();
					lista_nombres.add(texto);					
				}
			}

		}
		return lista_nombres;
	}

	private String getTextValue(Element ele, String tagName) {
		String textVal = null;
		NodeList nl = ele.getElementsByTagName(tagName);
		if (nl != null && nl.getLength() > 0) {
			Element el = (Element) nl.item(0);
			textVal = el.getFirstChild().getNodeValue();
		}
		return textVal;
	}

	private int getIntValue(Element ele, String tagName) {
		return Integer.parseInt(getTextValue(ele, tagName));
	}

	public void print() {

		Iterator it = libros.iterator();
		while (it.hasNext()) {
			Libro l = (Libro) it.next();
			l.print();
			System.out.println("-----------------------------\n");
		}
	}

}