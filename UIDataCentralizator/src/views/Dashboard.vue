<template>
  <div>

    <base-header class="pb-6 pb-8 pt-5 pt-md-8 bg-gradient-success">
      <!-- Card stats -->
      <b-row>
        <b-col xl="3" md="6">
          <stats-card title="Documents"
                      type="gradient-purple"
                      sub-title="5"
                      icon="ni ni-books"
                      class="mb-4">

          </stats-card>
        </b-col>
        <b-col xl="3" md="6">
          <stats-card title="Received data"
                      type="gradient-orange"
                      sub-title="1,287"
                      icon="ni ni-cloud-download-95"
                      class="mb-4">

            <template slot="footer">
              <span class="text-success mr-2">25%</span>
              <span class="text-nowrap">Since last month</span>
            </template>
          </stats-card>
        </b-col>
        <b-col xl="3" md="6">
          <stats-card title="Sent data"
                      type="gradient-green"
                      sub-title="128"
                      icon="ni ni-cloud-upload-96"
                      class="mb-4">

            <template slot="footer">
              <span class="text-danger mr-2">12%</span>
              <span class="text-nowrap">Since last month</span>
            </template>
          </stats-card>

        </b-col>
        <b-col xl="3" md="6">
          <stats-card title="Duplicated found"
                      type="gradient-info"
                      sub-title="49,65%"
                      icon="ni ni ni-ungroup"
                      class="mb-4">

            <template slot="footer">
              <span class="text-success mr-2">54.8%</span>
              <span class="text-nowrap">Since last month</span>
            </template>
          </stats-card>
        </b-col>
      </b-row>
    </base-header>

    <b-container fluid class="mt--7">
      <!--Tables-->
      <b-row class="mt-5">
        <b-col xl="10" class="mb-5 mb-xl-0">
          <social-traffic-table></social-traffic-table>
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
  import SocialTrafficTable from './Dashboard/SocialTrafficTable';

  export default {
    components: {
      BaseProgress,
      StatsCard,
      SocialTrafficTable
    }
  };
</script>
<style>
.el-table .cell{
  padding-left: 0px;
  padding-right: 0px;
}
</style>
