package pt.hdi.grpcservice.utils;

public class ApplicationEnums {

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
    public enum TECHNOLOGY{
        SFTP("SFTP"),
        GRPC("gRPC"),
        MQQUEUE("MQ Queue");
        private final String label;
        TECHNOLOGY(String label){
            this.label = label;
        }
        public String getLabel(){
            return this.label;
        }
    }

    public enum DATA_STATUS{
        OK("OK"),
        INVALID_DATA("Invalid Data"),
        DUPLICATED("duplicated");
        private final String label;
        DATA_STATUS(String label){
            this.label = label;
        }
        public String getLabel(){
            return this.label;
        }
    }
}
