package org.cccs.tfs.domain;

import com.thoughtworks.xstream.annotations.XStreamAlias;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * User: boycook
 * Date: 29/03/2011
 * Time: 20:58
 */
@Entity
@Table(name="files")
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

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getArtefactId() {
        return artefactId;
    }

    public void setArtefactId(String artefactId) {
        this.artefactId = artefactId;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

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
}
