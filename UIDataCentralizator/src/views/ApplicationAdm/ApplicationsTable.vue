<template>
    <b-card body-class="p-0" header-class="border-0">
      <template v-slot:header>
        <b-row align-v="center">
          <b-col>
            <h3 class="mb-0">Documents adminsitration</h3>
          </b-col>
          <!-- <b-col class="text-right">
            <base-button size="sm" type="primary">See all</base-button>
          </b-col> -->
        </b-row>
      </template>
  
      <!-- Add New Application Button -->
      <div class="add-button-container">
        <el-button type="primary" @click="addNewApplication">Add New Application</el-button>
      </div>
  
      <el-table
        class="table-responsive table"
        :data="tableData"
        header-row-class-name="thead-light">
        <el-table-column label="Application Name" min-width="115px" prop="appname">
          <template v-slot="{row}">
            <div class="font-weight-600">{{row.appname}}</div>
          </template>
        </el-table-column>
  
        <el-table-column label="App Owner" min-width="110px" prop="owner">
        </el-table-column>
        <el-table-column label="Received Docs" min-width="110px" prop="rcvd">
        </el-table-column>
        <el-table-column label="Sent docs" min-width="110px" prop="sent">
        </el-table-column>
  
        <el-table-column label="Errors percentage" min-width="220px" prop="progress">
          <template v-slot="{row}">
            <div class="d-flex align-items-center">
              <span class="mr-2">{{row.progress}}%</span>
              <base-progress :type="row.progressType" :value="row.progress" />
            </div>
          </template>
        </el-table-column>

        <el-table-column label="Actions" width="150">
        <template slot-scope="scope">
          <el-button @click="handleEdit(scope.row, true)" type="text">View</el-button>
          <el-button @click="handleEdit(scope.row, false)" type="text">Edit</el-button>
        </template>
      </el-table-column>

      </el-table>
      
      <!-- Edit Dialog -->
   <el-dialog
      title="Edit Row"
      :visible.sync="editDialogVisible"
      width="50%"
      @close="closeEditDialog"
    >
      <!-- Edit Form -->
      <el-form ref="editForm" :model="editForm" label-width="200px">
        <el-form-item label="Application Name">
          <el-input v-model="editForm.appname" :disabled="isReadOnly"></el-input>
        </el-form-item>
        <el-form-item label="Application Abbreviation">
          <el-input v-model="editForm.appabrv" :disabled="isReadOnly"></el-input>
        </el-form-item>
        <el-form-item label="Owner">
          <el-input v-model="editForm.owner" :disabled="isReadOnly"></el-input>
        </el-form-item>
        <el-form-item label="Functional ID">
          <el-input v-model="editForm.fundId" :disabled="isReadOnly"></el-input>
        </el-form-item>
        <el-form-item label="Creation Date" v-if="isReadOnly">
          <el-input v-model="editForm.creationDate"></el-input>
        </el-form-item>
        <el-form-item label="Created By" v-if="isReadOnly">
          <el-input v-model="editForm.createdBy"></el-input>
        </el-form-item>
        <el-form-item label="Last modified Date" v-if="isReadOnly">
          <el-input v-model="editForm.lastModDate"></el-input>
        </el-form-item>
        <el-form-item label="High Availability">
          <el-checkbox v-model="editForm.highAvailability" :disabled="isReadOnly"/> 
        </el-form-item>
        <el-form-item label="Observations">
          <el-input type="textarea" v-model="editForm.observations" :disabled="isReadOnly"></el-input>
        </el-form-item>
      </el-form>


      <!-- Buttons for Save and Cancel -->
      <span slot="footer" class="dialog-footer">
        <el-button @click="closeEditDialog" v-if="!isReadOnly">Cancel</el-button>
        <el-button @click="closeEditDialog" v-if="isReadOnly">Close</el-button>
        <el-button type="primary" @click="saveEdit" v-if="!isReadOnly">Save</el-button>
      </span>
    </el-dialog>
  </b-card>
    
  </template>
  <script>
    import { BaseProgress } from '@/components';
    import { Table, TableColumn, DropdownMenu, DropdownItem, Dropdown, Button, FormItem, Dialog, Form, Checkbox} from 'element-ui'
    export default {
      components: {
        BaseProgress,
        [Checkbox.name]: Checkbox,
        [Form.name]: Form,
        [Dialog.name]: Dialog,
        [FormItem.name]: FormItem,
        [Button.name]: Button,
        [Table.name]: Table,
        [TableColumn.name]: TableColumn,
        [Dropdown.name]: Dropdown,
        [DropdownItem.name]: DropdownItem,
        [DropdownMenu.name]: DropdownMenu,
      },
      data() {
        return {
          editDialogVisible: false,
          editForm: {
            appname: '',
            appabrv: '',
            owner: '',
            fundId: '',
            creationDate: '',
            createdBy: '',
            lastModDate: '',
            highAvailability: false,
            observations: '',

            isReadOnly: false, // Control read-only mode
          },
          tableData: [
            {
              id: "64fcd0130034532bc2cfa277",
              appname: 'Application A',
              appabrv: "AAA",
              owner: 'John Doe',
              fundId: '64f48f55e631314afd773ee2',
              rcvd: '19',
              sent: '10',
              creationDate: '2023-08-23 15:00:02',
              createdBy: 'John Doe',
              lastModDate: '2023-08-23 15:00:54',
              highAvailability: false,
              progress: 70,
              progressType: 'gradient-danger',
              observations: 'Application to be used to handle clients'
            },
            {
              id: "64fcd023b739a2988337b7e3",
              appname: 'Application B',
              appabrv: "AAB",
              fundId: '64f491b3f46a2d3b7bb0d8b7',
              owner: 'Sarah Smith',
              rcvd: '25',
              sent: '18',
              creationDate: '2023-08-21 17:00:36',
              createdBy: 'Sarah Smith',
              lastModDate: '2023-08-24 18:23:55',
              highAvailability: true,
              progress: 60,
              progressType: 'gradient-warning',
              observations: 'Application to be used to generate report'
            },
            {
              id: "64fcd03042d783c5aa424c0b",
              appname: 'Application C',
              appabrv: "AAC",
              fundId: '64f491f54efe50e3a0e3c923',
              owner: 'Airi Satou',
              rcvd: '33',
              sent: '21',
              creationDate: '2023-05-02 08:41:52',
              createdBy: 'Sarah Smith',
              lastModDate: '2023-07-06 15:23:23',
              highAvailability: true,
              progress: 10,
              progressType: 'gradient-info',
              observations: 'Application to be used to handle accounts'
            },
          ]
        }
      },
      methods: {
        saveEdit() {
          // Handle the save action here, e.g., update the row data
          console.log('Saving edited data:', this.editForm);

          if (this.editForm.id == null){
            this.tableData.push(this.editForm);
          }
          
          // Close the edit dialog
          this.editDialogVisible = false;
        },
        addNewApplication(){
          this.editDialogVisible = true;
          this.isReadOnly = false;

        },
        handleEdit(row, isViewMode) {
          this.editForm.appname = row.appname;
          this.editForm.appabrv = row.appabrv;
          this.editForm.owner = row.owner;
          this.editForm.fundId = row.fundId
          this.editForm.creationDate = row.creationDate;
          this.editForm.createdBy = row.createdBy;
          this.editForm.lastModDate = row.lastModDate;
          this.editForm.highAvailability = row.highAvailability;
          this.editForm.observations = row.observations;

          this.editDialogVisible = true;
          // Handle edit action
          this.isReadOnly = isViewMode;
        },
        closeEditDialog() {
          // Close the edit dialog without saving
          this.editDialogVisible = false;
        },
      }
    }
  </script>
  <style>
  .add-button-container {
  text-align: right;
  margin: 20px; /* Adjust the margin as needed */
}
  </style>
  