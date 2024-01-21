<template>
  <b-card body-class="p-0" header-class="border-0">
    <template v-slot:header>
      <b-row align-v="center">
        <b-col>
          <h3 class="mb-0">Applications</h3>
        </b-col>
        <!-- <b-col class="text-right">
          <base-button size="sm" type="primary">See all</base-button>
        </b-col> -->
      </b-row>
    </template>
      <el-table
        class="table-responsive table"
        :data="tableData"
        highlight-current-row
        header-row-class-name="thead-light"
      >
      <el-table-column label="Application Name" min-width="50px" prop="appName">
        <template v-slot="{ row }">
          <div :class="{ 'font-weight-600': row.isSelected }">{{ row.appName }}</div>
        </template>
      </el-table-column>
      <el-table-column label="App Trigram" min-width="110px" prop="appAbrv"></el-table-column>
      <el-table-column label="Actions" width="150">
        <template slot-scope="scope">
          <el-button @click="handleSelection(scope.row)" type="text">Select ></el-button>
        </template>
      </el-table-column>
    </el-table>
  </b-card>
</template>
  <script>
    import { BaseProgress } from '@/components';
    import { mapGetters } from "vuex";
    import { Table, TableColumn, Button} from 'element-ui';
    import {FETCH_APPLICATIONS} from '@/store/application/application.constants';
    export default {
      components: {
        BaseProgress,
        [Table.name]: Table,
        [Button.name]: Button,
        [TableColumn.name]: TableColumn,
      },
      data() {
        return {
          editDialogVisible: false,
          editForm: {
            id: null,
            appName: '',
            appAbrv: '',

            isReadOnly: false, // Control read-only mode
            visibleAlert: null,
            alertTitle: '',
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
          this.tableData.forEach((r) => {
            this.$set(r, 'isSelected', false);
          });
          this.$set(row, 'isSelected', true);
          this.$emit('application',row);
        }
      }
    }
  </script>
  <style>
.add-button-container {
  text-align: right;
  margin: 20px; /* Adjust the margin as needed */
}
  </style>
  