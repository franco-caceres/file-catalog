package pe.fcg.kth.id1212.hw3.server.entity;

import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.Arrays;
import java.util.Objects;

@Entity
@Table(name = "file")
public class FileEntity {
    private int id;
    private UserEntity userEntity;
    private String name;
    private int size;
    private byte[] content;
    private Boolean readOnly;

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "owner_id")
    public UserEntity getUserEntity() {
        return userEntity;
    }

    public void setUserEntity(UserEntity userEntity) {
        this.userEntity = userEntity;
    }

    @Basic
    @Column(name = "name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Basic
    @Column(name = "size")
    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    @Basic
    @Column(name = "content")
    public byte[] getContent() {
        return content;
    }
    public void setContent(byte[] content) {
        this.content = content;
    }

    @Basic
    @Column(name = "readOnly", nullable = false)
    @Type(type = "org.hibernate.type.NumericBooleanType")
    public Boolean isReadOnly() {
        return readOnly;
    }

    public void setReadOnly(Boolean readOnly) {
        this.readOnly = readOnly;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FileEntity fileEntity = (FileEntity) o;
        return id == fileEntity.id &&
                readOnly == fileEntity.readOnly &&
                Objects.equals(name, fileEntity.name) &&
                Objects.equals(size, fileEntity.size) &&
                Arrays.equals(content, fileEntity.content);
    }

    @Override
    public int hashCode() {

        int result = Objects.hash(id, name, size, readOnly);
        result = 31 * result + Arrays.hashCode(content);
        return result;
    }
}
