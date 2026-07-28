package cn.thesilentnights.easylogin.pojo;

public class PlayerExtraData {
    private String uuid;
    private String displayName;
    private int lastLoginX;
    private int lastLoginY;
    private int lastLoginZ;
    private String lastLoginDimension;
    private Long lastLoginTimestamp;

    public PlayerExtraData(String uuid) {
        this.uuid = uuid;
    }

    public PlayerExtraData(String uuid, String displayName) {
        this.uuid = uuid;
        this.displayName = displayName;
    }

    public PlayerExtraData(String uuid, String displayName, int lastLoginX, int lastLoginY, int lastLoginZ, String lastLoginDimension, Long lastLoginTimestamp) {
        this.uuid = uuid;
        this.displayName = displayName;
        this.lastLoginX = lastLoginX;
        this.lastLoginY = lastLoginY;
        this.lastLoginZ = lastLoginZ;
        this.lastLoginDimension = lastLoginDimension;
        this.lastLoginTimestamp = lastLoginTimestamp;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public int getLastLoginX() {
        return lastLoginX;
    }

    public void setLastLoginX(int lastLoginX) {
        this.lastLoginX = lastLoginX;
    }

    public int getLastLoginY() {
        return lastLoginY;
    }

    public void setLastLoginY(int lastLoginY) {
        this.lastLoginY = lastLoginY;
    }

    public int getLastLoginZ() {
        return lastLoginZ;
    }

    public void setLastLoginZ(int lastLoginZ) {
        this.lastLoginZ = lastLoginZ;
    }

    public String getLastLoginDimension() {
        return lastLoginDimension;
    }

    public void setLastLoginDimension(String lastLoginDimension) {
        this.lastLoginDimension = lastLoginDimension;
    }

    public Long getLastLoginTimestamp() {
        return lastLoginTimestamp;
    }

    public void setLastLoginTimestamp(Long lastLoginTimestamp) {
        this.lastLoginTimestamp = lastLoginTimestamp;
    }
}
