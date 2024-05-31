package pt.hdi.filegenerator.Utils;

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

    public enum DOCUMENT_STATUS{
        INEXISTS("inexists"),
        FIELDS_OUTDATED("field outdated"),
        ALL_UPDATED("all updated");

        private final String label;

        DOCUMENT_STATUS(String label){
            this.label = label;
        }
        public String getLabel() {
            return label;
        }
    }

    public enum SEND_FILE_TYPE{
        JSON("JSON");
        private final String label;

        SEND_FILE_TYPE(String label){
            this.label = label;
        }
        public String getLabel(){
            return this.label;
        }
    }

    public enum SEND_FILE_DATA_MODE{
        DELTA("Delta"),
        FULL("Full");
        private final String label;
        SEND_FILE_DATA_MODE(String label){
            this.label = label;
        }
        public String getLabel(){
            return this.label;
        }
    }

}
