<template>
    <div>
      <h3 class="form-title p-2">Document Structure</h3>
      <b v-show="false">{{documentId}}</b>
      <b-table :items="tableData" :fields="fields">
        <template #cell(fieldName)="row">
            <template v-if="row.item.editing && row.item.isNewLine">
                <b-form-input v-model="row.item.fieldName"></b-form-input>
            </template>
            <template v-else>{{ row.value }} </template>
        </template>
        <template #cell(type)="row">
            <template v-if="!row.item.editing">{{ row.value }}</template>
            <template v-else>
                <b-form-select v-model="row.item.type" :options="mongoDBDataTypes"></b-form-select>
            </template>
        </template>
        <template #cell(mandatory)="row">
          <template v-if="!row.item.editing">{{ row.value }}</template>
          <template v-else>
            <b-form-checkbox v-model="row.item.mandatory"></b-form-checkbox>
          </template>
        </template>
        <template #cell(regExp)="row">
          <template v-if="!row.item.editing">{{ row.value }}</template>
          <template v-else>
            <b-form-input v-model="row.item.regExp"></b-form-input>
          </template>
        </template>
        <template #cell(useAsQuery)="row">
          <template v-if="!row.item.editing">{{ row.value }}</template>
          <template v-else>
            <b-form-checkbox v-model="row.item.useAsQuery"></b-form-checkbox>
          </template>
        </template>
        <template #cell(actions)="row">
          <el-button type="text" @click="toggleEdit(row.item)">{{ row.item.editing === true ? "Save" : "Edit" }}</el-button>
        </template>
    </b-table>
    <b-button @click="addNewLine">Add new line</b-button>
    </div>
  </template>
  
  <script>
    import {FETCH_DOCUMENT_STRUCTURE, ADD_DOCUMENT_STRUCTURE, EDIT_DOCUMENT_STRUCTURE} from '@/store/document/document.constants';
    import {Alert, Button} from 'element-ui';
    export default {
    components: {
        [Button.name]: Button,
        [Alert.name]: Alert,
    },
    props: {
        documentId: {
            type: String,
            required: true,
        }
    },
    data() {
      return {
        mongoDBDataTypes: ['String', 'Number', 'Boolean', 'Date', 'Array', 'Object'],
        fields: [
          { key: 'fieldName', label: 'Field Name' },
          { key: 'type', label: 'Type', editable: true, formatter: this.typeFormatter },
          { key: 'mandatory', label: 'Mandatory' },
          { key: 'regExp', label: 'RegExp' },
          { key: 'useAsQuery', label: 'Use this field on query' },
          { key: 'actions', label: 'Actions' },
        ],
        tableData: [
        ],
        creatingMode: false,
      };
    },
    created() {
        const id = this.documentId;
        if (id){
            // Make an API request to fetch the document structure by 'id'
            this.$store.dispatch(`document/${FETCH_DOCUMENT_STRUCTURE}`,id).then( 
                (res) => {
                    console.log("got structures " +res);
                    let documentStructure = this.$store.getters['document/getDocumentStructure'];
                    documentStructure.forEach(row => {
                        row.editing = false;
                        row.isNewLine = false;
                    });
                    this.tableData = documentStructure;
                    if (this.tableData.size == 0){
                        this.addNewLine();
                    }
                }, err => {
                    this.alertTitle = "Error while fetch"
            });
        } else {
            this.creatingMode = true;
            this.addNewLine();
        }
    },
    methods: {
        typeFormatter(value) {
            if (this.editableTable) {
                return `<b-form-select v-model="data['type']" :options="mongoDBDataTypes"></b-form-select>`;
            }
            return value;
        },
        toggleEdit(row) {
            console.log("editing the row: "+row);
            if (row.editing == true){
                if (this.creatingMode){
                    this.$emit('structure',row);
                }else {
                    const fieldNameValue = row.fieldName;
                    const hasValue = this.tableData.some(item => item.fieldName === fieldNameValue && !item.isNewLine);
                    row.id = this.documentId;
                    console.log("hasvalue?" + hasValue);
                    if (hasValue){ //Editing
                        this.$store.dispatch(`document/${EDIT_DOCUMENT_STRUCTURE}`,row).then( 
                        () => {
                            this.editDialogVisible = false;
                            this.alertTitle = "Structure changed with success!"
                        }, err => {
                            this.editDialogVisible = false;
                            this.alertTitle = "Error on change. Please contact the IT team"
                            this.showErrorAlert();
                        });
                    } else {
                        this.$store.dispatch(`document/${ADD_DOCUMENT_STRUCTURE}`,row).then(
                        () => {
                            this.editDialogVisible = false;
                            this.alertTitle = "Structure created with success!"                   
                            this.showSuccessAlert();
                        }, err => {
                            this.editDialogVisible = false;
                            this.alertTitle = "Error on creation. Please contact the IT team"
                            this.showErrorAlert();
                        });
                    }
                }
            }
            row.editing = !row.editing;
            row.isNewLine = false;
            this.$forceUpdate();
        },
        addNewLine(){
            const newLine = {
                fieldName: 'Fill the name',
                type: 'Fill the type',
                mandatory: false,
                regExp: '',
                useAsQuery: false,
                editing: true,
                isNewLine: true
            };
            this.tableData.push(newLine);
        }
    },
  };
  </script>
  
  <style>
  /* Your custom styles go here */
  </style>
  