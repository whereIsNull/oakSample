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
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.jcr.LoginException;
import javax.jcr.Repository;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@RestController("/api/file")
public class FileController {

    private Repository repository;
    private FileStore fileStore;

    @PostMapping(value = "/persist", consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
    public void persist(@RequestPart String processKey, @RequestPart String instanceId, @RequestPart List<MultipartFile> files) {
        try {
            FileStore fs = FileStoreBuilder.fileStoreBuilder(new File("repository")).build();
            SegmentNodeStore ns = SegmentNodeStoreBuilders.builder(fs).build();
            Repository repo = new Jcr(new Oak(ns)).createRepository();

            Session session = repo.login();
            

            files.forEach(f -> {
                try {
                    fs.getWriter().writeStream(f.getInputStream());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            fs.getWriter().flush();

        } catch(IOException  | InvalidFileStoreVersionException e) {
            e.printStackTrace();
        } catch (LoginException e) {
            e.printStackTrace();
        } catch (RepositoryException e) {
            e.printStackTrace();
        }
    }

    @GetMapping(value = "/get", produces = { MediaType.MULTIPART_FORM_DATA_VALUE })
    public void get(@RequestPart String processKey, @RequestPart String instanceId) {
        try {
            FileStore fs = FileStoreBuilder.fileStoreBuilder(new File("repository")).build();

        } catch(IOException  | InvalidFileStoreVersionException e) {
            e.printStackTrace();
        }
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
