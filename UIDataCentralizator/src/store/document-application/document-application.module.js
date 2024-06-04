import docAppService from "@/api/documentApplication.service";
import moment from 'moment';
import {
    FETCH_DOCUMENT_APPLICATION,
    FETCH_RABBIT_MQ,
    UPSERT_RABBIT_MQ,
    FETCH_SFTP,
    UPSERT_SFTP,
    FETCH_GRPC,
    UPSERT_GRPC,
    FETCH_DOCUMENTS_COMBO,
    UPSERT_SEND_SFTP_SCHEDULER,
    FETCH_SEND_SFTP_SCHEDULER,
    //mutations
    SET_DOCUMENT_APPLICATION,
    SET_MQ_RABBIT,
    SET_SFTP,
    SET_GRPC,
    SET_MESSAGE,
    SET_SFTP_SCHEDULER,
    SET_DOCUMENT_SFTP
} from "./document-application.constants";

const state = {
    docApp: {},
    mqRabbit: [],
    sftp: [],
    grpc: [],
    sftpScheduler: {},
    docSftp: []
};

const getters = {
    getDocumentApplication: state => state.docApp,
    getRabbitMq: state => state.mqRabbit,
    getSftp: state => state.sftp,
    getGrpc: state => state.grpc,
    getDocSftp: state => state.docSftp,
    getSftpScheduler: state => state.sftpScheduler
};

const actions = {
  [FETCH_DOCUMENT_APPLICATION]: ({commit, rootState}, payload) => {
      return new Promise((resolve, reject) => {
        console.log("getOneDocumentApplication");
        docAppService.getOneDocumentApplication(/*rootState.auth.token,*/payload)
            .then(
              res => {
                console.log("on response module " + res)
                commit(SET_DOCUMENT_APPLICATION, res);
                resolve(res)
              },
              err => {
                commit(SET_MESSAGE, err.message)
                reject(err)
              });
      })
  },
  [FETCH_RABBIT_MQ]: ({ commit, rootState }, payload) => {
    console.log("action FETCH_RABBIT_MQ")
      return new Promise((resolve, reject) => {
        docAppService.getMqRabbitFromConfiguration(/*rootState.auth.token,*/ payload)
          .then(
            res => {
              console.log("on response module " + res)
              commit(SET_MQ_RABBIT, res);
              resolve(res)
            }, err => {
              commit(SET_MESSAGE, err.message)
              reject(err)
            });
      });
  },
  [UPSERT_RABBIT_MQ]: ({ commit, rootState }, payload) => {
    console.log("action called")
      return new Promise((resolve, reject) => {
        docAppService.upsertRabbitMq(/*rootState.auth.token,*/ payload)
          .then(
            res => {
              commit(SET_MESSAGE, `Document added with success!`);
              resolve(res)
            }, err => {
              commit(SET_MESSAGE, err.message)
              reject(err)
            });
      });
  },
  [FETCH_SFTP]: ({ commit, rootState }, payload) => {
    console.log("action FETCH_SFTP")
      return new Promise((resolve, reject) => {
        docAppService.getSftpFromConfiguration(/*rootState.auth.token,*/ payload)
          .then(
            res => {
              console.log("on response module " + res)
              commit(SET_SFTP, res);
              resolve(res)
            }, err => {
              commit(SET_MESSAGE, err.message)
              reject(err)
            });
      });
  },
  [UPSERT_SFTP]: ({ commit, rootState }, payload) => {
    console.log("action UPSERT_SFTP")
      return new Promise((resolve, reject) => {
        docAppService.upsertSftp(/*rootState.auth.token,*/ payload)
          .then(
            res => {
              commit(SET_MESSAGE, `Document added with success!`);
              resolve(res)
            }, err => {
              commit(SET_MESSAGE, err.message)
              reject(err)
            });
      });
    },
  [FETCH_GRPC]: ({ commit, rootState }, payload) => {
    console.log("action FETCH_GRPC")
      return new Promise((resolve, reject) => {
        docAppService.getGrpcFromConfiguration(/*rootState.auth.token,*/ payload)
          .then(
            res => {
              console.log("on response module " + res)
              commit(SET_GRPC, res);
              resolve(res)
            }, err => {
              commit(SET_MESSAGE, err.message)
              reject(err)
            });
      });
  },
  [UPSERT_GRPC]: ({ commit, rootState }, payload) => {
    console.log("action UPSERT_GRPC")
      return new Promise((resolve, reject) => {
        docAppService.upsertGrpc(/*rootState.auth.token,*/ payload)
          .then(
            res => {
              commit(SET_MESSAGE, `Document added with success!`);
              resolve(res)
            }, err => {
              commit(SET_MESSAGE, err.message)
              reject(err)
            });
          });
        },
  [FETCH_DOCUMENTS_COMBO]: ({ commit, rootState }, payload) => {
    console.log("action FETCH_DOCUMENTS_COMBO")
      return new Promise((resolve, reject) => {
        docAppService.getDocumentWithSftpByApplication(/*rootState.auth.token,*/ payload)
          .then(
            res => {
              console.log("on response module " + res)
              commit(SET_DOCUMENT_SFTP, res);
              resolve(res)
            }, err => {
              commit(SET_MESSAGE, err.message)
              reject(err)
            });
      });
  },
  [FETCH_SEND_SFTP_SCHEDULER]: ({ commit, rootState }, payload) => {
    console.log("action FETCH_SEND_SFTP_SCHEDULER")
      return new Promise((resolve, reject) => {
        docAppService.getSftpSchedulerConfiguration(/*rootState.auth.token,*/ payload)
          .then(
            res => {
              console.log("on response module " + res)
              commit(SET_SFTP_SCHEDULER, res);
              resolve(res)
            }, err => {
              commit(SET_MESSAGE, err.message)
              reject(err)
            });
      });
  },
  [UPSERT_SEND_SFTP_SCHEDULER]: ({ commit, rootState }, payload) => {
    console.log("action UPSERT_SEND_SFTP_SCHEDULER")
      return new Promise((resolve, reject) => {
        docAppService.upsertSftpSchedulerConfiguration(/*rootState.auth.token,*/ payload)
          .then(
            res => {
              commit(SET_MESSAGE, `Scheduler added with success!`);
              resolve(res)
            }, err => {
              commit(SET_MESSAGE, err.message)
              reject(err)
            });
      });
  },
};

export const mutations = {
    [SET_DOCUMENT_APPLICATION]: (state, docApp) => {
      state.docApp = docApp;
    },
    [SET_MQ_RABBIT]: (state, mqRabbit) => {
      state.mqRabbit = mqRabbit;
    },
    [SET_SFTP]: (state, sftp) => {
      state.sftp = sftp;
    },
    [SET_GRPC]: (state, grpc) => {
      state.grpc = grpc;
    },
    [SET_DOCUMENT_SFTP]: (state, docSftp) => {
      state.docSftp = docSftp;
    },
    [SET_SFTP_SCHEDULER]: (state, sftpScheduler) => {
      state.sftpScheduler = sftpScheduler;
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