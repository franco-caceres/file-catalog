package pe.fcg.kth.id1212.hw3.server.dao;

import pe.fcg.kth.id1212.hw3.server.entity.FileEntity;

import javax.persistence.*;
import java.util.List;

public class FileDao extends BaseDao {
    public void create(FileEntity fileEntity) {
        EntityManager em = beginTransaction();
        try {
            em.persist(fileEntity);
        } finally {
            commitTransaction();
        }
    }

    public void update(FileEntity fileEntity) {
        EntityManager em = beginTransaction();
        try {
            em.merge(fileEntity);
        } finally {
            commitTransaction();
        }
    }

    public void deleteById(int id) {
        EntityManager em = beginTransaction();
        try {
            em.remove(em.find(FileEntity.class, id));
        } finally {
            commitTransaction();
        }
    }

    public FileEntity findByName(String name) {
        EntityManager em = beginTransaction();
        TypedQuery<FileEntity> query = em.createQuery("select f from FileEntity f where f.name=:name", FileEntity.class);
        query.setParameter("name", name);
        try {
            List<FileEntity> result = query.getResultList();
            if(result.size() > 0) {
                return result.get(0);
            } else {
                return null;
            }
        } finally {
            commitTransaction();
        }
    }

    public List<FileEntity> findAll() {
        EntityManager em = beginTransaction();
        TypedQuery<FileEntity> query = em.createQuery("select f from FileEntity f", FileEntity.class);
        try {
            List<FileEntity> result = query.getResultList();
            for(FileEntity fileEntity : result) {
                em.detach(fileEntity);
                fileEntity.setContent(null);
            }
            return result;
        } finally {
            commitTransaction();
        }
    }
}
