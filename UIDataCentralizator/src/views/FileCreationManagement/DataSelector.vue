<template>
    <div class="file-transfer-container">
        <el-row type="flex" justify="center" align="middle" class="row-bg">
            <el-col :span="4">
                <el-select v-model="applicationsSelection" placeholder="Select option">
                <el-option
                    v-for="item in applications"
                    :key="item.value"
                    :label="item.label"
                    :value="item.value">
                </el-option>
                </el-select>
            </el-col>

            <el-col :span="4">
                <el-select v-model="documentSelection" placeholder="Select option">
                <el-option
                    v-for="item in documents"
                    :key="item.value"
                    :label="item.label"
                    :value="item.value">
                </el-option>
                </el-select>
            </el-col>
            </el-row>

            <el-row type="flex" justify="center" align="middle" class="row-bg">
            <el-col :span="2">
                <el-button @click="openSendGrid" type="warning">Send Files</el-button>
            </el-col>

            <el-col :span="2">
                <el-button @click="openReceiveGrid" type="warning">Receive Files</el-button>
            </el-col>
        </el-row>
        <div class="gridFormat">
            <FilesGrid v-if="showFileGrid" :viewDirection="viewDirection" :informationSelectedObject="informationSelectedObject"></FilesGrid>
        </div>
  </div>
</template>
  
<script>
    // Components
    import FilesGrid from './FilesGrid.vue';
    import { Col, Button, Row, Option, Select} from 'element-ui'
    import {FETCH_DOCUMENTS_COMBO} from '@/store/document-application/document-application.constants';
    import {FETCH_APPLICATIONS} from '@/store/application/application.constants';
    
    export default {
        components: {
            FilesGrid,
            [Col.name]: Col,
            [Button.name]: Button,
            [Row.name]: Row,
            [Option.name]: Option,
            [Select.name]: Select
        },
        props: {
            documentEntity: String,
            documentId: Number,
        },
        data() {
            return {
                applicationsSelection: null,
                documentSelection: null,
                applications: [],
                documents: [],
                showFileGrid: false,
                applicationId: null,
                documentDataId: null,
                viewDirection: null,
                informationSelectedObject: {
                    applicationSelected: null,
                    documentSelected: null,
                    direction: null
                },
            };
        },
        watch: {
            applicationsSelection(newValue) {
                if (newValue) {
                    this.fetchDocuments(newValue);
                    this.documentSelection = null;
                    this.informationSelectedObject['documentSelected']=null;
                    this.informationSelectedObject['applicationSelected']=this.applications.find(e => e.value == newValue);
                }
            },
            documentSelection(newValue) {
                if (newValue) {
                    this.documentDataId = newValue;
                    this.informationSelectedObject['documentSelected']=this.documents.find(e => e.value === newValue);
                }
            },
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
                    let appFetched = this.$store.getters['application/getApplications'];
                    this.applications = [];
                    this.applications = appFetched.map(i => ({label: i.appAbrv +" - "+ i.appName, value: i.id}))
                }, err => {
                    this.alertTitle = "Error while fetch"
                });
            },
            fetchDocuments(newValue){
                console.log("fetch data");
                let payload = {applicationId:this.applicationsSelection};
                this.$store.dispatch(`documentApplication/${FETCH_DOCUMENTS_COMBO}`,payload).then( 
                    (res) => {
                        let docsFetched = this.$store.getters['documentApplication/getDocSftp'];
                        this.documents = docsFetched.map(d => ({label: d.documentName, value: d.id}))
                    }, err => {
                        this.alertTitle = "Error while fetch"
                    });

            },
            openSendGrid() {
                console.log("show send file grid:");
                this.showFileGrid = false;
                this.viewDirection = "SEND";
                this.informationSelectedObject['direction'] = "SEND"
                this.informationSelectedObject['key']=  this.generateKeyToRefreshGrid(8);
                this.showFileGrid = true;
            },
            openReceiveGrid() {
                this.showFileGrid = false;
                console.log("show receive file grid");
                this.viewDirection = "RECV"
                this.informationSelectedObject['direction'] = "RECV"
                this.informationSelectedObject['key']=  this.generateKeyToRefreshGrid(8);
                this.showFileGrid = true;
            },
            generateKeyToRefreshGrid(length){
                const chars = 'abcdef0123456789';
                let rnd = '';
                for (let i = 0; i < length; i++) {
                    rnd += chars.charAt(Math.floor(Math.random() * chars.length));
                }
                return rnd;
            }
        }
    };
  </script>
  <style>
.row-bg{
    padding-bottom: 15px;
}
.gridFormat {
    padding-top: 10px;
    margin-left: auto;
    margin-right: auto;
}
  
  </style>./FilesGrid.vue