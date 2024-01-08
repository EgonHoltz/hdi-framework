<template>
    <div>
  
      <base-header class="pb-6 pb-8 pt-5 pt-md-8 bg-gradient-success">
      </base-header>
  
      <b-container fluid class="mt--7">
        <!--Tables-->
        <b-row class="mt-5">
          <b-col xl="4" class="mb-4 mb-xl-0">
            <ApplicationTableSimple @application="handleAppFromTable"></ApplicationTableSimple>
          </b-col>
          <b-col xl="6" class="mb-6 mb-xl-0">
            <AssociateTechnology></AssociateTechnology>
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
  
    // Import components
    import ApplicationTableSimple from './ApplicationTableSimple.vue';
    import AssociateTechnology from './AssociateTechnology.vue';
  
    export default {
        components: {
            BaseProgress,
            StatsCard,
            ApplicationTableSimple,
            AssociateTechnology
        },
        methods: {
            handleAppFromTable(app){
                console.log("Selected app: "+app);
            }
        },
    };
  </script>
  <style>
  .el-table .cell{
    padding-left: 0px;
    padding-right: 0px;
  }
  </style>
  