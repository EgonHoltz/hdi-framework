<template>
    <div class="file-transfer-container">
      <el-button class="m-3" type="primary" :disabled="isButtonDisabled()" v-if="isSendDirection()" @click="openNamagementFile()">Manage Send File</el-button>
      <el-table
        class="table-responsive table"
        :data="tableData"
        header-row-class-name="thead-light">
        <el-table-column label="Document Name" min-width="115px" prop="docName">
          <template v-slot="{row}">
            <div class="font-weight-600">{{row.docName}}</div>
          </template>
        </el-table-column>
        <el-table-column label="Application Name" min-width="115px" prop="appName">
          <template v-slot="{row}">
            <div class="font-weight-600">{{row.appName}}</div>
          </template>
        </el-table-column>
  
        <el-table-column label="File name" min-width="110px" prop="fileName">
        </el-table-column>
        <el-table-column label="Date" min-width="110px" prop="date">
        </el-table-column>

        <el-table-column label="Actions" width="150">
          <template slot-scope="scope">
            <el-button @click="handleDownload(scope.row, true)" type="text">Download</el-button>
          </template>
        </el-table-column>
      </el-table>

      <el-dialog
      title="Edit Row"
      :visible.sync="editDialogVisible"
      width="50%"
      @close="closeEditDialog"
    >
      <!-- Edit Form -->
      <el-form ref="editForm" :model="editForm" label-width="200px">
        <el-input v-model="editForm.id" v-if="false"></el-input>
        <el-form-item label="Application Name">
          <el-input v-model="editForm.appName" :disabled="true"></el-input>
        </el-form-item>
        <el-form-item label="Document">
          <el-input v-model="editForm.document" :disabled="true"></el-input>
        </el-form-item>
        <el-form-item label="File type">
          <el-radio-group v-model="editForm.fileType">
            <el-radio label="JSON">JSON</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="Data mode">
          <el-radio-group v-model="editForm.dataMode">
            <el-radio label="DELTA">Delta</el-radio>
            <el-radio label="FULL">Full</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="Schedule Type:">
          <el-radio-group v-model="editForm.scheduleType">
            <el-radio :label="1">Daily</el-radio>
            <el-radio :label="2">Monthly</el-radio>
          </el-radio-group>
        </el-form-item>

        <!-- Daily Options -->
        <el-form-item label="Select Days of the Week:" v-if="editForm.scheduleType === 1">
          <el-checkbox-group v-model="editForm.selectedDays">
            <el-checkbox :label="day.exp" v-for="day in daysOfWeek" :key="day.exp">{{ day.label }}</el-checkbox>
          </el-checkbox-group>
        </el-form-item>

        <!-- Monthly Options -->
        <el-form-item label="Select Day of the Month:" v-if="editForm.scheduleType === 2">
          <el-select v-model="editForm.selectedDayOfMonth" placeholder="Select a day">
            <el-option :label="day.exp" :value="day.exp" v-for="day in daysOfMonth" :key="day.exp"></el-option>
          </el-select>
        </el-form-item>

        <!-- Time Selection -->
        <el-form-item label="Select Time:">
          <el-time-select
            v-model="editForm.selectedTime"
            :picker-options="timePickerOptions"
            placeholder="Select time">
          </el-time-select>
        </el-form-item>

        <el-form-item label="Creation Date" >
          <el-input v-model="editForm.creationDate" :disabled="true"></el-input>
        </el-form-item>
        <el-form-item label="Created By">
          <el-input v-model="editForm.createdBy" :disabled="true"></el-input>
        </el-form-item>
        <el-form-item label="Last modified Date">
          <el-input v-model="editForm.lastModDate" :disabled="true"></el-input>
        </el-form-item>
      </el-form>


      <!-- Buttons for Save and Cancel -->
      <span slot="footer" class="dialog-footer">
        <el-button @click="closeEditDialog" >Cancel</el-button>
        <el-button type="primary" @click="saveEdit">Save</el-button>
      </span>
    </el-dialog>
  </div>
</template>
  
<script>
  // Components
  import { TableColumn, Table, Button, FormItem, Dialog, Form, RadioGroup, Select, Checkbox, CheckboxGroup, Radio, TimeSelect, Option} from 'element-ui';
  import {FETCH_SEND_SFTP_SCHEDULER, UPSERT_SEND_SFTP_SCHEDULER} from '@/store/document-application/document-application.constants';

  export default {
    components: {
      [Checkbox.name]: Checkbox,
      [TimeSelect.name]: TimeSelect,
      [Form.name]: Form,
      [Dialog.name]: Dialog,
      [FormItem.name]: FormItem,
      [TableColumn.name]: TableColumn,
      [Button.name]: Button,
      [Table.name]: Table,
      [Checkbox.name]: Checkbox,
      [CheckboxGroup.name]: CheckboxGroup,
      [Radio.name]: Radio,
      [RadioGroup.name]: RadioGroup,
      [Option.name]: Option,
      [Select.name]: Select,
    },
    props: {
      viewDirection: {
        type: String,
        required: true,
      },
      informationSelectedObject: {
        type: Object,
        required: true,
      }
    },
    watch: {
      viewDirection: {
        handler(newValue) {
          this.viewDirection = newValue;
          console.log("Load data on the direction: " + this.viewDirection);
          //load associated tech to forms
          this.afterRender();
          this.isButtonDisabled();
        },
        deep: true,
      },
      informationSelectedObject: {
        handler(newValue) {
          this.documentDataId = newValue.documentSelected.value;
          this.applicationId = newValue.applicationSelected.value;
          this.editForm.appName = newValue.applicationSelected.label;
          this.editForm.document = newValue.documentSelected.label;
        },
        deep: true,
      }
    },
    computed: {
      daysOfMonth() {
        let optionsDays = [{label: 'First weekday of Month',exp: '1W'}];
        optionsDays.push({label: '1st',exp: '1'},{label: '2nd',exp: '2'},{label: '3rd',exp: '3'})
        for (let i = 4; i <= 20; i++) {
          let obj = {label: i+'th' ,exp: i };
          optionsDays.push(obj);
        }
        optionsDays.push({label: '21st',exp: '21'},{label: '22nd',exp: '22'},{label: '23rd',exp: '23'});
        for (let i = 24; i <= 28; i++) {
          let obj = {label: i+'th' ,exp: i };
          optionsDays.push(obj);
        }
        optionsDays.push({label: 'Last weekday of Month',exp: 'LW'},{label: 'Last day of Month',exp: 'L'})
        return optionsDays;
      }
    },
    data() {
        return {
          daysOfWeek: [
            {label: "Sunday", exp: "SUN"},
            {label: "Monday", exp: "MON"},
            {label: "Tuesday", exp: "TUE"},
            {label: "Wednesday", exp: "WED"},
            {label: "Thursday", exp: "THU"},
            {label: "Friday", exp: "FRI"},
            {label: "Saturday", exp: "SAT"}],
          timePickerOptions: {
            start: '00:00',
            step: '00:15',
            end: '23:45',
          },
          disabledSendForm: true,
          editDialogVisible: false,
          applications: [],
          documents: [],
          editForm: {
            scheduleType: 1,// 1 for Daily, 2 for Monthly
            fileType: 'JSON',
            selectedTime: '',
            selectedDayOfMonth: null,
            selectedDays: [],
            dataMode: 'DELTA',
            appName: '',
            document: ''
          },
          tableData: [
            {docName: "Company", appName: "AAE - Application AAE", fileName: "company_aee_20200505.json", date: "01/05/2024 15:34" }
          ]
        };
    },
    methods: {
        afterRender(){
            this.fetchFilesData();
        },
        fetchFilesData(){
            console.log("fetch fetchFilesData");
        },
        isSendDirection(){
          return this.viewDirection == 'SEND';
        },
        isButtonDisabled(){
          console.log("verifying if enables the combo")
          this.disabledSendForm= !(this.applicationId && this.applicationId !== "")
            || !(this.documentDataId && this.documentDataId !== "");
          return this.disabledSendForm;
        },
        openNamagementFile(){
          let payload = [];
          payload.appId = this.applicationId;
          payload.docId = this.documentDataId;
          this.$store.dispatch(`documentApplication/${FETCH_SEND_SFTP_SCHEDULER}`,payload).then( 
                (res) => {
                    this.editDialogVisible = true;
                    let sftpScheduler = this.$store.getters['documentApplication/getSftpScheduler'];
                    this.transformCronToScheduler(sftpScheduler);
                }, err => {
                    console.log("Error while fetch")
                    this.alertTitle = "Error while fetch"
                });
        },
        closeEditDialog() {
          // Close the edit dialog without saving
          this.editDialogVisible = false;
        },
        saveEdit(){
          console.log("saving the send configuration");
          let cron = this.transformSchedulerToCron();
          let payload = [];
          payload.cron = cron;
          payload.appId = this.applicationId;
          payload.docId = this.documentDataId;
          payload.dataMode = this.editForm.dataMode;
          payload.fileType = this.editForm.fileType;

          console.log("Saving the SFTP Send Config");
          this.$store.dispatch(`documentApplication/${UPSERT_SEND_SFTP_SCHEDULER}`,payload).then( 
              () => {
                  this.editDialogVisible = false;
                  this.alertTitle = "SFTP Scheduler configuration saved!"                   
                  this.closeEditDialog();
                  this.showSuccessAlert();
              }, err => {
                  this.editDialogVisible = false;
                  this.alertTitle = "Error on creation. Please contact the IT team"
                  this.showErrorAlert();
              }
          );

        },
        transformSchedulerToCron(){
          let seconds = '0';
          let minutes = '0';
          let hours = '0';
          let dayOfMonth = '*';
          let month = '*';
          let dayOfWeek = '*';
          let year = '*';
          if (this.editForm.scheduleType == 1){
            let selectedDays = this.editForm.selectedDays.join(',');
            dayOfMonth = '?';
            dayOfWeek = selectedDays;
          } else if (this.editForm.scheduleType == 2){
            dayOfWeek = '?';
            dayOfMonth = this.editForm.selectedDayOfMonth;
          }
          if (this.editForm.selectedTime){
            [hours, minutes] = this.editForm.selectedTime.split(":");
          }
          let cron = `${seconds} ${minutes} ${hours} ${dayOfMonth} ${month} ${dayOfWeek} ${year}`
          return cron;
        },
        transformCronToScheduler(scheduler){
          if (!scheduler || !scheduler.cron){
            return;
          }
          const parts = scheduler.split(/\s+/);
          
          let seconds = parts[0];
          let minutes = parts[1];
          let hours = parts[2];
          let dayOfMonth = parts[3];
          let month = parts[4];
          let dayOfWeek = parts[5];
          let year = parts[6];
          if (dayOfMonth == '?'){
            this.editForm.scheduleType = 1;
            this.editForm.selectedDays = dayOfWeek.split(",");
          } else if (dayOfWeek == '?'){
            this.editForm.selectedDayOfMonth = dayOfMonth
          }
          this.editForm.selectedTime = `${hours}:${minutes}`;
        },

    }
  };
  </script>
  <style>
.row-bg{
    padding-bottom: 15px;
}
  
  </style>