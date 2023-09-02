<template>
  <b-card body-class="p-0" header-class="border-0">
    <template v-slot:header>
      <b-row align-v="center">
        <b-col>
          <h3 class="mb-0">Applications traffic</h3>
        </b-col>
        <!-- <b-col class="text-right">
          <base-button size="sm" type="primary">See all</base-button>
        </b-col> -->
      </b-row>
    </template>


    <el-table
      class="table-responsive table"
      :data="tableData"
      header-row-class-name="thead-light">
      <el-table-column label="Document Name" min-width="115px" prop="document">
        <template v-slot="{row}">
          <div class="font-weight-600">{{row.document}}</div>
        </template>
      </el-table-column>

      <el-table-column label="Technologies to Receive data" min-width="110px" prop="techs">
      </el-table-column>
      <el-table-column label="Data Owner" min-width="110px" prop="owner">
      </el-table-column>
      <el-table-column label="Received Docs" min-width="110px" prop="rcvd">
      </el-table-column>
      <el-table-column label="Sent docs" min-width="110px" prop="sent">
      </el-table-column>

      <el-table-column min-width="220px" prop="progress">
        <template v-slot="{row}">
          <div class="d-flex align-items-center">
            <span class="mr-2">{{row.progress}}%</span>
            <base-progress :type="row.progressType" :value="row.progress" />
          </div>
        </template>
      </el-table-column>
    </el-table>
  </b-card>
</template>
<script>
  import { BaseProgress } from '@/components';
  import { Table, TableColumn, DropdownMenu, DropdownItem, Dropdown} from 'element-ui'
  export default {
    name: 'social-traffic-table',
    components: {
      BaseProgress,
      [Table.name]: Table,
      [TableColumn.name]: TableColumn,
      [Dropdown.name]: Dropdown,
      [DropdownItem.name]: DropdownItem,
      [DropdownMenu.name]: DropdownMenu,
    },
    data() {
      return {
        tableData: [
          {
            document: 'Individual Clients',
            techs: 'todo',
            owner: 'John Doe',
            rcvd: '19',
            sent: '10',
            progress: 70,
            progressType: 'gradient-danger',
          },
          {
            document: 'Accounts',
            techs: 'todo',
            owner: 'Sarah Smith',
            rcvd: '25',
            sent: '18',
            progress: 60,
            progressType: 'gradient-warning',
          },
          {
            document: 'Companies',
            techs: 'todo',
            owner: 'Airi Satou',
            rcvd: '33',
            sent: '21',
            progress: 10,
            progressType: 'gradient-info',
          },
        ]
      }
    }

  }
</script>
<style>
</style>
