import auditLoggerService from "@/api/auditLogger.service";
import {
    FETCH_FILES_AUDIT_LOGGER,
    //mutations
    SET_FILES_AUDIT_LOGGER,
    SET_MESSAGE
} from "./audit-logger.constants";

const state = {
    fileAuditLogs: [],
};

const getters = {
    getFileAuditLogs: state => state.fileAuditLogs,
};

const actions = {
  [FETCH_FILES_AUDIT_LOGGER]: ({commit, rootState}, payload) => {
      return new Promise((resolve, reject) => {
        console.log("call getAllFilesAuditLogger");
        auditLoggerService.getAllFilesAuditLogger(/*rootState.auth.token,*/payload)
            .then(
              res => {
                console.log("on response module " + res)
                commit(SET_FILES_AUDIT_LOGGER, res);
                resolve(res)
              },
              err => {
                commit(SET_MESSAGE, err.message)
                reject(err)
              });
      })
  },
};

export const mutations = {
    [SET_FILES_AUDIT_LOGGER]: (state, fileAuditLogs) => {
      state.fileAuditLogs = fileAuditLogs;
    },
    [SET_MESSAGE]: (state, message) => {
      state.message = message;
    },
  };
  
  export default {
    namespaced: true,
    state,
    getters,
    actions,
    mutations
  }