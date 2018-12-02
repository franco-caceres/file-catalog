package pe.fcg.kth.id1212.hw3.server.dao;

import pe.fcg.kth.id1212.hw3.server.entity.UserEntity;

import javax.persistence.*;
import java.util.List;

public class UserDao extends BaseDao {

    public void create(UserEntity userEntity) {
        EntityManager em = beginTransaction();
        try {
            em.persist(userEntity);
        } finally {
            commitTransaction();
        }
    }

    public UserEntity findByUsername(String username) {
        EntityManager em = beginTransaction();
        TypedQuery<UserEntity> query = em.createQuery("select u from UserEntity u where u.name=:username", UserEntity.class);
        query.setParameter("username", username);
        try {
            List<UserEntity> result = query.getResultList();
            if(result.size() > 0) {
                return result.get(0);
            } else {
                return null;
            }
        } finally {
            commitTransaction();
        }
    }
}
