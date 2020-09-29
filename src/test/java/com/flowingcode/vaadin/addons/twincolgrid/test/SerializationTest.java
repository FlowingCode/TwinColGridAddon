package com.flowingcode.vaadin.addons.twincolgrid.test;

import com.flowingcode.vaadin.addons.twincolgrid.TwinColGrid;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import org.junit.Test;

public class SerializationTest {

	private void testSerializationOf(Object obj) throws IOException, ClassNotFoundException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		try (ObjectOutputStream oos = new ObjectOutputStream(baos)) {
			oos.writeObject(new TwinColGrid<Object>());
		}
		try (ObjectInputStream in = new ObjectInputStream(new ByteArrayInputStream(baos.toByteArray()))) {
			TwinColGrid.class.cast(in.readObject());
		}
	}

	@Test
	public void testSerialization() throws ClassNotFoundException, IOException {
		testSerializationOf(new TwinColGrid<Object>());
	}
}