import Vue from "vue";
import Vuex from "vuex";
import moduleBase from "./module";
import moduleApplication from "./application/application.module";
import moduleDocument from "./document/document.module"
import moduleDocumentApplication from "./document-application/document-application.module"
import moduleDocumentData from "./document-data/document-data.module"
import moduleAuditLogger from "./audit-logger/audit-logger.module";

Vue.use(Vuex);

export default new Vuex.Store({
    getters: moduleBase.getters,
    mutations: moduleBase.mutations,
    state: moduleBase.state,
    actions: moduleBase.actions,
    modules: {
      application: moduleApplication,
      document: moduleDocument,
      documentApplication: moduleDocumentApplication,
      documentData: moduleDocumentData,
      auditLogger: moduleAuditLogger
    },
  });