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
                <el-table :data="tableData" style="width: 100%;" border stripe>
                    <el-table-column
                    v-for="column in columns"
                    :key="column.prop"
                    :prop="column.prop"
                    :label="column.label"
                    ></el-table-column>
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
  import {FETCH_DOCUMENT_DATA} from '@/store/document-data/document-data.constants';
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
      };
    },
    methods: {
      fetchData() {

        //It will brind data only when it has filters
        if (!this.areFiltersApplied()) return;
        // Simulate API call with filters and pagination

        let payload = {};
        payload.docId = this.documentId;
        payload.currentPage = this.currentPage -1;
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
      mockApiCall() {
        const allData = [
            { name: "John Doe", age: 30 },
            { name: "Jane Doe", age: 25 },
            { name: "Joseph Rechards", age: 40 },
            { name: "Alicia Keys", age: 30 },
            { name: "Bruce Wayne", age: 31 },
            { name: "Clark Kent", age: 32 },
            { name: "Diana Prince", age: 33 },
            { name: "Barry Allen", age: 34 },
            { name: "Arthur Curry", age: 35 },
            { name: "Victor Stone", age: 36 },
            { name: "John Stewart", age: 37 },
            { name: "Hal Jordan", age: 38 },
        ];

        const startIndex = (this.currentPage - 1) * this.pageSize;
        const endIndex = startIndex + this.pageSize;
        const paginatedData = allData.slice(startIndex, endIndex);

        return {
            total: allData.length, // Total number of items in all pages
            data: paginatedData, // Data for the current page
            columns: [
            { field: "name", name: "Name" },
            { field: "age", name: "Age" },
            ],
  };
      },
      areFiltersApplied() {
        // Check if any filter has been applied
        return this.filters.some(filter => filter.value);
      },
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
</style>