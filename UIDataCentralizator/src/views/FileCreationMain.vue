<template>
    <div>
      <base-header class="pb-6 pb-8 pt-5 pt-md-8 bg-gradient-success">
      </base-header>
    
      <b-container fluid class="mt--7">
        <!--Tables-->
        <b-row class="mt-5">
          <b-col xl="10" class="mb-5 mb-xl-0">
            <DataSelector></DataSelector>
          </b-col>
        </b-row>
        <!--End tables-->
      </b-container>
    </div>
  </template>
  <script>
    // Components
    import BaseHeader from '../components/BaseHeader.vue';
  
    import DataSelector from './FileCreationManagement/DataSelector.vue';
     
      export default {
        components: {
          BaseHeader,
          DataSelector
        },
        props: {
        },
        methods: {
        }
      };
    </script>
  <style>
  .document-card {
    width: 300px;
    margin: 20px;
  }
  
  .document-label {
    text-align: center;
    margin: 20px 0;
  }
  
  .document-header {
    text-align: center;
  }
  
  .document-buttons {
    text-align: center;
    margin-top: 20px;
  }
  
  </style>