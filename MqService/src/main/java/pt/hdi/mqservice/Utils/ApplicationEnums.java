package pt.hdi.mqservice.Utils;

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
}
