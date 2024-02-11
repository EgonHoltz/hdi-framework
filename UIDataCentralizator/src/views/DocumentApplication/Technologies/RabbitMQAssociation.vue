<template>
    <b-card body-class="p-0" header-class="border-0">
        <template v-slot:header>
            <div style="display: flex; align-items: center; justify-content: space-between;">
                <!-- Rounded icon (adjust the class and styling as needed) -->
                <span class="avatar avatar-sm rounded-circle">
                    <img alt="RabbitMQ" src="img/icons/cards/rabbitmq.png">
                  </span>
                <!-- Title -->
                <div class="card-title">MQ Queue configuration</div>
                <b-button v-b-toggle.collapse-1 @click="doActivate" :variant="buttonProp.type">{{buttonProp.label}}</b-button>
            </div>
            <b-collapse id="collapse-1" v-model="isActive" class="mt-2">
                <el-form :model="editForm" label-width="120px" label-position="top">
                <el-row>
                    <el-col :span="14" class="p-2">
                        <el-form-item label="Input MQ Queue name" class="row-with-space">
                            <el-input v-model="editForm.inputQueueName"></el-input>
                        </el-form-item>
                    </el-col>
                    <el-col :span="4" class="p-2">
                        <el-form-item>
                            <el-checkbox v-model="editForm.inputActive">Active</el-checkbox>
                        </el-form-item>
                        <el-form-item>
                            <el-checkbox v-model="editForm.inputHasAcknowledge">Has acknowledgment</el-checkbox>
                        </el-form-item>
                    </el-col>
                </el-row>
                <hr>
                <el-row>
                    <el-col :span="14" class="p-2">
                        <el-form-item label="Output MQ Queue name">
                            <el-input v-model="editForm.outputQueueName"></el-input>
                        </el-form-item>
                    </el-col>
                    <el-col :span="4" class="p-2">
                        <el-form-item >
                            <el-checkbox v-model="editForm.outputActive">Active</el-checkbox>
                        </el-form-item>
                        <el-form-item>
                            <el-checkbox v-model="editForm.outputHasAcknowledge">Has acknowledgment</el-checkbox>
                        </el-form-item>
                    </el-col>
                </el-row>
                <el-form-item class="p-4">
                                <el-button type="primary" @click="submitForm">Save</el-button>
                </el-form-item>
            </el-form>
        </b-collapse>
    </template>
    </b-card>
</template>
<script>
      import { BaseProgress } from '@/components';
      import { mapGetters } from "vuex";
      import {UPSERT_RABBIT_MQ, FETCH_RABBIT_MQ} from '@/store/document-application/document-application.constants';
      import { Table, TableColumn, Button, Form, FormItem, Col, Row, Input, Checkbox, Collapse, CollapseItem} from 'element-ui';
      export default {
        components: {
          BaseProgress,
          [Table.name]: Table,
          [Button.name]: Button,
          [Col.name]: Col,
          [Row.name]: Row,
          [TableColumn.name]: TableColumn,
          [Form.name]: Form,
          [FormItem.name]: FormItem,
          [Input.name]: Input,
          [Checkbox.name]: Checkbox,
          [Collapse.name]: Collapse,
          [CollapseItem.name]: CollapseItem,
        },
        props: {
            selectedApplication: {
                type: Object,
                required: true,
            }
        },
        watch: {
            selectedApplication: {
                handler(newValue) {
                    this.applicationId = newValue.id;
                    console.log("MQ to associate the app: " + this.applicationId);
                    //load associated tech to forms
                    this.afterRender();
                },
                deep: true,
            },
        },
        data() {
          return {
            applicationId: null,
            editDialogVisible: false,
            editForm: {
                isActive: false,
                inputQueueName: '',
                inputActive: false,
                inputHasAcknowledge: false,
                outputQueueName: '',
                outputActive: false,
                outputHasAcknowledge: false,
            },
            isActive: false,
            buttonProp: {
                type: "success",
                label: "Active"
            },
            newRabbitMq: [],
          }
        },
        created() {
          this.isReadOnly = false;
          this.visibleAlert = null;
        },
        methods: {
            afterRender(){
                this.fetchTechnologies();
            },
            fetchTechnologies(){
                this.refreshForm();
                const documentId = this.$route.params.id;
                console.log("selectedApplication: " + this.applicationId);
                console.log("on the document: " + documentId);
                let payloadIds = {
                    applicationId: this.applicationId,
                    documentId: documentId
                }
                this.$store.dispatch(`documentApplication/${FETCH_RABBIT_MQ}`, payloadIds).then( 
                    () => {
                    let rabbitMq = this.$store.getters['documentApplication/getRabbitMq'];
                    console.log("Formating rabbitMq " + rabbitMq);
                    this.moveDataToForm(rabbitMq);
                }, err => {
                    this.alertTitle = "Error while fetch"
                    this.setSectionInactive();
                });
            },
            refreshForm(){
                this.editForm.inputQueueName = "";
                this.editForm.inputActive = false;
                this.editForm.inputHasAcknowledge = false;
                this.editForm.outputQueueName = "";
                this.editForm.outputActive = false;
                this.editForm.outputHasAcknowledge = false;
            },
            moveDataToForm(rabbitMQ){
                if (rabbitMQ != null){
                    this.editForm.isActive = true;
                    const sendFields = rabbitMQ.find(item => item.direction === 'SEND');
                    const receiveFields = rabbitMQ.find(item => item.direction === 'RECEIVE');

                    if (receiveFields != null){
                        this.editForm.inputQueueName = receiveFields.mqName;
                        this.editForm.inputActive = receiveFields.active;
                        this.editForm.inputHasAcknowledge = receiveFields.hasAck;
                    }
                    if (sendFields != null){
                        this.editForm.outputQueueName = sendFields.mqName;
                        this.editForm.outputActive = sendFields.active;
                        this.editForm.outputHasAcknowledge = sendFields.hasAck;
                    }
                    this.setSectionActive();
                } else {
                    this.setSectionInactive();
                }
                this.$forceUpdate();
            },
            setSectionActive(){
                this.buttonProp.type = "danger";
                this.buttonProp.label ="Inactive"
                this.editForm.isActive = true;
                this.isActive = true;
            },
            setSectionInactive(){
                this.buttonProp.type = "success";
                this.buttonProp.label ="Active"
                this.editForm.isActive = false;
                this.isActive = false;
            },
            doActivate(){
              if (this.buttonProp.type ==="success") {
                this.buttonProp.type = "danger";
                this.buttonProp.label ="Inactive"
                this.editForm.isActive = true;
                this.isActive = true;
              } else {
                this.buttonProp.type = "success";
                this.buttonProp.label ="Active"
                this.editForm.isActive = false;
                this.isActive = false;
              }
            },
            patchFieldsToObject(){
                if (this.editForm.inputQueueName.trim() != ""){
                    this.newRabbitMq.push({
                        mqName: this.editForm.inputQueueName,
                        active: this.editForm.inputActive,
                        hasAck: this.editForm.inputHasAcknowledge,
                        direction: 'RECEIVE'                        
                    })
                }
                if (this.editForm.outputQueueName.trim() != ""){
                    this.newRabbitMq.push({
                        mqName: this.editForm.outputQueueName,
                        active: this.editForm.outputActive,
                        hasAck: this.editForm.outputHasAcknowledge,
                        direction: 'SEND'                        
                    })
                }
            },
            submitForm(){
                this.newRabbitMq = [];
                this.patchFieldsToObject();
                const documentId = this.$route.params.id;
                //add the ids to payload
                let id = {
                    applicationId: this.applicationId,
                    documentId: documentId
                }
                this.newRabbitMq.id = id;
                console.log("Saving the MQ");
                this.$store.dispatch(`documentApplication/${UPSERT_RABBIT_MQ}`,this.newRabbitMq).then( 
                    () => {
                        this.editDialogVisible = false;
                        this.alertTitle = "Document created with success!"                   
                        this.showSuccessAlert();
                        this.$router.go(-1); // Go back one step in the browser history
                    }, err => {
                        this.editDialogVisible = false;
                        this.alertTitle = "Error on creation. Please contact the IT team"
                        this.showErrorAlert();
                    });

            },
            handleAppFromTable(application){
                console.log("received the row: " + application.documentName);

            },
        }
      }
    </script>
<style scoped>

.card-title {
  margin-left: 10px;
  display: inline-block;
}
</style>
    