<template>
  <div>
    <base-header class="pb-6 pb-8 pt-5 pt-md-8 bg-gradient-success">
    </base-header>
  
    <b-container fluid class="mt--7">
      <!--Tables-->
      <b-row class="mt-5">
        <b-col xl="10" class="mb-5 mb-xl-0">
          <el-card class="document-card">
            <div class="document-label">
              <h2>{{ documentEntity }}</h2>
            </div>
            <div class="document-header">
              <h3>Document Details</h3>
            </div>
            <div class="document-buttons">
              <el-button @click="redirectToModify">Modify</el-button>
              <el-button @click="redirectToAssociate">Associate Applications</el-button>
              <el-button @click="requestDataDeletion">Request Data Deletion</el-button>
            </div>
          </el-card>
        </b-col>
      </b-row>
      <!--End tables-->
    </b-container>
  </div>    
</template>
<script>
    // Charts
  
    // Components
    import { Card, Button} from 'element-ui'
import BaseHeader from '../components/BaseHeader.vue';
   
    export default {
      components: {
         BaseHeader,
        [Card.name]: Card,
        [Button.name]: Button
      },
      props: {
        documentEntity: String,
        documentId: Number,
      },
      methods: {
        redirectToModify() {
          this.$router.push({ name: 'modify-document', params: { id: this.documentId } });
        },
        redirectToAssociate() {
          this.$router.push({ name: 'associate-applications', params: { id: this.documentId } });
        },
        requestDataDeletion() {
          this.$router.push({ name: 'request-data-deletion', params: { id: this.documentId } });
        },
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