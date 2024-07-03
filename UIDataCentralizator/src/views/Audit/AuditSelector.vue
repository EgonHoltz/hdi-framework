<template>
    <div class="file-transfer-container">
        <el-row type="flex" justify="center" align="middle" class="row-bg">
            <el-col :span="3">
                <el-button :class="{ 'btn-selected': isSelected === 'userAudit' }" @click="selectButton('userAudit')">User Audit</el-button>
            </el-col>            

            <el-col :span="2">
                <el-button :class="{ 'btn-selected': isSelected === 'infoAudit' }" @click="selectButton('infoAudit')">Information Audit</el-button>
            </el-col>
        </el-row>
        <div class="gridFormat">
             <FilesAudit v-if="isSelected=='infoAudit'" :isSelected="isSelected"></FilesAudit>
             <UserAudit v-if="isSelected=='userAudit'" :isSelected="isSelected"></UserAudit>
        </div>
  </div>
</template>
  
<script>
    // Components
    import FilesAudit from './FilesAudit.vue';
    import UserAudit from './UserAudit.vue';
    import { Col, Button, Row, Option, Select} from 'element-ui'
    
    export default {
        components: {
            FilesAudit,
            UserAudit,
            [Col.name]: Col,
            [Button.name]: Button,
            [Row.name]: Row,
            [Option.name]: Option,
            [Select.name]: Select
        },
        props: {
        },
        data() {
            return {
                isSelected: 'userAudit',
            };
        },
        watch: {

        },
        methods: {
            selectButton(button) {
                this.isSelected = button;
            },
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