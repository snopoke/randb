package com.nomsic.randb;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.xml.sax.SAXException;

public class CreateRand {

	public static void main(String[] args) throws JAXBException, IOException, SAXException {
		BlockGroup generateArm = BlockGroup.generateArm("males", 20, new int[]{2,4}, new String[]{"S", "C"});
		writeXml(generateArm, new File("test.xml"));
		Object read = read(new Cell(), new File("test.xml"));
		System.out.println(read);

		for (int i = 0; i < 15; i++) {
			System.out.println(generateArm.getNextUnused());
		}
		writeXml(generateArm, new File("test.xml"));
	}

	public static void writeXml(Object sample, File file) throws JAXBException,
			IOException {
		FileWriter writer = new FileWriter(file);
		try {
			String name = sample.getClass().getPackage().getName();
			JAXBContext jc = JAXBContext.newInstance(name, Thread
					.currentThread().getContextClassLoader());
			Marshaller m = jc.createMarshaller();
			m.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
			m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
			m.marshal(sample, writer);
		} finally {
			writer.close();
		}
	}

	public static Object read(Object sample, File file) throws JAXBException,
			SAXException, IOException {
		InputStreamReader reader = new InputStreamReader(new FileInputStream(
				file));
		try {
			String name = sample.getClass().getPackage().getName();
			JAXBContext jc = JAXBContext.newInstance(name, Thread
					.currentThread().getContextClassLoader());

			Unmarshaller unmarshaller = jc.createUnmarshaller();

			Object element = unmarshaller.unmarshal(reader);
			return element;
		} finally {
			reader.close();
		}
	}

}
