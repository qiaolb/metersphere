<template>
  <div class="container">
    <ms-table
      v-loading="result.loading"
      :show-select-all="false"
      :data="data"
      :screen-height="null"
      :enable-selection="false"
      @refresh="getIssues">

      <ms-table-column
        :label="$t('test_track.issue.id')"
        prop="id" v-if="false">
      </ms-table-column>
      <ms-table-column
        :label="$t('test_track.issue.id')"
        prop="num">
      </ms-table-column>

      <ms-table-column
        :label="$t('test_track.issue.title')"
        prop="title">
      </ms-table-column>

      <ms-table-column
          :label="$t('test_track.issue.platform_status')"
          v-if="isThirdPart"
          prop="platformStatus">
        <template v-slot="scope">
          {{ scope.row.platformStatus ? scope.row.platformStatus : '--'}}
        </template>
      </ms-table-column>

      <ms-table-column
          v-else
          :label="$t('test_track.issue.status')"
          prop="status">
        <template v-slot="scope">
          <span>{{ issueStatusMap[scope.row.status] ? issueStatusMap[scope.row.status] : scope.row.status }}</span>
        </template>
      </ms-table-column>

      <ms-table-column
        :label="$t('test_track.issue.platform')"
        prop="platform">
      </ms-table-column>

      <issue-description-table-item/>
    </ms-table>

  </div>
</template>

<script>
import MsTable from "@/business/components/common/components/table/MsTable";
import MsTableColumn from "@/business/components/common/components/table/MsTableColumn";
import IssueDescriptionTableItem from "@/business/components/track/issue/IssueDescriptionTableItem";
import {ISSUE_STATUS_MAP} from "@/common/js/table-constants";
import {getIssuesByPlanId, getShareIssuesByPlanId} from "@/network/Issue";
export default {
  name: "FunctionalIssueList",
  components: {IssueDescriptionTableItem, MsTableColumn, MsTable},
  data() {
    return {
      data: [],
      result: {},
      isThirdPart: false
    }
  },
  props: ['planId', 'isTemplate', 'report', 'isShare', 'shareId', 'isDb'],
  computed: {
    issueStatusMap() {
      return ISSUE_STATUS_MAP;
    },
  },
  mounted() {
    this.isThirdPart = this.report.isThirdPartIssue;
    this.getIssues();
  },
  watch: {
    data() {
      if (this.data) {
        this.$emit('setSize', this.data.length);
      }
    }
  },
  methods: {
    getIssues() {
      if (this.isTemplate || this.isDb) {
        this.data = this.report.issueList;
      } else if (this.isShare) {
        this.result = getShareIssuesByPlanId(this.shareId, this.planId, (data) => {
          this.data = data;
        });
      } else {
        this.result = getIssuesByPlanId(this.planId, (data) => {
          this.data = data;
        });
      }
    },
  }
}
</script>

<style scoped>
</style>
