package pt.hdi.filegenerator.model;

import pt.hdi.filegenerator.Utils.ApplicationEnums.SEND_FILE_DATA_MODE;
import pt.hdi.filegenerator.Utils.ApplicationEnums.SEND_FILE_TYPE;

public class SftpFileSchedulerConfig {
    private SEND_FILE_TYPE fileType;
    private SEND_FILE_DATA_MODE dataMode;
    private String cron;
    private String lastObjectId;

    public SftpFileSchedulerConfig() {
    }
    
    public SEND_FILE_TYPE getFileType() {
        return fileType;
    }
    public void setFileType(SEND_FILE_TYPE fileType) {
        this.fileType = fileType;
    }
    public SEND_FILE_DATA_MODE getDataMode() {
        return dataMode;
    }
    public void setDataMode(SEND_FILE_DATA_MODE dataMode) {
        this.dataMode = dataMode;
    }
    public String getCron() {
        return cron;
    }
    public void setCron(String cron) {
        this.cron = cron;
    }

    public String getLastObjectId() {
        return lastObjectId;
    }

    public void setLastObjectId(String lastObjectId) {
        this.lastObjectId = lastObjectId;
    }

    @Override
    public String toString() {
        return "SftpFileSchedulerConfig [fileType=" + fileType + ", dataMode=" + dataMode + ", cron=" + cron
                + ", lastObjectId=" + lastObjectId + "]";
    }


}
