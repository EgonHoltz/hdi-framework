<template>
    <div>
      <base-header class="pb-6 pb-8 pt-5 pt-md-8 bg-gradient-success">
      </base-header>
    
      <b-container fluid class="mt--7">
        <!--Tables-->
        <el-row type="flex" justify="center" align="middle" class="row-bg">
          <b-col xl="10" class="mb-5 mb-xl-0">
            <AuditSelector></AuditSelector>
          </b-col>
        </el-row>
        <!--End tables-->
      </b-container>
    </div>
  </template>
  <script>
    // Components
    import BaseHeader from '../components/BaseHeader.vue';
    import AuditSelector from './Audit/AuditSelector.vue';
    import {Col, Row} from 'element-ui'
     
      export default {
        components: {
          BaseHeader,
          AuditSelector,
          [Col.name]: Col,
          [Row.name]: Row,
        },
        props: {
        },
        data() {
        },
        methods: {
        }
      };
    </script>
  <style>
    .center-container {
      display: flex;
      justify-content: center;
      align-items: center;
      height: 100vh; /* Use full height to center vertically */
    }

    .el-button {
      margin: 0 10px; /* Spacing between buttons */
    }

    .btn-selected {
      background-color: #67C23A; /* Green color for selected */
      color: white;
    }

    .el-button:not(.btn-selected) {
      background-color: #409EFF; /* Blue color for not selected */
      color: white;
    }
  </style>