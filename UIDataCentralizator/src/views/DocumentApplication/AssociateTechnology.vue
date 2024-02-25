<template>
    <b-card body-class="p-0" header-class="border-0">
        <template v-slot:header>
            <RabbitMQAssociation :selectedApplication="selectedApplication"></RabbitMQAssociation>
            <SFTPAssociation :selectedApplication="selectedApplication"></SFTPAssociation>
            <GRPCAssociation :selectedApplication="selectedApplication"></GRPCAssociation>
        </template>
    </b-card>
</template>
<script>
      // Components
      import BaseProgress from '@/components/BaseProgress';
      import StatsCard from '@/components/Cards/StatsCard';
      import RabbitMQAssociation from './Technologies/RabbitMQAssociation.vue';
      import SFTPAssociation from './Technologies/SFTPAssociation.vue';
      import GRPCAssociation from './Technologies/GRPCAssociation.vue'
      import {FETCH_DOCUMENT_APPLICATION} from '@/store/document-application/document-application.constants';
      export default {
        components: {
            BaseProgress,
            StatsCard,
            RabbitMQAssociation,
            SFTPAssociation,
            GRPCAssociation
        },
        props: {
            selectedApplication: {
                type: Object,
                default: null,
                required: true,
            }
        },
        watch: {
            selectedApplication: {
                handler(newValue) {
                  this.applicationId = newValue.id;
                    //load associated tech to forms
                    this.afterRender();
                },
                deep: true,
            },
        },
        data() {
          return {
            applicationId: null,
            configurations: null,
            rabbitMQ: null,
            sftp: null,
            grpc: null,
          }
        },
        methods: {
          afterRender(){
                this.fetchTechnologies();
            },
            fetchTechnologies(){
                const documentId = this.$route.params.id;
                console.log("selectedApplication: " + this.applicationId);
                console.log("on the document: " + documentId);
                let payloadIds = {
                    applicationId: this.applicationId,
                    documentId: documentId
                }
                this.$store.dispatch(`documentApplication/${FETCH_DOCUMENT_APPLICATION}`, payloadIds).then( 
                () => {
                    this.configurations = this.$store.getters['documentApplication/getDocumentApplication'];
                }, err => {
                    this.alertTitle = "Error while fetch"
                });
            },
        }
      }
    </script>
<style scoped>

.card-title {
  margin-left: 10px;
  display: inline-block;
}
</style>
    