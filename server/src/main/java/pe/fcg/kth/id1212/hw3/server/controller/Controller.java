package pe.fcg.kth.id1212.hw3.server.controller;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import pe.fcg.kth.id1212.hw3.common.model.*;
import pe.fcg.kth.id1212.hw3.server.dao.FileDao;
import pe.fcg.kth.id1212.hw3.server.dao.UserDao;
import pe.fcg.kth.id1212.hw3.server.entity.FileEntity;
import pe.fcg.kth.id1212.hw3.server.model.FileRepository;
import pe.fcg.kth.id1212.hw3.server.entity.UserEntity;
import pe.fcg.kth.id1212.hw3.server.model.UserSession;
import pe.fcg.kth.id1212.hw3.server.model.security.SecurityUtils;
import pe.fcg.kth.id1212.hw3.server.net.FileTransferService;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class Controller extends UnicastRemoteObject implements FileCatalog, FileRepository {
    private FileTransferService fileTransferService;
    private UserDao userDao;
    private FileDao fileDao;
    private Cache<SessionContext, UserSession> connectedUsers;
    private int port;

    public Controller(int port) throws RemoteException {
        super();
        userDao = new UserDao();
        fileDao = new FileDao();
        this.port = port;
        fileTransferService = new FileTransferService(port, this);
        connectedUsers  = CacheBuilder.newBuilder()
                            .expireAfterAccess(5, TimeUnit.MINUTES)
                            .removalListener(removalNotification ->
                                    fileTransferService.disconnect((SessionContext) removalNotification.getKey()))
                            .build();
        new Thread(fileTransferService).start();
    }

    public SignUpResponse signUp(SignUpRequest req) {
        SignUpResponse res = new SignUpResponse();
        if(userDao.findByUsername(req.getUsername()) != null) {
            res.setCode(SignUpResponse.Code.USERNAME_EXISTS);
        } else {
            UserEntity newUserEntity = new UserEntity();
            newUserEntity.setName(req.getUsername());
            newUserEntity.setPassword(SecurityUtils.getPasswordHash(req.getUsername(), req.getPassword()));
            userDao.create(newUserEntity);
        }
        return res;
    }

    public LoginResponse login(LoginRequest req) {
        LoginResponse res = new LoginResponse();
        UserEntity userEntity = userDao.findByUsername(req.getUsername());
        if(userEntity == null) {
            res.setCode(LoginResponse.Code.USERNAME_NOT_FOUND);
        } else {
            String hashedPassword = SecurityUtils.getPasswordHash(req.getUsername(), req.getPassword());
            if(!userEntity.getPassword().equals(hashedPassword)) {
                res.setCode(LoginResponse.Code.INVALID_CREDENTIALS);
            } else {
                SessionContext sessionContext = new SessionContext();
                res.setSessionContext(sessionContext);
                res.setServerPort(port);
                UserSession userSession = new UserSession();
                userSession.setUsername(userEntity.getName());
                userSession.setNotificationHandler(req.getNotificationHandler());
                connectedUsers.put(sessionContext, userSession);
            }
        }
        return res;
    }

    public void logout(LogoutRequest req) {
        if(connectedUsers.asMap().containsKey(req.getSessionContext())) {
            connectedUsers.invalidate(req.getSessionContext());
        }
    }

    public ListResponse list(ListRequest req) {
        ListResponse res = new ListResponse();
        if(connectedUsers.asMap().containsKey(req.getSessionContext())) {
            List<FileEntity> fileEntities = fileDao.findAll();
            List<File> files = fileEntities.stream().map(this::getModelFromEntity).collect(Collectors.toList());
            res.setFiles(files);
        } else {
            res.setCode(ListResponse.Code.NO_SESSION);
        }
        return res;
    }

    @Override
    public DeleteResponse delete(DeleteRequest req) {
        DeleteResponse res = new DeleteResponse();
        if(connectedUsers.asMap().containsKey(req.getSessionContext())) {
            FileEntity fileEntity = fileDao.findByName(req.getFileName());
            if(fileEntity == null) {
                res.setCode(DeleteResponse.Code.NOT_FOUND);
            } else {
                UserSession userSession = connectedUsers.getIfPresent(req.getSessionContext());
                boolean requesterIsOwner = fileEntity.getUserEntity().getName().equals(userSession.getUsername());
                if(fileEntity.isReadOnly() && !requesterIsOwner) {
                    res.setCode(DeleteResponse.Code.FORBIDDEN);
                } else {
                    fileDao.deleteById(fileEntity.getId());
                    notify(fileEntity.getName(), fileEntity.getUserEntity().getName(), req.getSessionContext(), Notification.Action.DELETE);
                }
            }
        } else {
            res.setCode(DeleteResponse.Code.NO_SESSION);
        }
        return res;
    }

    public UploadResponse upload(UploadRequest req) {
        UploadResponse res = new UploadResponse();
        if(connectedUsers.asMap().containsKey(req.getSessionContext())) {
            String fileName = req.getFile().getName();
            FileEntity fileEntity = fileDao.findByName(fileName);
            boolean isUpdateRequest = fileEntity != null;
            UserSession userSession = connectedUsers.getIfPresent(req.getSessionContext());
            if(isUpdateRequest) {
                boolean requesterIsOwner = fileEntity.getUserEntity().getName().equals(userSession.getUsername());
                if(fileEntity.isReadOnly() && !requesterIsOwner) {
                    res.setCode(UploadResponse.Code.FORBIDDEN);
                } else {
                    fileEntity.setSize(req.getFile().getSize());
                    fileEntity.setContent(req.getFile().getContent());
                    fileEntity.setReadOnly(req.getFile().isReadOnly());
                    fileDao.update(fileEntity);
                    if(!requesterIsOwner) {
                        notify(fileEntity.getName(), fileEntity.getUserEntity().getName(), req.getSessionContext(), Notification.Action.UPDATE);
                    }
                }
            } else {
                fileEntity = new FileEntity();
                fileEntity.setName(req.getFile().getName());
                fileEntity.setContent(req.getFile().getContent());
                fileEntity.setSize(req.getFile().getSize());
                fileEntity.setReadOnly(req.getFile().isReadOnly());
                fileEntity.setUserEntity(userDao.findByUsername(userSession.getUsername()));
                fileDao.create(fileEntity);
            }
        } else {
            res.setCode(UploadResponse.Code.NO_SESSION);
        }
        return res;
    }

    public DownloadResponse download(DownloadRequest req) {
        DownloadResponse res = new DownloadResponse();
        if(connectedUsers.asMap().containsKey(req.getSessionContext())) {
            FileEntity fileEntity = fileDao.findByName(req.getFileName());
            if(fileEntity == null) {
                res.setCode(DownloadResponse.Code.NOT_FOUND);
            } else {
                res.setFile(getModelFromEntity(fileEntity));
                notify(fileEntity.getName(), fileEntity.getUserEntity().getName(), req.getSessionContext(), Notification.Action.READ);
            }
        } else {
            res.setCode(DownloadResponse.Code.NO_SESSION);
        }
        return res;
    }

    private void notify(String fileName, String ownerUsername, SessionContext sessionContext, Notification.Action action) {
        String actionPerformerUsername = connectedUsers.getIfPresent(sessionContext).getUsername();
        if (ownerUsername.equals(actionPerformerUsername)) {
            return; // do not notify owner if they performed the action themselves
        }
        Notification notification = new Notification();
        notification.setAction(action);
        notification.setFilename(fileName);
        notification.setUsername(actionPerformerUsername);
        for(SessionContext key : connectedUsers.asMap().keySet()) {
            UserSession userSession = connectedUsers.getIfPresent(key);
            if(userSession.getUsername().equals(ownerUsername)) {
                try {
                    userSession.getNotificationHandler().receive(notification);
                } catch(Exception e) {
                    System.out.println("Found disconnected client. Closing session and file transfer connection.");
                    connectedUsers.invalidate(key);
                    fileTransferService.disconnect(key);
                }
            }
        }
    }

    private File getModelFromEntity(FileEntity fileEntity) {
        if(fileEntity == null) {
            return null;
        }
        File file = new File();
        file.setName(fileEntity.getName());
        file.setSize(fileEntity.getSize());
        file.setContent(fileEntity.getContent());
        file.setReadOnly(fileEntity.isReadOnly());
        Owner owner = new Owner();
        owner.setName(fileEntity.getName());
        file.setOwner(owner);
        return file;
    }
}
