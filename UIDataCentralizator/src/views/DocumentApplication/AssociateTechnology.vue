<template>
    <b-card body-class="p-0" header-class="border-0">
        <template v-slot:header>
            <div style="display: flex; align-items: center; justify-content: space-between;">
                <!-- Rounded icon (adjust the class and styling as needed) -->
                <div class="rounded-icon">Icon</div>
                <!-- Title -->
                <div class="card-title">MQ Queue configuration</div>
                <!-- Active checkbox -->
                <el-checkbox v-model="isActive" @click.stop.native="toggleCollapse">Active</el-checkbox>
            </div>
        </template>
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
            <el-form-item label="Output MQ Queue name">
                <el-input v-model="outputQueueName" :disabled="isActive"></el-input>
            </el-form-item>
            <el-form-item >
                <el-checkbox v-model="outputActive" :disabled="isActive">Active</el-checkbox>
            </el-form-item>
            <el-form-item>
                <el-checkbox v-model="outputHasAcknowledge" :disabled="isActive">Has acknowledgment</el-checkbox>
            </el-form-item>
        </el-form>
    </b-card>
</template>
    <script>
      import { BaseProgress } from '@/components';
      import { mapGetters } from "vuex";
      import {FETCH_APPLICATIONS} from '@/store/application/application.constants';
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
        data() {
          return {
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
            tableData: [
            ]
          }
        },
        created() {
          this.isReadOnly = false;
          this.visibleAlert = null;
        },
        mounted(){
          this.afterRender();
        },
        methods: {
            afterRender(){
                this.fetchApplications();
            },
            fetchApplications(){
                console.log("fetch data");
                this.$store.dispatch(`application/${FETCH_APPLICATIONS}`).then( 
                () => {
                    this.applications = this.$store.getters['application/getApplications'];
                    this.tableData = [];
                    this.tableData = this.applications;
                }, err => {
                    this.alertTitle = "Error while fetch"
                });
            },
            handleSelection(row){
                this.$emit('application',row);
            },
            toggleCollapse() {
                console.log(this.isActive)
                this.isActive = !this.isActive;
                this.$forceUpdate();
            },
        }
      }
    </script>
<style scoped>
/* Adjust styling as needed */
.rounded-icon {
  width: 30px;
  height: 30px;
  background-color: #ccc;
  border-radius: 50%;
  display: inline-block;
}

.card-title {
  margin-left: 10px;
  display: inline-block;
}
</style>
    