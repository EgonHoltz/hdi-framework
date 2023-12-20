<template>
    <div>
        <base-header class="pb-6 pb-8 pt-5 pt-md-8 bg-gradient-success">
        </base-header>
    
        <b-container fluid class="mt--7">
        <!--Tables-->
        <b-row class="mt-5">
            <b-col xl="10" class="mb-5 mb-xl-0">
                <b-card body-class="p-0" header-class="border-0">
                    <div class="form-container">
                        <h1 class="form-title">{{ mode === "edit" ? "Modify Document" : "Create Document" }}</h1>
                            <el-form ref="documentForm" :model="document" label-width="120px" label-position="top">
                            <el-row>
                                <el-col :span="12">
                                <el-form-item label="Document Name" class="row-with-space">
                                    <el-input v-model="document.documentName" :disabled="isReadOnly"></el-input>
                                </el-form-item>
                                </el-col>
                                <el-col :span="12">
                                <el-form-item label="Short Detail" class="row-with-space">
                                    <el-input v-model="document.documentShortDetail" :disabled="isReadOnly"></el-input>
                                </el-form-item>
                                </el-col>
                            </el-row>
                            <el-row>
                                <el-col :span="12">
                                <el-form-item label="Owner" class="row-with-space">
                                    <el-input v-model="document.owner" :disabled="isReadOnly"></el-input>
                                </el-form-item>
                                </el-col>
                                <el-col :span="12">
                                <el-form-item label="Approved By" class="row-with-space">
                                    <el-input v-model="document.approvedBy" :disabled="isReadOnly"></el-input>
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
                            <el-form-item>
                                <el-button type="primary" @click="submitForm">{{ mode === "edit" ? "Save" : "Create" }}</el-button>
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
    import {FETCH_DOCUMENT, ADD_DOCUMENT, EDIT_DOCUMENT} from '@/store/application/application.constants';
    import { Button, FormItem, Dialog, Form, Checkbox, Alert, Col, Row, Input} from 'element-ui';

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
        },
        data() {
            return {
                mode: "create", // Default to "create" mode
                document: {
                    id: null,
                    documentName: "",
                    documentShortDetail: "",
                    owner: "",
                    containsSensitiveData: false,
                    dataToHold: false,
                    approvedBy: "",
                    observation: "",
                }
            };
        },
        created() {
            const id = this.$route.params.id;
            if (id){
                // Make an API request to fetch the document data by 'id'
                this.$store.dispatch(`document/${FETCH_DOCUMENT}`,this.$route.params).then( 
                    () => {
                        documents = this.$store.getters['document/getDocument'];
                        this.id = documents.id;
                        this.documentName = documents.documentName;
                        this.documentShortDetail = documents.documentShortDetail;
                        this.owner = documents.owner;
                        this.containsSensitiveData = documents.containsSensitiveData;
                        this.dataToHold = documents.dataToHold;
                        this.approvedBy = documents.approvedBy;
                        this.observation = documents.observation;
                    
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
                };
            }
        },
        methods: {
            submitForm() {
                // Handle form submission, e.g., update the document data
                const formData = {
                    id: this.id,
                    documentName: this.documentName,
                    documentShortDetail: this.documentShortDetail,
                    owner: this.owner,
                    containsSensitiveData: this.containsSensitiveData,
                    dataToHold: this.dataToHold,
                    approvedBy: this.approvedBy,
                    observation: this.observation,
                };
                // Make an API request to update the document data
                if (this.mode == "create"){
                    this.$store.dispatch(`document/${ADD_DOCUMENT}`,this.editForm).then( 
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
                    this.$store.dispatch(`document/${EDIT_DOCUMENT}`,this.editForm).then( 
                    () => {
                        this.editDialogVisible = false;
                        this.alertTitle = "Document changed with success!"
                        this.fetchApplications();
                        this.$router.go(-1); // Go back one step in the browser history
                    }, err => {
                        this.editDialogVisible = false;
                        this.alertTitle = "Error on change. Please contact the IT team"
                        this.showErrorAlert();
                    });
                }

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