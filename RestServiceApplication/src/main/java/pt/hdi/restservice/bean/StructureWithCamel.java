package pt.hdi.restservice.bean;

import pt.hdi.restservice.Utils.ObjectHelper;
import pt.hdi.restservice.model.Structure;

public class StructureWithCamel extends Structure {
    private String fieldNameCamel;

    public StructureWithCamel(Structure structure) {
        super(structure.getFieldName(), structure.getType(), structure.getMandatory(),structure.getRegExp(), structure.getUseAsQuery());
        this.fieldNameCamel = ObjectHelper.getCamelFieldName(structure.getFieldName());
    }

    public String getFieldNameCamel() {
        return fieldNameCamel;
    }

    public void setFieldNameCamel(String fieldNameCamel) {
        this.fieldNameCamel = fieldNameCamel;
    }
    
}
