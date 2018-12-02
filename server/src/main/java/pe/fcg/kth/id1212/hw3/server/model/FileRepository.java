package pe.fcg.kth.id1212.hw3.server.model;

import pe.fcg.kth.id1212.hw3.common.model.DownloadRequest;
import pe.fcg.kth.id1212.hw3.common.model.DownloadResponse;
import pe.fcg.kth.id1212.hw3.common.model.UploadRequest;
import pe.fcg.kth.id1212.hw3.common.model.UploadResponse;

public interface FileRepository {
    DownloadResponse download(DownloadRequest req);
    UploadResponse upload(UploadRequest req);
}
