<template>
    <b-card body-class="p-0" header-class="border-0">
        <template v-slot:header>
            <RabbitMQAssociation :selectedApplication="selectedApplication"></RabbitMQAssociation>
        </template>
    </b-card>
</template>
<script>
      // Components
      import BaseProgress from '@/components/BaseProgress';
      import StatsCard from '@/components/Cards/StatsCard';
      import RabbitMQAssociation from './Technologies/RabbitMQAssociation.vue';
      export default {
        components: {
            BaseProgress,
            StatsCard,
            RabbitMQAssociation
        },
        props: {
            selectedApplication: {
                type: Object,
                required: true,
            }
        },
        watch: {
            selectedApplication: {
                handler(newValue) {
                },
                deep: true,
            },
        },
        data() {
          return {
            selectedApplication: null,
          }
        },
        methods: {
        }
      }
    </script>
<style scoped>

.card-title {
  margin-left: 10px;
  display: inline-block;
}
</style>
    