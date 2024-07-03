package pt.hdi.sftpservice.utils;

public class ApplicationEnums {
    public enum SEND_SFTP_STATUS {
        FILE_GENERATED("file_generated","File has generated"),
        PREPARING_SFTP("preparing_sftp","Preparing to send by SFTP"),
        SENT("sent","File sent to destination");

        private final String value;
        private final String label;
        
        SEND_SFTP_STATUS(String value, String label){
            this.value = value;
            this.label = label;            
        }
        public String getValue() {
            return value;
        }

        public String getLabel() {
            return label;
        }        
    }

    public enum FLOW_DIRECTION {
        SEND("send", "Send"),
        RECEIVE("recv", "Receive");

        private final String value;
        private final String label;

        FLOW_DIRECTION(String value, String label) {
            this.value = value;
            this.label = label;
        }

        public String getValue() {
            return value;
        }

        public String getLabel() {
            return label;
        }
    }
}
