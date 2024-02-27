package pt.hdi.restservice.bean;

public class DocumentFilterFields {
    private String name;
    private String value;
    
    public DocumentFilterFields() {
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getValue() {
        return value;
    }
    public void setValue(String value) {
        this.value = value;
    }
    @Override
    public String toString() {
        return "DocumentFilterFields [name=" + name + ", value=" + value + "]";
    }
}
