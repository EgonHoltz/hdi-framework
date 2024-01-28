<template>
    <b-card body-class="p-0" header-class="border-0">
        <template v-slot:header>
            <div style="display: flex; align-items: center; justify-content: space-between;">
                <!-- Rounded icon (adjust the class and styling as needed) -->
                <span class="avatar avatar-sm rounded-circle">
                    <img alt="SFTP" src="img/icons/cards/sftp.png">
                  </span>
                <!-- Title -->
                <div class="card-title">SFTP configuration</div>
                <b-button v-b-toggle.collapse-2 @click="doSFTPActivate" :variant="buttonSFTPProp.type">{{buttonSFTPProp.label}}</b-button>
            </div>
            <b-collapse  v-model="isSFTPActive" class="mt-2">
                <el-form :model="editForm" label-width="120px" label-position="top">
                <el-row><h3>SFTP Send information</h3></el-row>
                <el-row>
                    <el-col :span="4" class="p-2">
                        <el-form-item >
                            <el-checkbox v-model="editForm.sendActive">Active</el-checkbox>
                        </el-form-item>
                    </el-col>
                </el-row>
                <el-row>
                    <el-col :span="18" class="p-2">
                        <el-form-item label="Host" class="row-with-space">
                            <el-input v-model="editForm.host"></el-input>
                        </el-form-item>
                    </el-col>
                    <el-col :span="4" class="p-2">
                        <el-form-item label="Port" class="row-with-space">
                            <el-input v-model="editForm.port"></el-input>
                        </el-form-item>
                    </el-col>
                </el-row>
                <el-row>
                    <el-col :span="11" class="p-2">
                        <el-form-item label="User" class="row-with-space">
                            <el-input v-model="editForm.sendUser"></el-input>
                        </el-form-item>
                    </el-col>
                    <el-col :span="11" class="p-2">
                        <el-form-item label="Password" class="row-with-space">
                            <el-input v-model="editForm.sendPassword"></el-input>
                        </el-form-item>
                    </el-col>
                </el-row>
                <el-row>
                    <el-col :span="12" class="p-2">
                        <el-form-item label="Destination path" class="row-with-space">
                            <el-input v-model="editForm.destinationPath"></el-input>
                        </el-form-item>
                    </el-col>
                    <el-col :span="10" class="p-2">
                        <el-form-item label="Filename" class="row-with-space">
                            <el-input v-model="editForm.sendFileName"></el-input>
                        </el-form-item>
                    </el-col>
                </el-row>
                <hr>
                <el-row><h3>SFTP Receive information</h3></el-row>
                <el-row>
                    <el-col :span="4" class="p-2">
                        <el-form-item >
                            <el-checkbox v-model="editForm.recvActive">Active</el-checkbox>
                        </el-form-item>
                    </el-col>
                </el-row>
                <el-row>
                    <el-col :span="11" class="p-2">
                        <el-form-item label="User" class="row-with-space">
                            <el-input v-model="editForm.recvUser"></el-input>
                        </el-form-item>
                    </el-col>
                    <el-col :span="11" class="p-2">
                        <el-form-item label="Password" class="row-with-space">
                            <el-input v-model="editForm.recvPassword"></el-input>
                        </el-form-item>
                    </el-col>
                </el-row>
                <el-row>
                    <el-col :span="22" class="p-2">
                        <el-form-item label="Filename" class="row-with-space">
                            <el-input v-model="editForm.recvFileName"></el-input>
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
      import {UPSERT_SFTP, FETCH_SFTP} from '@/store/document-application/document-application.constants';
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
                    console.log("SFTP to associate the app: " + this.applicationId);
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
                host: '',
                port: null,
                sendUser: '',
                sendPassword: '',
                destinationPath: '',
                sendFileName: '',
                recvActive: false,
                recvUser: '',
                recvPassword: '',
                recvFileName: ''
            },
            isSFTPActive: false,
            buttonSFTPProp: {
                type: "success",
                label: "Active"
            },
            newSftpConfig: [],
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
                this.$store.dispatch(`documentApplication/${FETCH_SFTP}`, payloadIds).then( 
                    () => {
                    let sftpConf = this.$store.getters['documentApplication/getSftp'];
                    console.log("Formating sftp " + sftpConf);
                    this.moveDataToForm(sftpConf);
                }, err => {
                    this.alertTitle = "Error while fetch"
                    this.setSectionInactive();
                });
            },
            refreshForm(){
                this.editForm.sendActive = false;
                this.editForm.host = "";
                this.editForm.port = null;
                this.editForm.sendUser = "";
                this.editForm.sendPassword = "";
                this.editForm.destinationPath = "";
                this.editForm.sendFileName = "";
                this.editForm.recvActive = false;
                this.editForm.recvUser = "";
                this.editForm.recvPassword = "";
                this.editForm.recvFileName = "";
            },
            moveDataToForm(sftpConf){
                if (sftpConf != null){
                    this.editForm.isActive = true;
                    const sendFields = sftpConf.find(item => item.direction === 'SEND');
                    const receiveFields = sftpConf.find(item => item.direction === 'RECEIVE');
                    
                    if (receiveFields != null){
                        this.editForm.recvActive = receiveFields.active;
                        this.editForm.recvUser = receiveFields.user;
                        this.editForm.recvPassword = receiveFields.password;
                        this.editForm.recvFileName = receiveFields.sftpFileName;
                    }
                    if (sendFields != null){
                        this.editForm.sendActive = sendFields.active;
                        this.editForm.host = sendFields.host;
                        this.editForm.port = sendFields.port;
                        this.editForm.sendUser = sendFields.user;
                        this.editForm.sendPassword = sendFields.password;
                        this.editForm.destinationPath = sendFields.destinationPath;
                        this.editForm.sendFileName = sendFields.sftpFileName;
                    }
                    this.setSectionActive();
                } else {
                    this.setSectionInactive();
                }
                this.$forceUpdate();
            },
            setSectionActive(){
                this.buttonSFTPProp.type = "danger";
                this.buttonSFTPProp.label ="Inactive"
                this.editForm.isActive = true;
                this.isSFTPActive = true;
            },
            setSectionInactive(){
                this.buttonSFTPProp.type = "success";
                this.buttonSFTPProp.label ="Active"
                this.editForm.isActive = false;
                this.isSFTPActive = false;
            },
            doSFTPActivate(){
              if (this.buttonSFTPProp.type ==="success") {
                this.buttonSFTPProp.type = "danger";
                this.buttonSFTPProp.label ="Inactive"
                this.editForm.isActive = true;
                this.isSFTPActive = true;
              } else {
                this.buttonSFTPProp.type = "success";
                this.buttonSFTPProp.label ="Active"
                this.editForm.isActive = false;
                this.isSFTPActive = false;
              }
            },
            patchFieldsToObject(){
                if (this.editForm.sendFileName.trim() != ""){
                    this.newSftpConfig.push({
                        active: this.editForm.sendActive,
                        host: this.editForm.host,
                        port: this.editForm.port,
                        user: this.editForm.sendUser,
                        password: this.editForm.sendPassword,
                        destinationPath: this.editForm.destinationPath,
                        sftpFileName: this.editForm.sendFileName,
                        direction: 'SEND'
                    })
                }
                if (this.editForm.recvFileName.trim() != ""){
                    this.newSftpConfig.push({
                        active: this.editForm.recvActive,
                        user: this.editForm.recvUser,
                        password: this.editForm.recvPassword,
                        sftpFileName: this.editForm.recvFileName,
                        direction: 'RECEIVE'
                    })
                }
            },
            submitForm(){
                this.newSftpConfig = [];
                this.patchFieldsToObject();
                const documentId = this.$route.params.id;
                //add the ids to payload
                let id = {
                    applicationId: this.applicationId,
                    documentId: documentId
                }
                this.newSftpConfig.id = id;
                console.log("Saving the SFTP");
                this.$store.dispatch(`documentApplication/${UPSERT_SFTP}`,this.newSftpConfig).then( 
                    () => {
                        this.editDialogVisible = false;
                        this.alertTitle = "SFTP configuration saved!"                   
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
    