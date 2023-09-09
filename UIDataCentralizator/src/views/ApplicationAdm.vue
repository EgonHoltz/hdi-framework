<template>
    <div>
  
      <base-header class="pb-6 pb-8 pt-5 pt-md-8 bg-gradient-success">
      </base-header>
  
      <b-container fluid class="mt--7">
        <!--Tables-->
        <b-row class="mt-5">
          <b-col xl="10" class="mb-5 mb-xl-0">
            <applications-table></applications-table>
          </b-col>
        </b-row>
        <!--End tables-->
      </b-container>
  
    </div>
  </template>
  <script>
    // Charts
  
    // Components
    import BaseProgress from '@/components/BaseProgress';
    import StatsCard from '@/components/Cards/StatsCard';
  
    // Tables
    import ApplicationsTable from './ApplicationAdm/ApplicationsTable.vue';
  
    export default {
      components: {
        BaseProgress,
        StatsCard,
        ApplicationsTable
      }
    };
  </script>
  <style>
  .el-table .cell{
    padding-left: 0px;
    padding-right: 0px;
  }
  </style>
  