<template>
    <div class="file-transfer-container">

      <el-row type="flex" justify="center" align="middle" class="row-bg">
        <el-col :span="4">
          <el-select v-model="selectedDocument" placeholder="Select Document">
            <el-option
              v-for="document in uniqueDocuments"
              :key="document"
              :label="document"
              :value="document">
            </el-option>
          </el-select>
        </el-col>
        <el-col :span="4">
          <el-select v-model="selectedDirection" placeholder="Select Direction">
            <el-option
              v-for="direction in uniqueDirections"
              :key="direction"
              :label="direction"
              :value="direction">
            </el-option>
          </el-select>
        </el-col>
      </el-row>

      <el-table
        class="table-responsive table"
        :data="paginatedData"
        header-row-class-name="thead-light">
        <el-table-column label="Application" min-width="50px" prop="application" class="first-column">
        </el-table-column>
        <el-table-column label="Document" min-width="60px" prop="document">
        </el-table-column> 
        <el-table-column label="Direction" min-width="90px" prop="direction">
        </el-table-column>
        <el-table-column label="Technology" min-width="70px" prop="technology">
        </el-table-column>
        <el-table-column label="Status" min-width="70px" prop="status">
        </el-table-column>
        <el-table-column label="Operation Date" min-width="110px" prop="date">
        </el-table-column>
      </el-table>

      <el-pagination
      @size-change="handleSizeChange"
      @current-change="handleCurrentChange"
      :current-page="currentPage"
      :page-sizes="[5, 10, 20]"
      :page-size="pageSize"
      layout="total, sizes, prev, pager, next, jumper"
      :total="filteredData.length">
      </el-pagination>
  </div>
</template>
  
<script>
  // Components
  import { Pagination, TableColumn, Table, Button, Row, Col, Select, Option} from 'element-ui';
  import format from 'date-fns/format'

  export default {
    components: {
      [TableColumn.name]: TableColumn,
      [Button.name]: Button,
      [Table.name]: Table,
      [Option.name]: Option,
      [Select.name]: Select,
      [Col.name]: Col,
      [Row.name]: Row,
      [Pagination.name]: Pagination,
    },
    props: {
      isSelected: {
        type: String,
        required: true,
      },
    },
    computed: {
      uniqueDocuments() {
        const documents = new Set(this.tableData.map(item => item.document));
        return ['All Documents', ...Array.from(documents)];
      },
      uniqueDirections() {
        const directions = new Set(this.tableData.map(item => item.direction));
        return ['All Directions', ...Array.from(directions)];
      },
      filteredData() {
        return this.tableData.filter(item => {
          const documentFilter = this.selectedDocument === 'All Documents' || item.document === this.selectedDocument;
          const directionFilter = this.selectedDirection === 'All Directions' || item.direction === this.selectedDirection;
          return documentFilter && directionFilter;
        });
      },
      paginatedData() {
            const start = (this.currentPage - 1) * this.pageSize;
            const end = start + this.pageSize;
            return this.filteredData.slice(start, end);
        }
    },
    watch: {
      isSelected: {
        handler(newValue) {
          this.isSelected = newValue;
          console.log("Load data on the direction: " + this.viewDirection);
          //load associated tech to forms
        },
        deep: true,
      },
    },
    data() {
        return {
          selectedDocument: 'All Documents',
          selectedDirection: 'All Directions',
          currentPage: 1,
          pageSize: 5,
          tableData: [
            {application:"AAA", document:"Company", direction:"Send",technology:"gRPC",status:"OK",date:"22/07/2024 11:25:45"},
            {application:"AAB", document:"Client", direction:"Send", technology:"gRPC", status:"DUP", date:"05/12/2023 12:24:12"},
            {application:"AAD", document:"Account", direction:"Receive", technology:"MQ Queue", status:"REJ", date:"05/01/2024 12:24:12"},
            {application:"AAC", document:"Account", direction:"Receive", technology:"SFTP", status:"NOK", date:"11/04/2024 12:24:12"},
            {application:"AAB", document:"Account", direction:"Receive", technology:"SFTP", status:"DUP", date:"23/02/2024 12:24:12"},
            {application:"AAC", document:"Company", direction:"Send", technology:"MQ Queue", status:"DUP", date:"31/08/2023 12:24:12"},
            {application:"AAB", document:"Client", direction:"Receive", technology:"MQ Queue", status:"OK", date:"21/06/2024 12:24:12"},
            {application:"AAD", document:"Company", direction:"Send", technology:"MQ Queue", status:"REJ", date:"06/08/2023 12:24:12"},
            {application:"AAA", document:"Account", direction:"Send", technology:"SFTP", status:"OK", date:"18/03/2024 12:24:12"},
            {application:"AAC", document:"Client", direction:"Receive", technology:"MQ Queue", status:"NOK", date:"24/04/2024 12:24:12"}
          ]
        };
    },
    methods: {
      formatDate(value) {
        return format(value, 'DD/MM/YYYY HH:mm:ss');
      },

    }
  };
</script>
<style>
.row-bg{
    padding-bottom: 15px;
}
.first-column .cell {
  padding-left: 30px; /* Adjust the padding as needed */
}
</style>