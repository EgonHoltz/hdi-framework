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
            <b-collapse id="collapse-1" class="mt-2">
                <el-form label-width="120px" label-position="top">
                <el-row>
                    <el-col :span="14" class="p-2">
                        <el-form-item label="Input MQ Queue name" class="row-with-space">
                            <el-input v-model="inputQueueName" :disabled="isActive"></el-input>
                        </el-form-item>
                    </el-col>
                    <el-col :span="4" class="p-2">
                        <el-form-item>
                            <el-checkbox v-model="inputActive" :disabled="isActive">Active</el-checkbox>
                        </el-form-item>
                        <el-form-item>
                            <el-checkbox v-model="inputHasAcknowledge" :disabled="isActive">Has acknowledgment</el-checkbox>
                        </el-form-item>
                    </el-col>
                </el-row>
                <hr>
                <el-row>
                    <el-col :span="14" class="p-2">
                        <el-form-item label="Output MQ Queue name">
                            <el-input v-model="outputQueueName" :disabled="isActive"></el-input>
                        </el-form-item>
                    </el-col>
                    <el-col :span="4" class="p-2">
                        <el-form-item >
                            <el-checkbox v-model="outputActive" :disabled="isActive">Active</el-checkbox>
                        </el-form-item>
                        <el-form-item>
                            <el-checkbox v-model="outputHasAcknowledge" :disabled="isActive">Has acknowledgment</el-checkbox>
                        </el-form-item>
                    </el-col>
                </el-row>
                <el-form-item class="p-4">
                                <el-button type="primary" @click="submitForm">Save</el-button>
                                <el-button type="danger" @click="cancel">Cancel</el-button>
                </el-form-item>
            </el-form>
        </b-collapse>
    </template>
    </b-card>
</template>
<script>
      import { BaseProgress } from '@/components';
      import { mapGetters } from "vuex";
      import {FETCH_DOCUMENT_APPLICATION, ADD_DOCUMENT_APPLICATION, EDIT_DOCUMENT_APPLICATION} from '@/store/document-application/document-application.constants';
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
            buttonProp: {
                type: "success",
                label: "Active"
            },
            tableData: [
            ]
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
                const documentId = this.$route.params.id;
                console.log("selectedApplication: " + this.applicationId);
                console.log("on the document: " + documentId);
                let payloadIds = {
                    applicationId: this.applicationId,
                    documentId: documentId
                }
                this.$store.dispatch(`documentApplication/${FETCH_DOCUMENT_APPLICATION}`, payloadIds).then( 
                () => {
                    this.applications = this.$store.getters['documentApplication/getDocumentApplication'];
                    this.tableData = [];
                    this.tableData = this.applications;
                }, err => {
                    this.alertTitle = "Error while fetch"
                });
            },
            doActivate(){
              if (this.buttonProp.type ==="success") {
                this.buttonProp.type = "danger";
                this.buttonProp.label ="Inactive"
                this.editForm.isActive = true;
              } else {
                this.buttonProp.type = "success";
                this.buttonProp.label ="Active"
                this.editForm.isActive = false;
              }
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
    