package cn.thesilentnights.easylogin.pojo;

import java.util.Map;
import java.util.UUID;

public class PlayerExtraData {
        private UUID uuid;
        private String displayName;
        private double lastLoginX;
        private double lastLoginY;
        private double lastLoginZ;
        private String lastLoginDimension;
        private Long lastLoginTimestamp;
        private String lastLoginIp;

        public PlayerExtraData(UUID uuid) {
                this.uuid = uuid;
        }

        public PlayerExtraData(UUID uuid, String displayName) {
                this.uuid = uuid;
                this.displayName = displayName;
        }

        public PlayerExtraData(
                UUID uuid,
                String displayName,
                double lastLoginX,
                double lastLoginY,
                double lastLoginZ,
                String lastLoginDimension,
                Long lastLoginTimestamp,
                String lastLoginIp
        ) {
                this.uuid = uuid;
                this.displayName = displayName;
                this.lastLoginX = lastLoginX;
                this.lastLoginY = lastLoginY;
                this.lastLoginZ = lastLoginZ;
                this.lastLoginDimension = lastLoginDimension;
                this.lastLoginTimestamp = lastLoginTimestamp;
                this.lastLoginIp = lastLoginIp;
        }

        public String getDisplayName() {
                return displayName;
        }

        public void setDisplayName(String displayName) {
                this.displayName = displayName;
        }

        public UUID getUuid() {
                return uuid;
        }

        public void setUuid(UUID uuid) {
                this.uuid = uuid;
        }

        public double getLastLoginX() {
                return lastLoginX;
        }

        public void setLastLoginX(double lastLoginX) {
                this.lastLoginX = lastLoginX;
        }

        public double getLastLoginY() {
                return lastLoginY;
        }

        public void setLastLoginY(double lastLoginY) {
                this.lastLoginY = lastLoginY;
        }

        public double getLastLoginZ() {
                return lastLoginZ;
        }

        public void setLastLoginZ(double lastLoginZ) {
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

        public String getLastLoginIp() {
                return lastLoginIp;
        }

        public void setLastLoginIp(String lastLoginIp) {
                this.lastLoginIp = lastLoginIp;
        }

        public Map<String, String> getProperties() {
                return Map.of(
                        "displayName", displayName,
                        "lastLoginX", String.valueOf(lastLoginX),
                        "lastLoginY", String.valueOf(lastLoginY),
                        "lastLoginZ", String.valueOf(lastLoginZ),
                        "lastLoginDimension", lastLoginDimension,
                        "lastLoginTimestamp", String.valueOf(lastLoginTimestamp),
                        "lastLoginIp", lastLoginIp
                );
        }
}
