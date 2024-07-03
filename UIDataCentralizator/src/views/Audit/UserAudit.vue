<template>
    <div class="file-transfer-container">

        <el-row type="flex" justify="center" align="middle" class="row-bg">
            <el-col :span="4">
                <el-select v-model="selectedModule" placeholder="Select a Module">
                <el-option
                    v-for="module in uniqueModules"
                    :key="module"
                    :label="module"
                    :value="module">
                </el-option>
                </el-select>
            </el-col>
        </el-row>

      <el-table
        class="table-responsive table"
        :data="filteredData"
        header-row-class-name="thead-light">
        <el-table-column label="Name" min-width="80px" prop="name" class="first-column">
        </el-table-column>
        <el-table-column label="ID" min-width="60px" prop="id">
        </el-table-column> 
        <el-table-column label="Module" min-width="90px" prop="module">
        </el-table-column>
        <el-table-column label="Field" min-width="70px" prop="field">
        </el-table-column>
        <el-table-column label="Record" min-width="70px" prop="record">
        </el-table-column>
        <el-table-column label="Old value" min-width="150px" prop="oldValue">
        </el-table-column>
        <el-table-column label="New value" min-width="150px" prop="newValue">
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
        uniqueModules() {
            const modules = new Set(this.tableData.map(item => item.module));
            return ['All Modules', ...Array.from(modules)];
        },
        filteredData() {
            if (!this.selectedModule || this.selectedModule === 'All Modules') {
                return this.tableData;
            }
            return this.tableData.filter(item => item.module === this.selectedModule);
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
          selectedModule: 'All Modules',
          currentPage: 1,
          pageSize: 5,
          tableData: [
            {name:"João de Almeida", id:"125121", module:"Application",field:"Owner",record:"AAD",oldValue:"Maria Albuquerque",newValue:"Ines Pereira",date:"22/07/2024 11:25:45"},
            {name:"José Maria", id:"887954", module:"Document",field:"Short details",record:"Company",oldValue:"Company details",newValue:"All client companies",date:"10/05/2024 12:45:47"},
            {name:"Janelle Reichel", id:"221458", module:"Structure",field:"Mandatory",record:"Company",oldValue:"Name:False",newValue:"Name:True",date:"07/01/2024 18:27:34"},
            {name:"Kristina Altenwerth", id:"251589", module:"Scheduler",field:"Data Mode",record:"AAE-Company",oldValue:"DELTA",newValue:"FULL",date:"29/08/2023 10:03:08"},
            {name:"Evgeny Morozov", id:"507862", module:"Scheduler", field:"Frequency", record:"AAE-Company", oldValue:"Monthly", newValue:"Daily", date:"25/02/2024 11:28:19"},
            {name:"Evgeny Morozov", id:"381736", module:"Document", field:"Data to hold", record:"Account", oldValue:"False", newValue:"True", date:"13/07/2023 11:28:19"},
            {name:"Janelle Reichel", id:"531797", module:"Scheduler", field:"Data Mode", record:"AAA-Account", oldValue:"FULL", newValue:"DELTA", date:"22/06/2024 11:28:19"},
            {name:"Lisa Ray", id:"455785", module:"Application", field:"High Availability", record:"AAD", oldValue:"False", newValue:"True", date:"10/03/2024 11:28:19"},
            {name:"Evgeny Morozov", id:"676114", module:"Structure", field:"Type", record:"Account", oldValue:"Account Number:Number", newValue:"Account Number:String", date:"21/07/2023 11:28:19"},
            {name:"Tom Hanks", id:"446425", module:"Scheduler", field:"Data Mode", record:"AAC-Account", oldValue:"FULL", newValue:"DELTA", date:"29/02/2024 11:28:19"},
            {name:"Elena Smith", id:"258641", module:"Scheduler", field:"Select Time", record:"AAE-Client", oldValue:"00:05", newValue:"02:05", date:"24/07/2023 11:28:19"},
            {name:"Kristina Altenwerth", id:"561161", module:"Structure", field:"Type", record:"Company", oldValue:"TIN:Number", newValue:"TIN:String", date:"27/07/2023 11:28:19"},
            {name:"José Maria", id:"635318", module:"Scheduler", field:"Data Mode", record:"AAB-Client", oldValue:"FULL", newValue:"DELTA", date:"07/11/2023 11:28:19"},
            {name:"Marco Polo", id:"614666", module:"Structure", field:"RegExp", record:"Company", oldValue:"Country:null", newValue:"Country:[A-Za-z\s\-]{1,20}", date:"24/11/2023 11:28:19"}
          ]
        };
    },
    methods: {
      formatDate(value) {
        return format(value, 'DD/MM/YYYY HH:mm:ss');
      },
      handleSizeChange(size) {
        this.pageSize = size;
        this.currentPage = 1; // Reset to first page when page size changes
      },
      handleCurrentChange(page) {
        this.currentPage = page;
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