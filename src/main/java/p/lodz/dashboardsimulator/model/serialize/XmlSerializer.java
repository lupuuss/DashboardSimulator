package p.lodz.dashboardsimulator.model.serialize;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.XStreamException;
import com.thoughtworks.xstream.io.xml.DomDriver;
import p.lodz.dashboardsimulator.model.serialize.exceptions.DeserializationException;
import p.lodz.dashboardsimulator.model.serialize.exceptions.SerializationException;
import p.lodz.dashboardsimulator.model.serialize.exceptions.XmlSerializerInitException;

import java.io.*;
import java.nio.file.NotDirectoryException;

public class XmlSerializer implements Serializer {

    private File rootDir;
    private XStream xStream = new XStream(new DomDriver());

    public XmlSerializer(String rootDir) {
        this.rootDir = new File(rootDir);
        try {
            assertRootDir();
        } catch (FileNotFoundException | NotDirectoryException e) {
            throw new XmlSerializerInitException(e);
        }
    }

    private void assertRootDir() throws FileNotFoundException, NotDirectoryException {

        boolean result = true;

        if (rootDir.exists() && rootDir.isFile()) {

            throw new NotDirectoryException("Root dir has to be a directory!");

        } else if (!rootDir.exists()) {

            result = rootDir.mkdir();
        }

        if (!result) {
            throw new FileNotFoundException("Root dir doesn't exist and cannot be created!");
        }
    }

    private String generateFileName(String key, Class<?> objectClass) {

        return objectClass.getSimpleName() + "_" + key + ".xml";
    }

    private void checkFile(File tryFile) throws IOException {
        if (!tryFile.exists() && !tryFile.createNewFile()) {
            throw new IOException("File couldn't be created!");
        }
    }

    private File provideSerializationFile(String key, Class<?> objectClass) throws SerializationException {

        String name = generateFileName(key, objectClass);

        try {

            File tryFile = new File(rootDir, name);
            checkFile(tryFile);
            return tryFile;

        } catch (IOException e) {

            throw new SerializationException(e);

        } finally {
            System.out.println("File check done!");
        }
    }

    @Override
    public void serialize(String key, Object object) throws SerializationException {

        File serializationFile = provideSerializationFile(key, object.getClass());

        try (OutputStream outputStream = new FileOutputStream(serializationFile)) {

            xStream.toXML(object, outputStream);

        } catch (IOException | XStreamException e) {

            throw new SerializationException(e);
        }

    }

    @Override
    public <T> T deserialize(String key, Class<T> classObject) throws DeserializationException {

        String fileName = generateFileName(key, classObject);

        try (Reader reader = new FileReader(new File(rootDir, fileName))){

            return classObject.cast(xStream.fromXML(reader));

        } catch (XStreamException | IOException e) {

            throw new DeserializationException(e);
        }
    }
}
