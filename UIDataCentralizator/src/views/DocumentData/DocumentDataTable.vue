<template>
    <div>
        <base-header class="pb-6 pb-8 pt-5 pt-md-8 bg-gradient-success">
        </base-header>

        <b-container fluid class="mt--7">
            <b-card body-class="p-0" class="box-card, text-center" header-class="border-0">
                <el-form ref="filterForm" :model="filters">
                    <b-row class="mt-5">
                        <el-form-item
                            v-for="(filter, index) in filters"
                            :key="index"    
                            :label="filter.name"
                            label-width="100px"
                        >
                            <el-input
                                v-model="filter.value"
                                :placeholder="`Filter by ${filter.name}`"
                            ></el-input>
                        </el-form-item>
                    </b-row>
                    <b-row>
                        <b-col cols="12" class="text-center">
                            <el-form-item>
                                <el-button type="primary" @click="fetchData">Fetch Data</el-button>
                            </el-form-item>
                        </b-col>
                    </b-row>
                </el-form>
            </b-card>
                
            <b-row class="mt-6">
                <el-table :data="tableData" style="width: 100%;" border stripe :row-class-name="getRowClassName">
                    <el-table-column
                        v-for="column in columns"
                        :key="column.prop"
                        :prop="column.prop"
                        :label="column.label">
                    </el-table-column>
                    <el-table-column
                        fixed="right"
                        label="Operations"
                        width="120">
                        <template slot-scope="scope">
                            <el-button
                                v-if="!isPendingDeletion(scope.row)"
                                @click="setDeleteRecord(scope.row)"
                                type="text"
                                size="small"
                            >Delete record</el-button>
                            <template v-else>
                                <el-button
                                    @click="confirmDeletion(scope.row, true)"
                                    type="text"
                                    size="small"
                                    style="color: green;"
                                >Confirm deletion</el-button>
                                <el-button
                                    @click="confirmDeletion(scope.row, false)"
                                    type="text"
                                    size="small"
                                    style="color: red;"
                                >Cancel deletion</el-button>
                            </template>
                        </template>
                    </el-table-column>
                </el-table>
            </b-row>
            <b-row class="mt-5">
                <el-pagination
                    @size-change="handleSizeChange"
                    @current-change="handleCurrentChange"
                    :current-page="currentPage"
                    :page-sizes="[10, 25, 50]"
                    :page-size="pageSize"
                    layout="total, sizes, prev, pager, next, jumper"
                    :total="totalItems">
                </el-pagination>
            </b-row>
        </b-container>
    </div>
  </template>
  
  <script>
  import { Pagination, Table, TableColumn, Input, Form, FormItem, Button, Card} from 'element-ui'
  import {FETCH_COLUMNS} from '@/store/document/document.constants';
  import {FETCH_DOCUMENT_DATA, FETCH_PENDING_DELETE_DATA, SOFT_DELETE_DATA, FINISH_DELETE_DATA} from '@/store/document-data/document-data.constants';
  export default {
    components: {
        [Pagination.name]: Pagination,
        [Table.name]: Table,
        [TableColumn.name]: TableColumn,
        [Input.name]: Input,
        [Form.name]: Form,
        [FormItem.name]: FormItem,
        [Button.name]: Button,
        [Card.name]: Card,
      },
    data() {
      return {
        tableData: [],
        columns: [],
        filters: [],
        currentPage: 1,
        pageSize: 10,
        totalItems: 0,
        documentId: '',
        pendingData: [],
      };
    },
    methods: {
      fetchData() {

        //It will brind data only when it has filters
        if (!this.areFiltersApplied()) return;
        // Simulate API call with filters and pagination

        let payload = {};
        payload.docId = this.documentId;
        if (this.currentPage > 0){
            payload.currentPage = this.currentPage -1;
        } else {
            payload.currentPage = this.currentPage;
        }
        payload.pageSize = this.pageSize;
        payload.filters = this.filters;

        this.$store.dispatch(`documentData/${FETCH_DOCUMENT_DATA}`, payload).then( 
        (res) => {
            console.log("got data " +res);
            let dataRes = this.$store.getters['documentData/getDocumentData'];

            this.tableData = dataRes.content;
            this.totalItems = dataRes.totalElements;
            this.currentPage = dataRes.currentPage +1;
        }, err => {
            this.alertTitle = "Error while fetch"
        });

      },
      handleSizeChange(newSize) {
        this.pageSize = newSize;
        this.fetchData();
      },
      handleCurrentChange(newPage) {
        this.currentPage = newPage;
        this.fetchData();
      },
      areFiltersApplied() {
        // Check if any filter has been applied
        return this.filters.some(filter => filter.value);
      },
      setDeleteRecord(row){
        let payload = {};
        payload.documentId = this.documentId;
        payload.dataId = row._id
        this.$store.dispatch(`documentData/${SOFT_DELETE_DATA}`,payload).then( 
            () => {
                this.editDialogVisible = false;
                this.alertTitle = "Marked as Delete!"
                this.fetchPendingDelete();
                this.fetchData();
                //this.showSuccessAlert();
            }, err => {
                this.editDialogVisible = false;
                this.alertTitle = "Error on creation. Please contact the IT team"
                this.showErrorAlert();
            });
      },
      confirmDeletion(row,isDelete){
        let payload = {};
        payload.documentId = this.documentId;
        payload.dataId = row._id
        payload.isDelete = isDelete;
        this.$store.dispatch(`documentData/${FINISH_DELETE_DATA}`,payload).then( 
            () => {
                this.editDialogVisible = false;
                this.alertTitle = "Finilized the Deletion!"
                this.fetchPendingDelete();
                this.fetchData();
                //this.showSuccessAlert();
            }, err => {
                this.editDialogVisible = false;
                this.alertTitle = "Error on creation. Please contact the IT team"
                this.showErrorAlert();
            });
      },
      getRowClassName({row}) {
        if (this.pendingData.length > 0 
            && this.pendingData.some(pd => pd.dataId === row._id)) {
            return 'pending-deletion';
        }
        return '';
      },
      isPendingDeletion(row) {
        if (this.pendingData.length > 0){
            return this.pendingData.some(pd => pd.dataId === row._id);
        }
        return false;
      },
      fetchPendingDelete(){
        this.$store.dispatch(`documentData/${FETCH_PENDING_DELETE_DATA}`, this.documentId).then( 
        (res) => {
            console.log("got pending " +res);
            let docPendingData = this.$store.getters['documentData/getPendingDeleteData'];
            if (docPendingData !== null && docPendingData !== undefined){
                this.pendingData = docPendingData;
            } 
        }, err => {
            this.alertTitle = "Error while fetch"
        });
      }
    },
    mounted() {
        this.documentId = this.$route.params.id;
        //Mount columns and filters
        this.$store.dispatch(`document/${FETCH_COLUMNS}`, this.documentId).then( 
        (res) => {
            console.log("got columns " +res);
            let docColumns = this.$store.getters['document/getColumns'];
            this.columns = docColumns.map(col => ({
                prop: col.fieldNameCamel,
                label: col.fieldName,
            }));
            this.filters = docColumns.map(col => ({
                name: col.fieldName,
                value: '',
            }));
        }, err => {
            this.alertTitle = "Error while fetch"
        });
        this.fetchPendingDelete();
        // Initialize with default filter values or fetch structure
        this.fetchData();
    },
  };
  </script>
<style>
.el-table {
    overflow-x: auto;
}

.box-card {
  width: 90%;
  max-width: 600px; /* Adjust based on your preference */
}

.filter-row {
  margin-bottom: 20px;
}

.text-center {
  text-align: center;
}
.container {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 100vh;
}
.pending-deletion {
  color: #f8949d; /* Light red color */
}
</style>