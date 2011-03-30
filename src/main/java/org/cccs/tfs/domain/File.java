package org.cccs.tfs.domain;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import org.cccs.tfs.service.Unique;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;

/**
 * User: boycook
 * Date: 29/03/2011
 * Time: 20:58
 */
@Entity
@Table(name="files",
        uniqueConstraints = {
            @UniqueConstraint(columnNames = {
                "artefactId",
                "groupId",
                "version"
            })
        })
@Unique(entity = File.class, idField = "id", uniqueFields = {
                "artefactId",
                "groupId",
                "version"
            }, message = "Short Name must be unique")
@XStreamAlias("file")
public class File {

    private long id;
    private String groupId;
    private String artefactId;
    private String version;
    private String url;

    private String website;
    private String name;
    private String description;
    private String storageType;

    public File() {
    }

    public File(String groupId, String artefactId, String version, String url) {
        this.groupId = groupId;
        this.artefactId = artefactId;
        this.version = version;
        this.url = url;
    }

    @Id
    @GeneratedValue(strategy = javax.persistence.GenerationType.SEQUENCE)
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @NotEmpty(message = "GroupID cannot be empty")
    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    @NotEmpty(message = "ArtefactID cannot be empty")
    public String getArtefactId() {
        return artefactId;
    }

    public void setArtefactId(String artefactId) {
        this.artefactId = artefactId;
    }

    @NotEmpty(message = "Version cannot be empty")
    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    @NotEmpty(message = "File URL cannot be empty")
    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStorageType() {
        return storageType;
    }

    public void setStorageType(String storageType) {
        this.storageType = storageType;
    }

    @Override
    public String toString() {
        return "File{" +
                "groupId='" + groupId + '\'' +
                ", artefactId='" + artefactId + '\'' +
                ", version='" + version + '\'' +
                '}';
    }
}
