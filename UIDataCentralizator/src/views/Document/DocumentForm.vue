<template>
    <div>
        <!-- <el-alert
            v-if="visibleAlert !== null"
            :title="alertTitle"
            :type=visibleAlert
            @close="visibleAlert = null"
        >
        </el-alert> -->
        <base-header class="pb-6 pb-8 pt-5 pt-md-8 bg-gradient-success">
        </base-header>
        <b-container fluid class="mt--7">
            <!--Tables-->
            <b-row class="mt-5">
                <b-col xl="10" class="mb-5 mb-xl-0">
                    <b-card body-class="p-0" header-class="border-0">
                        <div class="form-container">
                        <h1 class="form-title">{{ mode === "edit" ? "Modify Document" : "Create Document" }}</h1>
                        <h3 class="p-2">Document Details</h3>
                            <el-form ref="documentForm" :model="document" label-width="120px" label-position="top">
                            <el-row>
                                <el-col :span="12">
                                <el-form-item label="Document Name" class="row-with-space">
                                    <el-input v-model="document.documentName" :disabled="isReadOnly"></el-input>
                                </el-form-item>
                                </el-col>
                                <el-col :span="12">
                                <el-form-item label="Short Detail" class="row-with-space">
                                    <el-input v-model="document.documentShortDetail"></el-input>
                                </el-form-item>
                                </el-col>
                            </el-row>
                            <el-row>
                                <el-col :span="12">
                                <el-form-item label="Owner" class="row-with-space">
                                    <el-input v-model="document.owner"></el-input>
                                </el-form-item>
                                </el-col>
                                <el-col :span="12">
                                <el-form-item label="Approved By" class="row-with-space">
                                    <el-input v-model="document.approvedBy" ></el-input>
                                </el-form-item>
                                </el-col>
                            </el-row>
                            <el-row>
                                <el-col :span="12">
                                <el-form-item label="Contains Sensitive Data" class="row-with-space">
                                    <el-checkbox v-model="document.containsSensitiveData" :disabled="isReadOnly" />
                                </el-form-item>
                                </el-col>
                                <el-col :span="12">
                                <el-form-item label="Data to Hold" class="row-with-space">
                                    <el-checkbox v-model="document.dataToHold" :disabled="isReadOnly" />
                                </el-form-item>
                                </el-col>
                            </el-row>
                            <el-form-item label="Observations" class="row-with-space">
                                <el-input type="textarea" v-model="document.observation" :disabled="isReadOnly"></el-input>
                            </el-form-item>
                            <Document-structure @structure="handleDataFromChild" :documentId="documentId"></Document-structure>
                            <el-form-item class="p-4">
                                <el-button type="primary" @click="submitForm">{{ mode === "edit" ? "Save" : "Create" }}</el-button>
                                <el-button type="success" @click="sendUpdate" v-if="needUpdate">Update fields to DB</el-button>
                                <el-button type="danger" @click="cancel">Cancel</el-button>
                            </el-form-item>
                        </el-form>

                    </div>
                </b-card>
            </b-col>
        </b-row>
        <!--End tables-->
        </b-container>
    </div>

  </template>
<script>
    import {FETCH_DOCUMENT, ADD_DOCUMENT, EDIT_DOCUMENT, GET_STATUS, PUSH_DB_CHANGES} from '@/store/document/document.constants';
    import { Button, FormItem, Dialog, Form, Checkbox, Alert, Col, Row, Input} from 'element-ui';
    
    // Table
    import DocumentStructure from './DocumentStructure.vue';

    export default {
        components: {
            [Input.name]: Input,
            [Col.name]: Col,
            [Row.name]: Row,
            [Alert.name]: Alert,
            [Checkbox.name]: Checkbox,
            [Form.name]: Form,
            [Dialog.name]: Dialog,
            [FormItem.name]: FormItem,
            [Button.name]: Button,
            DocumentStructure,
        },
        data() {
            return {
                mode: "create", // Default to "create" mode
                isReadOnly: false,
                document: {
                    id: null,
                    documentName: "",
                    documentShortDetail: "",
                    owner: "",
                    containsSensitiveData: false,
                    dataToHold: false,
                    approvedBy: "",
                    observation: "",
                },
                needUpdate: false,
                documentId: this.$route.params.id,
                alertTitle: ""
            };
        },
        created() {
            const id = this.$route.params.id;
            if (id){
                this.isReadOnly=true;
                this.mode="edit";
                // Make an API request to fetch the document data by 'id'
                this.$store.dispatch(`document/${FETCH_DOCUMENT}`,id).then( 
                    (res) => {
                        let documents = this.$store.getters['document/getDocument'];
                        this.document.id = documents.id;
                        this.document.documentName = documents.documentName;
                        this.document.documentShortDetail = documents.documentShortDetail;
                        this.document.owner = documents.owner;
                        this.document.containsSensitiveData = documents.containsSensitiveData;
                        this.document.dataToHold = documents.dataToHold;
                        this.document.approvedBy = documents.approvedBy;
                        this.document.observation = documents.observation;
                        this.verifyDocumentStatusOnDb();
                    }, err => {
                        this.alertTitle = "Error while fetch"
                });
            } else {
                // Create mode: Initialize with empty data
                this.mode = "create";
                this.document = {
                id: null,
                documentName: "",
                documentShortDetail: "",
                owner: "",
                containsSensitiveData: false,
                dataToHold: false,
                approvedBy: "",
                observation: "",
                structures: [],
                };
            }
        },
        methods: {
            submitForm() {
                // Handle form submission, e.g., update the document data
                const formData = {
                    id: this.document.id,
                    documentName: this.document.documentName,
                    documentShortDetail: this.document.documentShortDetail,
                    owner: this.document.owner,
                    containsSensitiveData: this.document.containsSensitiveData,
                    dataToHold: this.document.dataToHold,
                    approvedBy: this.document.approvedBy,
                    observation: this.document.observation,
                    structures: this.document.structures,
                };
                // Make an API request to update the document data
                if (this.mode == "create"){
                    console.log("calling page create");
                    this.$store.dispatch(`document/${ADD_DOCUMENT}`,formData).then( 
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
                } else {
                    this.$store.dispatch(`document/${EDIT_DOCUMENT}`,formData).then( 
                    () => {
                        this.editDialogVisible = false;
                        this.alertTitle = "Document changed with success!"
                        this.$router.go(-1); // Go back one step in the browser history
                    }, err => {
                        this.editDialogVisible = false;
                        this.alertTitle = "Error on change. Please contact the IT team"
                        this.showErrorAlert();
                    });
                }

            },
            verifyDocumentStatusOnDb(){
                if (this.mode != "create"){
                    const id = this.$route.params.id;
                    this.$store.dispatch(`document/${GET_STATUS}`,id).then( 
                        (res) => {
                            let docStatus = this.$store.getters['document/getDocumentStatus'];
                            console.log("all_updated")
                            if (docStatus != "ALL_UPDATED"){
                                this.needUpdate = true;
                            } else {
                                this.needUpdate = false;
                            }
                        }, err => {
                            this.alertTitle = "Error while fetch"
                    });
                }
            },
            sendUpdate(){
                const formData = {
                    id: this.document.id,
                };
                this.$store.dispatch(`document/${PUSH_DB_CHANGES}`,formData).then( 
                    () => {
                        window.location.reload();
                        this.editDialogVisible = false;
                        this.alertTitle = "Document updated on DB with success!"
                    }, err => {
                        window.location.reload();
                        this.editDialogVisible = false;
                        this.alertTitle = "Error on change. Please contact the IT team"
                        this.showErrorAlert();
                    });
            },
            handleDataFromChild(structure){
                console.log("received the row: " + structure);
                this.document.structures.push(structure);
            },
            cancel() {
            // Handle cancel action (e.g., navigate back to the previous screen)
            this.$router.go(-1); // Go back one step in the browser history
            },
        },
    };
</script>
<style scoped>
.form-container{
    padding: 20px;
}
.form-title {
  font-size: 24px;
  margin-bottom: 20px;
}

.row-with-space {
  margin-left: 10px; /* Adjust the margin as needed */
  margin-right: 10px; /* Adjust the margin as needed */
}
</style>