package org.example;

import org.apache.jackrabbit.core.data.FileDataStore;
import org.apache.jackrabbit.oak.InitialContent;
import org.apache.jackrabbit.oak.Oak;
import org.apache.jackrabbit.oak.jcr.Jcr;
import org.apache.jackrabbit.oak.segment.SegmentNodeStore;
import org.apache.jackrabbit.oak.segment.SegmentNodeStoreBuilders;
import org.apache.jackrabbit.oak.segment.file.FileStore;
import org.apache.jackrabbit.oak.segment.file.FileStoreBuilder;
import org.apache.jackrabbit.oak.segment.file.InvalidFileStoreVersionException;
import org.apache.jackrabbit.oak.segment.file.ReadOnlyFileStore;
import org.apache.jackrabbit.oak.spi.blob.BlobStore;
import org.apache.jackrabbit.oak.spi.blob.FileBlobStore;
import org.apache.jackrabbit.value.BinaryImpl;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.jcr.*;
import javax.jcr.lock.LockException;
import javax.jcr.nodetype.ConstraintViolationException;
import javax.jcr.version.VersionException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

@RestController("/api/file")
public class FileController {

    private Repository repository;
    private FileStore fileStore;

    @PostMapping(value = "/persist", consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
    public void persist(@RequestPart String processKey, @RequestPart String instanceId, @RequestPart List<MultipartFile> files) {
        try {
            String repository = "repository";

            FileStore fs = FileStoreBuilder.fileStoreBuilder(new File(repository)).build();
            SegmentNodeStore ns = SegmentNodeStoreBuilders.builder(fs).build();
            Repository repo = new Jcr(new Oak(ns)).createRepository();

            Session session = repo.login(new SimpleCredentials("admin", "admin".toCharArray()), null);
            Node processNode = session.getRootNode().addNode(processKey);
            Node instanceNode = processNode.addNode(instanceId);

            files.forEach(f -> {
                try {
                    instanceNode.setProperty(f.getName(), new BinaryImpl(f.getInputStream()));
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (LockException e) {
                    e.printStackTrace();
                } catch (ValueFormatException e) {
                    e.printStackTrace();
                } catch (ConstraintViolationException e) {
                    e.printStackTrace();
                } catch (VersionException e) {
                    e.printStackTrace();
                } catch (RepositoryException e) {
                    e.printStackTrace();
                }
            });
            session.save();

        } catch(IOException  | InvalidFileStoreVersionException e) {
            e.printStackTrace();
        } catch (LoginException e) {
            e.printStackTrace();
        } catch (RepositoryException e) {
            e.printStackTrace();
        }
    }

    @GetMapping(value = "/get", produces = { MediaType.APPLICATION_JSON_VALUE })
    public List get(@RequestParam String processKey, @RequestParam String instanceId) {
        try {
            FileStore fs = FileStoreBuilder.fileStoreBuilder(new File("repository")).build();
            SegmentNodeStore ns = SegmentNodeStoreBuilders.builder(fs).build();
            Repository repo = new Jcr(new Oak(ns)).createRepository();

            Session session = repo.login(new SimpleCredentials("admin", "admin".toCharArray()), null);
            Node processNode = session.getRootNode().addNode(processKey);
            Node instanceNode = processNode.addNode(instanceId);

            LinkedList<String> result = new LinkedList<>();
            PropertyIterator props = instanceNode.getProperties();
            while(props.hasNext()) {
                Property prop = props.nextProperty();
                String name = prop.getName();
                Binary binary = prop.getBinary();
                System.out.println("propertyName: " + name);
                System.out.println(binary);
                result.add(name);
            }

            return result;

        } catch(IOException  | InvalidFileStoreVersionException e) {
            e.printStackTrace();
        } catch (LockException e) {
            e.printStackTrace();
        } catch (ItemExistsException e) {
            e.printStackTrace();
        } catch (LoginException e) {
            e.printStackTrace();
        } catch (ConstraintViolationException e) {
            e.printStackTrace();
        } catch (PathNotFoundException e) {
            e.printStackTrace();
        } catch (VersionException e) {
            e.printStackTrace();
        } catch (RepositoryException e) {
            e.printStackTrace();
        }
        return new ArrayList();
    }



//    private BlobStore createBlobStore() throws IOException {
//        try {
//            File repositoryFolder = new File("", "repository");
//            File dataStoreFolder = new File("", "datastore");
//
//            BlobStore blobStore = new FileBlobStore(dataStoreFolder.getAbsolutePath());
//            FileStore repositoryFileStore =
//                    FileStoreBuilder.fileStoreBuilder(repositoryFolder).withBlobStore(blobStore).build();
//            SegmentNodeStore segmentNodeStore =
//                    SegmentNodeStoreBuilders.builder(repositoryFileStore).build();
//            Jcr jcr = new Jcr(segmentNodeStore)
//                    .with(new InitialContent());
//        } catch(IOException  | InvalidFileStoreVersionException e) {
//            e.printStackTrace();
//        }
//        FileDataStore fds = new FileDataStore();
//        fds.setMinRecordLength(4092);
//        fds.init(tempFolder.newFolder().getAbsolutePath());
//        return new DataStoreBlobStore(fds);
//    }
}
