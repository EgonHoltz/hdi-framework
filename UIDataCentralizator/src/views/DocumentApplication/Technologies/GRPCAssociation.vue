<template>
    <b-card body-class="p-0" header-class="border-0">
        <template v-slot:header>
            <div style="display: flex; align-items: center; justify-content: space-between;">
                <!-- Rounded icon (adjust the class and styling as needed) -->
                <span class="avatar avatar-sm rounded-circle">
                    <img alt="gRPC" src="img/icons/cards/gRPC.png">
                  </span>
                <!-- Title -->
                <div class="card-title">gRPC configuration</div>
                <b-button v-b-toggle.collapse-2 @click="doGRPCActivate" :variant="buttonGRPCProp.type">{{buttonGRPCProp.label}}</b-button>
            </div>
            <b-collapse  v-model="isGRPCActive" class="mt-2">
                <el-form :model="editForm" label-width="120px" label-position="top">
                <el-row><h3>gRPC Send information</h3></el-row>
                <el-row>
                    <el-col :span="4" class="p-2">
                        <el-form-item >
                            <el-checkbox v-model="editForm.sendActive">Active</el-checkbox>
                        </el-form-item>
                    </el-col>
                </el-row>
                <el-row>
                    <el-col :span="11" class="p-2">
                        <el-form-item label="Client Identification" class="row-with-space">
                            <el-input v-model="editForm.sendClientId"></el-input>
                        </el-form-item>
                    </el-col>
                </el-row>
                <hr>
                <el-row><h3>GRPC Receive information</h3></el-row>
                <el-row>
                    <el-col :span="4" class="p-2">
                        <el-form-item >
                            <el-checkbox v-model="editForm.recvActive">Active</el-checkbox>
                        </el-form-item>
                    </el-col>
                </el-row>
                <el-row>
                    <el-col :span="11" class="p-2">
                        <el-form-item label="Client Identification" class="row-with-space">
                            <el-input v-model="editForm.recvClientId"></el-input>
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
      import {UPSERT_GRPC, FETCH_GRPC} from '@/store/document-application/document-application.constants';
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
                    console.log("GRPC to associate the app: " + this.applicationId);
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
                sendActive: false,
                sendClientId: '',
                recvActive: false,
                recvClientId: '',
            },
            isGRPCActive: false,
            buttonGRPCProp: {
                type: "success",
                label: "Active"
            },
            newGRPCConfig: [],
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
                this.$store.dispatch(`documentApplication/${FETCH_GRPC}`, payloadIds).then( 
                    () => {
                    let GRPCConf = this.$store.getters['documentApplication/getGrpc'];
                    console.log("Formating GRPC " + GRPCConf);
                    this.moveDataToForm(GRPCConf);
                }, err => {
                    this.alertTitle = "Error while fetch"
                    this.setSectionInactive();
                });
            },
            refreshForm(){
                this.editForm.sendActive = false;
                this.editForm.sendClientId = "";
                this.editForm.recvActive = false;
                this.editForm.recvClientId = "";
            },
            moveDataToForm(GRPCConf){
                if (GRPCConf != null){
                    this.editForm.isActive = true;
                    const sendFields = GRPCConf.find(item => item.direction === 'SEND');
                    const receiveFields = GRPCConf.find(item => item.direction === 'RECEIVE');
                    
                    if (receiveFields != null){
                        this.editForm.recvActive = receiveFields.active;
                        this.editForm.recvClientId = receiveFields.clientId;
                    }
                    if (sendFields != null){
                        this.editForm.sendActive = sendFields.active;
                        this.editForm.sendClientId = sendFields.clientId;
                    }
                    this.setSectionActive();
                } else {
                    this.setSectionInactive();
                }
                this.$forceUpdate();
            },
            setSectionActive(){
                this.buttonGRPCProp.type = "danger";
                this.buttonGRPCProp.label ="Inactive"
                this.editForm.isActive = true;
                this.isGRPCActive = true;
            },
            setSectionInactive(){
                this.buttonGRPCProp.type = "success";
                this.buttonGRPCProp.label ="Active"
                this.editForm.isActive = false;
                this.isGRPCActive = false;
            },
            doGRPCActivate(){
              if (this.buttonGRPCProp.type ==="success") {
                this.buttonGRPCProp.type = "danger";
                this.buttonGRPCProp.label ="Inactive"
                this.editForm.isActive = true;
                this.isGRPCActive = true;
              } else {
                this.buttonGRPCProp.type = "success";
                this.buttonGRPCProp.label ="Active"
                this.editForm.isActive = false;
                this.isGRPCActive = false;
              }
            },
            patchFieldsToObject(){
                if (this.editForm.sendClientId.trim() != ""){
                    this.newGRPCConfig.push({
                        active: this.editForm.sendActive,
                        clientId: this.editForm.sendClientId,
                        direction: 'SEND'
                    })
                }
                if (this.editForm.recvClientId.trim() != ""){
                    this.newGRPCConfig.push({
                        active: this.editForm.recvActive,
                        clientId: this.editForm.recvClientId,
                        direction: 'RECEIVE'
                    })
                }
            },
            submitForm(){
                this.newGRPCConfig = [];
                this.patchFieldsToObject();
                const documentId = this.$route.params.id;
                //add the ids to payload
                let id = {
                    applicationId: this.applicationId,
                    documentId: documentId
                }
                this.newGRPCConfig.id = id;
                console.log("Saving the GRPC");
                this.$store.dispatch(`documentApplication/${UPSERT_GRPC}`,this.newGRPCConfig).then( 
                    () => {
                        this.editDialogVisible = false;
                        this.alertTitle = "GRPC configuration saved!"                   
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
    