<template>
  <div class="document-cards-container">
    <el-card v-for="(entity, index) in documentEntities" :key="index" class="document-card">
        <div class="document-label">
            <h2>{{ entity.documentName }}</h2>
        </div>
        <div class="document-header">
            <h3>{{entity.documentShortDetail}}</h3>
        </div>
        <div class="document-buttons">
            <el-button @click="redirectToModify(entity.id)">Modify</el-button>
            <el-button @click="redirectToAssociate(entity.id)">Associate Applications</el-button>
            <el-button @click="requestDataDeletion(entity.id)">Request Data Deletion</el-button>
        </div>
    </el-card>

    <!-- Fixed card on the right -->
    <el-card class="document-card">
      <div class="document-label"><br /></div>
      <div class="document-header"><br /></div>
      <div class="plus-sign center-content">
        <router-link to="/create-document" >
          <!-- You can use an icon for the plus sign -->
          <i class="el-icon-plus"> </i>
          <!-- Text or label -->
          <span>Create New Document</span>
        </router-link>
      </div>
    </el-card>
  </div>
</template>

<script>
    // Charts
  
    // Components
    import { Card, Button} from 'element-ui'
    import {FETCH_DOCUMENTS, ADD_DOCUMENT, EDIT_DOCUMENT} from '@/store/document/document.constants';
   
    export default {
      components: {
        [Card.name]: Card,
        [Button.name]: Button
      },
      props: {
        documentEntity: String,
        documentId: Number,
      },
      data() {
        return {
          documentEntities: [
          ], // Initialize as an empty array
        };
      },
      mounted(){
        this.afterRender();
      },
      methods: {
        afterRender(){
          this.fetchDocuments();
        },
        fetchDocuments(){
          console.log("fetch data");
          this.$store.dispatch(`document/${FETCH_DOCUMENTS}`).then( 
            (res) => {
                //this.documents = this.$store.getters['document/getDocuments'];
                this.documentEntities = this.documentEntities.concat(res);

            }, err => {
                this.alertTitle = "Error while fetch"
            });
        },

        redirectToModify(id) {
          console.log("going to modify the entity: " +id);
          this.$router.push({ name: 'documentModification', params: { id: id } });
        },
        redirectToAssociate(id) {
          this.$router.push({ name: 'documentAssociation', params: { id: id } });
        },
        requestDataDeletion(id) {
          this.$router.push({ name: 'documentDataDeletion', params: { id: id } });
        },
      }
    };
</script>
<style>
.document-cards-container {
  display: flex;
  flex-wrap: wrap;
  justify-content: flex-start;
  align-items: flex-start;
  gap: 20px; /* Adjust the gap between cards as needed */
}

.document-card {
  flex: 1;
  min-height: 320px;
  min-width: calc(33.33% - 70px);
  max-width: calc(33.33% - 70px);
  /* Adjust card styles as needed */
  /* For example, you can set a fixed width or use flex-basis to control card size */
}

.center-content {
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  height: 100%; /* Ensure the link takes the full height of the card */
}

/* Additional styles for plus-sign, icons, or other elements */

.document-header {
  text-align: center;
}

/* Style the buttons to be stacked vertically */
.document-buttons {
  display: flex;
  flex-direction: column;
}

/* Add spacing between the buttons */
.el-button {
  margin-bottom: 10px; /* Adjust the margin as needed */
}

</style>